package com.nuc.signin_android.about;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.nuc.signin_android.R;
import com.nuc.signin_android.base.BaseFragment;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @Author: cuizhe
 * @Date: 2019/4/10 20:14
 * @Description:
 */
public class MyselfFragment extends BaseFragment {

    private static final String TAG = "MyselfFragment";

    @BindView(R.id.me_image_avatar)
    ImageView meImageAvatar;
    @BindView(R.id.me_name)
    TextView meName;
    @BindView(R.id.me_major)
    TextView meMajor;
    @BindView(R.id.me_login)
    ImageView meLogin;
    @BindView(R.id.logout_btn)
    Button logoutBtn;


    public static MyselfFragment getInstance(){
        Log.i(TAG, "getInstance: 加载了 myself.");
        return new MyselfFragment();
    }

    @Override
    public void onStart() {
        Log.i(TAG, "onStart: userId = " + userId + "....");
        if (userId == null){
            meName.setText("请先登录");
            meMajor.setText("");
        }else {
            Log.i(TAG, "onStart: userId is " + userId);
            Log.i(TAG, "onStart: identity is " + identity);
            if ("teacher".equals(identity)){
                String name = pref.getString("name",null);
                String password = pref.getString("password",null);
                Log.i(TAG, "onStart: teacher.name = " + name);
                Log.i(TAG, "onStart: teacher.password = " + password);
                meName.setText(name);
            }
            if ("student".equals(identity)){
                String name = pref.getString("name",null);
                String password = pref.getString("password",null);
                Log.i(TAG, "onStart: student.name = " + name);
                Log.i(TAG, "onStart: student.password = " + password);
                meName.setText(name);
            }
        }
        super.onStart();
    }

    private void setNull() {
        meName.setText("请先登录");
        meMajor.setText("");
        meLogin.setVisibility(View.VISIBLE);
        editor = pref.edit();
        editor.clear();
        editor.apply();
    }

    @Override
    protected void logic() {
        if (userId != null && identity != null){
            meLogin.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i(TAG, "onResume: onresume");
    }

    @Override
    protected void init(View view, Bundle savedInstanceState) {
        logic();
    }

    @Override
    protected int getResourcesLayout() {
        return R.layout.fragment_about;
    }

    @OnClick(R.id.me_login)
    public void onMeLoginClicked() {
        Log.i(TAG, "onMeLoginClicked: 跳转到登录界面");
        Intent intent = new Intent(getContext(),LoginActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.logout_btn)
    public void onLogoutBtnClicked() {
        Log.i(TAG, "onLogoutBtnClicked: 退出登录");
        setNull();
    }
}
