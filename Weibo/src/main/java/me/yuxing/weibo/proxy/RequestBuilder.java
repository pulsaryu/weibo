package me.yuxing.weibo.proxy;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import me.yuxing.volley.HttpParams;
import me.yuxing.weibo.Account;
import me.yuxing.weibo.Config;
import me.yuxing.weibo.request.Api;

/**
 * Created by yuxing on 14-1-19.
 */
public class RequestBuilder {

    private static final String TAG = "RequestBuilder";
    private final Context context;
    private final String api;
    private int method = Request.Method.GET;
    private HttpParams httpParams = new HttpParams();
    private Object tag;
    private RequestCallback requestCallback;

    public RequestBuilder(Context context, String api) {
        this.context = context;
        this.api = api;
        httpParams.put("access_token", Account.getAccessToken(context));
    }

    public RequestBuilder setMethod(int method) {
        this.method = method;
        return this;
    }

    public RequestBuilder setRequestCallback(RequestCallback requestCallback) {
        this.requestCallback = requestCallback;
        return this;
    }

    public RequestBuilder setTag(Object tag) {
        this.tag = tag;
        return this;
    }

    public RequestBuilder addParams(String key, String value) {
        httpParams.put(key, value);
        return this;
    }

    public RequestBuilder addParams(String key, int value) {
        httpParams.put(key, String.valueOf(value));
        return this;
    }

    public RequestBuilder addParams(String key, long value) {
        httpParams.put(key, String.valueOf(value));
        return this;
    }

    public Request<String> build() {
        String url = createUrl();
        url += "?" + httpParams.toString();

        if (Config.DEBUG) Log.i(TAG, "request " + url);

        Request<String> request = new StringRequest(method, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (requestCallback != null) {
                    requestCallback.onCompleted(response, null);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (requestCallback != null) {

                }
            }
        });

        if (tag != null) {
            request.setTag(tag);
        }

        return request;
    }

    private String createUrl() {
        return String.format(Api.BASE_URL, api);
    }
}
