package com.air.logincomponent.component.login.helper;

/**
 * @author air on 2017/3/6.
 *         <p>
 *         用户已经登陆的状态
 *         </p>
 */

public class LoginedStatus implements UserStatus {
    @Override
    public void comment() {
        /*
        已经登陆可以进行评论
         */
    }

    @Override
    public void forward() {
        /*
        已经登陆可以进行转发
         */
    }
}
