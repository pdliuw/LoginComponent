package com.air.logincomponent.component.welcome;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.air.logincomponent.R;
import com.air.logincomponent.ui.base.AppCommonActivity;
import com.air.logincomponent.ui.factory.StartActivityFactory;

/**
 * @author pd_liu 2018年4月26日
 *         <p>
 *         1、页面滑动进行展示信息
 *         2、最后一页被点击后，开启登录校验
 *         3、最后一页被点击后，将最新版本号保存
 *         </p>
 */
public class SplashActivity extends AppCommonActivity {

    private ViewPager mViewPager;

    private int[] mSplashDrawableId = new int[]{R.mipmap.welcome01, R.mipmap.welcome02, R.mipmap.welcome03, R.mipmap.welcome04, R.mipmap.welcome05};

    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            StartActivityFactory.startLoginActivity(SplashActivity.this);

            /*
            跳转后，将最新版本号缓存到本地
             */
            VersionHelper.saveVersion(SplashActivity.this);
            //销毁本页面
            finish();
        }
    };

    /**
     * Inflate the activity content view.
     *
     * @return the activity content resourceId.
     */
    @Override
    protected int inflateContentViewById() {
        return R.layout.welcome_splash_activity;
    }

    /**
     * 初始化默认值
     */
    @Override
    protected void initialize() {

    }

    /**
     * 初始化视图
     *
     * @param savedInstanceState
     */
    @Override
    protected void initView(@Nullable Bundle savedInstanceState) {
        mViewPager = findViewById(R.id.view_pager);

        mViewPager.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return mSplashDrawableId.length;
            }

            @Override
            public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
                return view == object;
            }

            @NonNull
            @Override
            public Object instantiateItem(@NonNull ViewGroup container, int position) {
                ImageView imageView = new ImageView(container.getContext());
                imageView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                imageView.setScaleType(ImageView.ScaleType.FIT_XY);
                imageView.setImageResource(mSplashDrawableId[position]);

                /*
                最后一页，点击，校验 登录操作
                 */
                if (position == mSplashDrawableId.length - 1) {
                    imageView.setOnClickListener(mOnClickListener);
                }
                //add to the container.
                container.addView(imageView);
                return imageView;
            }

            @Override
            public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
                container.removeView((View) object);
            }
        });
    }

    /**
     * 初始化数据
     */
    @Override
    protected void initData() {

    }

    /**
     * 刷新当前页面时所需要加载的数据
     * 当前页面第一次加载时需要的数据
     */
    @Override
    protected void refreshData() {

    }

}
