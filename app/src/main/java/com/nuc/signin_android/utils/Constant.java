package com.nuc.signin_android.utils;

/**
 * @Author: cuizhe
 * @Date: 2019/4/10 22:02
 * @Description:
 */
public class Constant {
    public static final int TIMEOUT = 5 * 1000;//连接超时时间
    public static final int port = 25256;//端口号
    public static final String password = "123456885";//加密所使用的密钥
    public static final int CODE_TIMEOUT = 0;//连接超时
    public static final int CODE_SUCCESS = 1;//连接成功
    public static final int CODE_UNKNOWN_HOST = 2;//错误-未知的host

    public static String URL = "http://10.0.116.44:8080"; // IP地址请改为你自己的IP
    public static String TEACHER = "/teacher";
    public static String STUDENT = "/student";

    public static String URL_TEACHER_REGISTER = URL + TEACHER +"/register";
    public static String URL_TEACHER_LOGIN = URL + TEACHER +"/login";

    public static String URL_STUDENT_REGISTER = URL + STUDENT +"/register";
    public static String URL_STUDENT_LOGIN = URL + STUDENT +"/login";

    public static String URL_TEACHER_GETONE = URL + TEACHER + "/findById";
    public static String URL_STUDENT_GETONE = URL + STUDENT+ "/findById";


}
