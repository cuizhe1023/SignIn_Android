package com.nuc.signin_android.utils.sign_in_utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.DhcpInfo;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.ResultReceiver;
import android.util.Log;
import android.widget.Toast;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;

/**
 * @Author: cuizhe
 * @Date: 2019/4/23 10:17
 * @Description:
 */
public class WifiUtils {
    private static WifiManager mWifiManager;
    private static Context mContext;

    public WifiUtils(Context mContext) {
        this.mContext = mContext;
        mWifiManager = (WifiManager) mContext.getSystemService(Context.WIFI_SERVICE);
    }

    /**
     * 判断 Wi-Fi 是否开启
     * @return true/false
     */
    public boolean wifiIsOpen(){
        if (mWifiManager.isWifiEnabled()){
            return true;
        }else {
            return false;
        }
    }

    /**
     * 创建 Wi-Fi 热点
     *
     * @param mSSID 热点名称
     * @param mPassword 热点密码
     * @param isOPen 是否开启
     */
    public void setWifiApEnabled(String mSSID,String mPassword,boolean isOPen){
        if (mWifiManager.isWifiEnabled()){
            //如果 Wi-Fi 处于打开状态，则关闭 Wi-Fi。
            mWifiManager.setWifiEnabled(false);
        }
        Method method = null;
        try{
            WifiConfiguration config = new WifiConfiguration();
            config.SSID = mSSID;
            config.preSharedKey = mPassword;
            config.allowedAuthAlgorithms.set(WifiConfiguration.AuthAlgorithm.OPEN);//开放系统认证
            config.allowedProtocols.set(WifiConfiguration.Protocol.RSN);
            config.allowedProtocols.set(WifiConfiguration.Protocol.WPA);
            if (isOPen){
                config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
            }else {
                config.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.WPA_PSK);
            }
            config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.CCMP);
            config.allowedPairwiseCiphers.set(WifiConfiguration.PairwiseCipher.TKIP);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.CCMP);
            config.allowedGroupCiphers.set(WifiConfiguration.GroupCipher.TKIP);
            config.status = WifiConfiguration.Status.ENABLED;
            method = mWifiManager.getClass().getMethod("setWifiApEnabled", WifiConfiguration.class, Boolean.TYPE);

            boolean enable = (boolean) method.invoke(mWifiManager,config,true);
            if (enable){
                Toast.makeText(mContext, "热点已开启 SSID:" + mSSID + " ,password:" + mPassword,
                        Toast.LENGTH_SHORT).show();
            }else {
                Toast.makeText(mContext, "热点创建失败", Toast.LENGTH_SHORT).show();
            }
        }catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    public void setWifiApEnabledForAndroidO(boolean isEnable){
        ConnectivityManager connManager = (ConnectivityManager) mContext.getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        Field iConnMgrField = null;
        try {
            iConnMgrField = connManager.getClass().getDeclaredField("mService");
            iConnMgrField.setAccessible(true);
            Object iConnMgr = iConnMgrField.get(connManager);
            Class<?> iConnMgrClass = Class.forName(iConnMgr.getClass().getName());

            if(isEnable){
                Method startTethering = iConnMgrClass.getMethod("startTethering", int.class, ResultReceiver.class, boolean.class);
                startTethering.invoke(iConnMgr, 0, null, true);
            }else{
                Method startTethering = iConnMgrClass.getMethod("stopTethering", int.class);
                startTethering.invoke(iConnMgr, 0);
            }

        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    /**
     * 检查是否开启 Wi-Fi 热点
     * @return
     */
    public boolean isWifApEnable(){
        try {
            Method method = mWifiManager.getClass().getMethod("isWifiApEnabled");
            method.setAccessible(true);
            Log.d("Wifi_APManager", "isWifApEnable: "+(boolean) method.invoke(mWifiManager));
            return (boolean) method.invoke(mWifiManager);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return false;
    }

    public void closeWifi(){
        WifiManager wifiManager = (WifiManager) mContext.getSystemService(Context.WIFI_SERVICE);
        if (isWifApEnable()){
            try {
                Method method = wifiManager.getClass().getMethod("getWifiApConfiguration");
                method.setAccessible(true);
                WifiConfiguration config = (WifiConfiguration) method.invoke(wifiManager);
                Method method1 = wifiManager.getClass().getMethod("setWifiEnabled",
                        WifiConfiguration.class,boolean.class);
                method1.invoke(wifiManager,config,false);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
    }

    // 获取连接热点的IP地址
    public String getServerIp(){
        DhcpInfo dhcpInfo = mWifiManager.getDhcpInfo();
        int ip = dhcpInfo.serverAddress;
        String sip=(ip&0xff)+"."+((ip>>8)&0xff)+"."+((ip>>16)&0xff)+"."+((ip>>24)&0xff);
        return sip;
    }

    /**
     * 获取ip地址
     * 如果是移动网络，会显示自己的公网IP，如果是局域网，会显示局域网IP
     * 因此本例中服务器端需要断开移动网络以得到本机局域网IP
     */
    /**
     * 获取本机的 IP 地址
     * @return
     */
    public String getLocalIp() {
        String ipaddress = "";
        try {
            Enumeration<NetworkInterface> en = NetworkInterface
                    .getNetworkInterfaces();
            // 遍历所用的网络接口
            while (en.hasMoreElements()) {
                // 得到每一个网络接口绑定的所有ip
                NetworkInterface nif = en.nextElement();
                Enumeration<InetAddress> inet = nif.getInetAddresses();
                // 遍历每一个接口绑定的所有ip
                while (inet.hasMoreElements()) {
                    InetAddress ip = inet.nextElement();
                    if (!ip.isLoopbackAddress() && ip instanceof Inet4Address) {
                        ipaddress = ip.getHostAddress();
                        return ipaddress;
                    }
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }

        return ipaddress;
    }

    /**
     * 判断地址是否为IPV4地址
     */
    public boolean IsIpv4(String ipv4) {
        if (ipv4 == null || ipv4.length() == 0) {
            return false;//字符串为空或者空串
        }
        String[] parts = ipv4.split("\\.");//因为java doc里已经说明, split的参数是reg, 即正则表达式, 如果用"|"分割, 则需使用"\\|"
        if (parts.length != 4) {
            return false;//分割开的数组根本就不是4个数字
        }
        for (String part : parts) {
            try {
                int n = Integer.parseInt(part);
                if (n < 0 || n > 255) {
                    return false;//数字不在正确范围内
                }
            } catch (NumberFormatException e) {
                return false;//转换数字不正确
            }
        }
        return true;
    }
}
