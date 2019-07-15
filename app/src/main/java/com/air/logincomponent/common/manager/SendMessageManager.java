package com.air.logincomponent.common.manager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.content.LocalBroadcastManager;

/**
 * @author pd_liu on 2017/3/6.
 *         <p>
 *         基于 {@link LocalBroadcastManager} 封装通信机制
 *         </p>
 *         <p>
 *         Usage:
 *         1、注册广播{@link #registerBroadcastMsg(BroadcastReceiver, Class)}
 *         2、发送广播消息{@link #sendBroadcastMsg(Class)} {@link #sendBroadcastMsg(String)}
 *         3、解绑广播{@link #unRegisterBroadcastMsg()}
 *         4、发送整个App范围内的全局广播{@link #sendBroadcastAllInApp()}
 *         </p>
 */

public class SendMessageManager {

    /**
     * All in app can receive the broadcast.
     */
    public static final String ALL_IN_APP = "all_in_app";
    /**
     * Send broadcast with type flag
     */
    public static final String TYPE_FLAG = "send_broadcast_with_type_flag";
    private static final String TAG_LOG = "SendMessageManager";
    /**
     * 本地广播
     */
    private LocalBroadcastManager mLocalBroadcastManager;

    /**
     * 广播接收者
     */
    private BroadcastReceiver mBroadcastReceiver;

    private Context mContext;

    public SendMessageManager(Context context) {
        mContext = context;
    }

    /**
     * 注册广播
     *
     * @param broadcastReceiver 需要广播接收者
     * @param cls               intentFilter action class
     */
    public void registerBroadcastMsg(BroadcastReceiver broadcastReceiver, Class cls) {

        /*
        避免多次注册
         */
        if (mLocalBroadcastManager == null) {
            //创建广播过滤器
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(cls.getName());
            intentFilter.addAction(ALL_IN_APP);
            //存储广播对象
            mBroadcastReceiver = broadcastReceiver;

            //注册本地广播监听
            mLocalBroadcastManager = LocalBroadcastManager.getInstance(mContext);
            mLocalBroadcastManager.registerReceiver(mBroadcastReceiver, intentFilter);
        } else {
//            LogUtil.e(TAG_LOG, StringUtil.getStringById(mContext, R.string.error_tip));
        }

    }

    /**
     * 发送广播
     *
     * @param cls 所发送广播的接收者(目标)
     * @see #sendBroadcastMsg(String).
     */
    public void sendBroadcastMsg(Class cls) {
        sendBroadcastMsg(cls.getName());
    }

    /**
     * 发送一个广播，使此广播可以在整个App内接收
     */
    public void sendBroadcastAllInApp() {
        sendBroadcastMsg(ALL_IN_APP);
    }

    /**
     * @param action action.
     * @see #sendBroadcastMsg(Class).
     */
    public void sendBroadcastMsg(String action) {
        Intent intent = new Intent();
        intent.setAction(action);
        mLocalBroadcastManager.sendBroadcast(intent);
    }

    public void sendBroadcastMsg(String action, String type) {
        Intent intent = new Intent();
        intent.setAction(action);
        intent.putExtra(TYPE_FLAG, type);
        mLocalBroadcastManager.sendBroadcast(intent);
    }

    /**
     * 解除广播事件绑定
     */
    public void unRegisterBroadcastMsg() {
        mLocalBroadcastManager.unregisterReceiver(mBroadcastReceiver);
    }
}
