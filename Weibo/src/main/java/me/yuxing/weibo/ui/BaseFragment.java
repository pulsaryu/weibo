package me.yuxing.weibo.ui;

import android.app.Fragment;

import me.yuxing.weibo.ActionBarHelper;

/**
 * Created by yuxing on 13-11-12.
 */
public abstract class BaseFragment extends Fragment {

    protected ActionBarHelper getActionBarHelper() {
        if (getActivity() instanceof BaseActivity) {
            return ((BaseActivity) getActivity()).getActionBarHelper();
        } else {
            throw new IllegalArgumentException("The parent activity must extends of BaseActivity");
        }
    }
}
