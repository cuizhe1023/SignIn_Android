package com.nuc.signin_android.classroom.course.member;

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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @Author: cuizhe
 * @Date: 2019/4/17 13:35
 * @Description:
 */
public class MemberAdapter extends RecyclerView.Adapter<MemberAdapter.ViewHolder> {

    private Context context;
    private List<Map.Entry<String, Integer>> map = new ArrayList<>();
    private OnClickerListener onClickerListener;

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.member_item_rank)
        TextView memberItemRank;
        @BindView(R.id.class_avator_item)
        ImageView memberItemAvator;
        @BindView(R.id.class_random_number_item)
        TextView memberItemAllScore;
        @BindView(R.id.student_name_item)
        TextView memberItemStudentName;
        @BindView(R.id.student_id_item)
        TextView memberItemStudentId;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }

    public MemberAdapter(Context context, List<Map.Entry<String, Integer>> map, OnClickerListener onClickerListener) {
        this.context = context;
        this.map = map;
        this.onClickerListener = onClickerListener;
    }

    @NonNull
    @Override
    public MemberAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.member_recycler_item, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MemberAdapter.ViewHolder viewHolder, int i) {
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
        return map.size();
    }
}
