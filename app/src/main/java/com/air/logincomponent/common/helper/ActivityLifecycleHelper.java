package com.air.logincomponent.common.helper;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.os.Process;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.air.logincomponent.common.util.LogUtil;

import java.util.LinkedList;

/**
 * @author pd_liu on 2018/3/13.
 * <p>
 * Activity lifecycle manager.
 * </p>
 */

public class ActivityLifecycleHelper implements Application.ActivityLifecycleCallbacks {

    private static final String TAG_LOG = "ActivityLifecycleHelper";

    private final LinkedList<Activity> mActivityList;

    private int numberForeground;
    private int numberBackground;

    private ActivityLifecycleHelper() {
        mActivityList = new LinkedList<>();
    }

    public static ActivityLifecycleHelper getInstance() {
        return Holder.INSTANCE;
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        mActivityList.add(activity);
    }

    @Override
    public void onActivityStarted(Activity activity) {
        numberForeground = numberForeground + 1;
    }

    @Override
    public void onActivityResumed(Activity activity) {

    }

    @Override
    public void onActivityPaused(Activity activity) {

    }

    @Override
    public void onActivityStopped(Activity activity) {
        numberForeground = numberForeground - 1;
        if (numberForeground == 0) {
            /*
            当前App处于后台
             */
            LogUtil.e(TAG_LOG, "App处于后台");
        }
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        mActivityList.remove(activity);
    }

    public boolean finishAllActivity() {
        for (int i = 0; i < mActivityList.size(); i++) {
            mActivityList.get(i).finish();
        }
        return true;
    }

    /**
     * 退出App进程
     *
     * @return 是否成功退出
     */
    public boolean exitApp() {
        //关闭activity
        finishAllActivity();
        //关闭Dalvik vm
        Process.killProcess(Process.myPid());
        return true;
    }

    public boolean finishAllExceptActivity(Class exceptActivity) {
        for (int i = 0; i < mActivityList.size(); i++) {
            Activity currentActivity = mActivityList.get(i);
            if (currentActivity.getClass() != exceptActivity) {
                currentActivity.finish();
            }
        }
        return true;
    }

    private int lastIndex() {
        if (mActivityList.isEmpty()) {
            return mActivityList.size();
        } else {
            return mActivityList.size() - 1;
        }
    }

    public Activity getTopActivity() {
        return mActivityList.get(lastIndex());
    }

    public Activity getPreActivity() {
        if (lastIndex() == 0) {
            return null;
        } else {
            return mActivityList.get(lastIndex() - 1);
        }
    }

    @Nullable
    public Activity getActivity(@NonNull Class cls) {

        for (Activity activity :
                mActivityList) {

            if (cls == activity.getClass()) {
                return activity;
            }
        }
        return null;
    }

    static class Holder {
        private static final ActivityLifecycleHelper INSTANCE = new ActivityLifecycleHelper();
    }
}

