package com.nuc.signin_android.utils.net;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * @Author: cuizhe
 * @Date: 2019/4/14 10:28
 * @Description: 封装 OkHttp 的核心类
 */
public class OkHttpUtil {

    private static OkHttpClient mOkHttpClient = null;

    public static void init(){
        if (mOkHttpClient == null){
            OkHttpClient.Builder builder = new OkHttpClient.Builder()
                    .connectTimeout(5000, TimeUnit.MILLISECONDS)
                    .readTimeout(5000, TimeUnit.MILLISECONDS)
                    .writeTimeout(5000, TimeUnit.MILLISECONDS);
            mOkHttpClient = builder.build();
        }
    }

    /**
     * 封装的 get 请求
     *
     * @param url URL
     * @param okHttpCallBack 回调
     * @param params get 请求的参数列表
     */
    public static void get(String url, OkHttpCallBack okHttpCallBack, HashMap<String,String> params){
        Call call = null;
        try {
            HttpUrl.Builder urlBuilder = HttpUrl.parse(url).newBuilder();

            if (params != null){
                for (String key:params.keySet()) {
                    System.out.println("++++++++++++++++++ *get*  +++++++++++++++++++");
                    System.out.println("key:" + key + ",value:" + params.get(key));
                    System.out.println("++++++++++++++++++ *get*  +++++++++++++++++++");
                    urlBuilder.setQueryParameter(key,params.get(key));
                }
            }
            Request request = new Request.Builder().url(urlBuilder.build()).get().build();
            call = mOkHttpClient.newCall(request);
            call.enqueue(okHttpCallBack);// 异步调用
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 封装的 post 请求
     *
     * @param url URL
     * @param okHttpCallBack 回调
     */
    public static void post(String url, OkHttpCallBack okHttpCallBack, HashMap<String,String> bodyMap){
        Call call = null;
        try {
            FormBody.Builder builder = new FormBody.Builder();
            for (HashMap.Entry<String, String> entry:
                    bodyMap.entrySet()) {
                System.out.println("++++++++++++++++++++++++++++++++++++++++++");
                System.out.println("key:" + entry + ",value:" + bodyMap.get(entry));
                System.out.println("++++++++++++++++++++++++++++++++++++++++++");
                builder.add(entry.getKey(),entry.getValue());
            }
            RequestBody body = builder.build();
            Request request = new Request.Builder().post(body).url(url).build();
            call = mOkHttpClient.newCall(request);
            call.enqueue(okHttpCallBack);// 异步调用
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
