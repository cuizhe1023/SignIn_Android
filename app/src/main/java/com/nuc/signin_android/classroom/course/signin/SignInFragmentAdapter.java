package com.nuc.signin_android.classroom.course.signin;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nuc.signin_android.R;
import com.nuc.signin_android.base.OnClickerListener;
import com.nuc.signin_android.classroom.course.member.MemberAdapter;
import com.nuc.signin_android.entity.SelectCourse;
import com.nuc.signin_android.entity.Student_SignIn;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @Author: cuizhe
 * @Date: 2019/4/22 17:44
 * @Description:
 */
public class SignInFragmentAdapter extends RecyclerView.Adapter<SignInFragmentAdapter.ViewHolder> {

    private Context context;
    private List<Student_SignIn> mSelectCourseList = new ArrayList<>();
    private OnClickerListener onClickerListener;

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.member_item_rank)
        TextView memberItemRank;
        @BindView(R.id.class_avator_item)
        ImageView memberItemAvator;
        @BindView(R.id.student_gender_item)
        TextView memberItemStudentGender;
        @BindView(R.id.student_name_item)
        TextView memberItemStudentName;
        @BindView(R.id.student_id_item)
        TextView memberItemStudentId;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }

    }

    public SignInFragmentAdapter (Context context, List<Student_SignIn> mSelectCourseList, OnClickerListener onClickerListener) {
        this.context = context;
        this.mSelectCourseList = mSelectCourseList;
        this.onClickerListener = onClickerListener;
    }



    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.member_recycler_item, viewGroup, false);
        return new SignInFragmentAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Student_SignIn student_signIn = mSelectCourseList.get(i);
        viewHolder.memberItemStudentId.setText(student_signIn.getStudentId());
        viewHolder.memberItemStudentName.setText(student_signIn.getStudentName());

        if (onClickerListener != null){
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickerListener.click(viewHolder.getLayoutPosition(),viewHolder.itemView);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mSelectCourseList.size();
    }
}
