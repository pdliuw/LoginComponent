package com.air.logincomponent.component.login.helper;

/**
 * @author air on 2017/3/6.
 *         <p>
 *         用户退出登陆状态
 *         </p>
 */

public class LogoutStatus implements UserStatus {
    @Override
    public void comment() {
        /*
        登出状态下无法进行评论
         */
    }

    @Override
    public void forward() {
        /*
        登出状态下无法进行转发
         */
    }
}
