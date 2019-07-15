package com.air.logincomponent.component.welcome;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.air.logincomponent.common.manager.SharedPrefManager;
import com.air.logincomponent.common.util.LogUtil;

/**
 * @author pd_liu on 2018/5/9.
 * <p>
 * VersionHelper
 * </p>
 * <p>
 * 版本号的处理
 * </p>
 */

public class VersionHelper {

    private static final String TAG_LOG = "VersionHelper";

    /**
     * 获取版本号失败后的默认版本号的值
     */
    private static final int GET_VERSION_CODE_FAILED_VALUE = 0;
    private static final String GET_VERSION_NAME_FAILED_VALUE = "0";

    /**
     * 自动获取应用版本号并保存
     *
     * @param context
     * @return If true, saved successfully.
     */
    public static boolean saveVersion(Context context) {

        int savedVersionCode = getVersionCode(context);

        /*
        如果获取版本号失败，那么不保存版本号
        提示查询问题
         */
        if (savedVersionCode == GET_VERSION_CODE_FAILED_VALUE) {
            LogUtil.e(TAG_LOG, "获取版本号失败，请尽快查询此问题，并解决!!!");
            return false;
        }

        return saveVersion(savedVersionCode);
    }

    /**
     * 将版本号保存
     *
     * @param versionCode
     * @return If true, saved successfully.
     */
    public static boolean saveVersion(int versionCode) {

        //提交成功
        return SharedPrefManager.getInstance().putInt(SharedPrefManager.FIELLD_VERSION_INFO, versionCode);
    }

    public static boolean isShowFlash(Context context) {

        int localCacheVersion = SharedPrefManager.getInstance().getInt(SharedPrefManager.FIELLD_VERSION_INFO);

        int currentAppVersion = getVersionCode(context);

        if (currentAppVersion > localCacheVersion) {
            return true;
        } else {
            return false;
        }

    }

    /**
     * 获取版本号
     *
     * @param context
     * @return If return 0 ,getVersionCode failed.
     * Else, successful.
     */
    private static int getVersionCode(Context context) {
        PackageManager packageManager = context.getPackageManager();
        PackageInfo packageInfo = null;
        try {
            packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);

            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        //如果获取失败，那么返回一个获取失败的默认值
        return GET_VERSION_CODE_FAILED_VALUE;
    }

    /**
     * 获取版本名称
     *
     * @param context
     * @return If return 0 ,getVersionCode failed.
     * Else, successful.
     */
    public static String getVersionName(Context context) {
        PackageManager packageManager = context.getPackageManager();
        PackageInfo packageInfo = null;
        try {
            packageInfo = packageManager.getPackageInfo(context.getPackageName(), 0);

            return packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        //如果获取失败，那么返回一个获取失败的默认值
        return GET_VERSION_NAME_FAILED_VALUE;
    }
}
