package com.nuc.signin_android.utils.device;

import android.content.Context;
import android.net.wifi.WifiManager;

import java.lang.reflect.Method;

/**
 * @Author: cuizhe
 * @Date: 2019/4/15 11:42
 * @Description: 设备信息,wifi 信息
 */
public class DeviceUtils {
    private WifiManager mWifiManager;// wifi 信息
    private Context mContext;

    public DeviceUtils(Context mContext) {
        this.mContext = mContext;
    }

    /**
     * 通过反射获取 sn 号
     *
     * @return 安卓手机的序列号
     */
    public static String getDeviceId(){
        String sn = null;
        try {
            Class<?> c = Class.forName("android.os.SystemProperties");
            Method get = c.getMethod("get",String.class);
            sn = (String) get.invoke(c,"ro.serialno");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return sn;
    }
}
