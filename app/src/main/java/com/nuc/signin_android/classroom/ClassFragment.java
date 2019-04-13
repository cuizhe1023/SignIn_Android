package com.nuc.signin_android.classroom;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.nuc.signin_android.R;
import com.nuc.signin_android.base.BaseFragment;
import com.wangjie.rapidfloatingactionbutton.RapidFloatingActionButton;
import com.wangjie.rapidfloatingactionbutton.RapidFloatingActionHelper;
import com.wangjie.rapidfloatingactionbutton.RapidFloatingActionLayout;
import com.wangjie.rapidfloatingactionbutton.contentimpl.labellist.RFACLabelItem;
import com.wangjie.rapidfloatingactionbutton.contentimpl.labellist.RapidFloatingActionContentLabelList;

import java.util.ArrayList;
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
    private List list_create = new ArrayList<>();
    private List list_join = new ArrayList<>();
    private List<String>list_all = new ArrayList<>();
    private static final String TAG = "ClassFragment";

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
        items.add(new RFACLabelItem<Integer>()
                .setLabel("使用微课号加入微课")
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
//            Intent intent = new Intent(getContext(), CreateClassActivity.class);
//            startActivity(intent);
        }else {
            Log.i(TAG, "startActivityByPosition: 加入课堂");
//            Intent intent = new Intent(getContext(), JoinClassActivity.class);
//            startActivity(intent);
        }
    }

}
