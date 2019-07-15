package com.air.logincomponent.common.config;

import android.content.Context;

import com.air.logincomponent.common.manager.SharedPrefManager;
import com.air.logincomponent.data.http.NetWorkHelper;

/**
 * <p>
 *
 * @author air on 2017/3/6.
 * </p>
 */
public class InitializeConfig {


    public static void initialize(Context applicationContext) {
        //网络处理
        NetWorkHelper.getInstance().initialize(applicationContext);
        SharedPrefManager.getInstance().initialize(applicationContext);
    }
}
