package me.yuxing.weibo.proxy;

import me.yuxing.weibo.request.ApiError;

/**
 * Created by yuxing on 14-1-19.
 */
public interface RequestCallback {
    public void onCompleted(String response, ApiError error);
}
