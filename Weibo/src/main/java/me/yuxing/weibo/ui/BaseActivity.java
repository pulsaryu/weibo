package me.yuxing.weibo.ui;

import android.app.Activity;
import android.content.Intent;
import android.view.Menu;

import me.yuxing.weibo.Account;
import me.yuxing.weibo.ActionBarHelper;

/**
 * Created by yuxing on 13-10-21.
 */
public abstract class BaseActivity extends Activity {
    private static final String TAG = "BaseActivity";

    private ActionBarHelper mActionBarHelper = new ActionBarHelper();

    public void checkAuthAndStartActivity(Intent intent) {
        if (Account.isAuthorized(this)) {
            startActivity(intent);
        } else {
            Intent authIntent = new Intent(this, AuthorizeActivity.class);
            authIntent.putExtra(AuthorizeActivity.EXTRA_RETURN, intent);
            startActivity(authIntent);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        mActionBarHelper.onCreateOptionsMenu(menu);
        return super.onCreateOptionsMenu(menu);
    }

    public ActionBarHelper getActionBarHelper() {
        return mActionBarHelper;
    }
}
