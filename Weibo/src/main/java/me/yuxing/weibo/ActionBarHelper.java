package me.yuxing.weibo;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

/**
 * Created by yuxing on 13-11-12.
 */
public class ActionBarHelper {

    private static final String TAG = "ActionBarHelper";

    private Menu mOptionsMenu;

    public void onCreateOptionsMenu(Menu menu) {
        mOptionsMenu = menu;
    }

    public void setRefreshActionItemState(boolean refreshing) {
        if (mOptionsMenu == null) {
            Log.d(TAG, "mOptionsMenu is null");
            return;
        }

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
