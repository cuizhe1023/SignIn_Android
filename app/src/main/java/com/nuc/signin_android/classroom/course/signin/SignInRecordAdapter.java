package com.nuc.signin_android.classroom.course.signin;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nuc.signin_android.R;
import com.nuc.signin_android.base.OnClickerListener;
import com.nuc.signin_android.entity.SignIn;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @Author: cuizhe
 * @Date: 2019/4/27 12:08
 * @Description:
 */
public class SignInRecordAdapter extends RecyclerView.Adapter<SignInRecordAdapter.ViewHolder> {


    private Context context;
    private List<SignIn> mSignInList = new ArrayList<>();
    private OnClickerListener onClickerListener;

    public class ViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.sign_record_date)
        TextView signRecordDate;
        @BindView(R.id.sign_record_time)
        TextView signRecordTime;
        @BindView(R.id.sign_record_week)
        TextView signRecordWeek;
        @BindView(R.id.sign_record_information)
        TextView signRecordInformation;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }

    public SignInRecordAdapter(Context context, List<SignIn> mSignInList, OnClickerListener onClickerListener) {
        this.context = context;
        this.mSignInList = mSignInList;
        this.onClickerListener = onClickerListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.sign_in_record_item, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        SignIn signIn = mSignInList.get(i);
        String[] data = signIn.getSignDate().split(" ");
        viewHolder.signRecordDate.setText(data[0]);
        viewHolder.signRecordTime.setText(data[1]);
        viewHolder.signRecordWeek.setText(dateToWeek(data[0]));
        String info = signIn.getArriveNumber() + " 人/" + signIn.getStudentNumber() + " 人";
        viewHolder.signRecordInformation.setText(info);

        if (onClickerListener != null){
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onClickerListener.click(viewHolder.getLayoutPosition(),viewHolder.itemView);
                }
            });
        }
    }

    public static String dateToWeek(String datetime) {
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
        String[] weekDays = { "星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六" };
        Calendar cal = Calendar.getInstance(); // 获得一个日历
        Date datet = null;
        try {
            datet = f.parse(datetime);
            cal.setTime(datet);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        int w = cal.get(Calendar.DAY_OF_WEEK) - 1; // 指示一个星期中的某天。
        if (w < 0)
            w = 0;
        return weekDays[w];
    }


    @Override
    public int getItemCount() {
        return mSignInList.size();
    }
}
