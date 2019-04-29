package com.nuc.signin_android.classroom.course.signin;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nuc.signin_android.R;
import com.nuc.signin_android.base.BaseActivity;
import com.nuc.signin_android.entity.Student_SignIn;
import com.nuc.signin_android.net.GetApi;
import com.nuc.signin_android.utils.Constant;
import com.nuc.signin_android.utils.net.ApiListener;
import com.nuc.signin_android.utils.net.ApiUtil;

import org.json.JSONException;

import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * @Author: cuizhe
 * @Date: 2019/4/27 13:05
 * @Description:
 */
public class SignInInformationActivity extends BaseActivity {

    private static final String TAG = "SignInInforActivity";

    @BindView(R.id.sigin_information_back)
    ImageView signInInformationBack;
    @BindView(R.id.no_sign_in_number_text)
    TextView noSignInNumberText;
    @BindView(R.id.no_sign_in_recycler_view)
    RecyclerView noSignInRecyclerView;
    @BindView(R.id.sign_in_number_text)
    TextView signInNumberText;
    @BindView(R.id.sign_in_recycler_view)
    RecyclerView signInRecyclerView;

    private List<Student_SignIn> noSignInList;
    private List<Student_SignIn> signInList;
    private HashMap<String, String> params = new HashMap<>();

    private String signInId;

    private LinearLayoutManager linearLayoutManager;
    SignInFragmentAdapter adapter;

    @Override
    protected void logicActivity(Bundle savedInstanceState) {
        Bundle bundle = getIntent().getExtras();
        signInId = bundle.getString("signInId");
        showNoSignInStudentNumber();
        showNoSignInStudentList();
        showSignInStudentNumber();
        showSignInStudentList();
    }

    @OnClick(R.id.sigin_information_back)
    public void onSignInInformationBackClicker(){
        finish();
    }

    private void showNoSignInStudentNumber() {
        params.put("signInId", signInId);
        new GetApi(Constant.URL_STUDENTSIGNIN_GET_NO_SIGNIN_STUDENT_NUMBER, params).get(new ApiListener() {
            @Override
            public void success(ApiUtil apiUtil) {
                GetApi api = (GetApi) apiUtil;
                try {
                    String number = api.mJson.getString("noSignIn");
                    mainHandler.post(new Runnable() {
                        @SuppressLint("SetTextI18n")
                        @Override
                        public void run() {
                            noSignInNumberText.setText(number + " 人");
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void failure(ApiUtil apiUtil) {
                Log.i(TAG, "failure: 加载没有签到学生的人数失败");
            }
        });
    }


    private void showNoSignInStudentList() {
        params.put("signInId", signInId);
        new GetApi(Constant.URL_STUDENTSIGNIN_GET_NO_SIGN_IN_STUDENT, params).get(new ApiListener() {
            @Override
            public void success(ApiUtil apiUtil) {
                Log.e(TAG, "success: 没有签到的学生显示成功");
                GetApi api = (GetApi) apiUtil;
                String json = api.mJsonArray.toString();
                parseNoSignInListJSONWithGson(json);
                mainHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        linearLayoutManager = new LinearLayoutManager(SignInInformationActivity.this);
                        noSignInRecyclerView.setLayoutManager(linearLayoutManager);
                        adapter = new SignInFragmentAdapter(SignInInformationActivity.this, noSignInList, null);
                        noSignInRecyclerView.setAdapter(adapter);
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

    private void showSignInStudentNumber() {
        params.put("signInId", signInId);
        new GetApi(Constant.URL_STUDENTSIGNIN_GET_SIGNIN_STUDENT_NUMBER, params).get(new ApiListener() {
            @Override
            public void success(ApiUtil apiUtil) {
                GetApi api = (GetApi) apiUtil;
                try {
                    String number = api.mJson.getString("signIn");
                    mainHandler.post(new Runnable() {
                        @SuppressLint("SetTextI18n")
                        @Override
                        public void run() {
                            signInNumberText.setText(number + " 人");
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void failure(ApiUtil apiUtil) {
                Log.i(TAG, "failure: 加载签到学生的人数失败");
            }
        });
    }

    private void showSignInStudentList() {
        params.put("signInId", signInId);
        new GetApi(Constant.URL_STUDENTSIGNIN_GET_SIGN_IN_STUDENT, params).get(new ApiListener() {
            @Override
            public void success(ApiUtil apiUtil) {
                Log.e(TAG, "success: 签到的学生显示成功");
                GetApi api = (GetApi) apiUtil;
                String json = api.mJsonArray.toString();
                parseSignInListJSONWithGson(json);
                mainHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        linearLayoutManager = new LinearLayoutManager(SignInInformationActivity.this);
                        signInRecyclerView.setLayoutManager(linearLayoutManager);
                        adapter = new SignInFragmentAdapter(SignInInformationActivity.this, signInList, null);
                        signInRecyclerView.setAdapter(adapter);
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

    private void parseSignInListJSONWithGson(String json) {
        Gson gson = new Gson();
        signInList = gson.fromJson(json, new TypeToken<List<Student_SignIn>>() {
        }.getType());
    }

    private void parseNoSignInListJSONWithGson(String json) {
        Gson gson = new Gson();
        noSignInList = gson.fromJson(json, new TypeToken<List<Student_SignIn>>() {
        }.getType());
    }

    @Override
    protected int getLayoutView() {
        return R.layout.activity_signin_information;
    }
}
