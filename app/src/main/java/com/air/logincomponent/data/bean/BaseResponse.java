package com.air.logincomponent.data.bean;

import java.io.Serializable;

/**
 * @author pd_liu on 2017/3/6.
 * <p>
 * BaseResponse
 * </p>
 */

public class BaseResponse implements Serializable {

    private int status;
    private String message;

    public String getCode() {
        return getStatus();
    }

    public String getStatus() {
        return String.valueOf(this.status);
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return "BaseResponse{" +
                "code='" + getCode() + '\'' +
                ", message='" + getMessage() + '\'' +
                '}';
    }
}
