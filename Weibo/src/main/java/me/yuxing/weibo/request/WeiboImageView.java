package me.yuxing.weibo.request;

import android.content.Context;
import android.util.AttributeSet;

import com.android.volley.toolbox.NetworkImageView;

/**
 * Created by yuxing on 13-10-31.
 */
public class WeiboImageView extends NetworkImageView {

    public WeiboImageView(Context context) {
        this(context, null);
    }

    public WeiboImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WeiboImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

//    @Override
//    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//
//        Drawable d = getDrawable();
//
//        if (d == null) {
//            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//        } else {
//            int width = MeasureSpec.getSize(widthMeasureSpec);
//            int height = (int) Math.ceil((float)width * d.getIntrinsicHeight() / d.getIntrinsicWidth());
//            setMeasuredDimension(width, height);
//        }
//
//    }
}
