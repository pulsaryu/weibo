package me.yuxing.weibo;

import android.content.Intent;
import android.os.Bundle;

/**
 * Created by yuxing on 13-10-20.
 */
public class SplashActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent mainIntent = new Intent(this, MainActivity.class);
        checkAuthAndStartActivity(mainIntent);
    }
}
