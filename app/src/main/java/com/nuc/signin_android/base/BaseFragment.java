package com.nuc.signin_android.base;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nuc.signin_android.entity.Student;
import com.nuc.signin_android.entity.Teacher;
import com.nuc.signin_android.net.tools.MyAsyncTask;
import com.nuc.signin_android.net.tools.TaskListener;
import com.nuc.signin_android.utils.Constant;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * @Author: cuizhe
 * @Date: 2019/4/10 19:31
 * @Description:
 */
public abstract class BaseFragment extends Fragment {
    private static final String TAG = "BaseFragment";
    private Unbinder unbinder;
    protected SharedPreferences pref;
    protected SharedPreferences.Editor editor;
    protected String userId;
    protected String identity;
    protected Teacher user_teacher = new Teacher();
    protected Student user_student = new Student();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(getResourcesLayout(),container,false);
        unbinder = ButterKnife.bind(this,view);

        // 通过 SharedPreferences 存储用户信息
        pref = getActivity().getSharedPreferences("setting", Context.MODE_PRIVATE);
        userId = pref.getString("account",null);
        identity = pref.getString("identity",null);
        Log.i(TAG, "onCreateView: userId is " + userId);
        init(view,savedInstanceState);
        logic();
        return view;
    }

    protected abstract void logic();

    protected abstract void init(View view, Bundle savedInstanceState);
    protected abstract int getResourcesLayout();

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();// 解除绑定
    }

    protected void startActivityTo(Class activityClass, String s){
        Intent intent = new Intent(getContext(),activityClass);
        Bundle bundle = new Bundle();
        // 把数据打包为 bundle
        bundle.putString("random_number",s);
        // Intent 携带数据包
        intent.putExtras(bundle);
        startActivity(intent);
    }


    @Override
    public void onResume() {
        super.onResume();
        Log.i(TAG, "onResume: ");
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();

        Log.i(TAG, "onStart: ");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.i(TAG, "onPause: ");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.i(TAG, "onStop: ");
    }


    private void findStudentById(final String id) {
        String getStudentUrlStr = Constant.URL_STUDENT_LOGIN + "?studentId=" + id;

        Log.i(TAG, "findStudentById: url = " + getStudentUrlStr);
        new MyAsyncTask(getActivity(), new TaskListener() {
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

                        user_student.setStudentId((String) json.get("studentId"));
                        user_student.setStudentName((String) json.get("studentName"));
                        user_student.setStudentPassword((String) json.get("studentPassword"));
                        user_student.setClassId((String) json.get("classId"));
                        user_student.setGender((String) json.get("gender"));
                        user_student.setMacAddress((String) json.get("macAddress"));

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else {
                    Log.i(TAG, "onCompletedListener: 没有找到该用户！");
                }
            }
        }).execute(getStudentUrlStr);
    }

    private void findTeacherById(final String id) {
        String getTeacherUrlStr = Constant.URL_TEACHER_GETONE + "?teacherId=" + id;
        Log.i(TAG, "findTeacherById: url = " + getTeacherUrlStr);

        new MyAsyncTask(getActivity(), new TaskListener() {
            @Override
            public void onCompletedListener(Object result) {
                Log.i(TAG, "onCompletedListener: result = " + result);
                if (result != null) {
                    Log.i(TAG, "onCompletedListener: 查找成功：" + result.toString());
                    try {
                        JSONObject json = new JSONObject(result.toString());

                        Log.i(TAG, "onCompletedListener: "
                                + "teacherId:" + json.get("teacherId")
                                + ", teacherName:" + json.get("teacherName")
                                + ", teacherPassword:" + json.get("teacherPassword"));

                        user_teacher.setTeacherId((String) json.get("teacherId"));
                        user_teacher.setTeacherName((String) json.get("teacherName"));
                        user_teacher.setTeacherPassword((String) json.get("teacherPassword"));

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }else {
                    Log.i(TAG, "onCompletedListener: 没有找到该用户！");
                }
            }
        }).execute(getTeacherUrlStr);
    }

}
