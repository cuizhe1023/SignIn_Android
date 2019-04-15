package com.nuc.signin_android.utils.net;

import android.text.TextUtils;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * @Author: cuizhe
 * @Date: 2019/4/14 10:22
 * @Description: 封装 OkHttp 的回调 CallBack
 */
public abstract class OkHttpCallBack implements Callback {

    private static final String TAG = "OkHttpCallBack";

    public abstract void onSuccess(final Call call, JSONObject jsonObject, int code);

    @Override
    public void onResponse(Call call, Response response) {
        if (response.isSuccessful()){
            try {
                if (response.body() != null ) {
                    String str = response.body().string().trim();
                    Log.i(TAG, "onResponse: str = " + str);
                    if (!TextUtils.isEmpty(str)){
                        JSONObject object = (JSONObject) new JSONTokener(str).nextValue();
                        if (object != null){
                            onSuccess(call,object,response.code());
                        }else {
                            Log.e(TAG, "onResponse: object == null");
                            onFailure(call,null);
                        }
                    }else {
                        Log.e(TAG, "onResponse: response.body().string().trim() is isEmpty");
                        onFailure(call,null);
                    }
                }else {
                    Log.e(TAG, "onResponse: response.body == null" );
                    onFailure(call,null);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else {
            Log.e(TAG, "onResponse: isSuccessful is false" );
            onFailure(call,null);
        }
    }

    @Override
    public void onFailure(Call call, IOException e) {

    }
}
