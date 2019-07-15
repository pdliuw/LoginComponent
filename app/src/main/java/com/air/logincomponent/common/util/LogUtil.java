package com.air.logincomponent.common.util;

import android.text.TextUtils;
import android.util.Log;

/**
 * <p>
 *
 * @author air on 2017-03-6.
 * </p>
 * <p>
 * Log util class.
 * 为了不引入太多的依赖，目前此类使用的是原生的日志；
 * 实际中，您可以在这里定制您的独特的日志工具
 * </p>
 */
public final class LogUtil {
    private LogUtil() {

    }

    public static void i(String tag, String message) {
        if (TextUtils.isEmpty(tag) || TextUtils.isEmpty(message)) {
            return;
        }
        Log.i(tag, message);
    }

    public static void e(String tag, String message) {
        if (TextUtils.isEmpty(tag) || TextUtils.isEmpty(message)) {
            return;
        }
        Log.e(tag, message);
    }

    public static void w(String tag, String message) {
        if (TextUtils.isEmpty(tag) || TextUtils.isEmpty(message)) {
            return;
        }
        Log.w(tag, message);
    }

    public static void d(String tag, String message) {
        if (TextUtils.isEmpty(tag) || TextUtils.isEmpty(message)) {
            return;
        }
        Log.d(tag, message);
    }
}
