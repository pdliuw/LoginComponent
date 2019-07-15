package com.air.logincomponent.common.config;

import com.air.logincomponent.BuildConfig;

/**
 * <p>
 * Created by air on 2019-07-15.
 * </p>
 */
public final class EnvironmentConfig {
    private EnvironmentConfig() {

    }

    public static final boolean LOG_DEBUG = BuildConfig.LOG_DEBUG;
    public static final boolean URL_DEBUG = BuildConfig.URL_DEBUG;
    public static final boolean TOAST_DEBUG = BuildConfig.URL_DEBUG;
}
