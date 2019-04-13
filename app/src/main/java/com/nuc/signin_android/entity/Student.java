package com.nuc.signin_android.entity;

import lombok.Data;

/**
 * @Author: cuizhe
 * @Date: 2019/4/12 14:57
 * @Description:
 */
@Data
public class Student {

    private String studentId;

    private String studentName;

    private String studentPassword;

    private String classId;

    private String gender;

    private String macAddress;
}
