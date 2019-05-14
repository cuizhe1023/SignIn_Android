package com.nuc.signin_android.classroom.course.info;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.content.FileProvider;
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
import com.nuc.signin_android.net.DeleteApi;
import com.nuc.signin_android.net.GetApi;
import com.nuc.signin_android.net.PostApi;
import com.nuc.signin_android.net.UploadFileApi;
import com.nuc.signin_android.utils.Constant;
import com.nuc.signin_android.utils.FileUtil;
import com.nuc.signin_android.utils.ToastUtil;
import com.nuc.signin_android.utils.net.ApiListener;
import com.nuc.signin_android.utils.net.ApiUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @Author: cuizhe
 * @Date: 2019/4/11 17:28
 * @Description:
 */
public class CourseInfoFragment extends BaseFragment {

    private static final String TAG = "CourseInfoFragment";

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
    @BindView(R.id.download_info_rl)
    RelativeLayout layout1;
    @BindView(R.id.template_path)
    TextView template_path;
    @BindView(R.id.course_info_cut_line1)
    TextView cutLine1;
    @BindView(R.id.course_info_cut_line2)
    TextView cutLine2;
    @BindView(R.id.course_info_cut_line3)
    TextView cutLine3;

    String path;
    private HashMap<String, String> params = new HashMap<>();

    private static Course mCourse;
    private List<SelectCourse> selectCourseList;
    private static final String[][] MIME_MapTable = {
            {".xls", "application/vnd.ms-excel"},
            {".xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"}};


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
        if ("student".equals(identity)) {
            layout.setVisibility(View.INVISIBLE);
            layout1.setVisibility(View.INVISIBLE);
            uploadFileBtn.setVisibility(View.INVISIBLE);
            endCourseBtn.setVisibility(View.INVISIBLE);
            cutLine1.setVisibility(View.INVISIBLE);
            cutLine2.setVisibility(View.INVISIBLE);
            cutLine3.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    protected void init(View view, Bundle savedInstanceState) {
    }

    @Override
    protected int getResourcesLayout() {
        return R.layout.fragment_course_info;
    }

    @OnClick({R.id.end_class_btn})
    public void endClassBtn() {
        params.put("courseId", mCourse.getCourseId());
        new DeleteApi(Constant.URL_COURSE_DELETECOURSE, params).delect(new ApiListener() {
            @Override
            public void success(ApiUtil apiUtil) {
                mainHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        ToastUtil.showToast(getActivity(), "结束课程成功");
                    }
                });
            }

            @Override
            public void failure(ApiUtil apiUtil) {
                mainHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        ToastUtil.showToast(getActivity(), "请求失败");
                    }
                });
            }
        });
    }

    @OnClick(R.id.select_file_btn)
    public void onSelectFileBtnClicked() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(intent, 1);
    }

    @OnClick(R.id.upload_btn)
    public void onUploadExcelFileBtnClicked() {
        if (path != null){
            params.put("filePath", path);
            params.put("courseId", mCourse.getCourseId());
            new UploadFileApi(Constant.URL_COURSE_UPLOADFILE, params).uploadFile(new ApiListener() {
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
                        HashMap<String, String> map = new HashMap<>();
                        map.put("courseId", String.valueOf(selectCourse.getCourseId()));
                        map.put("studentId", selectCourse.getStudentId());
                        map.put("studentName", selectCourse.getStudentName());
                        map.put("gender", selectCourse.getGender());
                        new PostApi(Constant.URL_SELECT_COURSE_INSERT, map).post(new ApiListener() {
                            @Override
                            public void success(ApiUtil apiUtil) {
                                Log.i(TAG, "success: 信息导入成功");
                                mainHandler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        ToastUtil.showToast(getActivity(), "信息导入成功");
                                    }
                                });
                            }

                            @Override
                            public void failure(ApiUtil apiUtil) {
                                Log.i(TAG, "failure: 信息导入失败");
                                mainHandler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        ToastUtil.showToast(getActivity(), "信息导入失败,部分信息已存在");
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
                            ToastUtil.showToast(getActivity(), "上传文件失败！");
                        }
                    });
                }
            });
            params = null;
            params = new HashMap<>();
        }else {
            mainHandler.post(new Runnable() {
                @Override
                public void run() {
                    ToastUtil.showToast(getActivity(),"请选择文件进行上传");
                }
            });
        }
    }

    @OnClick(R.id.download_template_btn)
    public void onDownloadBtnClicker(){
        String url = Constant.URL_COURSE_DOWNLOAD;
        Request.Builder builder = new Request.Builder();
        Request request = builder.get().url(url).build();
        OkHttpClient mOkHttpClient = new OkHttpClient();
        // 将 Request 封装为 Call
        Call call = mOkHttpClient.newCall(request);

        // 执行call
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.i(TAG, "onFailure: " + e.getMessage());
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Log.i(TAG, "onResponse: ");
                InputStream is = response.body() != null ? response.body().byteStream() : null;
                Log.i(TAG, "onResponse: is = " + is);

                int len = 0;
                File file = new File(Environment.getExternalStorageDirectory()+"/download","template.xls");
                byte[] buf = new byte[1024];
                Log.i(TAG, "onResponse: file.name = " + file.getName());
                Log.i(TAG, "onResponse: file.path = " + file.getPath());
                Log.i(TAG, "onResponse: file.isEmpty = " + file.exists());
                Log.i(TAG, "onResponse: buf = " + buf.toString());
                FileOutputStream fos = new FileOutputStream(file);
                Log.i(TAG, "onResponse: fos = " + fos);
                while ((len = is != null ? is.read(buf) : 0)!=-1){
                    Log.i(TAG, "onResponse: len = " + len);
                    fos.write(buf,0,len);
                }
                fos.flush();
                fos.close();
                is.close();
                Log.i(TAG, "download success");
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        template_path.setText(file.getPath());
                    }
                });
            }
        });
    }

    @OnClick(R.id.template_path)
    public void openTemplate(){
        openfile(template_path.getText().toString().trim());
    }

    private void openfile(String path) {
        Log.i(TAG, "openfile: 点击了");
        File file = new File(path);
        if (!file.exists()) {
            ToastUtil.showToast(getActivity(),"请先下载模板");
            return;
        }
        try {
            Intent intent = new Intent();
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            //设置intent的Action属性
            intent.setAction(Intent.ACTION_VIEW);
            //获取文件file的MIME类型
            String type = getMIMEType(file);

            //设置文件的uri
            Uri fileURI = FileProvider.getUriForFile(getContext(), getContext().getApplicationContext().getPackageName() + ".fileProvider", file);

//            //设置intent的data和Type属性。
//            intent.setDataAndType(/*uri*/Uri.fromFile(file), type);
            intent.setDataAndType(fileURI, type);

            //跳转
            getActivity().startActivity(intent);
        } catch (ActivityNotFoundException e) {
            ToastUtil.showToast(getActivity(),"sorry附件不能打开，请下载相关软件！");
        }
    }

    private String getMIMEType(File file) {

        String type = "*/*";
        String fName = file.getName();
        //获取后缀名前的分隔符"."在fName中的位置。
        int dotIndex = fName.lastIndexOf(".");
        if (dotIndex < 0) {
            return type;
        }
        /* 获取文件的后缀名*/
        String end = fName.substring(dotIndex, fName.length()).toLowerCase();
        if (end == "") return type;
        //在MIME和文件类型的匹配表中找到对应的MIME类型。
        for (int i = 0; i < MIME_MapTable.length; i++) { //MIME_MapTable??在这里你一定有疑问，这个MIME_MapTable是什么？
            if (end.equals(MIME_MapTable[i][0]))
                type = MIME_MapTable[i][1];
        }
        return type;
    }

    private List<SelectCourse> parseJSON(String jsonData) {
        Gson gson = new Gson();
        List<SelectCourse> selectCourses = gson.fromJson(jsonData, new TypeToken<List<SelectCourse>>() {
        }.getType());
        Log.e(TAG, "parseJSON: 解析了数据！");
        return selectCourses;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            Uri uri = data.getData();
            if ("file".equalsIgnoreCase(uri.getScheme())) {//使用第三方应用打开
                path = uri.getPath();
                filePathText.setText(path);
                Toast.makeText(getActivity(), "使用第三方应用打开:" + path, Toast.LENGTH_SHORT).show();
                return;
            }
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {//4.4以后
                FileUtil fileUtil = new FileUtil();
                path = fileUtil.getPath(getActivity(), uri);
                String substring = path.substring(path.lastIndexOf("."));
                Log.e(TAG, "onActivityResult: 后缀名：" + substring);
                if (".xls".equals(substring) || ".xlsx".equals(substring)) {
                    filePathText.setText(path);
                    Toast.makeText(getActivity(), path, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity(), "请上传 Excel 文件", Toast.LENGTH_SHORT).show();
                }
            } else {//4.4以下下系统调用方法
                FileUtil fileUtil = new FileUtil();
                path = fileUtil.getRealPathFromURI(getActivity(), uri);
                filePathText.setText(path);
                Toast.makeText(getActivity(), path, Toast.LENGTH_SHORT).show();
            }
        }
    }
}
