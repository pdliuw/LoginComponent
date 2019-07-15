package com.air.logincomponent.ui.base;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityOptionsCompat;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.air.logincomponent.common.manager.SendMessageManager;
import com.air.logincomponent.common.util.LogUtil;

/**
 * @author pd_liu on 2017/3/6.
 *         <p>
 *         App common fragment.
 *         </p>
 *         <p>
 *         1、Fragment 首次对用户可见 :{@link #onFragmentFirstVisibleToUser()}
 *         2、点击事件：{@link #setCommonClickListener(View)} {@link #setClickListener(View)}
 *         3、点击事件回调：{@link #onClickImpl(View)}
 *         4、接收广播：{@link #receiverBroadcastMsg(Intent)}
 *         5、启动Activity：{@link #startActivityTransition(Intent)}.
 *         6、默认为图片点击时，添加前景
 *         </p>
 */

public abstract class AppCommonFragment extends BaseFragment {

    protected String TAG_LOG = getClass().getSimpleName();
    /**
     * 广播通信
     */
    private SendMessageManager mSendMessageManager;

    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            LogUtil.i(TAG_LOG, "receiver" + intent.getAction());
            //接收广播消息
            receiverBroadcastMsg(intent);
        }
    };
    /**
     * Click listener.
     */
    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            onClickImpl(v);
        }
    };
    /**
     * 是否已经创建过视图
     */
    private boolean mHasCreatedView;
    /**
     * 是否是首次对用户可见
     */
    private boolean mIsFirstVisible;

    /**
     * 设置点击事件
     *
     * @param view v
     * @see #onClickImpl(View)
     */
    protected void setCommonClickListener(View view) {
        if (view == null) {
            return;
        }
        if (!view.isClickable()) {
            if (view.getBackground() == null) {
                TypedValue typedValue = new TypedValue();
                Resources.Theme theme = view.getContext().getTheme();
                int top = view.getPaddingTop();
                int bottom = view.getPaddingBottom();
                int left = view.getPaddingLeft();
                int right = view.getPaddingRight();
                if (theme.resolveAttribute(android.R.attr.selectableItemBackground, typedValue, true)) {
                    view.setBackgroundResource(typedValue.resourceId);
                }
                view.setPadding(left, top, right, bottom);
            }
        }
        view.setOnClickListener(mOnClickListener);
    }

    protected void setClickListener(View view) {
        view.setOnClickListener(mOnClickListener);
    }

    /**
     * @param view v
     * @see #setCommonClickListener(View)
     * 接收点击事件
     */
    protected void onClickImpl(View view) {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        LogUtil.i(TAG_LOG, "onCreateView");
        View v = LayoutInflater.from(getContext()).inflate(inflateContentViewById(), container, false);
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //已经创建完视图
        mHasCreatedView = true;
        //是否是第一次可见
        mIsFirstVisible = true;
        LogUtil.i(TAG_LOG, "onViewCreated");
        //初始化外部传递的操作
        initialize(savedInstanceState);
        //初始化此页面的视图
        initView(view, savedInstanceState);
        //延迟初始化视图
        lazyInitialize();
        //初始化此页面的数据
        initData();


        /*
        注册通信机制
         */
        //广播
        mSendMessageManager = new SendMessageManager(this.getContext());
        mSendMessageManager.registerBroadcastMsg(mBroadcastReceiver, this.getClass());


    }

    void lazyInitialize() {
        if (mHasCreatedView && getUserVisibleHint() && mIsFirstVisible && nonNullValue()) {
            onFragmentVisibleToUser(getUserVisibleHint());
            onFragmentVisibleToUser(getUserVisibleHint(), mIsFirstVisible);
            onFragmentFirstVisibleToUser();
            mIsFirstVisible = false;
        }

        //visible to user.
        if (mHasCreatedView && !mIsFirstVisible) {

            onFragmentVisibleToUser(getUserVisibleHint());
            onFragmentVisibleToUser(getUserVisibleHint(), mIsFirstVisible);
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        //延迟初始化视图
        lazyInitialize();

    }

    /**
     * Inflate the fragment content view.
     *
     * @return the activity content resourceId.
     */
    protected abstract int inflateContentViewById();

    /**
     * 初始化创建对象的初始操作
     *
     * @param savedInstanceState bundle.
     */
    protected abstract void initialize(@Nullable Bundle savedInstanceState);

    /**
     * 初始化视图
     *
     * @param view               rootView
     * @param savedInstanceState bundle.
     */
    protected abstract void initView(@NonNull View view, @Nullable Bundle savedInstanceState);

    /**
     * Callback that Fragment first visible to user.
     */
    protected void onFragmentFirstVisibleToUser() {
        //初始化此页面用于刷新的数据
        refreshData();
    }

    /**
     * Callback that Fragment visible to user.
     */
    protected void onFragmentVisibleToUser(boolean isVisibleToUser) {
    }

    /**
     * Callback that Fragment visible to user.
     */
    protected void onFragmentVisibleToUser(boolean isVisibleToUser, boolean isFirstVisible) {
    }

    protected boolean nonNullValue() {
        return true;
    }

    /**
     * 初始化数据
     */
    protected abstract void initData();

    /**
     * 刷新当前页面时所需要加载的数据
     * 当前页面第一次加载时需要的数据
     */
    protected abstract void refreshData();

    @Override
    public void onStart() {
        super.onStart();
        LogUtil.i(TAG_LOG, "onStart");
    }

    @Override
    public void startActivity(Intent intent) {
        startActivityTransition(intent);
    }

    @Override
    public void onResume() {
        super.onResume();
        LogUtil.i(TAG_LOG, "onResume");
    }

    @Override
    public void onPause() {
        super.onPause();
        LogUtil.i(TAG_LOG, "onPause");
    }

    @Override
    public void onStop() {
        super.onStop();
        LogUtil.i(TAG_LOG, "onStop");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        LogUtil.i(TAG_LOG, "onDestroyView");
    }

    @Override
    public void onDestroy() {

        //解绑广播
        if (mSendMessageManager != null) {
            mSendMessageManager.unRegisterBroadcastMsg();
        }
        super.onDestroy();
        LogUtil.i(TAG_LOG, "onDestroy");
    }

    /**
     * 此方法接受广播消息
     *
     * @param intent
     * @see {@link SendMessageManager#sendBroadcastMsg(Class)}.
     */
    protected void receiverBroadcastMsg(Intent intent) {
        if (intent.getAction() == SendMessageManager.ALL_IN_APP) {
            receiverBroadcastInAll();
        }
        receiverBroadcastMsg(intent, intent.getStringExtra(SendMessageManager.TYPE_FLAG));
    }

    protected void receiverBroadcastMsg(Intent intent, String type) {

    }

    /**
     * 接收App范围内的全局广播
     *
     * @deprecated
     */
    protected void receiverBroadcastInAll() {

    }

    protected void startActivityTransition(Intent intent) {
        startActivity(intent, ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity()).toBundle());
    }

    public void startActivityTransition(Intent intent, View view, String sharedName) {
        startActivity(intent, ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(), view, sharedName).toBundle());
    }
}
