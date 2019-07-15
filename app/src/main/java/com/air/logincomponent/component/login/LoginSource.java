package com.air.logincomponent.component.login;


import com.air.logincomponent.data.http.HttpContext;

import java.util.Map;

/**
 * @author air on 2017/3/6.
 *         <p>
 *         LoginSource
 *         </p>
 */

public interface LoginSource {

    void getVerificationCode(Map<String, String> options, HttpContext.Response response);
    void submitLogin(Map<String, String> options, HttpContext.Response response);

    void submitRegister(Map<String, String> options, HttpContext.Response response);
}
