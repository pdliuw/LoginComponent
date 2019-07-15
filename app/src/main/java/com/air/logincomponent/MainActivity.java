package com.air.logincomponent;

import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.air.logincomponent.ui.base.AppCommonActivity;

public class MainActivity extends AppCommonActivity {

    @Override
    protected int inflateContentViewById() {
        return R.layout.activity_main;
    }

    @Override
    protected void initialize() {

        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
    }

    @Override
    protected void initView(@Nullable Bundle savedInstanceState) {

    }

    @Override
    protected void initData() {

    }

    @Override
    protected void refreshData() {

    }
}
