package com.nuc.signin_android.utils.net;

/**
 * @Author: cuizhe
 * @Date: 2019/4/14 10:38
 * @Description:
 */
public interface ApiListener {
    void success(ApiUtil apiUtil);

    void failure(ApiUtil apiUtil);
}
