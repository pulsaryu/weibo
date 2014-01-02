package me.yuxing.weibo.widget;

import android.content.Context;
import android.util.AttributeSet;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import me.yuxing.weibo.WeiboApplication;

/**
 * Created by yuxing on 13-11-13.
 */
public class AcynsImageView extends NetworkImageView {

    private ImageLoader mImageLoader;

    public AcynsImageView(Context context) {
        this(context, null);
    }

    public AcynsImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AcynsImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        mImageLoader = ((WeiboApplication) context.getApplicationContext()).getImageLoader();
    }

    public void setImageUrl(String url) {
        super.setImageUrl(url, mImageLoader);
    }
}
