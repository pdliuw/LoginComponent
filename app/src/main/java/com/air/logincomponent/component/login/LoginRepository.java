package com.air.logincomponent.component.login;

import com.air.logincomponent.data.api.LoginApi;
import com.air.logincomponent.data.http.HttpContext;

import java.util.Map;

/**
 * @author air on 2017/3/6.
 *         <p>
 *         LoginRepository
 *         </p>
 */

public class LoginRepository implements LoginSource {
    @Override
    public void getVerificationCode(Map<String, String> options, HttpContext.Response response) {
        HttpContext httpContext = new HttpContext();

        LoginApi api = httpContext.createApi(LoginApi.class);

        httpContext.execute(api.getVerificationCode(options),response);
    }

    @Override
    public void submitLogin(Map<String, String> options, HttpContext.Response response) {
        HttpContext httpContext = new HttpContext();

        LoginApi api = httpContext.createApi(LoginApi.class);

        httpContext.execute(api.submitLogin(options),response);
    }

    @Override
    public void submitRegister(Map<String, String> options, HttpContext.Response response) {
        HttpContext httpContext = new HttpContext();

        LoginApi api = httpContext.createApi(LoginApi.class);

        httpContext.execute(api.submitRegister(options), response);
    }
}
