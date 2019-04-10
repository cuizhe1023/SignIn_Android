package com.nuc.signin_android.about;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.nuc.signin_android.MainActivity;
import com.nuc.signin_android.R;
import com.nuc.signin_android.base.BaseActivity;
import com.nuc.signin_android.net.SendDataToService;
import com.nuc.signin_android.net.tools.MyAsyncTask;
import com.nuc.signin_android.net.tools.TaskListener;
import com.nuc.signin_android.utils.Constant;
import com.nuc.signin_android.utils.ToastUtil;

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

    @Override
    protected void logicActivity(Bundle savedInstanceState) {

    }

    @Override
    protected int getLayoutView() {
        return R.layout.activity_teacher_register;
    }

    @OnClick(R.id.back_teacher_register_image)
    private void onBackRegister(){
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
                register(teacherId,teacherName,teacherPassword);
            }
        }else {
            ToastUtil.showToast(this,"账号、密码都不能为空！");
        }
    }

    private void register(String account, String name, String password) {

        String registerUrlStr = Constant.URL_REGISTER_TEACHER + "?TeacherId=" + account +
                "&TeacherName=" +name+"&TeacherPassword=" + password;

        Log.e("register", "url:" + registerUrlStr);
        new MyAsyncTask(TeacherRegisterActivity.this, new TaskListener() {
            @Override
            public void onCompletedListener(Object result) {
                String s = (String) result;
                Log.e("register", "onCompletedListener: " + s);

                if (s.equals("注册成功")){
                    ToastUtil.showToast(TeacherRegisterActivity.this,"注册成功");
                    Intent intent = new Intent(TeacherRegisterActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }else if (s.equals("该账号已注册，请使用此账号直接登录或使用其他账号注册")){
                    ToastUtil.showToast(TeacherRegisterActivity.this,"注册成功");
                }else {
                    ToastUtil.showToast(TeacherRegisterActivity.this,"注册成功");
                }
            }
        }).execute(registerUrlStr);
    }


}
