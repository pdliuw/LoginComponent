package com.air.logincomponent.component.login;

import android.os.CountDownTimer;
import android.text.TextUtils;


import com.air.logincomponent.common.util.LogUtil;
import com.air.logincomponent.common.util.ToastUtil;
import com.air.logincomponent.component.login.helper.LoginContext;
import com.air.logincomponent.component.login.helper.LoginedStatus;
import com.air.logincomponent.data.bean.BaseResponse;
import com.air.logincomponent.data.bean.LoginResponse;
import com.air.logincomponent.data.http.HttpContext;

import java.util.HashMap;
import java.util.Map;

/**
 * @author air on 2017/3/6.
 * <p>
 * LoginPresenter
 * </p>
 * @author air 207年3月7日
 * <p>
 * 更新接口
 * </p>
 * <p>
 * 2017年3月7日：
 * 增加：登录、注册的验证码的接口的参数有所变动：
 * type
 * string
 * (query)
 * 业务类型：司机注册(APP_REGISTER), 司机登录(APP_LOGIN)
 * <p>
 * Default value : APP_LOGIN
 * </p>
 */

public class LoginPresenter implements LoginContract.Presenter {
    private static final String TAG_LOG = "LoginPresenter";
    private static final String APP_REGISTER = "APP_REGISTER";
    private static final String APP_LOGIN = "APP_LOGIN";
    private String mVerificationCodeType = APP_LOGIN;
    /**
     * 倒计时的秒数
     */
    private static final int COUNT_SECOND_UNIT = 60;
    /**
     * 手机号码的长度
     */
    private static final int PHONE_NUMBER_LENGTH = 11;

    private LoginSource mLoginSource;
    private LoginContract.View mLoginView;

    private String mPhoneNumber;
    private String mVerificationCode;
    private String mIDCardNumber;
    private String mIDCardName;

    public LoginPresenter(LoginSource loginSource, LoginContract.View loginView) {
        this.mLoginSource = loginSource;
        this.mLoginView = loginView;

        mLoginView.setPresenter(this);
    }

    @Override
    public void verificationCode() {

        if (TextUtils.isEmpty(mPhoneNumber)) {
            mLoginView.errorPhone("请填写手机号");
            return;
        }

        if (mPhoneNumber.trim().length() != PHONE_NUMBER_LENGTH) {
            mLoginView.errorPhone("请填写" + PHONE_NUMBER_LENGTH + "手机号");
            return;
        }

        Map<String, String> options = new HashMap<>();
        options.put("mobile", mPhoneNumber);
        options.put("type", mVerificationCodeType);

        mLoginSource.getVerificationCode(options, new HttpContext.Response<BaseResponse>() {
            @Override
            public void success(BaseResponse result) {

            }

            @Override
            public void start() {
                super.start();
                //加载框
                mLoginView.showLoading();
                //开始验证码倒计时
                startVerificationCount();
            }

            @Override
            public void stop() {
                super.stop();
                mLoginView.hideLoading();
            }
        });
    }

    @Override
    public void submitLogin() {
        /*
        Verification many conditions.
         */
        if (TextUtils.isEmpty(mPhoneNumber) || TextUtils.isEmpty(mVerificationCode)) {
            ToastUtil.showApp("手机号和验证码不能为空!");
            return;
        }
        if (mPhoneNumber.length() != 11) {
            mLoginView.errorPhone("请输入正确的手机号码");
            return;
        }

        /*
        Http request
         */
        Map<String, String> options = new HashMap<>();
        options.put("mobile", mPhoneNumber);
        options.put("code", mVerificationCode);

        mLoginSource.submitLogin(options, new HttpContext.Response<LoginResponse>() {
            @Override
            public void success(LoginResponse result) {
                LogUtil.i(TAG_LOG, result.toString());
                LoginResponse.DataBean dataBean = result.getData();
                //设置全局状态为登录状态
                LoginContext.getInstance().setStatus(new LoginedStatus());
                //更新用户信息
                LoginContext.AppUserInfo newUserInfo = new LoginContext.AppUserInfo();
                newUserInfo.accessToken = dataBean.getAccessToken();
                newUserInfo.tokenType = dataBean.getTokenType();
                newUserInfo.refreshToken = dataBean.getRefreshToken();
                newUserInfo.expiresIn = dataBean.getExpiresIn();
                newUserInfo.expire = dataBean.getExpire();
                newUserInfo.scope = dataBean.getScope();
                newUserInfo.sub = dataBean.getSub();
                //Todo:无参数，待完善
                //newUserInfo.memberId = dataBean.getMemberId();
                newUserInfo.setName(dataBean.getUserName());
                newUserInfo.setPhoneNumber(mPhoneNumber);
                newUserInfo.verificationCode = mVerificationCode;
                //Todo:无参数,待完善
                //newUserInfo.headPortrait = dataBean.getHeadPortrait();
                LoginContext.getInstance().updateUserInfo(newUserInfo);

                mLoginView.loginSuccess();
            }

            @Override
            public void error(String error) {
                super.error(error);
                mLoginView.loginError(error);
            }

            @Override
            public void start() {
                super.start();
                mLoginView.showLoading();
            }

            @Override
            public void stop() {
                super.stop();
                mLoginView.hideLoading();
            }

        });
    }

    @Override
    public void submitRegister() {
//        /*
//        Verification many conditions.
//         */
//        if (TextUtils.isEmpty(mPhoneNumber) || TextUtils.isEmpty(mVerificationCode)) {
//            ToastUtil.showApp("手机号和验证码不能为空!");
//            return;
//        }
//        if (TextUtils.isEmpty(mIDCardName) || TextUtils.isEmpty(mIDCardNumber)) {
//            ToastUtil.showApp("身份证信息不能为空!");
//            return;
//        }
//        /*
//        Http request
//         */
//        Map<String, String> options = new HashMap<>();
//        options.put("mobile", mPhoneNumber);
//        options.put("code", mVerificationCode);
//        options.put("idCard", mIDCardNumber);
//        options.put("name", mIDCardName);
//
//        mLoginSource.submitRegister(options, new HttpContext.Response<RegisterResponse>() {
//            @Override
//            public void success(RegisterResponse result) {
//                LogUtil.i(TAG_LOG, result.toString());
//
//                mLoginView.registerSuccess();
//            }
//
//            @Override
//            public void start() {
//                super.start();
//                mLoginView.showLoading();
//            }
//
//            @Override
//            public void stop() {
//                super.stop();
//                mLoginView.hideLoading();
//            }
//
//        });
    }

    @Override
    public void startVerificationCount() {

        new CountDownTimer(COUNT_SECOND_UNIT * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                mLoginView.updateVerificationCount(millisUntilFinished / 1000 + "秒");
            }

            @Override
            public void onFinish() {
                mLoginView.stopVerificationCount("停止");
            }
        }.start();
        //开始倒计时
        mLoginView.startVerificationCount("开始");
    }

    @Override
    public void stopVerificationCount() {
        mLoginView.stopVerificationCount("停止");
    }

    public void setPhoneNumber(String phoneNumber) {
        mPhoneNumber = phoneNumber;
    }

    /**
     * 设置当前获取验证码是：登录的验证码，还是：注册的验证码
     *
     * @param isLogin
     *         if true, is login.
     */
    public void setVerificationCodeType(boolean isLogin) {
        if (isLogin) {
            mVerificationCodeType = APP_LOGIN;
        } else {
            mVerificationCodeType = APP_REGISTER;
        }
    }

    public void setIDCardNumber(String number) {
        mIDCardNumber = number;
    }

    public void setIDCardName(String name) {
        mIDCardName = name;
    }

    public void setVerificationCode(String code) {
        mVerificationCode = code;
    }
}
