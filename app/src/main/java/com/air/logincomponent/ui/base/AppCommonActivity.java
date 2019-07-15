package com.air.logincomponent.ui.base;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.transition.ChangeBounds;
import android.transition.Explode;
import android.transition.Fade;
import android.transition.Slide;
import android.util.TypedValue;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;


import com.air.logincomponent.R;
import com.air.logincomponent.common.manager.SendMessageManager;
import com.air.logincomponent.common.util.LogUtil;

import java.util.List;

/**
 * @author air on 2017/3/6.
 * <p>
 * Activity的基类
 * </p>
 * <p>
 * Usage:
 * 1、发送广播：{@link #sendBroadcastMsg(Class)}
 * 2、接受广播重写：{@link #receiverBroadcastMsg(Intent)}
 * 3、加载Activity content layout {@link #inflateContentViewById()}
 * 4、侧滑退出Activity {@link #isSupportSlideBack()}
 * 5、设置点击事件：{@link #setCommonClickListener(View)}
 * 6、接受点击事件：{@link #onClickImpl(View)}
 * 7、设置Activity action title：{@link #setActionTitle(String)}
 * 8、是否展示头部布局：{@link #showAppActionBar()}
 * 9、是否支持点击焦点控件外的区域进行隐藏软键盘：{@link #isSupportHideOutside()}  焦点控件的设置 {....}
 * 10、启动Activity：附带默认动画{@link #startActivityByDefaultAnim(Intent)}
 * Transition ---> 启动位置调用：{@link #startActivityTransition(Intent)}
 * 目标位置调用：{@link #showDefaultActivityTransition()}{@link #showFadeActivityTransition()}
 * {@link #showBoundActivityTransition()} {@link #showExplodeActivityTransition()}.
 * </p>
 */

public abstract class AppCommonActivity extends BaseActivity {

    /**
     * Animation default duration.
     */
    private static final int ANIMATION_DURATION_DEFAULT = 500;
    /**
     * Tag
     */
    protected String TAG_LOG = this.getClass().getSimpleName();
    /**
     * 广播接收者接受广播信息
     */
    BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            LogUtil.i(TAG_LOG, "receiver" + intent.getAction());
            //接收广播消息
            receiverBroadcastMsg(intent);
        }
    };
    /**
     * 侧滑窗口帮助类
     */
//    private SwipeWindowHelper mSwipeWindowHelper;
    /**
     * 本地广播进行通信
     */
    private SendMessageManager mSendMessageManager;
    /**
     * Click listener 的统一管理
     */
    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            onClickImpl(v);
        }
    };
    private View.OnClickListener mOnNavClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            onNavClickImp(v);
        }
    };

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        LogUtil.i(TAG_LOG, "onPostCreate");
        //Crash.
        //        CrashHandler.getInstance().startHandler(this);
        //        CrashHandler.getInstance().setCurrentActivity(this);
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        LogUtil.i(TAG_LOG, "onPostResume");
    }

    /**
     * 启动页面附带默认动画
     *
     * @param intent
     *         意图
     *
     * @see #startActivityTransition(Intent) .
     * 已废弃 请查看替代者：{@link //StartActivityFactory}
     */
    @Deprecated
    protected void startActivityByDefaultAnim(Intent intent) {
        startActivity(intent);
//        overridePendingTransition(R.anim.app_activity_open_enter_slide_anim, R.anim.app_activity_open_exit_fade_anim);
    }

    /**
     * 支持启动页面带动画
     * {@link #showDefaultActivityTransition()}
     *
     * @param intent
     *         意图
     */
    public void startActivityTransition(Intent intent) {
        startActivity(intent, ActivityOptionsCompat.makeSceneTransitionAnimation(this).toBundle());
    }

    public void startActivityTransition(Intent intent, View view, String sharedName) {
        startActivity(intent, ActivityOptionsCompat.makeSceneTransitionAnimation(this, view, sharedName).toBundle());
    }

    public void startActivityTransition(@NonNull Intent intent, @NonNull View[] views, @NonNull String[] sharedNames) {

        if (views.length != sharedNames.length || views.length <= 0 || sharedNames.length <= 0) {
            return;
        }
        Pair<View, String>[] sharedElements = new Pair[views.length];

        for (int i = 0; i < views.length; i++) {
            //Create pair.
            Pair<View, String> pair = Pair.create(views[i], sharedNames[i]);

            //Array data.
            sharedElements[i] = pair;
        }

        startActivity(intent, ActivityOptionsCompat.makeSceneTransitionAnimation(this, sharedElements).toBundle());
    }

    @Override
    public void onBackPressed() {

        finishRelease();
    }

    /**
     * 关闭并释放
     */
    protected void finishRelease() {
        if (isFinishing()) {
            //如果正在finishing current activity，那么返回
            return;
        }
        supportFinishAfterTransition();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        //增加，点击焦点外的控件，进行收起软键盘
        if (isSupportHideOutside()) {
            if (ev.getAction() == MotionEvent.ACTION_DOWN) {
                //当前获取焦点的View
                View v = getCurrentFocus();
                if (v instanceof EditText) {
                    if (!isTouchView(hideSoftByEditViewIds(), ev)) {
                        // hide input method
                        InputMethodManager inputMethod = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                        inputMethod.hideSoftInputFromWindow(getWindow().findViewById(Window.ID_ANDROID_CONTENT).getWindowToken(), 0);
                    }
                }
            }
        }
        //当前页面是否支持手势滑动退出，如果不是，那么调用默认的
        if (!isSupportSlideBack()) {
            return super.dispatchTouchEvent(ev);
        }
        /*判断手势退出操作是否已存在，执行对应的操作方法*/
//        if (mSwipeWindowHelper == null) {
//            mSwipeWindowHelper = new SwipeWindowHelper(getWindow());
//        }
        boolean flag = super.dispatchTouchEvent(ev);
        return flag;
    }

    /**
     * Is touch view.
     *
     * @param views
     *         touch views.
     * @param ev
     *         event.
     *
     * @return is touch view.
     */
    private boolean isTouchView(List<View> views, MotionEvent ev) {
        if (views == null || views.isEmpty()) {
            return false;
        }

        int[] location = new int[2];

        for (int i = 0; i < views.size(); i++) {
            View view = views.get(i);
            view.getLocationOnScreen(location);
            int x = location[0];
            int y = location[1];
            //In view x.
            boolean inWidth = ev.getX() > x && ev.getX() < (x + view.getWidth());
            //In view y.
            boolean inHeight = ev.getY() > y && ev.getY() < (y + view.getHeight());
            if (inWidth && inHeight) {
                return true;
            }
        }

        return false;
    }

    /**
     * subclass override
     *
     * @return EditText Views
     */
    protected List<View> hideSoftByEditViewIds() {
        return null;
    }


    /**
     * 是否支持侧滑退出
     *
     * @return 默认支持
     */
    protected boolean isSupportSlideBack() {
        return true;
    }

    /**
     * 设置点击事件
     *
     * @param view
     *         v
     *
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
                if (theme.resolveAttribute(android.R.attr.selectableItemBackgroundBorderless, typedValue, true)) {
                    view.setBackgroundResource(typedValue.resourceId);
                }
                view.setPadding(left, top, right, bottom);
            }
        }
        view.setOnClickListener(mOnClickListener);

    }

    protected void setClickListener(View view) {
        if (view == null) {
            return;
        }
        view.setOnClickListener(mOnClickListener);
    }

    /**
     * @param view
     *         v
     *
     * @see #setCommonClickListener(View)
     * 接收点击事件
     */
    protected void onClickImpl(View view) {
        int id = view.getId();

    }

    /**
     * Toolbar back click.
     *
     * @param view
     */
    protected void onNavClickImp(View view) {
        onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        /*
        每次Activity一创建就会执行，一般只执行一次；
         */
        /*
        invalidateOptionsMenu()刷新menu里的选项里内容，它会调用onCreateOptionsMenu(Menu menu)方法
         */
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        /*
        每次menu被打开时，该方法就会执行一次；
         */
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        /*
        每次menu菜单项被点击时，该方法就会执行一次；
         */
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        /*
        创建控件绑定的上下文菜单menu，根据方法里的View参数识别是哪个控件绑定
         */
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        /*
        点击控件绑定的上下菜单menu的内容项
         */
        return super.onContextItemSelected(item);
    }

    /**
     * 发送广播消息
     *
     * @param cls
     *
     * @see #receiverBroadcastMsg(Intent) .
     */
    protected void sendBroadcastMsg(Class cls) {
        mSendMessageManager.sendBroadcastMsg(cls);
    }

    /**
     * 发送广播消息，并携带类型参数
     *
     * @param cls
     * @param type
     *         携带的类型参数
     *
     * @see #receiverBroadcastMsg(Intent) .
     */
    protected void sendBroadcastMsg(Class cls, String type) {
        mSendMessageManager.sendBroadcastMsg(cls.getName(), type);
    }

    protected void sendBroadcastMsgAllInApp() {
        mSendMessageManager.sendBroadcastAllInApp();
    }

    /**
     * 此方法接受广播消息
     *
     * @param intent
     *
     * @see #sendBroadcastMsg(Class) .
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
     */
    protected void receiverBroadcastInAll() {

    }

    protected boolean showAppActionBar() {
        return true;
    }

    /**
     * 是否支持 点击焦点外区域进行收起软键盘
     *
     * @return true or false;
     */
    protected boolean isSupportHideOutside() {
        return false;
    }

    /**
     * Inflate the activity content view.
     *
     * @return the activity content resourceId.
     */
    protected abstract int inflateContentViewById();

    /**
     * Inflate the activity content view.
     *
     * @return the activity content view object.
     */
    protected View inflateContentViewByView() {
        return null;
    }

    /**
     * 初始化默认值
     */
    protected abstract void initialize();

    /**
     * 初始化视图
     *
     * @param savedInstanceState
     */
    protected abstract void initView(@Nullable Bundle savedInstanceState);

    /**
     * 初始化数据
     */
    protected abstract void initData();

    /**
     * 刷新当前页面时所需要加载的数据
     * 当前页面第一次加载时需要的数据
     * 此方法，需要手动的调用才会执行
     */
    protected abstract void refreshData();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogUtil.i(TAG_LOG, "onCreate-----------------------");
        //竖屏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
//        /*
//        设置样式
//         */
//        setTheme(ThemeContext.getUsingTheme());
        /*
        setContentView
         */
        if (inflateContentViewByView() == null) {
            setContentView(inflateContentViewById());
        } else {
            setContentView(inflateContentViewByView());
        }

        //EventBus用于组件间的通信
//        EventBus.getDefault().register(this);

        //StatusBar
        View statusBar = findViewById(R.id.status_bar);
        //Toolbar
        Toolbar toolbar = findViewById(R.id.tool_bar);
        if (toolbar == null) {
            LogUtil.w(TAG_LOG, "请在xml中补齐actionBar");
        } else {
            setSupportActionBar(toolbar);
            ActionBar actionBar = getSupportActionBar();
            //不显示默认的Title
            actionBar.setDisplayShowTitleEnabled(true);
            //不显示默认的Home（an activity icon or logo.）
            actionBar.setDisplayShowHomeEnabled(false);
            //是否显示左上角的返回按钮
            actionBar.setDisplayHomeAsUpEnabled(true);
            toolbar.setNavigationOnClickListener(mOnNavClickListener);

            //Title
            TextView titleTv = findViewById(R.id.title_tv);
            titleTv.setVisibility(View.VISIBLE);
            setCommonClickListener(titleTv);
            if (titleTv == null) {
                LogUtil.w(TAG_LOG, "Title空");
            } else {
                titleTv.setText(getTitle());
                titleTv.setVisibility(View.GONE);
                actionBar.setTitle(getTitle());
            }


            if (showAppActionBar()) {

            } else {
                toolbar.setVisibility(View.GONE);
                statusBar.setVisibility(View.GONE);
            }
        }

        if (statusBar == null) {
            LogUtil.w(TAG_LOG, "StatusBar为空");
        } else {
            Rect frame = new Rect();
            getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        }

        showFadeActivityTransition();

        /*
        注册通信机制
         */
        mSendMessageManager = new SendMessageManager(this);
        mSendMessageManager.registerBroadcastMsg(mBroadcastReceiver, this.getClass());


        initialize();
        initView(savedInstanceState);
        initData();

        //JPush(如果不调用此方法，将会导致App无法接收推送消息)
        //2019年3月11日 start
        //PushAgent.getInstance(this).onAppStart();
    }

    @Override
    protected void onStart() {
        super.onStart();
        LogUtil.i(TAG_LOG, "onStart");
//        CrashHandler.getInstance().startHandler(this);
//        CrashHandler.getInstance().setCurrentActivity(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        LogUtil.i(TAG_LOG, "onResume");
        //UMeng_统计
        //MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        LogUtil.i(TAG_LOG, "onPause");
        //UMeng_统计
        //MobclickAgent.onPause(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        LogUtil.i(TAG_LOG, "onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LogUtil.i(TAG_LOG, "onDestroy");

        //解绑EventBus
//        EventBus.getDefault().unregister(this);
        //解绑广播
        if (mSendMessageManager != null) {
            mSendMessageManager.unRegisterBroadcastMsg();
        }
    }

    /**
     * 显示AppBarGroup[Status、Toolbar]
     */
    protected void showAppBar() {
        ViewGroup barGroup = findViewById(R.id.app_bar_group);
        barGroup.setVisibility(View.VISIBLE);
    }

    /**
     * 隐藏AppBarGroup[Status、Toolbar]
     */
    protected void hideAppBar() {
        ViewGroup barGroup = findViewById(R.id.app_bar_group);
        barGroup.setVisibility(View.GONE);
    }

    /**
     * 显示状态栏
     */
    protected void showStatus() {
        View status = findViewById(R.id.status_bar);
        status.setVisibility(View.VISIBLE);
    }

    /**
     * 隐藏状态栏
     */
    protected void hideStatus() {
        View status = findViewById(R.id.status_bar);
        status.setVisibility(View.GONE);
    }

    protected void setActionTitle(String title) {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(title);
    }

    protected Toolbar getToolBar() {
        Toolbar toolbar = findViewById(R.id.tool_bar);
        return toolbar;
    }

    /**
     * Add current activity with transition animation.
     */
    protected void showDefaultActivityTransition() {
        //Animation.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setEnterTransition(new Slide(Gravity.RIGHT).setDuration(ANIMATION_DURATION_DEFAULT));
        }
    }

    /**
     * Add current activity with transition animation.
     */
    protected void showBoundActivityTransition() {
        //Animation.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setEnterTransition(new ChangeBounds().setDuration(ANIMATION_DURATION_DEFAULT));
        }
    }

    /**
     * Add current activity with transition animation.
     */
    protected void showFadeActivityTransition() {
        //Animation.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setEnterTransition(new Fade().setDuration(ANIMATION_DURATION_DEFAULT));
        }
    }

    /**
     * Add current activity with transition animation.
     */
    protected void showExplodeActivityTransition() {
        //Animation.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setEnterTransition(new Explode().setDuration(ANIMATION_DURATION_DEFAULT));
        }
    }

    /**
     * Add current activity with transition animation.
     */
    protected void showSharedElementActivityTransition() {
        //Animation.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().setSharedElementEnterTransition(new Fade().setDuration(ANIMATION_DURATION_DEFAULT));
        }
    }
}
