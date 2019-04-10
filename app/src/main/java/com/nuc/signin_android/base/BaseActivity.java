package com.nuc.signin_android.base;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

/**
 * @Author: cuizhe
 * @Date: 2019/4/10 17:49
 * @Description:
 */
public abstract class BaseActivity extends AppCompatActivity {
    protected FragmentManager fragmentManager;
    protected Activity activity;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutView());
        fragmentManager = getSupportFragmentManager();
        activity = this;
        logicActivity(savedInstanceState);
    }

    protected abstract void logicActivity(Bundle savedInstanceState);

    protected abstract int getLayoutView();
}
