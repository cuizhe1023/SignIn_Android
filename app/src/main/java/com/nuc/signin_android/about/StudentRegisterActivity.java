package com.nuc.signin_android.about;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.nuc.signin_android.MainActivity;
import com.nuc.signin_android.R;
import com.nuc.signin_android.base.BaseActivity;
import com.nuc.signin_android.net.PostApi;
import com.nuc.signin_android.utils.Constant;
import com.nuc.signin_android.utils.ToastUtil;
import com.nuc.signin_android.utils.device.DeviceUtils;
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
public class StudentRegisterActivity extends BaseActivity {

    private final String TAG = "StudentRegisterActivity";

    @BindView(R.id.back_student_register_image)
    ImageView backRegister;
    @BindView(R.id.input_student_id)
    EditText id;
    @BindView(R.id.input_student_name)
    EditText name;
    @BindView(R.id.input_student_password)
    EditText password;
    @BindView(R.id.input_student_repassword)
    EditText rePassword;
    @BindView(R.id.input_classNumber)
    EditText classNumber;
    @BindView(R.id.input_sex)
    EditText sex;
    @BindView(R.id.btn_student_register)
    Button register;
    private HashMap<String,String> params = new HashMap<>();

    @Override
    protected void logicActivity(Bundle savedInstanceState) {
        
    }

    @Override
    protected int getLayoutView() {
        return R.layout.activity_student_register;
    }

    @OnClick(R.id.back_student_register_image)
    public void onBackRegister(){
        activity.finish();
    }

    @OnClick(R.id.btn_student_register)
    public void onStudentRegisterBtnClicker(){
        String studentId = id.getText().toString().trim();
        String studentName = name.getText().toString().trim();
        String studentPassword = password.getText().toString().trim();
        String studentRePassword = rePassword.getText().toString().trim();
        String classId = classNumber.getText().toString().trim();
        String gender = sex.getText().toString().trim();

        if (!TextUtils.isEmpty(studentId) && !TextUtils.isEmpty(studentName)
                && !TextUtils.isEmpty(studentPassword) && !TextUtils.isEmpty(studentRePassword)
                && !TextUtils.isEmpty(classId) && !TextUtils.isEmpty(gender)){
            Log.i(TAG, "onStudentRegisterBtnClicker: 信息不为空." );
            if (!studentPassword.equals(studentRePassword)){
                ToastUtil.showToast(this,"两次密码不相同");
            }else {
                registerTeacher(studentId,studentName,studentPassword,classId,gender,DeviceUtils.getDeviceId());
            }
        }
    }

    private void registerTeacher(String studentId, String studentName,
                                 String studentPassword, String classId,
                                 String gender, String serialNumber) {

        params.put("studentId",studentId);
        params.put("studentName",studentName);
        params.put("studentPassword",studentPassword);
        params.put("classId",classId);
        params.put("gender",gender);
        params.put("serialNumber",serialNumber);

        new PostApi(Constant.URL_STUDENT_REGISTER,params).post(new ApiListener() {
            @Override
            public void success(ApiUtil apiUtil) {
                mainHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        ToastUtil.showToast(StudentRegisterActivity.this,"注册成功");
                        Intent intent = new Intent(StudentRegisterActivity.this, MainActivity.class);
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
                        ToastUtil.showToast(StudentRegisterActivity.this,"注册失败!");
                    }
                });
            }
        });
    }
}
