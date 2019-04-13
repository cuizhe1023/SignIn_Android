package com.nuc.signin_android;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.util.Log;
import android.view.MenuItem;

import com.nuc.signin_android.about.MyselfFragment;
import com.nuc.signin_android.base.BaseActivity;
import com.nuc.signin_android.classroom.ClassFragment;
import com.nuc.signin_android.utils.ActivityUtils;


public class MainActivity extends BaseActivity {

    private static final String TAG = "MainActivity";
    BottomNavigationView bottomMenu = null;

    @Override
    protected void logicActivity(Bundle savedInstanceState) {
        // 保存 APP 状态
        if (savedInstanceState != null){
            Log.i(TAG, "logicActivity: " + savedInstanceState.getInt("bottom_id"));
            showFragment(savedInstanceState.getInt("bottom_id"));
        }else {
            //进行 fragment 替换，加载主界面
            Log.i(TAG, "logicActivity: savedInstanceState is null");
            ActivityUtils.replaceFragmentToActivity(fragmentManager, ClassFragment.getInstance(),R.id.main_frame);
        }

        bottomMenu = findViewById(R.id.bv_bottomNavigation);
        bottomMenu.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                showFragment(menuItem.getItemId());
                return true;
            }
        });
    }

    @Override
    protected int getLayoutView() {
        return R.layout.activity_main;
    }

    /**
     * 根据 id 显示相应的页面
     *
     * @param bottom_id 底栏的 id
     */
    private void showFragment(int bottom_id) {
        switch (bottom_id){
            case R.id.bottom_menu_class:
                ActivityUtils.replaceFragmentToActivity(fragmentManager, ClassFragment.getInstance(),R.id.main_frame);
                break;
            case R.id.bottom_menu_about:
                ActivityUtils.replaceFragmentToActivity(fragmentManager, MyselfFragment.getInstance(),R.id.main_frame);
                break;
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("botton_id",bottomMenu.getSelectedItemId());
    }
}
