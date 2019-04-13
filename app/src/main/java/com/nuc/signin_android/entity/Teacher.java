package com.nuc.signin_android.entity;

import java.io.Serializable;

import lombok.Data;

/**
 * @Author: cuizhe
 * @Date: 2019/4/11 21:38
 * @Description:
 */
@Data
public class Teacher implements Serializable {

    private String teacherId;

    private String teacherName;

    private String teacherPassword;
}
