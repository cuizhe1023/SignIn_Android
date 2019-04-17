package com.nuc.signin_android.classroom.course.info;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.nuc.signin_android.R;
import com.nuc.signin_android.base.BaseFragment;

import butterknife.BindView;

/**
 * @Author: cuizhe
 * @Date: 2019/4/11 17:28
 * @Description:
 */
public class CourseInfoFragment extends BaseFragment {

    private static final String TAG = "CourseInfoFragment";

    @BindView(R.id.back_from_show_image)
    ImageView backFromShowImage;
    @BindView(R.id.show_title_text)
    TextView showTitleText;
    @BindView(R.id.show_image)
    ImageView showImage;
    @BindView(R.id.show_class_number_text)
    TextView showClassNumberText;
    @BindView(R.id.show_name_text)
    TextView showNameText;
    @BindView(R.id.show_description_text)
    TextView showDescriptionText;
    @BindView(R.id.show_bankehao_text)
    TextView showBankehaoText;
    @BindView(R.id.show_longtime_text)
    TextView showLongtimeText;
    @BindView(R.id.checkbox)
    CheckBox checkbox;
    @BindView(R.id.end_class_btn)
    Button endClassBtn;

    private static String courseId;

    public static CourseInfoFragment getInstance(String course_id) {
        // Required empty public constructor
        courseId = course_id;
        return new CourseInfoFragment();
    }

    @Override
    protected void logic() {

    }

    @Override
    protected void init(View view, Bundle savedInstanceState) {

    }

    @Override
    protected int getResourcesLayout() {
        return R.layout.fragment_course_info;
    }
}
