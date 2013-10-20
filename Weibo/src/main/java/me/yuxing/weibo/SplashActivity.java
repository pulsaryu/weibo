package me.yuxing.weibo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

/**
 * Created by yuxing on 13-10-20.
 */
public class SplashActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startActivity(new Intent(this, AuthorizeActivity.class));
    }
}
