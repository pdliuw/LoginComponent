package com.air.logincomponent.component.login;

import com.air.logincomponent.ui.base.BaseView;

/**
 * @author air on 2017/3/6.
 *         <p>
 *         LoginContract
 *         </p>
 */

public interface LoginContract {

    interface Presenter{
        void verificationCode();

        void submitLogin();

        void submitRegister();

        void startVerificationCount();

        void stopVerificationCount();
    }

    interface View extends BaseView<Presenter> {

        void startVerificationCount(String text);

        void updateVerificationCount(String text);

        void stopVerificationCount(String text);

        void loginSuccess();

        void loginError(String error);

        void errorPhone(String error);

        void registerSuccess();

        void showLoading();

        void hideLoading();
    }
}
