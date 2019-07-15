package com.air.logincomponent.ui.base;

/**
 * @author pd_liu on 2018/3/8.
 *         <p>
 *         BaseView
 *         </p>
 */

public interface BaseView<T> {

    /**
     * 传递Presenter对象
     *
     * @param presenter presenter.
     */
    void setPresenter(T presenter);
}
