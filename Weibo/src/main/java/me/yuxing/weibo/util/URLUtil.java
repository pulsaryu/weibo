package me.yuxing.weibo.util;

import android.net.Uri;
import android.os.Bundle;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Set;

/**
 * Created by yuxing on 13-10-22.
 */
public class URLUtil {

    public static String buildQuery(Bundle params) {
        StringBuilder query = new StringBuilder();
        Set<String> keys = params.keySet();
        boolean isFirst = true;
        try {
            for (String key : keys) {
                if (isFirst) {
                    isFirst = false;
                } else {
                    query.append("&");
                }
                query.append(key).append("=").append(URLEncoder.encode(params.getString(key), "UTF-8"));
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return query.toString();
    }

    public static Bundle getQueryOfUrl(String url) {
        Bundle params = new Bundle();
        Uri uri = Uri.parse(url);
        Set<String> names = uri.getQueryParameterNames();
        for (String name : names) {
            params.putString(name, uri.getQueryParameter(name));
        }
        return params;
    }
}
