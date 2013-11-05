package me.yuxing.weibo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by yuxing on 13-10-31.
 */
public abstract class ArrayAdapter<T> extends BaseAdapter {

    private List<T> mData;
    private final LayoutInflater mInflater;

    private final Context mContext;

    public ArrayAdapter(Context context) {
        mContext = context;
        mData = new ArrayList<T>();
        mInflater = LayoutInflater.from(context);
    }

    public Context getContext() {
        return mContext;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public T getItem(int position) {
        return mData.get(position);
    }

    public void addItem(T data) {
        mData.add(data);
    }

    public void clear() {
        mData.clear();
    }

    public void addItems(List<T> items) {
        mData.addAll(items);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        T data = mData.get(position);
        if (convertView == null) {
            convertView = newView(mInflater, parent, data);
        }
        bindView(convertView, data);
        return convertView;
    }

    public abstract View newView(LayoutInflater inflater, ViewGroup parent, T data);

    public abstract void bindView(View view, T data);

}
