package com.air.logincomponent.component.welcome;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.view.View;

import com.air.logincomponent.R;
import com.air.logincomponent.ui.base.AppCommonActivity;
import com.air.logincomponent.ui.factory.StartActivityFactory;
import com.air.logincomponent.ui.widget.CircleProgressTextView;

/**
 * @author air
 *         <p>
 *         欢迎界面
 *         </p>
 *         <p>
 *         1、判断是否已经登录（自动登录、手动登录）
 *         2、判断版本号（展示App介绍）
 *         </p>
 *         <p>
 *         大致流程：
 *         1、首先获取版本号并与缓存的版本好比对，校验是否展示版本升级信息引导
 *         2、展示完毕后，更新当前的版本，并缓存到本地
 *         3、引导完毕，校验登录功能（自动登录、手动登录）
 *         判断——>自动登录、手动登录的依据是：缓存到本地的登录信息，因此登录成功后，会将信息更新到本地
 *         </p>
 */
public class WelcomeActivity extends AppCommonActivity {
    /**
     * 倒计时进度的时间（30 * 100）
     */
    private static final int COUNT_DOWN = 30;
    /**
     * 倒计时的单位（毫秒）
     */
    private static final int COUNT_DOWN_UNIT = 100;
    /**
     * 进度单位
     */
    private static final int CIRCLE_PROGRESS_UNIT = 12;
    /**
     * 标记位,标记是否已经开启进入下一个页面(避免重复进入)
     */
    private boolean mHasEnterNextPage;

    @Override
    protected int inflateContentViewById() {
        return R.layout.welcome_activity;
    }

    @Override
    protected void initialize() {

    }

    @Override
    protected void initView(@Nullable Bundle savedInstanceState) {


        final CircleProgressTextView circleProgressTextView = (CircleProgressTextView) findViewById(R.id.circle_progress_txt);
        circleProgressTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //进入到主页面中
                enterMainPage();
            }
        });

        // FIXME: 2017/11/22 待修正：线程的创建规范
        //创建线程进行倒计时
        new Thread(new Runnable() {
            @Override
            public void run() {

                int number = 0;
                //设置初始进度值.
                circleProgressTextView.setProgress(number);
                while (number < COUNT_DOWN) {
                    //暂停一秒
                    try {
                        Thread.sleep(COUNT_DOWN_UNIT);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    number++;
                    //设置当前进度
                    circleProgressTextView.setProgress(number * CIRCLE_PROGRESS_UNIT);
                }
                //进度加载完毕，进行“跳过”操作
                enterMainPage();

            }
        }).start();

    }

    @Override
    protected void initData() {

    }

    @Override
    protected void refreshData() {

    }

    /**
     * 进入到主页面
     */
    private void enterMainPage() {
        //避免重复操作：进入下一个页面
        if (mHasEnterNextPage) {
            return;
        }
        mHasEnterNextPage = true;

        /*
        进行判断版本信息
         */
        //这里进行判断版本信息，是否加载引导页面
        if (VersionHelper.isShowFlash(this)) {
            startActivity(new Intent(this, SplashActivity.class));
        } else {
            //校验，是否自动登陆、手动登录
            StartActivityFactory.startLoginActivity(this);
        }

        finish();
    }

    @Override
    public void onBackPressed() {
        /*
        Do nothing.
         */
    }
}
