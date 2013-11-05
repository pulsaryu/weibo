package me.yuxing.volley;

import android.os.Bundle;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Set;

/**
 * Created by yuxing on 13-10-26.
 */
public class HttpParams {
    private Bundle mParams = new Bundle();

    public void put(String name, String value) {
        mParams.putString(name, value);
    }

    public void put(String name, long value) {
        put(name, String.valueOf(value));
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        Set<String> keys = mParams.keySet();
        boolean first = true;
        try {
            for (String key : keys) {
                if (first) {
                    first = false;
                } else {
                    sb.append("&");
                }

                sb.append(key);
                sb.append("=");
                sb.append(URLEncoder.encode(mParams.getString(key), "utf-8"));

            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return sb.toString();
    }
}
