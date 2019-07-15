package com.air.logincomponent;


import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDex;

import com.air.logincomponent.common.config.InitializeConfig;
import com.air.logincomponent.common.helper.ActivityLifecycleHelper;

/**
 * @author pd_liu on 2017/3/6.
 *         <p>
 *         DriverApplication
 *         </p>
 */

public class DriverApplication extends Application {

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        /*
        Do something!
        initialize ...
         */
        InitializeConfig.initialize(this);

        registerActivityLifecycleCallbacks(ActivityLifecycleHelper.getInstance());
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }


    public static ActivityLifecycleHelper getActivityLifecycleHelper() {
        return ActivityLifecycleHelper.getInstance();
    }
}

