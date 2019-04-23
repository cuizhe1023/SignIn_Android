package com.nuc.signin_android.classroom.course.signin;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.nuc.signin_android.R;
import com.nuc.signin_android.base.BaseActivity;
import com.nuc.signin_android.entity.Course;
import com.nuc.signin_android.net.PostApi;
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
import java.util.Date;
import java.util.HashMap;

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

    HashMap<String,String> params = new HashMap<>();

    private LocalService localService; // 用于启动监听的服务
    private ServiceConnection sc; // 服务连接

    private WifiUtils wifiUtils;

    private String teacherId;
    private String teacherName;
    private Course mCourse;
    private String signInId;

    @Override
    protected void onStart() {
        super.onStart();
        // 注册 EvenBus
        EventBus.getDefault().register(this);
    }

    @Override
    protected void logicActivity(Bundle savedInstanceState) {
        teacherId = getIntent().getExtras().getString("id");
        teacherName = getIntent().getExtras().getString("name");
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
                // 向表中插入数据.
                createSignInData();
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {

            }
        };
    }

    private void createSignInData() {
        params.put("teacherId",teacherId);
        params.put("teacherName",teacherName);
        params.put("courseId",mCourse.getCourseId());
        SimpleDateFormat simDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        String nowDate = simDate.format(new Date());
        params.put("signDate",nowDate);
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
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getData(String data) {
        receivedContent.setText(data);
        Log.i(TAG, "getData: data = " + data);
        String decryptText = AESUtil.decrypt(Constant.password, data);
        decryptContent.setText(decryptText);
        Log.i(TAG, "getData: 解码后 = " + AESUtil.decrypt(Constant.password, data));
        String studentInfo[] = decryptText.split(",");
        String id = studentInfo[0];
        String name = studentInfo[1];
        // 将签到结果插入数据库中.
        Log.i(TAG, "getData: id = " + id + ", name = " + name );
        Log.i(TAG, "getData: signInId = " + signInId);
        insertStudentSignIn();
    }

    private void insertStudentSignIn() {

    }

    /**
     * 绑定service
     */
    private void connection() {
        Log.i(TAG, "connection: 执行了！");
        Intent intent = new Intent(this, LocalService.class);
        bindService(intent, sc, BIND_AUTO_CREATE);
    }

    @OnClick({R.id.start_sign_in_btn,R.id.end_sign_in_btn})
    public void onTeacherSignInBtnClicker(View view){
        switch (view.getId()){
            case R.id.start_sign_in_btn:
                connection();
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
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (sc != null){
            unbindService(sc);
        }
    }
}
