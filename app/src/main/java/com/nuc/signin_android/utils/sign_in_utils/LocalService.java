package com.nuc.signin_android.utils.sign_in_utils;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import com.nuc.signin_android.utils.Constant;

/**
 * @Author: cuizhe
 * @Date: 2019/4/23 10:28
 * @Description:
 */
public class LocalService extends Service {

    private IBinder iBinder = new LocalService.LocalBinder();

    @Override
    public IBinder onBind(Intent intent) {
        return iBinder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    public void startWaitDataThread(){
        new ListenThread(Constant.port).start();
    }

    /**
     * 定义内容类继承 Binder
     */
    public class LocalBinder extends Binder {
        //返回本地服务
        public LocalService getService(){
            return LocalService.this;
        }
    }

}
