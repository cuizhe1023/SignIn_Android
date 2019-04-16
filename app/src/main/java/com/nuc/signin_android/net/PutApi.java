package com.nuc.signin_android.net;

import com.nuc.signin_android.utils.net.ApiUtil;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * @Author: cuizhe
 * @Date: 2019/4/15 13:22
 * @Description:
 */
public class PutApi extends ApiUtil {

    public JSONObject mJson;
    private String mUrl;

    public PutApi(String mUrl, HashMap<String,String> paramsMap) {
        this.mUrl = mUrl;
        params = paramsMap;
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
