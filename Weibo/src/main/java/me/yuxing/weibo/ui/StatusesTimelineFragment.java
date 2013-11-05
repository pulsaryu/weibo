package me.yuxing.weibo.ui;

import android.app.ListFragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.TextView;

import com.android.volley.Request;

import java.util.Arrays;

import me.yuxing.weibo.ArrayAdapter;
import me.yuxing.weibo.R;
import me.yuxing.weibo.WeiboApplication;
import me.yuxing.weibo.model.Status;
import me.yuxing.weibo.model.Timeline;
import me.yuxing.weibo.request.ApiError;
import me.yuxing.weibo.request.ApiRequestManager;
import me.yuxing.weibo.request.WeiboImageView;

/**
 * Created by yuxing on 13-10-22.
 */
public class StatusesTimelineFragment extends ListFragment implements AbsListView.OnScrollListener{

    private static final String TAG = "StatusesTimelineFragment";
    private static final int COUNT_REMAIND_FOR_LOAD_MORE = 5;
    private StatusAdapter mStatusAdapter;
    private Request<?> mRequest;
    private boolean isLoading = true;
    private boolean isRefresh = true;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mStatusAdapter = new StatusAdapter(getActivity());
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getListView().setOnScrollListener(this);

        setHasOptionsMenu(true);
        setListAdapter(mStatusAdapter);
        setListShown(false);

        loadData(0);
    }

    private void loadData(long maxId) {
        ((BaseActivity)getActivity()).setRefreshActionItemState(true);
        isLoading = true;

        if (mRequest != null) {
            mRequest.cancel();
        }

        mRequest = ApiRequestManager.fetchStatusedHomeTimelin(getActivity(), maxId, new ApiRequestManager.Callback<Timeline>() {
            @Override
            public void onResponse(Timeline response, ApiError error) {
                if (response != null) {
                    if (isRefresh) {
                        mStatusAdapter.clear();
                        isRefresh = false;
                    }
                    mStatusAdapter.addItems(Arrays.asList(response.statuses));
                    mStatusAdapter.notifyDataSetChanged();
                }

                setListShown(true);
                ((BaseActivity)getActivity()).setRefreshActionItemState(false);
                isLoading = false;
            }
        });
    }

    private void loadMore() {
        long maxId = mStatusAdapter.getItem(mStatusAdapter.getCount() - 1).id;
        loadData(maxId);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mRequest != null) {
            mRequest.cancel();
        }
    }

    private void refreshData() {
        isRefresh = true;
        getListView().setSelection(0);
        loadData(0);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.add(0,R.id.action_refresh, 0, R.string.refresh)
                .setIcon(R.drawable.ic_action_refresh)
                .setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_refresh:
                refreshData();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if (isLoading == false && totalItemCount - firstVisibleItem - visibleItemCount <= COUNT_REMAIND_FOR_LOAD_MORE) {
            loadMore();
        }
    }

    private static class StatusAdapter extends ArrayAdapter<Status> {

        public StatusAdapter(Context context) {
            super(context);
        }

        @Override
        public View newView(LayoutInflater inflater, ViewGroup parent, Status data) {
            return inflater.inflate(R.layout.timeline_list_item, parent, false);
        }

        @Override
        public void bindView(View view, Status status) {
            TextView nameView = (TextView) view.findViewById(R.id.name);
            WeiboImageView pictureView = (WeiboImageView) view.findViewById(R.id.picture);

            nameView.setText(status.text);
            if (status.bmiddle_pic == null) {
                pictureView.setVisibility(View.GONE);
            } else {
                pictureView.setVisibility(View.VISIBLE);
                pictureView.setImageUrl(status.bmiddle_pic, WeiboApplication.getInstance().getImageLoader());
            }
        }
    }
}
