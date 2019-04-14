package com.nuc.signin_android.net;

import com.nuc.signin_android.utils.net.ApiUtil;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * @Author: cuizhe
 * @Date: 2019/4/14 12:19
 * @Description:
 */
public class GetApi extends ApiUtil {

    public String mResponse;
    private String mUrl;

    public GetApi(String mUrl, HashMap<String,String> getParamsMap) {
        this.mUrl = mUrl;
        params = getParamsMap;
    }

    @Override
    protected String getUrl() {
        return mUrl;
    }

    @Override
    protected void parseData(JSONObject jsonObject) throws Exception {
        mResponse = jsonObject.toString();
    }
}
