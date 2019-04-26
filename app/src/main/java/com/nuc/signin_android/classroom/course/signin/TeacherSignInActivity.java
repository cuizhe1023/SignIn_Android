package com.nuc.signin_android.classroom.course.signin;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nuc.signin_android.R;
import com.nuc.signin_android.base.BaseActivity;
import com.nuc.signin_android.base.OnClickerListener;
import com.nuc.signin_android.entity.Course;
import com.nuc.signin_android.entity.Student_SignIn;
import com.nuc.signin_android.net.GetApi;
import com.nuc.signin_android.net.PostApi;
import com.nuc.signin_android.net.PutApi;
import com.nuc.signin_android.utils.Constant;
import com.nuc.signin_android.utils.ToastUtil;
import com.nuc.signin_android.utils.net.ApiListener;
import com.nuc.signin_android.utils.net.ApiUtil;
import com.nuc.signin_android.utils.sign_in_utils.AESUtil;
import com.nuc.signin_android.utils.sign_in_utils.LocalService;
import com.nuc.signin_android.utils.sign_in_utils.WifiUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONException;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @Author: cuizhe
 * @Date: 2019/4/22 19:20
 * @Description:
 */
public class TeacherSignInActivity extends BaseActivity {

    private static final String TAG = "TeacherSignInActivity";

    @BindView(R.id.start_sign_in_btn)
    Button startSignInBtn;
    @BindView(R.id.end_sign_in_btn)
    Button endSignInBtn;
    @BindView(R.id.tv_localAddress)
    TextView localAddress;
    @BindView(R.id.tv_sign_in_id)
    TextView signInIdText;
    @BindView(R.id.tv_receivedContent)
    TextView receivedContent;
    @BindView(R.id.tv_decryptContent)
    TextView decryptContent;
    @BindView(R.id.no_sign_in_student_list)
    RecyclerView noSignInRecyclerView;

    private List<Student_SignIn> noSignInList = new ArrayList<>();
    HashMap<String,String> params = new HashMap<>();

    private LocalService localService; // 用于启动监听的服务
    private ServiceConnection sc; // 服务连接

    private WifiUtils wifiUtils;
    private LinearLayoutManager linearLayoutManager;
    private SignInFragmentAdapter signInFragmentAdapter;

    private String teacherId;
    private String teacherName;
    private Course mCourse;
    private String signInId;
    private String studentNumber;
    SimpleDateFormat simDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
    String nowDate = simDate.format(new Date());

    @Override
    protected void onStart() {
        super.onStart();
        // 注册 EvenBus
        if (!EventBus.getDefault().isRegistered(this)){
            EventBus.getDefault().register(this);
        }
    }

    @Override
    protected void logicActivity(Bundle savedInstanceState) {
        teacherId = getIntent().getExtras().getString("id");
        teacherName = getIntent().getExtras().getString("name");
        studentNumber = getIntent().getExtras().getString("studentNumber");
        mCourse = (Course) getIntent().getExtras().getSerializable("course");

        wifiUtils = new WifiUtils(getApplicationContext());
        Log.i(TAG, "logicActivity: ");
        init();
    }

    @Override
    protected int getLayoutView() {
        return R.layout.activity_signin_teacher;
    }

    protected void init() {
        localAddress.setText(wifiUtils.getLocalIp());
        sc = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                LocalService.LocalBinder localBinder = (LocalService.LocalBinder) service;
                localService = localBinder.getService();
                localService.startWaitDataThread();
                ToastUtil.showToast(TeacherSignInActivity.this, "监听已启动");
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {

            }
        };
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getData(String data) {
        receivedContent.setText(data);
        String decryptText = AESUtil.decrypt(Constant.password, data);
        decryptContent.setText(decryptText);
        String studentInfo[] = decryptText.split(",");
        String id = studentInfo[0];
        String name = studentInfo[1];
        String courseId = studentInfo[2];
        // 将签到结果插入数据库中.
        Log.i(TAG, "getData: id = " + id + ", name = " + name + ", courseId = " + courseId);
        Log.i(TAG, "getData: signInId = " + signInId);
        insertStudentSignIn(id,signInId,courseId);
    }

    private void insertStudentSignIn(String id, String signInId, String courseId) {
        Log.i(TAG, "insertStudentSignIn: ");
        // 将签到者的数据更新在数据库，对其通过 id 对其进行状态进行置 1 操作
        if (mCourse.getCourseId().equals(courseId)){
            params.put("studentId",id);
            params.put("signDate",nowDate);
            params.put("signInId",signInId);
            new PutApi(Constant.URL_STUDENTSIGNIN_UPDATESTATUS,params).put(new ApiListener() {
                @Override
                public void success(ApiUtil apiUtil) {
                    PutApi api = (PutApi) apiUtil;
                    String json = null;
                    try {
                        json = api.mJson.getString("studentName");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Log.i(TAG, "success: json = " + json);
                    Log.e(TAG, "success: 更新数据成功！更新数据者：" + json);
                }

                @Override
                public void failure(ApiUtil apiUtil) {

                }
            });
        }
    }

    /**
     * 绑定service
     */
    private void connection() {
        Log.i(TAG, "connection: 执行了！");
        Intent intent = new Intent(this, LocalService.class);
        bindService(intent, sc, BIND_AUTO_CREATE);
    }

    private void createSignInData() {
        Log.i(TAG, "createSignInData: ");
        params.put("teacherId",teacherId);
        params.put("teacherName",teacherName);
        params.put("courseId",mCourse.getCourseId());
        params.put("signDate",nowDate);
        Log.i(TAG, "createSignInData: teacherId = " + teacherId);
        Log.i(TAG, "createSignInData: teacherName = " + teacherName);
        Log.i(TAG, "createSignInData: courseId = " + mCourse.getCourseId());
        Log.i(TAG, "createSignInData: signDate = " + nowDate);
        // 向表中添加数据
        new PostApi(Constant.URL_SIGNIN_CREATE,params).post(new ApiListener() {
            @Override
            public void success(ApiUtil apiUtil) {
                PostApi api = (PostApi) apiUtil;
                mainHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            signInId = api.mJson.getString("signInId");
                            Log.i(TAG, "run: signInId = " + signInId);
                            signInIdText.setText(signInId);
                            // 初始化签到信息
                            initSignInData();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }

            @Override
            public void failure(ApiUtil apiUtil) {

            }
        });
        params = null;
        params = new HashMap<>();
    }

    @OnClick({R.id.start_sign_in_btn,R.id.end_sign_in_btn})
    public void onTeacherSignInBtnClicker(View view){
        switch (view.getId()){
            case R.id.start_sign_in_btn:
                connection();
                // 向表中插入数据.
                createSignInData();
                break;
            case R.id.end_sign_in_btn:
                if (sc != null)
                    try {
                        unbindService(sc);
                        ToastUtil.showToast(this,"结束签到");
                    } catch (Exception e) {
                        e.printStackTrace();
                        ToastUtil.showToast(this,"您没有开启服务！");
                    }
                //getSignInStudentNumber(); // 在 Sign_In 表更新签到人数的信息
                getNoSignInStudent(); // 显示没有签到的学生列表
                break;
        }
    }

    private void getSignInStudentNumber() {
        // 获取没有签到的学生人数
        params.put("signInId",signInId);
        new GetApi(Constant.URL_STUDENTSIGNIN_GET_SIGNIN_STUDENT_NUMBER,params).get(new ApiListener() {
            @Override
            public void success(ApiUtil apiUtil) {
                GetApi api = (GetApi) apiUtil;
                try {
                    int number = api.mJson.getInt("signIn");
                    params = null;
                    params = new HashMap<>();
                    Log.i(TAG, "success: number = " + number);
                    //  updateSignInData(number);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void failure(ApiUtil apiUtil) {

            }
        });
        params = null;
        params = new HashMap<>();
    }

    private void updateSignInData(int number) {
        params.put("signInId",signInId);
        params.put("arriveNumber",String.valueOf(number));
        params.put("studentNumber",studentNumber);

        new PutApi(Constant.URL_SIGNIN_UPDATE_SIGN_IN_DATA,params).put(new ApiListener() {
            @Override
            public void success(ApiUtil apiUtil) {
                Log.i(TAG, "success: 更新数据成功");
                PutApi api = (PutApi) apiUtil;
                String json = api.mJson.toString();
                Log.i(TAG, "success: json = " + json);
            }

            @Override
            public void failure(ApiUtil apiUtil) {

            }
        });
    }

    private void getNoSignInStudent() {
        params.put("signInId",signInId);
        Log.i(TAG, "getNoSignInStudent: signInId = " + signInId);
        new GetApi(Constant.URL_STUDENTSIGNIN_GET_NO_SIGN_IN_STUDENT,params).get(new ApiListener() {
            @Override
            public void success(ApiUtil apiUtil) {
                GetApi api = (GetApi) apiUtil;
                String json = api.mJsonArray.toString();
                parseJSONWithGson(json);
                Log.i(TAG, "success: 没签到的学生：");
                for (Student_SignIn signIn :
                        noSignInList) {
                    Log.i(TAG, "success: studentName = " + signIn.getStudentName());
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        linearLayoutManager = new LinearLayoutManager(TeacherSignInActivity.this);
                        noSignInRecyclerView.setLayoutManager(linearLayoutManager);
                        signInFragmentAdapter = new SignInFragmentAdapter(TeacherSignInActivity.this, noSignInList,
                                new OnClickerListener() {
                                    @Override
                                    public void click(int position, View view) {
                                        Log.e(TAG, "click: noSignInList.get("+position+") = " + noSignInList.get(position));
                                        Intent intent = new Intent(TeacherSignInActivity.this,StudentLeaveReasonActivity.class);
                                        Bundle bundle = new Bundle();
                                        bundle.putSerializable("student", noSignInList.get(position));
                                        bundle.putString("signInId",signInId);
                                        intent.putExtras(bundle);
                                        startActivity(intent);
                                    }
                                });
                        noSignInRecyclerView.setAdapter(signInFragmentAdapter);
                    }
                });
            }

            @Override
            public void failure(ApiUtil apiUtil) {

            }
        });
        params = null;
        params = new HashMap<>();
    }

    private void parseJSONWithGson(String json) {
        Gson gson = new Gson();
        List<Student_SignIn> list = gson.fromJson(json,new TypeToken<List<Student_SignIn>>(){}.getType());
        noSignInList = list;
    }

    private void initSignInData() {
        Log.i(TAG, "initSignInData: signInId = " + signInIdText.getText().toString().trim());
        params.put("signInId",signInIdText.getText().toString().trim());
        params.put("signDate",nowDate);
        params.put("courseId",mCourse.getCourseId());
        new PostApi(Constant.URL_STUDENTSIGNIN_INITSIGNIN,params).post(new ApiListener() {
            @Override
            public void success(ApiUtil apiUtil) {
                mainHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        ToastUtil.showToast(TeacherSignInActivity.this,"初始化签到信息成功");
                    }
                });
            }

            @Override
            public void failure(ApiUtil apiUtil) {
                mainHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        ToastUtil.showToast(TeacherSignInActivity.this,"初始化签到信息失败");
                    }
                });
            }
        });
        params = null;
        params = new HashMap<>();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
