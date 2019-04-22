package com.nuc.signin_android.classroom.course;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.util.Log;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.nuc.signin_android.R;
import com.nuc.signin_android.base.BaseActivity;
import com.nuc.signin_android.classroom.course.member.MemberFragment;
import com.nuc.signin_android.classroom.course.info.CourseInfoFragment;
import com.nuc.signin_android.classroom.course.signin.SignInFragment;
import com.nuc.signin_android.entity.Course;
import com.nuc.signin_android.utils.ActivityUtils;

import butterknife.BindView;

/**
 * @Author: cuizhe
 * @Date: 2019/4/17 12:39
 * @Description:
 */
public class CourseActivity extends BaseActivity {
    private static final String TAG = "CourseActivity";

    @BindView(R.id.course_frame)
    FrameLayout courseFrame;

    BottomNavigationView bottomMenu = null;

    private Course course;
    private String courseId;

    @Override
    protected void logicActivity(Bundle savedInstanceState) {
        Intent intent = getIntent();
//        courseId = intent.getExtras().getString("courseId");
        Bundle bundle = intent.getExtras();
        course = (Course) bundle.getSerializable("course");
        courseId = course.getCourseId();
        Log.i(TAG, "logicActivity: courseId = " + courseId);

        if (savedInstanceState != null){
            Log.i(TAG, "logicActivity: " + savedInstanceState.getInt("bottom_id_class"));
            showFragment(savedInstanceState.getInt("bottom_id_class"));
        }else {
            ActivityUtils.replaceFragmentToActivity(fragmentManager, MemberFragment.getInstance(course),R.id.course_frame);
        }

        bottomMenu = findViewById(R.id.class_bottom_menu);
        bottomMenu.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                showFragment(menuItem.getItemId());
                return true;
            }
        });
    }

    /**
     * 根据id显示相应的页面
     * @param menu_id
     */
    private void showFragment(int menu_id) {
        switch (menu_id){
            case R.id.bottom_person:
                ActivityUtils.replaceFragmentToActivity(fragmentManager,MemberFragment.getInstance(course),R.id.course_frame);
                break;
            case R.id.bottom_sign_in:
                ActivityUtils.replaceFragmentToActivity(fragmentManager, SignInFragment.getInstance(course),R.id.course_frame);
                break;
            case R.id.bottom_info:
                ActivityUtils.replaceFragmentToActivity(fragmentManager, CourseInfoFragment.getInstance(course),R.id.course_frame);
                break;
        }
    }

    @Override
    protected int getLayoutView() {
        Log.i(TAG, "getLayoutView: " + R.layout.activity_course);
        return R.layout.activity_course;
    }
}
