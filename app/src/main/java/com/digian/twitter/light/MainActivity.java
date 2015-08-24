package com.digian.twitter.light;

/**
 * Created by forrestal on 20/08/2015.
 */

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.digian.twitter.light.fragments.SignInFragment;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;

import io.fabric.sdk.android.Fabric;

public class MainActivity extends AppCompatActivity implements TwitterSignInCallback {

    private static final String TAG = MainActivity.class.getSimpleName();

    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
    private static final String TWITTER_KEY = "gkQPY641YDB2lVnyJ1aMyYfWj";
    private static final String TWITTER_SECRET = "2tbrFaaDQYCsNxhXeTcFWZVQAB1qypIZI31JQwSItVj564YS9r";

    /**
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this, new Twitter(authConfig));

        //Load IntroFragment
        getFragmentManager().beginTransaction().replace(android.R.id.content, SignInFragment.newInstance()).commit();
    }

    /**
     * @param menu - inflate the menu; this adds items to the action bar if it is present.
     * @return - true if consumed etc.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    /**
     * @param item which is clicked in actionbar/toolbar
     * @return whether consumed etc..
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Callback from Fabric Twitter Sign In call
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult(int requestCode, int resultCode, Intent data)");
        Log.d(TAG, "requestCode: " + requestCode);
        Log.d(TAG, "resultCode: " + resultCode);

        // Pass the activity result to the fragment, which will then pass the result to the login button.
        Fragment fragment = getCurrentFragment();
        if (fragment != null) {
            Log.d(TAG, "Passing result back to the sign in fragment");
            fragment.onActivityResult(requestCode, resultCode, data);
        }
    }

    /**
     * Utility method to get current fragment and also used for test classes hence package private visibility
     *
     * @return active fragment
     */
    @VisibleForTesting
    @CheckResult
    @Nullable
    Fragment getCurrentFragment() {
        return getFragmentManager().findFragmentById(android.R.id.content);
    }

    /**
     * Set fragment - used for tests/spys etc.
     *
     * @param fragment
     */
    @VisibleForTesting
    void setCurrentFragment(@NonNull Fragment fragment) {
        getFragmentManager().beginTransaction().replace(android.R.id.content, fragment).commit();
    }

    /**
     * Returns Twitter session of now logged in user
     * <p/>
     * The active session is automatically persisted, but can be retrieved again from below:
     * <p/>
     * TwitterSession session = Twitter.getSessionManager().getActiveSession();
     * TwitterAuthToken authToken = session.getAuthToken();
     * String token = authToken.token;
     * String secret = authToken.secret;
     *
     * @param result active Twitter session
     */
    @Override
    public void signInSuccess(@NonNull Result<TwitterSession> result) {
        Log.d(TAG, "signInSuccess(Result<TwitterSession> result)");
    }

    @Override
    public void signInFailure(@NonNull TwitterException exception) {
        Log.e(TAG, "signInFailure(TwitterException exception)", exception);
    }
}