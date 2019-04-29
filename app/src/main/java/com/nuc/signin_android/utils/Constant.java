package com.nuc.signin_android.utils;

/**
 * @Author: cuizhe
 * @Date: 2019/4/10 22:02
 * @Description:
 */
public class Constant {
    public static final int TIMEOUT = 5 * 1000;// 连接超时时间
    public static final int port = 25256;// 端口号
    public static final String password = "123456885";// 加密所使用的密钥
    public static final int CODE_TIMEOUT = 0;// 连接超时
    public static final int CODE_SUCCESS = 1;// 连接成功
    public static final int CODE_UNKNOWN_HOST = 2;// 错误-未知的host

    // 阿里云 ip ： http://47.94.220.113:8080
    // 手机热点 192.168.43.152
    // 连接实验室网线 10.0.116.9
    public static final String URL = "http://192.168.43.152:8080"; // IP地址请改为你自己的IP
    public static final String TEACHER = "/teacher";
    public static final String STUDENT = "/student";
    public static final String COURSE = "/course";
    public static final String SELECT_COURSE = "/select_course";
    public static final String SIGN_IN = "/sign_in";
    public static final String STUDENTSIGNIN = "/studentSignIn";

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

    // http://10.0.116.44:8080/course/upload
    public static final String URL_COURSE_UPLOADFILE = URL + COURSE + "/upload";

    public static final String URL_COURSE_STUDENTCOURSE = URL + COURSE + "/studentCourse";

    public static final String URL_SELECT_COURSE_INSERT = URL + SELECT_COURSE + "/insert";

    public static final String URL_SELECT_COURSE_STUDENTLIST = URL + SELECT_COURSE + "/studentList";

    public static final String URL_SELECT_COURSE_SUM_STUDENT = URL + SELECT_COURSE + "/studentSum";

    public static final String URL_SIGNIN_CREATE = URL + SIGN_IN + "/create";

    public static final String URL_SIGNIN_UPDATE_SIGN_IN_DATA = URL + SIGN_IN + "/updateSignInData";

    public static final String URL_SIGNIN_GET_SIGN_IN_LIST = URL + SIGN_IN + "/getSignInList";

    public static final String URL_STUDENTSIGNIN_INITSIGNIN = URL + STUDENTSIGNIN + "/initSignIn";

    public static final String URL_STUDENTSIGNIN_UPDATESTATUS = URL + STUDENTSIGNIN + "/updateStatus";

    public static final String URL_STUDENTSIGNIN_GET_NO_SIGN_IN_STUDENT = URL + STUDENTSIGNIN + "/getNoSignInStudentList";

    public static final String URL_STUDENTSIGNIN_GET_SIGN_IN_STUDENT = URL + STUDENTSIGNIN + "/getSignInStudentList";

    public static final String URL_STUDENTSIGNIN_UPDAT_REASON = URL + STUDENTSIGNIN + "/updateReason";

    public static final String URL_STUDENTSIGNIN_GET_NO_SIGNIN_STUDENT_NUMBER = URL + STUDENTSIGNIN + "/getNoSignInStudentNumber";

    public static final String URL_STUDENTSIGNIN_GET_SIGNIN_STUDENT_NUMBER = URL + STUDENTSIGNIN + "/getSignInStudentNumber";

    public static final String URL_STUDENTSIGNIN_GET_LEAVE_REASON = URL + STUDENTSIGNIN + "/getLeaveReason";
}
