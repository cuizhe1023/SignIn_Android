package com.nuc.signin_android.base;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nuc.signin_android.entity.Course;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * @Author: cuizhe
 * @Date: 2019/4/10 19:31
 * @Description:
 */
public abstract class BaseFragment extends Fragment {
    private static final String TAG = "BaseFragment";
    private Unbinder unbinder;
    protected SharedPreferences pref;
    protected SharedPreferences.Editor editor;
    protected String userId;
    protected String identity;
    protected Handler mainHandler = new Handler();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(getResourcesLayout(),container,false);
        unbinder = ButterKnife.bind(this,view);

        // 通过 SharedPreferences 存储用户信息
        pref = getActivity().getSharedPreferences("setting", Context.MODE_PRIVATE);
        userId = pref.getString("account",null);
        identity = pref.getString("identity",null);
        Log.i(TAG, "onCreateView: userId is " + userId);
        init(view,savedInstanceState);
        logic();
        return view;
    }

    protected abstract void logic();

    protected abstract void init(View view, Bundle savedInstanceState);
    protected abstract int getResourcesLayout();

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();// 解除绑定
    }

    protected void startActivityTo(Class activityClass, String id, String name, Course course){
        Intent intent = new Intent(getContext(),activityClass);
        Bundle bundle = new Bundle();
        // 把数据打包为 bundle
        bundle.putString("id",id);
        bundle.putString("name",name);
        bundle.putSerializable("course",course);
        // Intent 携带数据包
        intent.putExtras(bundle);
        startActivity(intent);
    }


    @Override
    public void onResume() {
        super.onResume();
        Log.i(TAG, "onResume: ");
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();

        Log.i(TAG, "onStart: ");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.i(TAG, "onPause: ");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.i(TAG, "onStop: ");
    }

}
