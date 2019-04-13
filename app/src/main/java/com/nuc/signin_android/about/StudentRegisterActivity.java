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
import com.nuc.signin_android.net.tools.MyAsyncTask;
import com.nuc.signin_android.net.tools.TaskListener;
import com.nuc.signin_android.utils.Constant;
import com.nuc.signin_android.utils.ToastUtil;

import java.io.IOException;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

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
                register(studentId,studentName,studentPassword,classId,gender);
            }
        }
    }

    private void register(String studentId, String studentName,
                          String studentPassword, String classId, String gender) {

        String registerUrlStr = Constant.URL_STUDENT_REGISTER + "?studentId="+studentId
                + "&studentName=" + studentName + "&studentPassword=" + studentPassword
                + "&classId="+ classId +"&gender=" + gender;
        Log.e(TAG, "register: url = "+registerUrlStr);

        new MyAsyncTask(StudentRegisterActivity.this, new TaskListener() {
            @Override
            public void onCompletedListener(Object result) {
                String s = (String) result;
                Log.e("register", "onCompletedListener: " + s);

                if (s.equals("注册成功")){
                    ToastUtil.showToast(StudentRegisterActivity.this,"注册成功");
                    Intent intent = new Intent(StudentRegisterActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }else if (s.equals("该账号已注册，请使用此账号直接登录或使用其他账号注册")){
                    ToastUtil.showToast(StudentRegisterActivity.this,"该账号已注册，请使用此账号直接登录或使用其他账号注册");
                }else {
                    ToastUtil.showToast(StudentRegisterActivity.this,"连接异常");
                }
            }
        }).execute(registerUrlStr);

        // OkHttp 请求
//        try {
//            OkHttpClient client = new OkHttpClient();
//            RequestBody requestBody = new FormBody.Builder()
//                    .add("studentId",studentId)
//                    .add("studentName",studentName)
//                    .add("studentPassword",studentPassword)
//                    .add("classId",classId)
//                    .add("gender",gender)
//                    .build();
//            Request request = new Request.Builder()
//                    .url(Constant.URL)
//                    .post(requestBody)
//                    .build();
//            Response response = client.newCall(request).execute();
//            String responseData = response.body().string();
//            Log.i(TAG, "register: " + responseData);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }
}
