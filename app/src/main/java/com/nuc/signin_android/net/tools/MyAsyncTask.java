package com.nuc.signin_android.net.tools;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

/**
 * @Author: cuizhe
 * @Date: 2019/4/10 21:55
 * @Description:
 * AsyncTask 类的三个泛型参数：
 * （1）Param 在执行AsyncTask是需要传入的参数，可用于后台任务中使用
 * （2）后台任务执行过程中，如果需要在 UI 上先是当前任务进度，则使用这里指定的泛型作为进度单位
 * （3）任务执行完毕后，如果需要对结果进行返回，则这里指定返回的数据类型
 */
public class MyAsyncTask  extends AsyncTask<String, Integer, String> {

    private Context context;
    private TaskListener taskListener;

    public MyAsyncTask(Context context, TaskListener taskListener) {
        this.context = context;
        this.taskListener = taskListener;
    }

    //在doInBackground()方法执行前调用，主要执行一些初始化的工作，在UI线程中执行
    @Override
    protected void onPreExecute() {
        Log.w("MyAsyncTask", "task onPreExecute()");
    }

    /**
     * @param params 这里的params是一个数组，即 AsyncTask 在激活运行是调用 execute() 方法传入的参数
     */
    @Override
    protected String doInBackground(String... params) {
        Log.w("MyAsyncTask", "task doInBackground()");
        HttpURLConnection connection = null;
        StringBuilder response = new StringBuilder();
        try {
            String result = Uri.encode(params[0], "-![.:/,%?&=]");// 对 url 进行转码
            Log.d("url!!!!!", "doInBackground: " + result);
            Log.i("uri", "doInBackground: " + result);
            URL url = new URL(result); // 声明一个URL,注意如果用百度首页实验，请使用https开头，否则获取不到返回报文
            connection = (HttpURLConnection) url.openConnection(); // 打开该URL连接
            connection.setRequestMethod("GET"); // 设置请求方法，“POST或GET”，我们这里用GET，在说到POST的时候再用POST
            connection.setConnectTimeout(80000); // 设置连接建立的超时时间
            connection.setReadTimeout(80000); // 设置网络报文收发超时时间
            InputStream in = connection.getInputStream();  // 通过连接的输入流获取下发报文，然后就是Java的流处理
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response.toString(); // 这里返回的结果就作为onPostExecute方法的入参
    }
    @Override
    protected void onProgressUpdate(Integer... values) {
        // 如果在 doInBackground 方法，那么就会立刻执行本方法
        // 本方法在 UI 线程中执行，可以更新 UI 元素，典型的就是更新进度条进度，一般是在下载时候使用
    }

    /**
     * 在工作完成后次方法被调用，在 UI 线程中执行，所以可以直接操作 UI 元素
     * @param result
     */
    @Override
    protected void onPostExecute(String result) {
        Log.w("MyAsyncTask", "task onPostExecute()");
        Log.d("MyAsyncTask", "onPostExecute: " + result);
        taskListener.onCompletedListener(result);
    }
}