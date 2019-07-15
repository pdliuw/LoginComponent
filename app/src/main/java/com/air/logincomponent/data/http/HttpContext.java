package com.air.logincomponent.data.http;

import android.app.Activity;


import com.air.logincomponent.DriverApplication;
import com.air.logincomponent.common.config.AppApiFlag;
import com.air.logincomponent.common.util.LogUtil;
import com.air.logincomponent.common.util.ToastUtil;
import com.air.logincomponent.data.bean.BaseResponse;
import com.air.logincomponent.di.retrofit.RetrofitServer;

import java.net.BindException;
import java.net.ConnectException;
import java.net.HttpRetryException;
import java.net.MalformedURLException;
import java.net.NoRouteToHostException;
import java.net.PortUnreachableException;
import java.net.ProtocolException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.net.UnknownServiceException;

import retrofit2.HttpException;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * @author pd_liu on 2017/3/6.
 * <p>
 * HttpContext
 * </p>
 * <p>
 * Usage:
 * The send request:    {@link #createApi(Class)}{@link #execute(Observable, Response)}
 * The request result:  {@link Response}
 * </p>
 * <p>
 * Document:
 * 使用案例请查阅：{@link HttpSample},您根据项目的实际情况来编写"使用案例"
 * </p>
 */

public class HttpContext {

    private static final String TAG_LOG = "HttpContext";

    private Subscription mSubscription;

    /**
     * 创建网络所需的API
     *
     * @param service
     *         api
     * @param <T>
     *         t
     *
     * @return t
     */
    public <T> T createApi(Class<T> service) {
        return RetrofitServer.getRetrofit().create(service);
    }

    /**
     * 执行网络操作
     *
     * @param observable
     *         {@link Observable}
     * @param response
     *         {@link Response}
     * @param <T>
     *         T
     */
    public <T> void execute(Observable<T> observable, final Response<T> response) {

        mSubscription = observable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<T>() {
                    @Override
                    public void onCompleted() {
                        LogUtil.i(TAG_LOG, "Completed:");
                        response.stop();
                    }

                    @Override
                    public void onError(Throwable e) {
                        LogUtil.e(TAG_LOG, "Error:\n" + e.getCause() + "\t" + e.getMessage());

                        //错误日志信息
                        String errorMessage = "未知错误";
                        int errorCode = 0;
                        if (e instanceof HttpException) {
                            HttpException httpException = ((HttpException) e);
                            errorCode = httpException.code();
                            errorMessage = httpException.message();

                            if (AppApiFlag.RESULT_UNAUTHORIZED == errorCode) {
                                errorMessage = "身份过期，请重新登录";
                                /*
                                身份未认证，则切换为登出状态。
                                 */
                                Activity topActivity = DriverApplication.getActivityLifecycleHelper().getTopActivity();
                                /*
                                TODO:(这里填写您所需的操作以及跳转的逻辑)
                                 */
                                //Do something!
                            }

                        } else if (e.getCause() instanceof BindException) {
                            errorMessage = "尝试将套接字绑定到本地地址和端口时发生错误；" +
                                    "通常，该端口正在使用中，或者请求的本地地址无法分配。";
                        } else if (e.getCause() instanceof ConnectException) {
                            errorMessage = "网络连接异常, 请检查后重试!";
                        } else if (e.getCause() instanceof HttpRetryException) {
                            errorMessage = "HTTP 重试异常!";
                        } else if (e.getCause() instanceof MalformedURLException) {
                            errorMessage = "格式错误的URL, 请检查后重试!";
                        } else if (e.getCause() instanceof NoRouteToHostException) {
                            errorMessage = "无法访问远程主机, 请检查后重试!";
                        } else if (e.getCause() instanceof PortUnreachableException) {
                            errorMessage = "在连接的数据报上收到ICMP端口不可达消息, 请检查后重试!";
                        } else if (e.getCause() instanceof ProtocolException) {
                            errorMessage = "底层协议中存在错误，例如TCP错误, 请检查后重试!";
                        } else if (e.getCause() instanceof SocketException) {
                            errorMessage = "创建或访问套接字时发生错误, 请检查后重试!";
                        } else if (e.getCause() instanceof SocketTimeoutException) {
                            //提示开发人员信息
                            errorMessage = "套接字读取或接受发生超时, 请检查后重试!";
                            //提示用户信息
                            errorMessage = "网络连接超时, 请检查后重试!";
                        } else if (e.getCause() instanceof UnknownHostException) {
                            errorMessage = "主机的IP地址无法确定, 请检查后重试!";
                        } else if (e.getCause() instanceof UnknownServiceException) {
                            errorMessage = "发生了未知的服务异常。 URL连接返回的MIME类型不合理，或者应用程序试图写入只读URL连接, 请检查后重试!";
                        } else if (e.getCause() instanceof URISyntaxException) {
                            errorMessage = "一个字符串不能被解析为一个URI引用, 请检查后重试!";
                        } else {
                            errorMessage = e.getLocalizedMessage();
                        }
                        LogUtil.e(TAG_LOG, "Error:\n" + errorCode + "\t" + errorMessage);
                        //Callback.
                        response.error(errorMessage);
                        ToastUtil.showApp(errorMessage);
                        //主动调用completed
                        onCompleted();
                    }

                    @Override
                    public void onNext(T t) {
                        /*
                        响应成功
                         */
                        LogUtil.i(TAG_LOG, "Next：\n" + t.toString());

                        /*
                        判断请求是否成功
                         */
                        if (t instanceof BaseResponse) {
                            //自动校验自定义的响应码
                            //进行判断响应码
                            BaseResponse responseBean = (BaseResponse) t;
                            String code = responseBean.getCode();
                            String message = responseBean.getMessage();
                            ToastUtil.showApp(message);
                            LogUtil.w(TAG_LOG, message);
                            if (AppApiFlag.RESULT_OK.equals(code)) {
                                //响应成功
                                response.success(t);
                            } else {
                                //响应失败
                                response.error(message);
                            }
                        } else {
                            //不进行校验自定义的响应码
                            //响应成功
                            response.success(t);
                        }

                    }

                    @Override
                    public void onStart() {
                        super.onStart();
                        LogUtil.i(TAG_LOG, "Start:");
                        //判断是否有网络
                        if (!NetWorkHelper.getInstance().isNetworkConnected()) {
                            //无网络
                            ToastUtil.showApp("请检查网络是否连接");
                            //取消订阅
                            unsubscribe();
                            //调用
                            response.error("无网络，请检查后刷新");
                        } else {
                            response.start();
                        }
                    }
                });
    }

    /**
     * 取消网络请求任务
     */
    public void cancel() {
        if (mSubscription != null) {
            if (!mSubscription.isUnsubscribed()) {
                mSubscription.unsubscribe();
            }
        }
    }

    /**
     * Http 网络响应
     */
    public static abstract class Response<T> {

        /**
         * 网络开始处理
         *
         * @see #stop()
         */
        public void start() {
        }

        /**
         * 网络结束处理(success or error) {@link #success(Object)}{@link #error(String)}}
         *
         * @see #start()
         */
        public void stop() {
        }

        /**
         * 网络响应成功
         *
         * @param result
         *
         * @see #error(String)
         */
        public abstract void success(T result);

        /**
         * 网络响应失败
         *
         * @see #success(Object)
         */
        public void error(String error) {
        }
    }
}