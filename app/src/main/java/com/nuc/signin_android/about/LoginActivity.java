package com.nuc.signin_android.about;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.nuc.signin_android.R;
import com.nuc.signin_android.base.BaseActivity;
import com.nuc.signin_android.net.GetApi;
import com.nuc.signin_android.net.tools.MyAsyncTask;
import com.nuc.signin_android.net.tools.TaskListener;
import com.nuc.signin_android.utils.Constant;
import com.nuc.signin_android.utils.ToastUtil;
import com.nuc.signin_android.utils.net.ApiListener;
import com.nuc.signin_android.utils.net.ApiUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.OnCheckedChanged;
import butterknife.OnClick;

/**
 * @Author: cuizhe
 * @Date: 2019/4/10 21:01
 * @Description:
 */
public class LoginActivity extends BaseActivity {

    private static final String TAG = "LoginActivity";

    @BindView(R.id.get_identity)
    RadioGroup rdoIdentity;
    @BindView(R.id.btn_login)
    Button login;
    @BindView(R.id.link_register)
    TextView register;
    @BindView(R.id.user_id)
    EditText userId;
    @BindView(R.id.user_password)
    EditText userPassword;

    private AlertDialog identity_register;

    HashMap<String,String> params = new HashMap<>();

    private String identity = "";
    @Override
    protected void logicActivity(Bundle savedInstanceState) {

    }

    /**
     * 在登录界面选择身份的方法
     */
    @OnCheckedChanged({R.id.rdo_btn_teacher,R.id.rdo_btn_student})
    public void onGetIdentityToLoginClicker(){
        rdoIdentity.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case R.id.rdo_btn_teacher:
                        identity = "teacher";
                        break;
                    case R.id.rdo_btn_student:
                        identity = "student";
                        break;
                }
                Log.i(TAG, "onCheckedChanged: 选择以" + identity + "身份登录");
            }
        });
    }

    /**
     * 登录按钮的处理
     */
    @OnClick(R.id.btn_login)
    public void onLoginBtnClicker(){
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String srtId = userId.getText().toString();
                String strPassword = userPassword.getText().toString();
                Log.i(TAG, "onClick: " + srtId + "," + strPassword + " 请求登录.");

                if (!TextUtils.isEmpty(srtId) && !TextUtils.isEmpty(strPassword)){
                    Log.i(TAG, "onClick: identity=" + identity);
                    if (identity.equals("teacher")){
                        getTeacher(srtId,strPassword);
                        Log.i(TAG, "onClick: 教师登陆");
                    }
                    if (identity.equals("student")){
                        getStudent(srtId,strPassword);
                        Log.i(TAG, "onClick: 学生登录");
                    }
                }
            }
        });
    }

    /**
     * 教师登录信息的处理
     *
     * @param id 教师id
     * @param password 密码
     */
    private void teacherLogin(final String id, String password) {

        String loginUrlStr = Constant.URL_TEACHER_LOGIN + "?teacherId=" + id +
                "&teacherPassword=" + password;

        Log.i(TAG, "teacherLogin: url:" + loginUrlStr);
        new MyAsyncTask(LoginActivity.this, new TaskListener() {
            @Override
            public void onCompletedListener(Object result) {
                if (result!=null && !"".equals(result)){
                    Log.i(TAG, "onCompletedListener: 登陆成功：" + result.toString());
                    try {
                        JSONObject json = new JSONObject(result.toString());

                        Log.i(TAG, "onCompletedListener: "
                                + "teacherId："+json.get("teacherId")
                                + "，teacherName：" + json.get("teacherName")
                                + "，teacherPassword：" +json.get("teacherPassword"));

                        // 将登录的用户名存入 SharedPreferences
                        editor = pref.edit();
                        editor.putString("account",(String) json.get("teacherId"));
                        editor.putString("identity",identity);
                        editor.putString("name",(String) json.get("teacherName"));
                        editor.putString("password",(String) json.get("teacherPassword"));
                        editor.apply();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    ToastUtil.showToast(LoginActivity.this,"登陆成功，欢迎" + pref.getString("name",null));
                    finish();
                }else {
                    ToastUtil.showToast(LoginActivity.this,"请检查用户名和密码是否正确！");
                }
            }
        }).execute(loginUrlStr);
    }

    private void getTeacher(String id, String password){

        params.put("teacherId",id);
        params.put("teacherPassword",password);

        new GetApi(Constant.URL_TEACHER_LOGIN,params).get(new ApiListener() {
            @Override
            public void success(ApiUtil apiUtil) {
                Log.i(TAG, "success: 老师登录成功");
            }

            @Override
            public void failure(ApiUtil apiUtil) {
                Log.i(TAG, "failure: 老师登录失败");
            }
        });
    }
    /**
     * 学生信息的处理
     *
     * @param id 学生id
     * @param password 密码
     */
    private void studentLogin(final String id, String password) {
        String loginUrlStr = Constant.URL_STUDENT_LOGIN + "?studentId=" + id +
                "&studentPassword=" + password;

        Log.i(TAG, "studentLogin: url:" + loginUrlStr);

        new MyAsyncTask(LoginActivity.this, new TaskListener() {
            @Override
            public void onCompletedListener(Object result) {

                if (result != null){
                    Log.i(TAG, "onCompletedListener: 登陆成功：" + result.toString());
                    try {
                        JSONObject json = new JSONObject(result.toString());

                        Log.i(TAG, "onCompletedListener: "
                                + "studentId:"+json.get("studentId")
                                + ", studentName:" + json.get("studentName")
                                + ", studentPassword:" +json.get("studentPassword")
                                + ", classId:" + json.get("classId")
                                + ", gender:" + json.get("gender")
                                + ", macAddress:" + json.get("macAddress"));

                        editor = pref.edit();
                        editor.putString("account",(String) json.get("studentId"));
                        editor.putString("identity",identity);
                        editor.putString("name",(String) json.get("studentName"));
                        editor.putString("password",(String) json.get("studentPassword"));
                        editor.apply();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    ToastUtil.showToast(LoginActivity.this,"登陆成功，欢迎" + pref.getString("name",null));
                    finish();
                }else {
                    ToastUtil.showToast(LoginActivity.this,"请检查用户名和密码是否正确！");
                }
            }
        }).execute(loginUrlStr);
    }

    private void getStudent(String id, String password){

        params.put("studentId",id);
        params.put("studentPassword",password);

        new GetApi(Constant.URL_STUDENT_LOGIN,params).get(new ApiListener() {
            @Override
            public void success(ApiUtil apiUtil) {
                Log.i(TAG, "success: 学生登录成功");
            }

            @Override
            public void failure(ApiUtil apiUtil) {
                Log.i(TAG, "failure: 学生登录失败");
            }
        });
    }

    @Override
    protected int getLayoutView() {
        return R.layout.activity_login;
    }

    /**
     * 跳转至注册页面
     */
    @OnClick(R.id.link_register)
    public void onSelectIdentityToRegisterClicker(){
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showIdentityAlertDialog(v);
            }
        });
    }

    /**
     * 注册时选择身份的弹窗
     *
     * @param view
     */
    private void showIdentityAlertDialog(View view){
        final String[] items = {"我是老师","我是学生"};
        final AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
        alertBuilder.setTitle("请选择你的身份");
        alertBuilder.setSingleChoiceItems(items, 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                Intent intent = null;
                switch (i){
                    case 0:
                        intent = new Intent(LoginActivity.this, TeacherRegisterActivity.class);
                        startActivity(intent);
                        identity_register.dismiss();
                        break;
                    case 1:
                        intent = new Intent(LoginActivity.this, StudentRegisterActivity.class);
                        startActivity(intent);
                        break;
                }
                Log.i(TAG, "onClick: 点击了" + i);
            }
        });
        identity_register = alertBuilder.create();
        alertBuilder.show();
    }



}
