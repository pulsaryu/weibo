package me.yuxing.weibo.ui;

import android.content.Context;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import me.yuxing.weibo.ArrayAdapter;
import me.yuxing.weibo.R;
import me.yuxing.weibo.WeiboApplication;
import me.yuxing.weibo.model.Status;
import me.yuxing.weibo.model.Timeline;
import me.yuxing.weibo.request.ApiError;
import me.yuxing.weibo.request.ApiRequestManager;
import me.yuxing.weibo.request.WeiboImageView;
import me.yuxing.weibo.widget.AcynsImageView;

/**
 * Created by yuxing on 13-10-22.
 */
public class StatusesTimelineFragment extends BaseFragment implements AbsListView.OnScrollListener{

    private static final String TAG = "StatusesTimelineFragment";
    private static final int COUNT_REMAIND_FOR_LOAD_MORE = 5;
    private StatusAdapter mStatusAdapter;
    private Request<?> mRequest;
    private boolean isLoading = true;
    private boolean isRefresh = true;
    private ListView mListView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mStatusAdapter = new StatusAdapter(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_statuses_timeline, container, false);
        mListView = (ListView) view.findViewById(R.id.listView);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setHasOptionsMenu(true);

        mListView.setOnScrollListener(this);
        mListView.setAdapter(mStatusAdapter);

        loadData(0);
    }

    private void loadData(long maxId) {
        getActionBarHelper().setRefreshActionItemState(true);
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
                } else if (error != null) {
                }

                getActionBarHelper().setRefreshActionItemState(false);
                isLoading = false;
            }
        });
    }

    private void loadMore() {
        if (mStatusAdapter.getCount() == 0)
            return;
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
        mListView.setSelection(0);
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

        private DateFormat mDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z");
        private DateFormat mSourceFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss Z yyyy", new Locale("en", "CN"));

        public StatusAdapter(Context context) {
            super(context);
        }

        @Override
        public View newView(LayoutInflater inflater, ViewGroup parent, Status data) {
            return inflater.inflate(R.layout.timeline_list_item, parent, false);
        }

        @Override
        public void bindView(View view, Status status) {
            TextView textView = (TextView) view.findViewById(R.id.text);
            textView.setText(Html.fromHtml(status.text.replaceAll("(http|https):[_/\\-\\.0-9a-zA-Z]+", "<a href=\"$0\">$0</a>")));
            textView.setMovementMethod(new LinkMovementMethod());

            WeiboImageView pictureView = (WeiboImageView) view.findViewById(R.id.picture);
            if (status.bmiddle_pic == null) {
                pictureView.setVisibility(View.GONE);
            } else {
                pictureView.setVisibility(View.VISIBLE);
                pictureView.setImageUrl(status.bmiddle_pic, WeiboApplication.getInstance().getImageLoader());
            }

            try {
                ((TextView) view.findViewById(R.id.time)).setText(formatTime(mSourceFormat.parse(status.created_at).getTime()));
            } catch (ParseException e) {
                e.printStackTrace();
            }

            //bind user
            ((AcynsImageView) view.findViewById(R.id.userAvatar)).setImageUrl(status.user.profile_image_url);
            ((TextView) view.findViewById(R.id.userName)).setText(status.user.name);
        }

        private String formatTime(long time) {
            String format;

            int distance = (int) (System.currentTimeMillis() - time) / 1000;

            if (distance < 10) {
                format = getContext().getString(R.string.time_just_now);
            } else if (distance < 60) {
                format = String.format(getContext().getString(R.string.time_n_minutes_ago), distance);
            } else if (distance < 3600) {
                format = String.format(getContext().getString(R.string.time_n_seconds_ago), distance/60);
            } else {
                format = mDateFormat.format(new Date(time));
            }

            return format;
        }
    }
}
