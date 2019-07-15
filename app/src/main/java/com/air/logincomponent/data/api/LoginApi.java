package com.air.logincomponent.data.api;

import com.air.logincomponent.data.bean.BaseResponse;
import com.air.logincomponent.data.bean.LoginResponse;

import java.util.Map;

import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

/**
 * @author air on 2017/3/6.
 *         <p>
 *         LoginApi
 *         </p>
 */

public interface LoginApi {
    /**
     * 获取验证码
     *
     * @param options
     * @return
     */
    @POST("api/app/v1/LoginCtrl/getVerificationCode")
    @FormUrlEncoded
    Observable<BaseResponse> getVerificationCode(@FieldMap Map<String, String> options);

    /**
     * 登陆
     *
     * @param options
     * @return
     */
    @POST("api/app/v1/LoginCtrl/login")
    @FormUrlEncoded
    Observable<LoginResponse> submitLogin(@FieldMap Map<String, String> options);

    /**
     * 注册
     *
     * @param options
     *
     * @return
     */
    @POST("api/app/v1/LoginCtrl/register")
    @FormUrlEncoded
    Observable<LoginResponse> submitRegister(@FieldMap Map<String, String> options);

    /**
     * 登陆
     *
     * @param options
     * @return
     */
    @POST("app/login/v1.0")
    @FormUrlEncoded
    Observable<LoginResponse> submitLogin1(@FieldMap Map<String, String> options);
}
