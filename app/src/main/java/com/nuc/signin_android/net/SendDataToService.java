package com.nuc.signin_android.net;

import android.content.Context;
import android.util.Log;

import com.nuc.signin_android.net.tools.MyAsyncTask;
import com.nuc.signin_android.net.tools.TaskListener;
import com.nuc.signin_android.utils.Constant;

/**
 * @Author: cuizhe
 * @Date: 2019/4/10 22:00
 * @Description:
 */
public class SendDataToService {

    public String teacherRegister(Context context, String teacherId,
                                  String teacherName, String teacherPassword){
        String result = "Error";
        String registerUrlStr = Constant.URL_REGISTER_TEACHER + "?TeacherId=" + teacherId +
                "&TeacherName=" + teacherName + "&TeacherPassword=" + teacherPassword;

        Log.e("register", "url:" + registerUrlStr);

        new MyAsyncTask(context, new TaskListener() {
            @Override
            public void onCompletedListener(Object result) {
                String s = (String) result;
                Log.e("register", "onCompletedListener: " + s);

                if (s.equals("注册成功")){
                    result = s;
                }else if (s.equals("该账号已注册，请使用此账号直接登录或使用其他账号注册")){
                    result = s;
                }
            }
        }).execute(registerUrlStr);

        return result;
    }
}
