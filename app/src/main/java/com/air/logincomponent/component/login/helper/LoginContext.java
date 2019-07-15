package com.air.logincomponent.component.login.helper;

import android.text.TextUtils;

import com.air.logincomponent.common.manager.SharedPrefManager;
import com.google.gson.Gson;

/**
 * @author air on 2017/3/6.
 * <p>
 * 登陆控制的环境类
 * </p>
 * <p>
 * 登陆环境类控制着登陆状态，应该是全局只有一个实例对象
 * </p>
 * <p>
 * Usage:
 * 获取单例 {@link #getInstance()}
 * </p>
 */

public final class LoginContext implements Cloneable {

    /**
     * 用户状态：默认为未登录状态
     */
    private UserStatus mStatus = new LogoutStatus();
    /**
     * 用户信息
     */
    private AppUserInfo mUserInfo = new AppUserInfo();

    /**
     * 构造私有不允许外界直接创建对象
     */
    private LoginContext() {
    }

    /**
     * 获取单例对象
     *
     * @return instance
     */
    public static LoginContext getInstance() {
        return Holder.INSTANCE;
    }

    @Override
    protected LoginContext clone() throws CloneNotSupportedException {

        /*
        浅拷贝
         */
        LoginContext loginContext = (LoginContext) super.clone();
        loginContext.mStatus = this.mStatus;

        return loginContext;
    }

    /**
     * 设置用户的状态
     *
     * @param status
     *         {@link UserStatus}
     */
    public void setStatus(UserStatus status) {
        mStatus = status;
    }

    /**
     * 更新用户的信息
     *
     * @param userInfo
     */
    public void updateUserInfo(AppUserInfo userInfo) {
        mUserInfo = userInfo;
        //将本地存储的用户信息也一并更新
        saveUser();
    }

    /**
     * 清空当前的用户信息
     */
    public void clearUserInfo() {
        //清空本地缓存的用户信息
        SharedPrefManager.getInstance().remove(SharedPrefManager.FIELD_USER_INFO);
    }

    /**
     * 登出
     */
    public boolean logOut() {
        //设置全局状态为登出状态
        setStatus(new LogoutStatus());
        //清空用户信息
        clearUserInfo();
        return true;
    }

    /**
     * 是否自动登录，没有退出登录（退出App，但是没有手动点击退出也算是在登录中的状态）
     */
    public boolean isAutomaticLogin() {
        /*
        通过判断本地是否缓存者用户的信息，来决定用户是否登陆中
        （信息不为空那么为登录中，信息为空那么已经推出）【当用户退出时，要切记将本地的缓存信息清空】
         */
        return SharedPrefManager.getInstance().contains(SharedPrefManager.FIELD_USER_INFO);
    }

    /**
     * 保存数据到本地
     *
     * @return
     */
    private boolean saveUser() {
        Gson gson = new Gson();
        String json = gson.toJson(mUserInfo);
        return SharedPrefManager.getInstance().putString(SharedPrefManager.FIELD_USER_INFO, json);
    }

    /**
     * 从本地将数据恢复
     *
     * @return
     */
    public AppUserInfo restoreUser() {
        String json = SharedPrefManager.getInstance().getString(SharedPrefManager.FIELD_USER_INFO);
        Gson gson = new Gson();
        AppUserInfo userInfo = gson.fromJson(json, AppUserInfo.class);
        return userInfo;
    }

    public AppUserInfo getUserInfo() {
        return mUserInfo;
    }

    /**
     * 用户是否已经登录
     * 当前App用户是否在登录状态中
     *
     * @return whether has logined.
     */
    public boolean isLogined() {
        if (mStatus instanceof LoginedStatus) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 进行评论
     */
    public void comment() {
        mStatus.comment();
    }

    /**
     * 进行转发
     */
    public void forward() {
        mStatus.forward();
    }

    static class Holder {
        /**
         * Singleton
         */
        private static final LoginContext INSTANCE = new LoginContext();
    }

    /**
     * 用户的信息
     */
    public static class AppUserInfo {
        /**
         * Token
         * Default:7c5e6e7ab5f04b2dba2ad426baf0dbdc
         */
        public String accessToken = "";
        /**
         * Refresh token.
         */
        public String refreshToken = "";
        /**
         * Token type
         * Default:Bearer
         */
        public String tokenType = "";
        /**
         * Expires
         */
        public String expiresIn = "2018-12-02 18:12:24";
        /**
         * Expire
         */
        public String expire = "";
        /**
         * Scope
         */
        public String scope = "";
        /**
         * Sub
         */
        public String sub = "";
        /**
         * MemberId
         */
        public String memberId = "124567890";
        /**
         * Name
         */
        private String name = "用户名";
        /**
         * User id.
         */
        private String userId = "";
        /**
         * 手机号码
         */
        private String phoneNumber = "XXXXXXXXXXX";
        /**
         * 登陆时与手机号码相对应的验证码
         * 默认值：定义了验证码的长度，以便应用进行校验
         */
        public String verificationCode = "123456";

        /**
         * 用户头像
         */

        public String headPortrait;
        /**
         * 当前所属的公司的信息
         */
        private String companyId = "";
        private String companyName = "";

        /**
         * Get Authorization.
         *
         * @return Authorization.
         */
        public String getAuthorizationValue() {
            if (TextUtils.isEmpty(tokenType) || TextUtils.isEmpty(accessToken)) {
                //返回空，代表其它地方不使用Token
                return null;
            }
            //Authorization value = tokenType value（首字母必须大写） + 空格 + token value.

            CharSequence oldTokenTypeFirst = tokenType.subSequence(0, 1);
            String upperTokenType = tokenType.toUpperCase();
            String newTokenTypeFirst = upperTokenType.substring(0, 1);

            String newTokenType = tokenType.replace(oldTokenTypeFirst, newTokenTypeFirst);
            StringBuffer buffer = new StringBuffer();
            //追加首字母大写的tokenType.
            buffer.append(newTokenType);
            //追加空格.
            buffer.append(" ");
            //追加token
            buffer.append(this.accessToken);
            String authorization = buffer.toString();
            return authorization;
        }

        public String getCompanyId() {
            return this.companyId;
        }

        public void setCompanyId(String companyId) {
            if (companyId == null) {
                return;
            }
            this.companyId = companyId;
        }

        public String getCompanyName() {
            return this.companyName;
        }

        public void setCompanyName(String companyName) {
            if (companyName == null) {
                return;
            }
            this.companyName = companyName;
        }

        public String getUserId() {
            return this.userId;
        }

        public void setUserId(String userId) {
            if (userId == null) {
                return;
            }
            this.userId = userId;
        }

        public String getPhoneNumber() {
            return this.phoneNumber;
        }

        public void setPhoneNumber(String phoneNumber) {
            if (TextUtils.isEmpty(phoneNumber)) {
                return;
            }
            this.phoneNumber = phoneNumber;
        }

        public String getName() {
            return this.name;
        }

        public void setName(String name) {
            if (name == null) {
                return;
            }
            this.name = name;
        }

        public String getAuthorizationKey() {
            return "Authorization";
        }
    }
}


