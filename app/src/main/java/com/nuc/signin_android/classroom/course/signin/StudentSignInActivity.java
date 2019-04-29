package com.nuc.signin_android.classroom.course.signin;

import android.app.ProgressDialog;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nuc.signin_android.R;
import com.nuc.signin_android.base.BaseActivity;
import com.nuc.signin_android.entity.Course;
import com.nuc.signin_android.utils.Constant;
import com.nuc.signin_android.utils.ToastUtil;
import com.nuc.signin_android.utils.sign_in_utils.AESUtil;
import com.nuc.signin_android.utils.sign_in_utils.SendDataThread;
import com.nuc.signin_android.utils.sign_in_utils.WifiUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @Author: cuizhe
 * @Date: 2019/4/22 19:20
 * @Description:
 */
public class StudentSignInActivity extends BaseActivity {

    private static final String TAG = "StudentSignInActivity";

    @BindView(R.id.student_signin_back)
    ImageView studentSigninBack;
    @BindView(R.id.tv_teacher_ip)
    TextView teacherIpText; // 教师端的 IP 地址
    @BindView(R.id.tv_host_ip)
    TextView hostIpText; // 本机的 IP 地址
    @BindView(R.id.btn_student_sign_in)
    Button studentSignInBtn;

    private ProgressDialog mProgressDialog;//加载的小菊花
    private String strServerIp;
    private String strHostIp;
    private boolean isConnectAp = true;
    private boolean isWifiOpen = true;
    private WifiUtils wifiUtils = null;

    private String studentId;
    private String studentName;
    private Course mCourse;

    @Override
    protected void onStart() {
        super.onStart();
        // 注册 EvenBus
        EventBus.getDefault().register(this);
    }

    @Override
    protected void logicActivity(Bundle savedInstanceState) {

        studentId = getIntent().getExtras().getString("id");
        studentName = getIntent().getExtras().getString("name");
        mCourse = (Course) getIntent().getExtras().getSerializable("course");

        Log.e(TAG, "logicActivity: id = " + studentId + ",name = " + studentName + ",course = " + mCourse.toString());

        wifiUtils = new WifiUtils(getApplicationContext());
        before();
    }

    @Override
    protected int getLayoutView() {
        return R.layout.activity_signin_student;
    }

    @OnClick(R.id.student_signin_back)
    public void onStudentSigninBackClicker(){
        finish();
    }

    /**
     * 在设置 ip 之前的一些判断
     * 检测 wifi 是否打开
     * 检测是否连接热点
     */
    public void before() {
        if (!wifiUtils.wifiIsOpen()) {
            Toast.makeText(this, "请打开 Wi-Fi 并连接教师的热点！", Toast.LENGTH_SHORT).show();
            isWifiOpen = false;
        }
        // 首先判断有没有连接热点.
        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        Log.e("wifiInfo", wifiInfo.toString());
        String SSID = wifiInfo.getSSID();
        Log.e("SSID",SSID);
        if ("<unknown ssid>".equals(SSID) && isWifiOpen){
            Toast.makeText(this, "请检查是否连接教师热点", Toast.LENGTH_SHORT).show();
            isConnectAp = false;
        }
    }

    @OnClick(R.id.btn_student_sign_in)
    public void onStudentSignInBtnClicker(){
        before();
        if (isConnectAp){
            String content = studentId + "," + studentName + "," +mCourse.getCourseId();
            Log.e(TAG, "onStudentSignInBtnClicker: content = " + content);

            strServerIp = wifiUtils.getServerIp();
            Log.e(TAG, "onStudentSignInBtnClicker: strServerIp = " + strServerIp);
            if (wifiUtils.IsIpv4(strServerIp)){
                new SendDataThread(strServerIp,Constant.port, AESUtil.encrypt(Constant.password,content)).start();
                showProgressDialog("尝试发送数据到\n\t\t" + strServerIp, true);
            } else {
                ToastUtil.showToast(this, "IP不合法!");
            }
        }
    }

    /**
     * 连接结果
     *
     * @param resultCode 0：连接超时；1：发送成功 2:失败
     */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void sendResult(Integer resultCode) {
        Log.d("resultCode", "resultCode: " + resultCode);
        dismissProgressDialog();

        switch (resultCode) {
            case Constant.CODE_SUCCESS:
                ToastUtil.showToast(this, "发送成功");
                break;
            case Constant.CODE_TIMEOUT:
                ToastUtil.showToast(this, "连接超时");
                break;
            case Constant.CODE_UNKNOWN_HOST:
                ToastUtil.showToast(this, "错误-未知的host");
                break;
        }
    }

    /**
     * 数据加载
     *
     * @param msg      内容
     * @param isCancel 是否允许关闭 true - 允许 false - 不允许
     */
    public void showProgressDialog(final String msg, final boolean isCancel) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    if (mProgressDialog == null) {
                        mProgressDialog = new ProgressDialog(StudentSignInActivity.this);
                    }
                    if (mProgressDialog.isShowing()) {
                        return;
                    }
                    mProgressDialog.setMessage(msg);
                    mProgressDialog.setCancelable(isCancel);
                    mProgressDialog.setCanceledOnTouchOutside(false);
                    mProgressDialog.setOnCancelListener(null);
                    mProgressDialog.show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 隐藏数据加载的进度小菊花
     **/
    public void dismissProgressDialog() {
        try {
            if (mProgressDialog != null && mProgressDialog.isShowing()) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mProgressDialog.dismiss();
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
