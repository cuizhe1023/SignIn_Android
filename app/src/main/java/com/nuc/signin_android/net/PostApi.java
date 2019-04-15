package com.nuc.signin_android.net;

import com.nuc.signin_android.utils.net.ApiUtil;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * @Author: cuizhe
 * @Date: 2019/4/14 12:42
 * @Description:
 */
public class PostApi extends ApiUtil {

    public JSONObject mJson;
    public String mUrl;

    public PostApi(String mUrl, HashMap<String,String> getParamsMap){
        this.mUrl = mUrl;
        params = getParamsMap;
    }

    @Override
    protected void parseData(JSONObject jsonObject) throws Exception {
        mJson = jsonObject;
    }

    @Override
    protected String getUrl() {
        return mUrl;
    }
}
