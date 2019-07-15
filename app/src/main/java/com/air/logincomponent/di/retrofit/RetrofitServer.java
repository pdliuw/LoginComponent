package com.air.logincomponent.di.retrofit;

import android.text.TextUtils;

import com.air.logincomponent.common.config.AppApiFlag;
import com.air.logincomponent.common.util.LogUtil;
import com.air.logincomponent.component.login.helper.LoginContext;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.concurrent.TimeUnit;

import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import okio.ForwardingSource;
import okio.Okio;
import okio.Source;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @author air on 2017/3/6.
 *         <p>
 *         RetrofitServer
 *         https://square.github.io/retrofit/
 *         全局添加Authorization
 *         </p>
 */

public class RetrofitServer {

    private static final String TAG_LOG = "RetrofitServer";
    private static final Charset UTF8 = Charset.forName("UTF-8");

    private static final ProgressListener progressListener = new ProgressListener() {
        @Override
        public void update(long bytesRead, long contentLength, boolean done) {
            LogUtil.i(TAG_LOG, "read:\t" + bytesRead);
            LogUtil.i(TAG_LOG, "contentLength:\t" + contentLength);
            LogUtil.i(TAG_LOG, "done:\t" + done);
            LogUtil.i(TAG_LOG, String.format("%d%% done\n", (100 * bytesRead) / contentLength));
        }
    };

    public static final Retrofit getRetrofit() {

        OkHttpClient okHttpClient = getOkHttpClient();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(AppApiFlag.BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
        return retrofit;
    }

    /**
     * 获取OkHttpClient
     *
     * @return {@link OkHttpClient}
     */
    private static OkHttpClient getOkHttpClient() {


        OkHttpClient.Builder builder = new OkHttpClient.Builder().addInterceptor(new LoggingInterceptors())
                //添加网络进度管理
                .addNetworkInterceptor(chain -> {

                    /*
                    添加Authorization
                     */
                    String key = LoginContext.getInstance().getUserInfo().getAuthorizationKey();
                    String value = LoginContext.getInstance().getUserInfo().getAuthorizationValue();

                    Request chainRequest;

                    if (TextUtils.isEmpty(value)) {
                        chainRequest = chain.request();
                    } else {
                        //value ,不为空则添加到请求头中.
                        chainRequest = chain.request().newBuilder()
                                .header(key, value)
                                .build();
                    }

                    Response originalResponse = chain.proceed(chainRequest);
                    return originalResponse.newBuilder()
                            .body(new ProgressResponseBody(originalResponse.body(), progressListener))
                            .build();
                })
                .connectTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                //配置SSLSocketFactory
                .sslSocketFactory(SSLSocketFactoryUtil.createSSLSocketFactory(),SSLSocketFactoryUtil.createTrustAllManager())
                .readTimeout(30, TimeUnit.SECONDS);

        return builder.build();
    }

    /**
     * Network progress listener.
     */
    interface ProgressListener {
        /**
         * update progress.
         *
         * @param bytesRead     read bytes
         * @param contentLength length.
         * @param done          done
         */
        void update(long bytesRead, long contentLength, boolean done);
    }

    /**
     * 网络日志处理
     * {@link LogUtil}
     */
    private static class LoggingInterceptors implements Interceptor {
        @Override
        public Response intercept(Chain chain) throws IOException {
            long t1 = System.nanoTime();
            Request oldRequest = chain.request();
            /*
            Request
             */
            StringBuffer sb = new StringBuffer("Sending request");
            sb.append("\nurl=");
            sb.append(oldRequest.url());
            sb.append("\nmethod=");
            sb.append(oldRequest.method());
            sb.append("\non=");
            sb.append(chain.connection());
            sb.append("\nheaders=");
            sb.append(oldRequest.headers());
            sb.append("tag ");
            sb.append(oldRequest.tag());

            //2019年5月13日
            //针对小药药项目，并在不该动其他地方的基础上，在全局上对请求的URL地址进行修改以满足需求：
            Request newRequest = createNewRequest(oldRequest);

            RequestBody requestBody = newRequest.body();
            if (requestBody != null) {
                sb.append("\nRequestBody");
                sb.append("\n\tcontentLength=");
                sb.append(requestBody.contentLength());
                sb.append("\n\tcontentType=");
                sb.append(requestBody.contentType().toString());

                try {
                    final Request copy = newRequest.newBuilder().build();
                    final Buffer buffer = new Buffer();
                    copy.body().writeTo(buffer);
                    Charset charset = UTF8;
                    MediaType contentType = copy.body().contentType();
                    if (contentType != null) {
                        charset = contentType.charset(UTF8);
                    }
                    sb.append("\nBody details:--->>>\n");
                    sb.append(buffer.readString(charset));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            LogUtil.i(TAG_LOG, sb.toString());

            /*
            Response
             */
            Response response = chain.proceed(newRequest);

            long t2 = System.nanoTime();
            LogUtil.i(TAG_LOG, String.format("Received response for %s in %.1fms%n%s",
                    newRequest.url(), (t2 - t1) / 1e6d, response.headers()));
            return response;
        }
    }

    /**
     * 针对小药药项目进行全局的将服务器地址变从"链四方地址"变更为"小药药地址"
     * @param oldRequest "链四方项目的请求对象"
     * @return "小药药项目的请求对象"
     */
    public static Request createNewRequest(Request oldRequest){

        HttpUrl oldUrl = oldRequest.url();
        String oldHost = oldUrl.host();
        int oldPort = oldUrl.port();
        String oldQuery = oldUrl.query();
        boolean oldIsHttps = oldUrl.isHttps();
        String oldScheme = oldUrl.scheme();

        String oldUrlStr = oldUrl.toString();

        String newFullUrlStr = oldUrlStr.replaceFirst("app", "xyy-app");

        Request newRequest = oldRequest.newBuilder()
                .method(oldRequest.method(), oldRequest.body())
                .url(newFullUrlStr)
                .build();
        LogUtil.e(TAG_LOG, "链四方FullUrlStr:"+oldUrlStr);
        LogUtil.e(TAG_LOG, "小药药FullUrlStr:"+newFullUrlStr);

        return newRequest;
    }

    private static class ProgressResponseBody extends ResponseBody {
        private final ResponseBody responseBody;
        private final ProgressListener progressListener;
        private BufferedSource bufferedSource;

        ProgressResponseBody(ResponseBody responseBody, ProgressListener progressListener) {
            this.responseBody = responseBody;
            this.progressListener = progressListener;
        }

        @Override
        public MediaType contentType() {
            return responseBody.contentType();
        }

        @Override
        public long contentLength() {
            return responseBody.contentLength();
        }

        @Override
        public BufferedSource source() {
            if (bufferedSource == null) {
                bufferedSource = Okio.buffer(source(responseBody.source()));
            }
            return bufferedSource;
        }

        private Source source(Source source) {
            return new ForwardingSource(source) {
                long totalBytesRead = 0L;

                @Override
                public long read(Buffer sink, long byteCount) throws IOException {
                    long bytesRead = super.read(sink, byteCount);
                    // read() returns the number of bytes read, or -1 if this source is exhausted.
                    totalBytesRead += bytesRead != -1 ? bytesRead : 0;
                    progressListener.update(totalBytesRead, responseBody.contentLength(), bytesRead == -1);
                    return bytesRead;
                }
            };
        }
    }
}
