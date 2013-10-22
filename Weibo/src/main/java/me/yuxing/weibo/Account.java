package me.yuxing.weibo;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by yuxing on 13-10-21.
 */
public class Account {
    private static final String PREF_NAME = "accounts";
    private static final String KEY_UID = "uid";
    private static final String KEY_ACCESS_TOKEN = "access_token";
    private static final String KEY_EXPIRE_IN = "expire_in";

    public static boolean isAuthorized(final Context context) {
        SharedPreferences pref = getPreferences(context);
        return pref.getString(KEY_UID, null) != null
                    && pref.getLong(KEY_EXPIRE_IN, 0) > System.currentTimeMillis();
    }

    public static SharedPreferences getPreferences(final Context context) {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public static void addToken(final Context context, final String uid, final String accessToken, final int expireIn) {
        SharedPreferences pref = getPreferences(context);
        SharedPreferences.Editor edit = pref.edit();
        edit.putString(KEY_UID, uid);
        edit.putString(KEY_ACCESS_TOKEN, accessToken);
        edit.putLong(KEY_EXPIRE_IN, System.currentTimeMillis() + expireIn * 1000);
        edit.commit();
    }

    public static String getAccessToken(final Context context) {
        return getPreferences(context).getString(KEY_ACCESS_TOKEN, null);
    }

    public static void clearToken(final Context context) {
        getPreferences(context).edit().clear().commit();
    }
}
