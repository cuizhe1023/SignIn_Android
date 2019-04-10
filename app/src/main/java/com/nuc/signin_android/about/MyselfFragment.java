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
    @BindView(R.id.me_name)
    TextView meName;
    @BindView(R.id.me_major)
    TextView meMajor;
    @BindView(R.id.me_login)
    ImageView meLogin;
    @BindView(R.id.logout_btn)
    Button logoutBtn;

    public static MyselfFragment getInstance(){
        return new MyselfFragment();
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onStart() {
        // 调用接口获取登陆学生的信息

        super.onStart();
    }

    private void setNull() {
        meName.setText("请先登录");
        meMajor.setText("");
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void logic() {

    }

    @Override
    protected void init(View view, Bundle savedInstanceState) {

    }

    @Override
    protected int getResourcesLayout() {
        return 0;
    }

    @OnClick(R.id.me_login)
    public void onMeLoginClicked() {
        Intent intent = new Intent(getContext(),LoginActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.logout_btn)
    public void onLogoutBtnClicked() {
        setNull();
    }
}
