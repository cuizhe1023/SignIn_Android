package com.nuc.signin_android.entity;

import lombok.Data;

/**
 * @Author: cuizhe
 * @Date: 2019/4/24 23:25
 * @Description:
 */
@Data
public class Student_SignIn {
    private Integer id;

    private Integer signInId;

    private String studentId;

    private String studentName;

    private String signInDate;

    private Integer signStatus;

    private String leaveReason;
}
