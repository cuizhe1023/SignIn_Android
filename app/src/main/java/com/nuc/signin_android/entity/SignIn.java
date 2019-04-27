package com.nuc.signin_android.entity;

import lombok.Data;

/**
 * @Author: cuizhe
 * @Date: 2019/4/27 11:46
 * @Description:
 */
@Data
public class SignIn {
    Integer signInId;

    String teacherId;

    String teacherName;

    Integer courseId;

    String signDate;

    Integer arriveNumber;

    Integer studentNumber;
}
