package com.air.logincomponent.common.config;

/**
 * <p>
 * @author air 2017/3/6.
 * </p>
 */
public final class AppApiFlag {

    private AppApiFlag(){}

    public static final String RELEASE_URL = "https://tms.api.ybm100.com/";
    /**
     * 测试服务器地址
     * TODO:(更改为您的测试地址)
     */
    public static final String DEBUG_URL = "http://101.201.77.210/";

    /**
     * 正式/发布地址
     * TODO:(更改为您的正式/发布地址)
     */
    public static final String BASE_URL = getUrl();

    /**
     * 根据当前打包的环境【debug、release自动获取测试、正式的地址】
     *
     * @return 当前环境所对应的服务器地址
     */
    static String getUrl() {
        boolean isDebug = EnvironmentConfig.URL_DEBUG;
        if (isDebug) {
            //测试
            return DEBUG_URL;
        } else {
            //正式
            return RELEASE_URL;
        }
    }


    /**
     * 请求成功
     */
    public static final String RESULT_OK = "200";
    /**
     * 未认证用户身份
     */
    public static final int RESULT_UNAUTHORIZED = 401;
    /**
     * 用户身份过期（Token过期）
     */
    public static final String RESULT_TOKEN_EXPIRED = "40101";

    /**
     * 请求失败系统繁忙
     */
    public static final String RESULT_ERROR = "-1";
    /**
     * 登陆信息超时
     */
    public static final String RESULT_ERROR_LOGIN_OUT_TIME = "50201";
    /**
     * 请求方式不正确
     */
    public static final String RESULT_ERROR_REQUEST_TYPE_ERROR = "50101";
    /**
     * 账号非法（不存在账号信息）
     */
    public static final String RESULT_ERROR_ACCOUNT_NUMBER_NULL = "50010";
    /**
     * Token过期
     */
    public static final String RESULT_ERROR_TOKEN_OUT_TIME = "50002";
    /**
     * 用户信息不存在
     */
    public static final String RESULT_ERROR_USER_INFO_NULL = "43101";
    /**
     * 无效的时间范围
     */
    public static final String RESULT_ERROR_OUT_TIME = "40005";
    /**
     * TimeStamp已过有效期
     */
    public static final String RESULT_ERROR_STAMP_OUT_TIME = "40004";
    /**
     * 数据参数缺失
     */
    public static final String RESULT_ERROR_PARAMES_LESS = "40003";
    /**
     * 数据类型错误
     */
    public static final String RESULT_ERROR_PARAMSE_TYPE = "40002";
    /**
     * 请求参数不合法（为空或格式不正确）
     */
    public static final String RESULT_ERROR_PARAMS_NULL = "40001";
    /**
     * 服务器的时间格式
     */
    public static final String WEB_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

}
