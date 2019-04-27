package com.nuc.signin_android.classroom.course.signin;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nuc.signin_android.R;
import com.nuc.signin_android.base.BaseFragment;
import com.nuc.signin_android.base.OnClickerListener;
import com.nuc.signin_android.entity.Course;
import com.nuc.signin_android.entity.SignIn;
import com.nuc.signin_android.net.GetApi;
import com.nuc.signin_android.utils.Constant;
import com.nuc.signin_android.utils.net.ApiListener;
import com.nuc.signin_android.utils.net.ApiUtil;
import com.wangjie.rapidfloatingactionbutton.RapidFloatingActionButton;
import com.wangjie.rapidfloatingactionbutton.RapidFloatingActionHelper;
import com.wangjie.rapidfloatingactionbutton.RapidFloatingActionLayout;
import com.wangjie.rapidfloatingactionbutton.contentimpl.labellist.RFACLabelItem;
import com.wangjie.rapidfloatingactionbutton.contentimpl.labellist.RapidFloatingActionContentLabelList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;

/**
 * @Author: cuizhe
 * @Date: 2019/4/22 17:42
 * @Description: 显示课程签到的情况，方便老师查看每次签到多少人.
 */
public class SignInFragment extends BaseFragment implements RapidFloatingActionContentLabelList.OnRapidFloatingActionContentLabelListListener {

    private static final String TAG = "SignInFragment";

    @BindView(R.id.do_title_text)
    TextView doTitleText;
    @BindView(R.id.sign_in_recycler_view)
    RecyclerView signInRecyclerView;
    @BindView(R.id.do_fbtn)
    RapidFloatingActionButton doFbtn;
    @BindView(R.id.do_fbtn_layout)
    RapidFloatingActionLayout doFbtnLayout;

    private HashMap<String, String> params = new HashMap<>();
    private static Course mCourse;

    private RapidFloatingActionHelper rfabHelper;
    private SignInRecordAdapter adapter;

    private String id;
    private String name;
    private static String number = studentNumber;
    private List<SignIn> list_all = new ArrayList<>();// 签到信息的历史列表

    private LinearLayoutManager linearLayoutManager;

    public static SignInFragment getInstance(Course course) {
        mCourse = course;
        return new SignInFragment();
    }

    @Override
    protected void logic() {
        id = userId;
        name = pref.getString("name", null);
        RapidFloatingActionContentLabelList rfaContent = new RapidFloatingActionContentLabelList(getContext());
        List<RFACLabelItem> items = new ArrayList<>();
        items.add(new RFACLabelItem<Integer>()
                .setLabel("发起签到")
                .setResId(R.drawable.ic_add_black_24dp)
                .setIconNormalColor(0xffd84315)
                .setIconPressedColor(0xffbf360c)
                .setWrapper(1)
        );
        rfaContent
                .setItems(items)
                .setIconShadowRadius(5)
                .setIconShadowColor(0xff888888)
                .setIconShadowDy(5)
        ;
        rfabHelper = new RapidFloatingActionHelper(
                getContext(),
                doFbtnLayout,
                doFbtn,
                rfaContent
        ).build();
        rfaContent.setOnRapidFloatingActionContentLabelListListener(this);
    }

    @Override
    protected void init(View view, Bundle savedInstanceState) {

    }

    @Override
    protected int getResourcesLayout() {
        return R.layout.fragment_signin;
    }

    @Override
    public void onRFACItemLabelClick(int position, RFACLabelItem item) {
        startActivityByPosition(position);
    }

    @Override
    public void onRFACItemIconClick(int position, RFACLabelItem item) {
        startActivityByPosition(position);
    }

    private void startActivityByPosition(int position) {
        if (position == 0) {
            if ("student".equals(identity)) {
                startActivityTo(StudentSignInActivity.class, id, name, mCourse, number);
            }
            if ("teacher".equals(identity)) {
                Log.i(TAG, "startActivityByPosition: studentNumber = " + number);
                startActivityTo(TeacherSignInActivity.class, id, name, mCourse, number);
            }
        }

    }

    private void updateDate() {
        // 更新该课程的签到信息
        if (list_all.size() != 0) {
            list_all.clear();
        }

        getSignInList(mCourse.getCourseId());
    }

    private void getSignInList(String courseId) {
        params.put("courseId", courseId);
        new GetApi(Constant.URL_SIGNIN_GET_SIGN_IN_LIST, params).get(new ApiListener() {
            @Override
            public void success(ApiUtil apiUtil) {
                GetApi api = (GetApi) apiUtil;
                String json = api.mJsonArray.toString();
                parseJSONWithGson(json);
                for (SignIn signIn :
                        list_all) {
                    Log.e(TAG, "success: signInId = " + signIn.getSignInId());
                    Log.e(TAG, "success: teacherId = " + signIn.getTeacherId());
                    Log.e(TAG, "success: teacherName = "  + signIn.getTeacherName());
                    Log.e(TAG, "success: date = " + signIn.getSignDate());
                    Log.e(TAG, "success: courseId = " + signIn.getCourseId());
                    Log.e(TAG, "success: arriveNumber = " + signIn.getArriveNumber());
                    Log.e(TAG, "success: studentNumber = " + signIn.getStudentNumber());
                }
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        linearLayoutManager = new LinearLayoutManager(getContext());
                        signInRecyclerView.setLayoutManager(linearLayoutManager);
                        adapter = new SignInRecordAdapter(getActivity(), list_all, new OnClickerListener() {
                            @Override
                            public void click(int position, View view) {
                                Log.e(TAG, "click: noSignInList.get("+position+") = " + list_all.get(position));
                                Intent intent = new Intent(getActivity(),SignInInformationActivity.class);
                                Bundle bundle = new Bundle();
                                bundle.putString("signInId", String.valueOf(list_all.get(position).getSignInId()));
                                intent.putExtras(bundle);
                                startActivity(intent);
                            }
                        });
                        signInRecyclerView.setAdapter(adapter);
                    }
                });
            }

            @Override
            public void failure(ApiUtil apiUtil) {

            }
        });
    }

    private void parseJSONWithGson(String json) {
        Gson gson = new Gson();
        List<SignIn> courseList = gson.fromJson(json, new TypeToken<List<SignIn>>() {}.getType());
        list_all = courseList;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateDate();
    }
}
