package me.yuxing.weibo.ui;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;

import me.yuxing.weibo.Account;
import me.yuxing.weibo.R;
import me.yuxing.weibo.WeiboApplication;
import me.yuxing.weibo.model.OAuthToken;
import me.yuxing.weibo.util.URLUtil;

/**
 * Created by yuxing on 13-10-21.
 */
public class AuthorizeActivity extends Activity {
    public static final String EXTRA_RETURN = "return";
    private static final String TAG = "AuthorizeActivity";
    private static final String API_OAUTH2_AUTHORIZE = "https://open.weibo.cn/oauth2/authorize";
    private static final String API_OAUTH2_ACCESS_TOKEN = "https://open.weibo.cn/oauth2/access_token";

    private WebView mLoginView;
    private ProgressBar mProgressBar;
    private String mOAuthState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authorize);

        mLoginView = (WebView) findViewById(R.id.login_view);
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);

        setUpWebView();
    }

    @SuppressLint("SetJavaScriptEnabled")
    private void setUpWebView() {
        mLoginView.getSettings().setJavaScriptEnabled(true);
        mLoginView.setWebViewClient(new WeiboWebViewClient());
        mLoginView.setWebChromeClient(new WeiboWebChromeClient());

        Bundle params = new Bundle();
        params.putString("client_id", getString(R.string.weibo_app_key));
        params.putString("redirect_uri", getString(R.string.weibo_callback));
        params.putString("scope", "all");
        mOAuthState = genNewOAuthState();
        params.putString("state", mOAuthState);
        params.putString("display", "mobile");
        params.putString("forcelogin", "true");
        String url = API_OAUTH2_AUTHORIZE + "?" + URLUtil.buildQuery(params);
        mLoginView.loadUrl(url);
    }

    private class WeiboWebViewClient extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (url.startsWith(getString(R.string.weibo_callback))) {
                Log.d(TAG, "url " + url);
                onAuthorizeCallback(url);
                return true;
            }
            return false;
        }

    }

    private class WeiboWebChromeClient extends WebChromeClient {

        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
            AuthorizeActivity.this.setTitle(title);
        }
    }

    private static String genNewOAuthState() {
        return Integer.toHexString((int)(Math.random() * 10000000));
    }

    private void onAuthorizeCallback(String url) {
        Bundle query = URLUtil.getQueryOfUrl(url);
        String state = query.getString("state");
        String code = query.getString("code");

        if (validateState(state)) {
            mLoginView.setVisibility(View.GONE);
            mProgressBar.setVisibility(View.VISIBLE);
            requestAccessTokenWithCode(code);
        }
    }

    private boolean validateState(String state) {
        return mOAuthState.equals(state);
    }

    private void requestAccessTokenWithCode(String code) {
        RequestQueue requestQueue = WeiboApplication.getInstance().getRequestQueue();
        Bundle params = new Bundle();
        params.putString("client_id", getString(R.string.weibo_app_key));
        params.putString("client_secret", getString(R.string.weibo_app_secret));
        params.putString("grant_type", "authorization_code");
        params.putString("code", code);
        params.putString("redirect_uri", getString(R.string.weibo_callback));
        String url = API_OAUTH2_ACCESS_TOKEN + "?" + URLUtil.buildQuery(params);
        Request<String> request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, response);
                Gson gson = new Gson();
                OAuthToken oAuthToken = gson.fromJson(response, OAuthToken.class);
                onAuthorized(oAuthToken);

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "error " + error.getMessage());
                Toast.makeText(AuthorizeActivity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        requestQueue.add(request);
    }

    private void onAuthorized(OAuthToken oAuthToken) {
        Account.addToken(this, oAuthToken.uid, oAuthToken.access_token, oAuthToken.expires_in);
        Intent returnIntent = getIntent().getParcelableExtra(EXTRA_RETURN);
        if (returnIntent != null) {
            startActivity(returnIntent);
        }
        finish();
    }
}
