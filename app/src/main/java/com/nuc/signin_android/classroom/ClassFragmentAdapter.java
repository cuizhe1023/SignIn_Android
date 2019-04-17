package com.nuc.signin_android.classroom;

import android.content.Context;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nuc.signin_android.R;
import com.nuc.signin_android.base.OnClickerListener;
import com.nuc.signin_android.entity.Course;
import java.util.List;

/**
 * @Author: cuizhe
 * @Date: 2019/4/16 20:55
 * @Description:
 */
public class ClassFragmentAdapter extends RecyclerView.Adapter<ClassFragmentAdapter.ViewHolder> {

    private Context context;
    private List<Course> mCourseList;
    private static final String TAG = "ClassFragmentAdapter";


    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView classImage;
        TextView courseId;
        TextView classId;
        TextView courseName;
        TextView teacherName;

        public ViewHolder(View itemView){
            super(itemView);
            classImage = itemView.findViewById(R.id.course_img_item);
            classId = itemView.findViewById(R.id.class_id_item);
            courseId = itemView.findViewById(R.id.course_number_item);
            courseName = itemView.findViewById(R.id.course_name_item);
            teacherName = itemView.findViewById(R.id.course_teacher_name);
        }
    }

    public ClassFragmentAdapter(Context context, List<Course> mCourseList) {
        this.context = context;
        this.mCourseList = mCourseList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.class_recycler_item,viewGroup,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Course course = mCourseList.get(i);
        viewHolder.classId.setText(course.getClassId());
        viewHolder.courseId.setText(course.getCourseId());
        viewHolder.courseName.setText(course.getCourseName());
        viewHolder.teacherName.setText(course.getTeacherName());
    }

    @Override
    public int getItemCount() {
        return mCourseList.size();
    }
}
