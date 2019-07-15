package com.air.logincomponent.common.manager;

import android.content.Context;
import android.content.SharedPreferences;

import com.air.logincomponent.common.util.LogUtil;

/**
 * @author air on 2017/3/6.
 *         <p>
 *         SharedPrefManager
 *         </p>
 *         <p>
 *         1、全局初始化：{@link #initialize(Context)}
 *         2、如需要使用key ,请在此类中进行定义维护
 *         3、{@link #getInstance()}
 *         </p>
 */

public class SharedPrefManager {

    /**
     * 定义的字段，供外部使用
     */
    public static final String FIELD_ACCESS_TOKEN = "access_token";
    /**
     * 用户信息
     */
    public static final String FIELD_USER_INFO = "user_info";
    /**
     * 版本信息
     */
    public static final String FIELLD_VERSION_INFO = "version_info";
    private static final String TAG_LOG = "SharedPrefManager";
    /**
     * 样式
     */
    public static final String FIELD_KEY_THEME = "key_theme";
    /**
     * SharedPreferences name.
     */
    private static final String NAME = "APP_CACHE";

    /**
     * 默认值
     */
    private static final String DEFAULT_STRING_VALUE = "default";
    private static final int DEFAULT_INT_VALUE = 0;
    private static final boolean DEFAULT_BOOLEAN_VALUE = false;

    private Context mApplicationContext;

    public static SharedPrefManager getInstance() {
        return Holder.INSTANCE;
    }

    public void initialize(Context application) {
        mApplicationContext = application;
    }

    private SharedPreferences getSharedPreferences() {
        SharedPreferences sf = mApplicationContext.getSharedPreferences(NAME, Context.MODE_PRIVATE);
        return sf;
    }

    public boolean putString(String key, String value) {
        SharedPreferences.Editor editor = getSharedPreferences().edit();
        editor.putString(key, value);
        return editor.commit();
    }

    public boolean putInt(String key, int value) {
        SharedPreferences.Editor editor = getSharedPreferences().edit();
        editor.putInt(key, value);
        return editor.commit();
    }

    public String getString(String key) {
        return getString(key, DEFAULT_STRING_VALUE);
    }

    public String getString(String key, String defaultValue) {
        return getSharedPreferences().getString(key, defaultValue);
    }

    public int getInt(String key) {
        return getInt(key, DEFAULT_INT_VALUE);
    }

    public int getInt(String key, int defaultValue) {
        return getSharedPreferences().getInt(key, defaultValue);
    }

    public boolean remove(String key) {
        if (!contains(key)) {
            LogUtil.e(TAG_LOG, "key：" + key + "，无效.请检查后重试");
            return false;
        }
        return getSharedPreferences().edit().remove(key).commit();
    }

    public boolean getBoolean(String key) {
        return getBoolean(key, DEFAULT_BOOLEAN_VALUE);
    }

    public boolean getBoolean(String key, boolean defaultValue) {
        return getSharedPreferences().getBoolean(key, defaultValue);
    }

    public boolean contains(String key) {
        return getSharedPreferences().contains(key);
    }

    static class Holder {
        private static final SharedPrefManager INSTANCE = new SharedPrefManager();
    }
}
