package com.nuc.signin_android.net;

import com.nuc.signin_android.utils.net.ApiUtil;

import org.json.JSONObject;

/**
 * @Author: cuizhe
 * @Date: 2019/4/14 12:42
 * @Description:
 */
public class PostApi extends ApiUtil {

    public String mUrl;

    public PostApi(String mUrl, String teacherId, String teacherName, String teacherPassword){
        this.mUrl = mUrl;
        addParams("teacherId",teacherId);
        addParams("teacherName",teacherName);
        addParams("teacherPassword",teacherPassword);
    }

    @Override
    protected void parseData(JSONObject jsonObject) throws Exception {

    }

    @Override
    protected String getUrl() {
        return mUrl;
    }
}
