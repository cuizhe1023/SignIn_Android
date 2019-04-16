package com.nuc.signin_android.classroom;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.nuc.signin_android.R;
import com.nuc.signin_android.base.BaseActivity;
import com.nuc.signin_android.net.PostApi;
import com.nuc.signin_android.utils.Constant;
import com.nuc.signin_android.utils.ToastUtil;
import com.nuc.signin_android.utils.net.ApiListener;
import com.nuc.signin_android.utils.net.ApiUtil;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @Author: cuizhe
 * @Date: 2019/4/16 11:38
 * @Description: 创建课堂信息，导入学生信息，查看学生签到情况
 */
public class CreateCourseActivity extends BaseActivity {

    private static final String TAG = "CreateClassActivity";


    @BindView(R.id.class_id_edit)
    EditText classIdEdit;
    @BindView(R.id.course_name_edit)
    EditText courseNameEdit;
    @BindView(R.id.course_time_edit)
    EditText courseTimeEdit;
    @BindView(R.id.create_course_btn)
    Button createCourseBtn;

    private HashMap<String,String> params = new HashMap<>();


    @Override
    protected void logicActivity(Bundle savedInstanceState) {

    }

    @Override
    protected int getLayoutView() {
        return R.layout.activity_create_class;
    }

    @OnClick(R.id.create_course_btn)
    public void  onCreateClassBtnClicked(){
        if (userId != null && "teacher".equals(identity)){
            // 如果用户登录了，且身份是教师，才可以创建课堂
            // 获取课堂的信息
            String classId = classIdEdit.getText().toString().trim();
            String courseName = courseNameEdit.getText().toString().trim();
            String courseTime = courseTimeEdit.getText().toString().trim();
            String teacherId = pref.getString("account",null);
            String teacherName = pref.getString("name",null);
            insertCourse(classId,courseName,courseTime,teacherId,teacherName);
        }else if (userId != null && "student".equals(identity)){
            // 如果是学生，提示： 只有教师才可以创建课堂
            ToastUtil.showToast(this,"只有教师才可以创建课堂");
        }else if (userId == null){
            ToastUtil.showToast(this,"请先登录！");
        }
    }

    private void insertCourse(String classId, String courseName,
                              String courseTime, String teacherId, String teacherName) {
        params.put("classId",classId);
        params.put("courseName",courseName);
        params.put("courseTime",courseTime);
        params.put("teacherId",teacherId);
        params.put("teacherName",teacherName);
        new PostApi(Constant.URL_COURSE_INSERT,params).post(new ApiListener() {
            @Override
            public void success(ApiUtil apiUtil) {
                mainHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        ToastUtil.showToast(CreateCourseActivity.this,"课程添加成功!");
                    }
                });
            }

            @Override
            public void failure(ApiUtil apiUtil) {
                mainHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        ToastUtil.showToast(CreateCourseActivity.this,"课程添加失败!");
                    }
                });
            }
        });
    }
}
