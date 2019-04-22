package com.nuc.signin_android.classroom.course.info;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nuc.signin_android.R;
import com.nuc.signin_android.base.BaseFragment;
import com.nuc.signin_android.entity.Course;
import com.nuc.signin_android.entity.SelectCourse;
import com.nuc.signin_android.net.PostApi;
import com.nuc.signin_android.net.UploadFileApi;
import com.nuc.signin_android.utils.Constant;
import com.nuc.signin_android.utils.FileUtil;
import com.nuc.signin_android.utils.ToastUtil;
import com.nuc.signin_android.utils.net.ApiListener;
import com.nuc.signin_android.utils.net.ApiUtil;

import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

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
    TextView showCourseNameText;
    @BindView(R.id.show_teacher_name_text)
    TextView showTeacherNameText;
    @BindView(R.id.show_course_id_text)
    TextView showCourseIdText;
    @BindView(R.id.show_course_time_text)
    TextView showLongtimeText;
    @BindView(R.id.end_class_btn)
    Button endCourseBtn;
    @BindView(R.id.select_file_btn)
    Button selectFileBtn;
    @BindView(R.id.upload_btn)
    Button uploadFileBtn;
    @BindView(R.id.file_path)
    TextView filePathText;
    @BindView(R.id.course_info_rl)
    RelativeLayout layout;
    @BindView(R.id.course_info_cut_line1)
    TextView cutLine1;
    @BindView(R.id.course_info_cut_line2)
    TextView cutLine2;

    String path;
    private HashMap<String,String> params = new HashMap<>();

    private static Course mCourse;
    private List<SelectCourse> selectCourseList;

    public static CourseInfoFragment getInstance(Course course) {
        // Required empty public constructor
        mCourse = course;
        return new CourseInfoFragment();
    }

    @Override
    protected void logic() {
        showTitleText.setText(mCourse.getCourseName());
        showCourseIdText.setText(mCourse.getCourseId());
        showLongtimeText.setText(mCourse.getCourseTime());
        showClassNumberText.setText(mCourse.getClassId());
        showCourseNameText.setText(mCourse.getCourseName());
        showTeacherNameText.setText(mCourse.getTeacherName());
        if ("student".equals(identity)){
            layout.setVisibility(View.INVISIBLE);
            uploadFileBtn.setVisibility(View.INVISIBLE);
            endCourseBtn.setVisibility(View.INVISIBLE);
            cutLine1.setVisibility(View.INVISIBLE);
            cutLine2.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    protected void init(View view, Bundle savedInstanceState) {
    }

    @Override
    protected int getResourcesLayout() {
        return R.layout.fragment_course_info;
    }

    @OnClick(R.id.select_file_btn)
    public void onSelectFileBtnClicked(){
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(intent,1);
    }

    @SuppressLint("HandlerLeak")
    @OnClick(R.id.upload_btn)
    public void onUploadExcelFileBtnClicked(){
        params.put("filePath",path);
        params.put("courseId",mCourse.getCourseId());
        new UploadFileApi(Constant.URL_COURSE_UPLOADFILE,params).uploadFile(new ApiListener() {
            @Override
            public void success(ApiUtil apiUtil) {
                Log.i(TAG, "onResponse: 上传成功");
                UploadFileApi uploadFileApi = (UploadFileApi) apiUtil;
                String json = uploadFileApi.mJsonArray.toString();
                Log.i(TAG, "success: json = " + json);
                selectCourseList = parseJSON(json);
                Log.i(TAG, "success: selectCourseList = " + selectCourseList.toString());
                for (SelectCourse selectCourse :
                        selectCourseList) {
                    HashMap<String,String> map = new HashMap<>();
                    map.put("courseId", String.valueOf(selectCourse.getCourseId()));
                    map.put("studentId",selectCourse.getStudentId());
                    map.put("studentName",selectCourse.getStudentName());
                    map.put("gender",selectCourse.getGender());
                    new PostApi(Constant.URL_SELECT_COURSE_INSERT,map).post(new ApiListener() {
                        @Override
                        public void success(ApiUtil apiUtil) {
                            Log.i(TAG, "success: 信息导入成功");
                            mainHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    ToastUtil.showToast(getActivity(),"信息导入成功");
                                }
                            });
                        }

                        @Override
                        public void failure(ApiUtil apiUtil) {
                            Log.i(TAG, "failure: 信息导入失败");
                            mainHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    ToastUtil.showToast(getActivity(),"信息导入失败");
                                }
                            });
                        }
                    });
                }
            }

            @Override
            public void failure(ApiUtil apiUtil) {
                Log.i(TAG, "failure: 上传文件失败！");
                mainHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        ToastUtil.showToast(getActivity(),"上传文件失败！");
                    }
                });
            }
        });
        params = null;
        params = new HashMap<>();
    }


    private List<SelectCourse> parseJSON(String jsonData){
        Gson gson = new Gson();
        List<SelectCourse> selectCourses = gson.fromJson(jsonData,new TypeToken<List<SelectCourse>>(){}.getType());
        Log.e(TAG, "parseJSON: 解析了数据！");
        return selectCourses;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            Uri uri = data.getData();
            if ("file".equalsIgnoreCase(uri.getScheme())){//使用第三方应用打开
                path = uri.getPath();
                filePathText.setText(path);
                Toast.makeText(getActivity(),"使用第三方应用打开:" + path,Toast.LENGTH_SHORT).show();
                return;
            }
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {//4.4以后
                FileUtil fileUtil = new FileUtil();
                path = fileUtil.getPath(getActivity(), uri);
                String substring = path.substring(path.lastIndexOf("."));
                Log.e(TAG, "onActivityResult: 后缀名：" + substring);
                if (".xls".equals(substring) || ".xlsx".equals(substring)){
                    filePathText.setText(path);
                    Toast.makeText(getActivity(),path,Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(getActivity(),"请上传 Excel 文件",Toast.LENGTH_SHORT).show();
                }
            } else {//4.4以下下系统调用方法
                FileUtil fileUtil = new FileUtil();
                path = fileUtil.getRealPathFromURI(getActivity(),uri);
                filePathText.setText(path);
                Toast.makeText(getActivity(), path, Toast.LENGTH_SHORT).show();
            }
        }
    }
}
