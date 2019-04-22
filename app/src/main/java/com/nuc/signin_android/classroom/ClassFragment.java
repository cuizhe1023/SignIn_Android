package com.nuc.signin_android.classroom;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nuc.signin_android.R;
import com.nuc.signin_android.base.BaseFragment;
import com.nuc.signin_android.base.OnClickerListener;
import com.nuc.signin_android.classroom.course.CourseActivity;
import com.nuc.signin_android.entity.Course;
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
 * @Date: 2019/4/10 20:10
 * @Description: 主界面
 */
public class ClassFragment extends BaseFragment implements RapidFloatingActionContentLabelList.OnRapidFloatingActionContentLabelListListener {

    @BindView(R.id.class_fragment_recycler)
    RecyclerView classFragmentRecycler;
    @BindView(R.id.activity_main_rfab)
    RapidFloatingActionButton activityMainRfab;
    @BindView(R.id.activity_main_rfal)
    RapidFloatingActionLayout activityMainRfal;

    private RapidFloatingActionHelper rfabHelper;
    private List<Course>list_all = new ArrayList<>();
    private static final String TAG = "ClassFragment";
    private HashMap<String,String> params = new HashMap<>();

    private ClassFragmentAdapter classFragmentAdapter;
    private LinearLayoutManager linearLayoutManager;

    public static ClassFragment getInstance() {
        // Required empty public constructor
        return new ClassFragment();
    }

    @Override
    protected void logic() {
        RapidFloatingActionContentLabelList rfaContent = new RapidFloatingActionContentLabelList(getContext());
        List<RFACLabelItem> items = new ArrayList<>();
        items.add(new RFACLabelItem<Integer>()
                .setLabel("创建微课")
                .setResId(R.drawable.ic_add_black_24dp)
                .setIconNormalColor(0xffd84315)
                .setIconPressedColor(0xffbf360c)
                .setWrapper(0)
        );
        rfaContent
                .setItems(items)
                .setIconShadowRadius(5)
                .setIconShadowColor(0xff888888)
                .setIconShadowDy(5)
        ;
        rfabHelper = new RapidFloatingActionHelper(
                getContext(),
                activityMainRfal,
                activityMainRfab,
                rfaContent
        ).build();
        rfaContent.setOnRapidFloatingActionContentLabelListListener(this);
    }

    @Override
    protected void init(View view, Bundle savedInstanceState) {

    }

    @Override
    protected int getResourcesLayout() {
        return R.layout.fragment_class;
    }

    /**
     * 从服务器获取数据更新
     */
    private void updateDate() {
        if (list_all.size() != 0){
            list_all.clear();
        }

        // 如果是老师，显示创建的课程信息.
        if (userId != null && "teacher".equals(identity)){
            getCreateList(userId);
        }

        // 如果是学生，显示选修了的课程信息。
        if (userId != null && "student".equals(identity)){
            getStudentCourseList(userId);
        }
    }

    private void getStudentCourseList(String studentId) {
        params.put("studentId",studentId);

        new GetApi(Constant.URL_COURSE_STUDENTCOURSE,params).get(new ApiListener() {
            @Override
            public void success(ApiUtil apiUtil) {
                GetApi api = (GetApi) apiUtil;
                String json = api.mJsonArray.toString();
                parseJSONWithGson(json);
                for (Course course :
                        list_all) {
                    Log.i(TAG, "success: courseId = " + course.getCourseId());
                    Log.i(TAG, "success: classId = "  + course.getClassId());
                    Log.i(TAG, "success: courseName = " + course.getCourseName());
                    Log.i(TAG, "success: courseTeacherId = " + course.getTeacherId());
                }

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        linearLayoutManager = new LinearLayoutManager(getContext());
                        classFragmentRecycler.setLayoutManager(linearLayoutManager);
                        classFragmentAdapter = new ClassFragmentAdapter(getContext(), list_all
                                , new OnClickerListener() {
                            @Override
                            public void click(int position, View view) {
                                Log.e(TAG, "click: list_create.get("+position+") = " + list_all.get(position));

                                Intent intent = new Intent(getContext(), CourseActivity.class);
                                Bundle bundle = new Bundle();
                                bundle.putSerializable("course", list_all.get(position));
                                intent.putExtras(bundle);
                                startActivity(intent);
                            }
                        });
                        classFragmentRecycler.setAdapter(classFragmentAdapter);
                    }
                });

            }

            @Override
            public void failure(ApiUtil apiUtil) {

            }
        });

    }

    private void getCreateList(String teacherId){

        params.put("teacherId",teacherId);

        new GetApi(Constant.URL_COURSE_CREATELIST,params).get(new ApiListener() {
            @Override
            public void success(ApiUtil apiUtil) {
                GetApi api = (GetApi) apiUtil;
                String json = api.mJsonArray.toString();
                parseJSONWithGson(json);
                for (Course course :
                        list_all) {
                    Log.i(TAG, "success: courseId = " + course.getCourseId());
                    Log.i(TAG, "success: classId = "  + course.getClassId());
                    Log.i(TAG, "success: courseName = " + course.getCourseName());
                    Log.i(TAG, "success: courseTeacherId = " + course.getTeacherId());
                }
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        linearLayoutManager = new LinearLayoutManager(getContext());
                        classFragmentRecycler.setLayoutManager(linearLayoutManager);
                        classFragmentAdapter = new ClassFragmentAdapter(getContext(), list_all
                                , new OnClickerListener() {
                            @Override
                            public void click(int position, View view) {

                                Intent intent = new Intent(getContext(), CourseActivity.class);
                                Bundle bundle = new Bundle();
                                bundle.putSerializable("course", list_all.get(position));
                                intent.putExtras(bundle);
                                startActivity(intent);
                            }
                        });
                        classFragmentRecycler.setAdapter(classFragmentAdapter);
                    }
                });

            }

            @Override
            public void failure(ApiUtil apiUtil) {
                Log.i(TAG, "failure: 老师创建的课堂列表加载失败");
            }
        });
    }

    private void parseJSONWithGson(String jsonData){
        Gson gson = new Gson();
        List<Course> courseList = gson.fromJson(jsonData,new TypeToken<List<Course>>(){}.getType());
        list_all = courseList;
        for (Course course :
                courseList) {
            Log.e(TAG, "parseJSONWithGson: courseId = " + course.getCourseId());
            Log.e(TAG, "parseJSONWithGson: classId = "  + course.getClassId());
            Log.e(TAG, "parseJSONWithGson: courseName = " + course.getCourseName());
            Log.e(TAG, "parseJSONWithGson: courseTeacherId = " + course.getTeacherId());
        }
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
        if (position == 0){
            Log.i(TAG, "startActivityByPosition: 创建课堂");
            Intent intent = new Intent(getContext(), CreateCourseActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        updateDate();
    }
}
