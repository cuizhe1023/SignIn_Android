package com.nuc.signin_android.about;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;

import com.nuc.signin_android.MainActivity;
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
 * @Date: 2019/4/10 21:24
 * @Description:
 */
public class TeacherRegisterActivity extends BaseActivity {

    private final String TAG = "TeacherRegisterActivity";

    @BindView(R.id.back_teacher_register_image)
    ImageView backRegister;
    @BindView(R.id.input_teacher_id)
    EditText id;
    @BindView(R.id.input_teacher_name)
    EditText name;
    @BindView(R.id.input_teacher_password)
    EditText password;
    @BindView(R.id.input_teacher_repassword)
    EditText rePassword;
    private HashMap<String,String> params = new HashMap<>();

    @Override
    protected void logicActivity(Bundle savedInstanceState) {

    }

    @Override
    protected int getLayoutView() {
        return R.layout.activity_teacher_register;
    }

    @OnClick(R.id.back_teacher_register_image)
    public void onBackRegister(){
        activity.finish();
    }

    @OnClick(R.id.btn_teacher_register)
    public void onTeacherRegisterBtnClicker(){
        String teacherId = id.getText().toString().trim();
        String teacherName = name.getText().toString().trim();
        String teacherPassword = password.getText().toString().trim();
        String teacherRePassword = rePassword.getText().toString().trim();

        if (!TextUtils.isEmpty(teacherId) && !TextUtils.isEmpty(teacherName)
                && !TextUtils.isEmpty(teacherPassword) && !TextUtils.isEmpty(teacherRePassword)){
            Log.i(TAG, "OnTeacherRegisterBtnClicker: 信息不为空.");
            if (!teacherPassword.equals(teacherRePassword)){
                ToastUtil.showToast(this,"两次密码不相同");
            }else {
                registerTeacher(teacherId,teacherName,teacherPassword);
            }
        }else {
            ToastUtil.showToast(this,"账号、密码都不能为空！");
        }
    }

    /**
     * 进行注册
     *
     * @param account 工号
     * @param name 名称
     * @param password 密码
     */
    private void registerTeacher(String account, String name, String password) {

        params.put("teacherId",account);
        params.put("teacherName",name);
        params.put("teacherPassword",password);


        new PostApi(Constant.URL_TEACHER_REGISTER,params).post(new ApiListener() {
            @Override
            public void success(ApiUtil apiUtil) {
                mainHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        ToastUtil.showToast(TeacherRegisterActivity.this,"注册成功");
                        Intent intent = new Intent(TeacherRegisterActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });
            }

            @Override
            public void failure(ApiUtil apiUtil) {
                mainHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        ToastUtil.showToast(TeacherRegisterActivity.this,"注册失败!");
                    }
                });
            }
        });
    }


}
