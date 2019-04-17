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

    public static final String URL = "http://10.0.116.44:8080"; // IP地址请改为你自己的IP
    public static final String TEACHER = "/teacher";
    public static final String STUDENT = "/student";
    public static final String COURSE = "/course";

    // http://10.0.116.44:8080/teacher/register
    public static final String URL_TEACHER_REGISTER = URL + TEACHER +"/register";

    // http://10.0.116.44:8080/teacher/login
    public static final String URL_TEACHER_LOGIN = URL + TEACHER +"/login";

    // http://10.0.116.44:8080/teacher/findById
    public static final String URL_TEACHER_GETONE = URL + TEACHER + "/findById";

    // http://10.0.116.44:8080/student/register
    public static final String URL_STUDENT_REGISTER = URL + STUDENT +"/register";

    // http://10.0.116.44:8080/student/login
    public static final String URL_STUDENT_LOGIN = URL + STUDENT +"/login";

    // http://10.0.116.44:8080/student/findById
    public static final String URL_STUDENT_GETONE = URL + STUDENT+ "/findById";

    // http://10.0.116.44:8080/student/updateSN
    public static final String URL_STUDENT_UPDATESN = URL + STUDENT + "/updateSN";

    // http://10.0.116.44:8080/course/insert
    public static final String URL_COURSE_INSERT = URL + COURSE + "/insert";

    // http://10.0.116.44:8080/course/createList
    public static final String URL_COURSE_CREATELIST = URL + COURSE + "/createList";
}
