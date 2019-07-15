package com.air.logincomponent.di.retrofit;

import android.content.Context;
import android.util.Log;

import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;


/**
 * <p>
 * Created by air on 2019-06-06.
 * </p>
 */
public class SSLSocketFactoryUtil {

//import java.security.KeyManagementException;
//import java.security.NoSuchAlgorithmException;
//import java.security.SecureRandom;
//import java.security.cert.CertificateException;
//import java.security.cert.CertificateFactory;
//import java.security.cert.X509Certificate;

    /*
     * 默认信任所有的证书
     * */
    public static SSLSocketFactory createSSLSocketFactory() {
        SSLSocketFactory sslSocketFactory = null;
        try {
            SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, new TrustManager[]{createTrustAllManager()}, new SecureRandom());
            sslSocketFactory = sslContext.getSocketFactory();
        } catch (Exception e) {

        }
        return sslSocketFactory;
    }

    public static SSLSocketFactory createSSLSocketFactory(Context context, int keyServerStroreID) {
        SSLSocketFactory mSSLSocketFactory = null;
        if (mSSLSocketFactory == null) {
            synchronized (SSLSocketFactoryUtil.class) {
                if (mSSLSocketFactory == null) {

                    InputStream trustStream = context.getResources().openRawResource(keyServerStroreID);
                    SSLContext sslContext;
                    try {
                        sslContext = SSLContext.getInstance("SSL");
                    } catch (NoSuchAlgorithmException e) {
                        Log.e("httpDebug", "createSingleSSLSocketFactory", e);
                        return null;
                    }
                    //获得服务器端证书
                    TrustManager[] turstManager = getTurstManager(trustStream);

                    //初始化ssl证书库
                    try {
                        sslContext.init(null, turstManager, new SecureRandom());
                    } catch (KeyManagementException e) {
                        Log.e("httpDebug", "createSingleSSLSocketFactory", e);
                    }

                    //获得sslSocketFactory
                    mSSLSocketFactory = sslContext.getSocketFactory();
                }
            }
        }
        return mSSLSocketFactory;
    }


    public static X509TrustManager createTrustAllManager() {
        X509TrustManager tm = null;
        try {

            tm = new X509TrustManager() {
                public void checkClientTrusted(X509Certificate[] chain, String authType)
                        throws CertificateException {
                    //do nothing，接受任意客户端证书
                }

                public void checkServerTrusted(X509Certificate[] chain, String authType)
                        throws CertificateException {
                    //do nothing，接受任意服务端证书
                }

                public X509Certificate[] getAcceptedIssuers() {
                    return new X509Certificate[0];
                }
            };
//            InputStream trustStream = Constant.context.getResources().openRawResource(R.raw.tcggscapi);
//            testReadX509CerFile(trustStream);
        } catch (Exception e) {

        }
        return tm;
    }


    /**
     * 读取*.cer公钥证书文件， 获取公钥证书信息
     *
     * @author xgh
     */
    public static void testReadX509CerFile(InputStream inStream) throws Exception {

        try {
            // 读取证书文件

            // 创建X509工厂类
            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            // 创建证书对象
            X509Certificate oCert = (X509Certificate) cf
                    .generateCertificate(inStream);
            inStream.close();
            SimpleDateFormat dateformat = new SimpleDateFormat("yyyy/MM/dd");
            String info = null;
            // 获得证书版本
            info = String.valueOf(oCert.getVersion());
            System.out.println("证书版本:" + info);
            // 获得证书序列号
            info = oCert.getSerialNumber().toString(16);
            System.out.println("证书序列号:" + info);
            // 获得证书有效期
            Date beforedate = oCert.getNotBefore();
            info = dateformat.format(beforedate);
            System.out.println("证书生效日期:" + info);
            Date afterdate = oCert.getNotAfter();
            info = dateformat.format(afterdate);
            System.out.println("证书失效日期:" + info);
            // 获得证书主体信息
            info = oCert.getSubjectDN().getName();
            System.out.println("证书拥有者:" + info);
            // 获得证书颁发者信息
            info = oCert.getIssuerDN().getName();
            System.out.println("证书颁发者:" + info);
            // 获得证书签名算法名称
            info = oCert.getSigAlgName();
            System.out.println("证书签名算法:" + info);

        } catch (Exception e) {
            System.out.println("解析证书出错！");
            e.printStackTrace();
        }
    }

    /**
     * 获得指定流中的服务器端证书库
     */

    public static TrustManager[] getTurstManager(InputStream... certificates) {
        try {
            CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            keyStore.load(null, null);
            int index = 0;
            for (InputStream certificate : certificates) {
                if (certificate == null) {
                    continue;
                }
                Certificate certificate1;
                try {
                    certificate1 = certificateFactory.generateCertificate(certificate);
                } finally {
                    certificate.close();
                }

                String certificateAlias = Integer.toString(index++);
                keyStore.setCertificateEntry(certificateAlias, certificate1);
            }

            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory
                    .getDefaultAlgorithm());

            trustManagerFactory.init(keyStore);
            return trustManagerFactory.getTrustManagers();

        } catch (Exception e) {
            Log.e("httpDebug", "SSLSocketFactoryUtils", e);
        }
        return new TrustManager[]{createTrustAllManager()};
    }
}
