package com.nuc.signin_android.utils.net;

import android.util.Log;

import com.nuc.signin_android.utils.Constant;

import java.io.File;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
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

    private static final String TAG = "OkHttpUtil";

    public enum REQUEST_TYPE{
        POST,PUT,DELECT
    }

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
                    Log.e("GET>>>>>>>>>>>>>", "key="+key+",value="+params.get(key));
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
     * 封装的 sendRequest 请求
     *
     * @param url URL
     * @param okHttpCallBack 回调
     */
    public static void sendRequest(String url, OkHttpCallBack okHttpCallBack,
                                   HashMap<String,String> bodyMap,
                                   REQUEST_TYPE requestType){
        Call call = null;
        try {
            FormBody.Builder builder = new FormBody.Builder();
            for (HashMap.Entry<String, String> key:
                    bodyMap.entrySet()) {
                Log.e("sendRequest>>>>>>>>" + requestType, "key="+key+",value="+bodyMap.get(key));
                builder.add(key.getKey(),key.getValue());
            }
            RequestBody body = builder.build();
            Request request = null;
            switch (requestType){
                case POST:
                    request = new Request.Builder().post(body).url(url).build();
                    break;
                case PUT:
                    request = new Request.Builder().put(body).url(url).build();
                    break;
                case DELECT:
                    request = new Request.Builder().post(body).url(url).build();
                    break;
            }
            call = mOkHttpClient.newCall(request);
            call.enqueue(okHttpCallBack);// 异步调用
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 封装上传文件的请求
     *
     * @param url URL
     * @param okHttpCallBack 回调
     */
    public static void uploadFile(String url, OkHttpCallBack okHttpCallBack, HashMap<String,String> bodyMap){
        Call call = null;
        String filePath = bodyMap.get("filePath");
        String courseId = bodyMap.get("courseId");
        Log.e(TAG, "uploadFile: filePath = " + filePath );
        Log.e(TAG, "uploadFile: courseId = " + courseId );
        File file = new File(filePath);
        Log.i(TAG, "uploadFile: file.getName = " + file.getName());
        MultipartBody.Builder builder = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("courseId",courseId)
                .addFormDataPart("file",file.getName(),
                        RequestBody.create(MediaType.parse("application/vnd.ms-excel"),file));

        RequestBody requestBody = builder.build();
        Request request = new Request.Builder()
                .url(Constant.URL_COURSE_UPLOADFILE)
                .post(requestBody)
                .build();

        call = mOkHttpClient.newCall(request);
        call.enqueue(okHttpCallBack);// 异步调用
    }

}
