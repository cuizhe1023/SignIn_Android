package com.nuc.signin_android.classroom.course.member;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.nuc.signin_android.R;
import com.nuc.signin_android.base.BaseFragment;

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
    RecyclerView classMemberRecycler;
    @BindView(R.id.sum_member_text)
    TextView sumMemberText;
    @BindView(R.id.qiandao_btn)
    FloatingActionButton qiandaoBtn;

    private MemberAdapter courseFragmentAdapter;
    private Map<String, Integer> map;
    private List<Map.Entry<String, Integer>> entries;
    private static String courseId;

    public static MemberFragment getInstance(String course_id){
        courseId = course_id;
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
}
