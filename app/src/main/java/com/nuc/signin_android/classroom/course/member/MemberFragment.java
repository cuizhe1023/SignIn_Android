package com.nuc.signin_android.classroom.course.member;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
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
import java.util.Map;

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
    @BindView(R.id.appbar_title_text)
    TextView appbarTitleText;
    @BindView(R.id.rank)
    TextView rank;
    @BindView(R.id.score_text)
    TextView scoreText;
    @BindView(R.id.class_member_recycler)
    RecyclerView memberRecycler;
    @BindView(R.id.sum_member_text)
    TextView sumMemberText;
    @BindView(R.id.qiandao_btn)
    FloatingActionButton qiandaoBtn;

    private Map<String, Integer> map;
    private List<Map.Entry<String, Integer>> entries;
    private static String courseId ;
    private List<SelectCourse> list_all = new ArrayList<>();
    private List<SelectCourse> list_create = new ArrayList<>();
    private HashMap<String,String> params = new HashMap<>();

    private MemberAdapter courseFragmentAdapter;
    private LinearLayoutManager linearLayoutManager;

    public static MemberFragment getInstance(String course_id){
        courseId = course_id;
        Log.i(TAG, "getInstance: courseId = " + courseId);
        return new MemberFragment();
    }

    @Override
    protected void logic() {

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

        // 如果是老师，显示选修了这门课的学生的信息.
        getStudentList(courseId);
        getStudentNumber(courseId);
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
