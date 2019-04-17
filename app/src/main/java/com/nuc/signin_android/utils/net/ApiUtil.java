package com.nuc.signin_android.utils.net;

import android.util.Log;

import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;

import okhttp3.Call;

/**
 * @Author: cuizhe
 * @Date: 2019/4/14 10:38
 * @Description: 统一处理服务端的回调
 */
public abstract class ApiUtil {
    private static final String TAG = "ApiUtil";

    private ApiListener mApiListener = null;

    private int mStatus;

    protected HashMap<String, String> params = new HashMap<>();

    private OkHttpCallBack mSendListener = new OkHttpCallBack() {
        @Override
        public void onSuccess(Call call, Object response, int code) {
            mStatus = code;
            try {
                Log.i("ApiUtil", "onSuccess: " + mStatus);
                if (isSuccess()){
                    parseData(response);
                    mApiListener.success(ApiUtil.this);
                }else {
                    mApiListener.failure(ApiUtil.this);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(Call call, IOException e) {
            mApiListener.failure(ApiUtil.this);
        }
    };

    public boolean isSuccess(){
        return 0 == mStatus || 200 == mStatus;
    }

    protected abstract void parseData(Object jsonObject) throws Exception;// 解析数据的方法

    protected abstract String getUrl(); // 获取 URL

    /**
     * 发送 GET 请求
     * @param listener 告诉客户端请求是否成功
     */
    public void get(ApiListener listener){
        mApiListener = listener;
        OkHttpUtil.get(getUrl(),mSendListener,params);
    }

    /**
     * 发送 post 请求
     * @param listener
     */
    public void post(ApiListener listener){
        mApiListener = listener;
        OkHttpUtil.sendRequest(getUrl(),mSendListener,params,OkHttpUtil.REQUEST_TYPE.POST);
    }

    /**
     * 发送 put 请求
     * @param listener
     */
    public void put(ApiListener listener){
        mApiListener = listener;
        OkHttpUtil.sendRequest(getUrl(),mSendListener,params,OkHttpUtil.REQUEST_TYPE.PUT);
    }

    /**
     * 发送 delete 请求
     * @param listener
     */
    public void delect(ApiListener listener){
        mApiListener = listener;
        OkHttpUtil.sendRequest(getUrl(),mSendListener,params,OkHttpUtil.REQUEST_TYPE.DELECT);
    }
}
