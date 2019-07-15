package com.air.logincomponent.ui.factory;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

import com.air.logincomponent.MainActivity;
import com.air.logincomponent.component.login.LoginActivity;
import com.air.logincomponent.component.login.helper.LoginContext;
import com.air.logincomponent.component.login.helper.LoginedStatus;

/**
 * <p>
 * @author air 2017/3/6.
 * </p>
 */
public class StartActivityFactory {


    /**
     * 登录逻辑操作
     *
     * @param activity
     *         the object of call the method.
     */
    public static void startLoginActivity(AppCompatActivity activity) {
        //判断是否自动登录
        if (LoginContext.getInstance().isAutomaticLogin()) {
            //自动登录
            //设置全局状态为登录状态
            LoginContext.getInstance().setStatus(new LoginedStatus());
            //更新用户信息
            LoginContext.AppUserInfo newUserInfo = new LoginContext.AppUserInfo();
            newUserInfo = LoginContext.getInstance().restoreUser();
            LoginContext.getInstance().updateUserInfo(newUserInfo);
            //跳转到首页
            activity.startActivity(new Intent(activity, MainActivity.class));
        } else {
            //手动登录
            activity.startActivity(new Intent(activity, LoginActivity.class));
        }
    }
}
