package me.yuxing.weibo;

import android.app.Activity;
import android.content.Intent;

/**
 * Created by yuxing on 13-10-21.
 */
public abstract class BaseActivity extends Activity {

    public void checkAuthAndStartActivity(Intent intent) {
        if (Account.isAuthorized(this)) {
            super.startActivity(intent);
        } else {
            Intent authIntent = new Intent(this, AuthorizeActivity.class);
            authIntent.putExtra(AuthorizeActivity.EXTRA_RETURN, intent);
            startActivity(authIntent);
        }
    }
}
