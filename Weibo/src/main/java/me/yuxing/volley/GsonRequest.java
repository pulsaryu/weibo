package me.yuxing.volley;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;

import java.io.UnsupportedEncodingException;

/**
 * Created by yuxing on 13-10-30.
 */
public class GsonRequest<T> extends Request<T> {

    private static final String TAG = "GsonRequest";

    private static final String PROTOCOL_CHARSET = "utf-8";
    private final Response.Listener<T> mListener;
    private final String mRequestBody;
    private final Class<T> mCls;

    public GsonRequest(Class<T> cls, int method, String url, HttpParams params, Response.Listener<T> listener, Response.ErrorListener errorListener) {
        super(method, url, errorListener);
        mCls = cls;
        mRequestBody = params == null ? null : params.toString();
        mListener = listener;
    }

    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse response) {
        String parsed;
        try {
            parsed = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
        } catch (UnsupportedEncodingException e) {
            parsed = new String(response.data);
        }

        Log.i(TAG, "response = " + parsed);

        Gson gson = new Gson();
        T t = gson.fromJson(parsed, mCls);
        if (t != null) {
            return Response.success(t, HttpHeaderParser.parseCacheHeaders(response));
        }
        return null;
    }

    @Override
    protected void deliverResponse(T response) {
        if (mListener != null) {
            mListener.onResponse(response);
        }
    }

    @Override
    public byte[] getBody() throws AuthFailureError {
        try {
            return mRequestBody == null ? null : mRequestBody.getBytes(PROTOCOL_CHARSET);
        } catch (UnsupportedEncodingException e) {
            VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s",
                    mRequestBody, PROTOCOL_CHARSET);
            return null;
        }
    }
}
