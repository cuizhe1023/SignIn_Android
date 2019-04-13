package com.nuc.signin_android.utils;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;

/**
 * @Author: cuizhe
 * @Date: 2019/4/10 18:07
 * @Description:
 */
public class ActivityUtils {
    private static final String TAG = "ActivityUtils";

    /**
     * 替换 fragment
     *
     * @param fragmentManager FragmentManager
     * @param fragment fragment 容器
     * @param frmeId 替换的 fragment
     */
    public static void replaceFragmentToActivity(FragmentManager fragmentManager,
                                                 Fragment fragment,
                                                 int frmeId){
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(frmeId,fragment);
        fragmentTransaction.commit();
        Log.i(TAG, "replaceFragmentToActivity: 替换成功：" + fragment.toString() + " to fragment");
    }
}
