package com.digian.twitter.light;

/**
 * Created by forrestal on 20/08/2015.
 * The sole activity managing all the other fragments for sign in, home timeline display, refreshing timeline
 * and sending tweets
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
import android.widget.Toast;

import com.digian.twitter.light.fragments.HomeTimelineFragment;
import com.digian.twitter.light.fragments.SignInFragment;
import com.digian.twitter.light.fragments.TweetComposerFragment;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.tweetui.TweetUi;

import io.fabric.sdk.android.Fabric;
import io.fabric.sdk.android.Kit;

public class MainActivity extends AppCompatActivity implements TwitterSignInCallback, TweetComposerCallback {

    private static final String TAG = MainActivity.class.getSimpleName();

    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
    private static final String TWITTER_KEY = "gkQPY641YDB2lVnyJ1aMyYfWj";
    private static final String TWITTER_SECRET = "2tbrFaaDQYCsNxhXeTcFWZVQAB1qypIZI31JQwSItVj564YS9r";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate(Bundle savedInstanceState)");
        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        setUpFabricKits(authConfig);

        setContentView(R.layout.fragment_container);

        if (savedInstanceState == null) {
            //Load SignInFragment
            Log.d(TAG,"SignInFragment created");
            getFragmentManager().beginTransaction().add(R.id.fragment_container, SignInFragment.newInstance()).commit();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart()");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume()");
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause()");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop()");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy()");
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        return false;
    }

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

    @Override
    public void signInSuccess(@NonNull Result<TwitterSession> result) {
        //Twitter session is not used as user is automatcially logged in
        Log.d(TAG, "signInSuccess(Result<TwitterSession> result)");
        getFragmentManager().beginTransaction().replace(R.id.fragment_container, getHomeTimelineFragment()).commit();
    }

    @Override
    public void signInFailure(@NonNull TwitterException exception) {
        Log.e(TAG, "signInFailure(TwitterException exception)", exception);
        displayToast(exception);
    }

    @Override
    public void displayTweetComposer() {
        getFragmentManager().beginTransaction().replace(R.id.fragment_container, TweetComposerFragment.newInstance()).addToBackStack(null).commit();
    }

    @Override
    public void showUpdatedTimeline() {
        Log.d(TAG, "Show updated timeline");
        getFragmentManager().beginTransaction().replace(R.id.fragment_container, getHomeTimelineFragment()).commit();
    }

    @Override
    public void onBackPressed() {
        Log.d(TAG, "onBackPressed()");
        if (getFragmentManager().getBackStackEntryCount() > 0) {
            getFragmentManager().popBackStack();
        } else {
            this.finish();
        }
    }

    @VisibleForTesting
    void displayToast(@NonNull TwitterException exception) {
        Toast.makeText(this, exception.getMessage(), Toast.LENGTH_LONG).show();
    }

    @VisibleForTesting
    @CheckResult
    @Nullable
    Fragment getCurrentFragment() {
        return getFragmentManager().findFragmentById(R.id.fragment_container);
    }

    @VisibleForTesting
    @NonNull
    @CheckResult
    HomeTimelineFragment getHomeTimelineFragment() {
        return HomeTimelineFragment.newInstance();
    }

    @VisibleForTesting
    void setCurrentFragment(@NonNull Fragment fragment) {
        getFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
    }

    private void setUpFabricKits(TwitterAuthConfig authConfig) {
        Fabric.with(this, new Kit[]{new TwitterCore(authConfig), new TweetUi()});
    }
}