package com.nuc.signin_android.entity;

import java.io.Serializable;

import lombok.Data;

/**
 * @Author: cuizhe
 * @Date: 2019/4/16 20:35
 * @Description:
 */
@Data
public class Course implements Serializable {
    String courseId;

    String classId;

    String courseName;

    String courseTime;

    String teacherId;

    String teacherName;

    String nameListPath;

    String scoreListPath;
}
