package com.nuc.signin_android.classroom.course.member;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nuc.signin_android.R;
import com.nuc.signin_android.base.BaseFragment;
import com.nuc.signin_android.entity.Course;
import com.nuc.signin_android.entity.SelectCourse;
import com.nuc.signin_android.net.GetApi;
import com.nuc.signin_android.utils.Constant;
import com.nuc.signin_android.utils.net.ApiListener;
import com.nuc.signin_android.utils.net.ApiUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;

/**
 * @Author: cuizhe
 * @Date: 2019/4/17 12:50
 * @Description:
 */
public class MemberFragment extends BaseFragment {

    private static final String TAG = "MemberFragment";

    @BindView(R.id.back_from_member_image)
    ImageView backFromMemberImage;
    @BindView(R.id.member_title_text)
    TextView memberTitleText;
    @BindView(R.id.tv_member_student_list)
    TextView memberStudentList;
    @BindView(R.id.score_text)
    TextView scoreText;
    @BindView(R.id.class_member_recycler)
    RecyclerView memberRecycler;
    @BindView(R.id.sum_member_text)
    TextView sumMemberText;

    private List<SelectCourse> list_all = new ArrayList<>();
    private HashMap<String,String> params = new HashMap<>();

    private MemberAdapter courseFragmentAdapter;
    private LinearLayoutManager linearLayoutManager;

    private static Course mCourse;

    public static MemberFragment getInstance(Course course){
        mCourse = course;
        return new MemberFragment();
    }

    @Override
    protected void logic() {
        memberTitleText.setText(mCourse.getCourseName());
        memberStudentList.setText("学生信息");
    }

    @Override
    protected void init(View view, Bundle savedInstanceState) {

    }

    @Override
    protected int getResourcesLayout() {
        return R.layout.fragment_member_layout;
    }

    public void updateDate(){
        if (list_all.size() != 0){
            list_all.clear();
        }

        getStudentList(mCourse.getCourseId());
        getStudentNumber(mCourse.getCourseId());
    }

    private void getStudentNumber(String courseId) {
        params.put("courseId",courseId);
        new GetApi(Constant.URL_SELECT_COURSE_SUM_STUDENT,params).get(new ApiListener() {
            @Override
            public void success(ApiUtil apiUtil) {
                GetApi api = (GetApi) apiUtil;
                JSONObject json = api.mJson;
                try {
                    String sum = json.getString("sum");
                    Log.i(TAG, "success: 学生人数有:" + sum + " 人。");
                    mainHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            studentNumber = sum;
                            sumMemberText.setText(sum + " 人");
                        }
                    });
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

    private void getStudentList(String courseId) {
        params.put("courseId",courseId);

        new GetApi(Constant.URL_SELECT_COURSE_STUDENTLIST,params).get(new ApiListener() {
            @Override
            public void success(ApiUtil apiUtil) {
                GetApi api = (GetApi) apiUtil;
                String json = api.mJsonArray.toString();
                parseJSONWithGson(json);
                for (SelectCourse selectCourse :
                        list_all) {
                    Log.e(TAG, "parseJSON: studentId = " + selectCourse.getStudentId());
                    Log.e(TAG, "parseJSON: studentName = "  + selectCourse.getStudentName());
                    Log.e(TAG, "parseJSON: gender = " + selectCourse.getGender());
                }
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        linearLayoutManager = new LinearLayoutManager(getContext());
                        memberRecycler.setLayoutManager(linearLayoutManager);
                        courseFragmentAdapter = new MemberAdapter(getActivity(),list_all,null);
                        memberRecycler.setAdapter(courseFragmentAdapter);
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

    private void parseJSONWithGson(String jsonData) {
        Gson gson = new Gson();
        List<SelectCourse> courseList = gson.fromJson(jsonData,new TypeToken<List<SelectCourse>>(){}.getType());
        list_all = courseList;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateDate();
    }
}
