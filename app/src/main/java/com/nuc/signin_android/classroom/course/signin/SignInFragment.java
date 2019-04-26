package com.nuc.signin_android.classroom.course.signin;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.nuc.signin_android.R;
import com.nuc.signin_android.base.BaseFragment;
import com.nuc.signin_android.entity.Course;
import com.nuc.signin_android.entity.Student_SignIn;
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
    @BindView(R.id.do_recycler_view)
    RecyclerView doRecyclerView;
    @BindView(R.id.do_fbtn)
    RapidFloatingActionButton doFbtn;
    @BindView(R.id.do_fbtn_layout)
    RapidFloatingActionLayout doFbtnLayout;

    private HashMap<String,String> params = new HashMap<>();
    private static Course mCourse;

    private RapidFloatingActionHelper rfabHelper;
    private SignInFragmentAdapter adapter;

    private String id;
    private String name;

    private LinearLayoutManager linearLayoutManager;

    public static SignInFragment getInstance(Course course){
        mCourse = course;
        return new SignInFragment();
    }

    @Override
    protected void logic() {
        id = userId;
        name = pref.getString("name",null);
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
        if (position == 0){
            if ("student".equals(identity)){
                startActivityTo(StudentSignInActivity.class,id,name,mCourse,studentNumber);
            }
            if ("teacher".equals(identity)){
                Log.i(TAG, "startActivityByPosition: studentNumber = " + studentNumber);
                startActivityTo(TeacherSignInActivity.class,id,name,mCourse,studentNumber);
            }
        }

    }

    private void updateDate() {
        // 更新该课程的签到信息

    }

    @Override
    public void onResume() {
        super.onResume();
        updateDate();
    }
}
