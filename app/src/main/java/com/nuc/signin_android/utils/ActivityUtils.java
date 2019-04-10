package com.nuc.signin_android.utils;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

/**
 * @Author: cuizhe
 * @Date: 2019/4/10 18:07
 * @Description:
 */
public class ActivityUtils {
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
    }
}
