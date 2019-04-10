package com.nuc.signin_android.base;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * @Author: cuizhe
 * @Date: 2019/4/10 19:31
 * @Description:
 */
public abstract class BaseFragment extends Fragment {
    private Unbinder unbinder;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(getResourcesLayout(),container,false);
        unbinder = ButterKnife.bind(this,view);
        /*
        数据处理 doSomething
         */
        init(view,savedInstanceState);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    protected abstract void logic();

    protected abstract void init(View view, Bundle savedInstanceState);
    protected abstract int getResourcesLayout();

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();// 解除绑定
    }

    protected void startActivityTo(Class activityClass, String s){
        Intent intent = new Intent(getContext(),activityClass);
        Bundle bundle = new Bundle();
        // 把数据打包为 bundle
        bundle.putString("random_number",s);
        // Intent 携带数据包
        intent.putExtras(bundle);
        startActivity(intent);
    }


}
