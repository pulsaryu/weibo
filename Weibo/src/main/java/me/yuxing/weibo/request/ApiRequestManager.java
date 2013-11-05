package me.yuxing.weibo.request;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;

import java.io.UnsupportedEncodingException;

import me.yuxing.volley.GsonRequest;
import me.yuxing.volley.HttpParams;
import me.yuxing.weibo.Account;
import me.yuxing.weibo.R;
import me.yuxing.weibo.WeiboApplication;
import me.yuxing.weibo.model.Timeline;
import me.yuxing.weibo.ui.AuthorizeActivity;
import me.yuxing.weibo.ui.MainActivity;

/**
 * Created by yuxing on 13-10-28.
 */
public class ApiRequestManager {

    private static final String TAG = "ApiRequestManager";
    private static final int ERROR_CODE_EXPIRED_TOKEN = 21327;

    public static <T> Request<T> newQueryRequest(final Context context, Class<T> cls, final String api, HttpParams params, final Callback<T> callback) {
        return newRequest(context, cls, Request.Method.GET, api, params, callback);
    }

    public static <T> Request<T> newPostRequest(final Context context, Class<T> cls, final String api, HttpParams params, final Callback<T> callback) {
        return newRequest(context, cls, Request.Method.POST, api, params, callback);
    }

    public static <T> Request<T> newRequest(final Context context, Class<T> cls, final int method, final String api, HttpParams params, final Callback<T> callback) {
        String url = String.format(Api.BASE_URL, api);

        if (method == Request.Method.GET) {
            url += "?" + params.toString();
            params = null;
        }

        Request<T> request = new GsonRequest<T>(cls, method, url, params, new Response.Listener<T>() {
            @Override
            public void onResponse(T response) {
                if (callback != null) {
                    callback.onResponse(response, null);
                }
             }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                ApiError apiError = parseError(error.networkResponse);
                onNetworkError(context, apiError);
                if (callback != null) {
                    callback.onResponse(null, apiError);
                }
            }
        });

        RequestQueue requestQueue = WeiboApplication.getInstance().getRequestQueue();
        requestQueue.add(request);
        return request;
    }

    public static void cancelAll(Object tag) {
        WeiboApplication.getInstance().getRequestQueue().cancelAll(tag);
    }

    private static ApiError parseError(NetworkResponse response) {
        String parse;
        try {
            parse = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
        } catch (UnsupportedEncodingException e) {
            parse = new String(response.data);
        }

        Gson gson = new Gson();
        ApiError apiError = gson.fromJson(parse, ApiError.class);
        return apiError;
    }

    private synchronized static void onNetworkError(final Context context, final ApiError error) {
        RequestQueue requestQueue = WeiboApplication.getInstance().getRequestQueue();
        if (error.error_code == ERROR_CODE_EXPIRED_TOKEN) {
            cancelAll(requestQueue);
            showReauthDialog(context);
        }
    }

    private static void showReauthDialog(final Context context) {
        new AlertDialog.Builder(context)
                .setTitle(R.string.title_error)
                .setMessage(R.string.message_expired_token)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(context, AuthorizeActivity.class);

                        if (context instanceof Activity) {
                            Activity activity = (Activity) context;
                            intent.putExtra(AuthorizeActivity.EXTRA_RETURN, activity.getIntent());
                            activity.finish();
                        } else {
                            Intent mainIntent = new Intent(context, MainActivity.class);
                            mainIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            intent.putExtra(AuthorizeActivity.EXTRA_RETURN, mainIntent);
                        }
                        context.startActivity(intent);
                    }
                })
                .show();
    }

    private static void cancelAll(RequestQueue requestQueue) {
        requestQueue.cancelAll(new RequestQueue.RequestFilter() {
            @Override
            public boolean apply(Request<?> request) {
                return true;
            }
        });
    }

    public static Request<Timeline> fetchStatusedHomeTimelin(final Context context, long maxId, final Callback<Timeline> callback) {
        HttpParams params = new HttpParams();
        params.put("access_token", Account.getAccessToken(context));
        params.put("count", 25);
        if (maxId > 0) {
            params.put("max_id", maxId);
        }

        Request<Timeline> request = ApiRequestManager.newQueryRequest(context, Timeline.class, Api.STATUSES_HOME_TIMELINE, params, callback);
        return request;
    }

    public static interface Callback<T> {
        public void onResponse(T response, ApiError error);
    }
}
