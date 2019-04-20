package com.nuc.signin_android.net;

import com.nuc.signin_android.utils.net.ApiUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * @Author: cuizhe
 * @Date: 2019/4/20 13:37
 * @Description:
 */
public class UploadFileApi extends ApiUtil {

    public JSONObject mJson;
    public JSONArray mJsonArray;
    private String mUrl;

    public UploadFileApi(String mUrl, HashMap<String,String> paramsMap) {
        this.mUrl = mUrl;
        params = paramsMap;
    }

    @Override
    protected void parseData(Object jsonObject) throws Exception {
        if (jsonObject instanceof JSONObject){
            mJson = (JSONObject) jsonObject;
        }
        if (jsonObject instanceof JSONArray){
            mJsonArray = (JSONArray) jsonObject;
        }
    }

    @Override
    protected String getUrl() {
        return mUrl;
    }

}
