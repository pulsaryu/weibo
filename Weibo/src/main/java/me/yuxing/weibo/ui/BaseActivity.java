package me.yuxing.weibo.ui;

import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;

import me.yuxing.weibo.Account;
import me.yuxing.weibo.R;

/**
 * Created by yuxing on 13-10-21.
 */
public abstract class BaseActivity extends Activity {

    private Menu mOptionsMenu;

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
        mOptionsMenu = menu;
        return super.onCreateOptionsMenu(menu);
    }

    public void setRefreshActionItemState(boolean refreshing) {
        if (mOptionsMenu == null)
            return;

        final MenuItem refreshItem = mOptionsMenu.findItem(R.id.action_refresh);
        if (refreshItem != null) {
            if (refreshing) {
                refreshItem.setActionView(R.layout.actionbar_indeterminate_progress);
            } else {
                refreshItem.setActionView(null);
            }
        }
    }
}
