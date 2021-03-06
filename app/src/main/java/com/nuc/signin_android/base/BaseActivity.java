package com.nuc.signin_android.base;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.nuc.signin_android.utils.net.OkHttpUtil;

import butterknife.ButterKnife;

/**
 * @Author: cuizhe
 * @Date: 2019/4/10 17:49
 * @Description:
 */
public abstract class BaseActivity extends AppCompatActivity {

    private static final String TAG = "BaseActivity";

    protected FragmentManager fragmentManager;
    protected Activity activity;

    protected SharedPreferences pref;
    protected SharedPreferences.Editor editor;
    protected String userId;
    protected String identity;
    protected Handler mainHandler = new Handler();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutView());
        ButterKnife.bind(this);

        // 调用 OkHttpUtil
        OkHttpUtil.init();

        // 通过 SharedPreferences 存储和获取用户信息
        pref = getSharedPreferences("setting",MODE_PRIVATE);
        Log.e(TAG, "onCreate: 保存的用户名：" + pref.getString("account","") );
        if (pref.getString("account","") != null){
            userId = pref.getString("account","");
            identity = pref.getString("identity","");
        }
        Log.i(TAG, "onCreate: userId is " + userId);

        fragmentManager = getSupportFragmentManager();
        activity = this;
        logicActivity(savedInstanceState);
    }

    protected abstract void logicActivity(Bundle savedInstanceState);

    protected abstract int getLayoutView();
}
