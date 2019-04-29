package com.nuc.signin_android.classroom.course.signin;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.nuc.signin_android.R;
import com.nuc.signin_android.base.BaseActivity;
import com.nuc.signin_android.entity.Student_SignIn;
import com.nuc.signin_android.net.GetApi;
import com.nuc.signin_android.net.PostApi;
import com.nuc.signin_android.utils.Constant;
import com.nuc.signin_android.utils.ToastUtil;
import com.nuc.signin_android.utils.net.ApiListener;
import com.nuc.signin_android.utils.net.ApiUtil;

import org.json.JSONException;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @Author: cuizhe
 * @Date: 2019/4/26 18:01
 * @Description:
 */
public class StudentLeaveReasonActivity extends BaseActivity {

    private static final String TAG = "StudentLeaveReasonActivity";

    @BindView(R.id.leave_reason_back_image)
    ImageView leaveReasonBack;
    @BindView(R.id.leave_student_name_text)
    TextView leaveStudentName;
    @BindView(R.id.leave_student_id_text)
    TextView leaveStudentId;
    @BindView(R.id.leave_time_text)
    TextView leaveTime;
    @BindView(R.id.et_leave_reason)
    EditText leaveReason;
    @BindView(R.id.leave_image)
    ImageView leaveImage;

    private Student_SignIn student_signIn;
    private String signInId;

    private SimpleDateFormat simDate = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式
    private String nowDate = simDate.format(new Date());

    private HashMap<String,String> params = new HashMap<>();

    @Override
    protected void logicActivity(Bundle savedInstanceState) {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        student_signIn = (Student_SignIn) bundle.getSerializable("student");
        signInId = bundle.getString("signInId");

        leaveStudentName.setText(student_signIn.getStudentName());
        leaveStudentId.setText(student_signIn.getStudentId());
        leaveTime.setText(nowDate);

        params.put("signInId",signInId);
        params.put("studentId", leaveStudentId.getText().toString().trim());
        new GetApi(Constant.URL_STUDENTSIGNIN_GET_LEAVE_REASON,params).get(new ApiListener() {
            @Override
            public void success(ApiUtil apiUtil) {
                GetApi api = (GetApi) apiUtil;
                try {
                    String result = api.mJson.getString("leaveReason");
                    if (!"null".equals(result)){
                        leaveReason.setText(result);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void failure(ApiUtil apiUtil) {

            }
        });
    }

    @Override
    protected int getLayoutView() {
        return R.layout.activity_leave_reason;
    }

    @OnClick(R.id.leave_reason_back_image)
    public void onLeaveReasonBackImageClicker(){
        finish();
    }

    @OnClick(R.id.leave_image)
    public void onLeaveReasonImageClicker(){
        String reason = leaveReason.getText().toString();
        if (reason != null){
            params.put("signInId",signInId);
            params.put("studentId",leaveStudentId.getText().toString());
            params.put("leaveReason",reason);
            new PostApi(Constant.URL_STUDENTSIGNIN_UPDAT_REASON,params).post(new ApiListener() {
                @Override
                public void success(ApiUtil apiUtil) {
                    mainHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            ToastUtil.showToast(StudentLeaveReasonActivity.this,"修改学生请假理由成功");
                        }
                    });
                }

                @Override
                public void failure(ApiUtil apiUtil) {
                    mainHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            ToastUtil.showToast(StudentLeaveReasonActivity.this,"修改学生请假理由失败");
                        }
                    });
                }
            });
        }else {
            ToastUtil.showToast(StudentLeaveReasonActivity.this,"请假理由不能为空");
        }
    }
}
