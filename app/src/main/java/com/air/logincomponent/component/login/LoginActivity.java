package com.air.logincomponent.component.login;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.widget.NestedScrollView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.air.logincomponent.MainActivity;
import com.air.logincomponent.R;
import com.air.logincomponent.component.login.helper.LoginContext;
import com.air.logincomponent.ui.base.AppCommonActivity;
import com.air.logincomponent.ui.dialog.AppLoadingDialogFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * @author air 2017/3/6.
 * <p>
 * 登陆主页面
 * </p>
 * <p>
 * @author air
 * 2017/3/6日
 * 1、增加：something!
 * </p>
 */
public class LoginActivity extends AppCommonActivity implements LoginContract.View {

    private LoginPresenter mPresenter;

    private AppLoadingDialogFragment mAppLoading;

    private TextInputLayout mPhoneInputLayout, mPwdInputLayout;

    private TextInputEditText mLoginUserPhoneEt, mLoginUserPwdEt;

    private Button mUserLoginConfirmBtn;

    private Button mVerificationCodeBtn;

    private String mGetVerificationCodeTipDefault;

    private TextWatcher mTextWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            //Changed
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

            mPhoneInputLayout.setError(null);
            mPwdInputLayout.setError(null);
        }

        @Override
        public void afterTextChanged(Editable s) {
            //手机号码不为空   &&   验证码输入的长度，与服务器返回的验证码长度一致
            if ((mLoginUserPhoneEt.getText().toString().trim().length() > 0) && (mLoginUserPwdEt.getText().toString().trim().length() == LoginContext.getInstance().getUserInfo().verificationCode.length())) {
                //登录按钮可点击
                mUserLoginConfirmBtn.setEnabled(true);
            } else {
                //登录按钮不可点击
                mUserLoginConfirmBtn.setEnabled(false);
            }
        }
    };
    /**
     * 号码框的焦点监听
     */
    private View.OnFocusChangeListener mPhoneOnFocusChangeListener = new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (hasFocus) {
                //滚动到底部
                NestedScrollView nestedScrollView = findViewById(R.id.nested_scroll_view);
                nestedScrollView.smoothScrollTo(0, (int) nestedScrollView.getHeight());
            }
        }
    };
    /**
     * 密码框的焦点监听
     */
    private View.OnFocusChangeListener mPwdOnFocusChangeListener = new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (hasFocus) {
                //滚动到底部
                NestedScrollView nestedScrollView = findViewById(R.id.nested_scroll_view);
                nestedScrollView.smoothScrollTo(0, (int) nestedScrollView.getHeight());
            }
        }
    };

    @Override
    protected void onClickImpl(View view) {
        super.onClickImpl(view);

        switch (view.getId()) {

            //登陆
            case R.id.login_btn:
                //登陆前，更新输入框的数据
                mPresenter.setPhoneNumber(mLoginUserPhoneEt.getEditableText().toString());
                mPresenter.setVerificationCode(mLoginUserPwdEt.getEditableText().toString());
                //提交登陆操作
                mPresenter.submitLogin();
                break;
            //获取验证码
            case R.id.verification_code_btn:
                //先保存获取验证码的提示信息
                mGetVerificationCodeTipDefault = mVerificationCodeBtn.getText().toString();
                //Phone Number.
                String phoneNum = mLoginUserPhoneEt.getEditableText().toString();
                mPresenter.setPhoneNumber(phoneNum);
                mPresenter.verificationCode();
                break;

            case R.id.go_register_tv:
//                String hint = mLoginUserPhoneEt.getText().toString();
//                startActivityTransition(RegisterActivity.newIntent(this, hint), new View[]{mLoginUserPhoneEt, mLoginUserPwdEt, mUserLoginConfirmBtn}, new String[]{"name", "password", "submit"});
                break;

            case R.id.cancel_btn:
                //退出
                onBackPressed();
                break;
            default:

        }
    }

    @Override
    protected int inflateContentViewById() {
        return R.layout.login_activity;
    }

    @Override
    protected void initialize() {

        new LoginPresenter(new LoginRepository(), this);
    }

    @Override
    protected void initView(@Nullable Bundle savedInstanceState) {
        mUserLoginConfirmBtn = findViewById(R.id.login_btn);
        setCommonClickListener(mUserLoginConfirmBtn);

        setActionTitle("登陆");

        //获取验证码
        mVerificationCodeBtn = findViewById(R.id.verification_code_btn);
        setCommonClickListener(mVerificationCodeBtn);


        mPhoneInputLayout = findViewById(R.id.phone_group);
        mPwdInputLayout = findViewById(R.id.password_group);
        /*
        手机号、验证码、监听器
         */
        mLoginUserPhoneEt = findViewById(R.id.login_user_phone_et);
        mLoginUserPwdEt = findViewById(R.id.login_user_password_et);
        //添加监听器
        mLoginUserPhoneEt.addTextChangedListener(mTextWatcher);
        mLoginUserPwdEt.addTextChangedListener(mTextWatcher);
        //触发监听器
        mLoginUserPhoneEt.setText("");

        //软键盘监听
        mLoginUserPhoneEt.setOnFocusChangeListener(mPhoneOnFocusChangeListener);
        mLoginUserPwdEt.setOnFocusChangeListener(mPwdOnFocusChangeListener);

        //Display false.
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        TextView goRegisterTv = findViewById(R.id.go_register_tv);
        setCommonClickListener(goRegisterTv);


        Button cancelBtn = findViewById(R.id.cancel_btn);
        setCommonClickListener(cancelBtn);

    }

    @Override
    protected void initData() {

    }

    @Override
    protected void refreshData() {

    }

    @Override
    public void loginSuccess() {
        startActivity(new Intent(this, MainActivity.class));

        finish();
//        StartActivityFactory.startBillUnfinishedActivity(this, new Intent(this, BillUnfinishedActivity.class));
    }

    @Override
    public void loginError(String error) {
        mPhoneInputLayout.setError(error);
        mPwdInputLayout.setError(error);
    }

    @Override
    public void errorPhone(String error) {
        mPhoneInputLayout.setError(error);
    }

    @Override
    public void registerSuccess() {
        //Do nothing.
    }

    @Override
    public void setPresenter(LoginContract.Presenter presenter) {
        mPresenter = (LoginPresenter) presenter;
    }

    @Override
    public void startVerificationCount(String text) {
        mVerificationCodeBtn.setEnabled(false);
    }

    @Override
    public void updateVerificationCount(String text) {
        mVerificationCodeBtn.setText(text);
    }

    @Override
    public void stopVerificationCount(String text) {
        mVerificationCodeBtn.setText(mGetVerificationCodeTipDefault);
        mVerificationCodeBtn.setEnabled(true);
    }

    @Override
    public void showLoading() {
        if (mAppLoading == null) {
            mAppLoading = new AppLoadingDialogFragment();
        }
        mAppLoading.show(getSupportFragmentManager(), "loading");
    }

    @Override
    public void hideLoading() {
        if (mAppLoading != null) {
            mAppLoading.dismiss();
        }
    }

    @Override
    protected boolean isSupportHideOutside() {
        return true;
    }

    @Override
    protected List<View> hideSoftByEditViewIds() {
        List<View> hideViews = new ArrayList<>();

        hideViews.add(mLoginUserPhoneEt);
        hideViews.add(mLoginUserPwdEt);
        hideViews.add(mVerificationCodeBtn);
        return hideViews;
    }
}
