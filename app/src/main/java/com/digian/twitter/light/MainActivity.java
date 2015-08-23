package com.digian.twitter.light;

/**
 * Created by forrestal on 20/08/2015.
 */

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.VisibleForTesting;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.digian.twitter.light.fragments.SignInFragment;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;

import io.fabric.sdk.android.Fabric;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
    private static final String TWITTER_KEY = "gkQPY641YDB2lVnyJ1aMyYfWj";
    private static final String TWITTER_SECRET = "2tbrFaaDQYCsNxhXeTcFWZVQAB1qypIZI31JQwSItVj564YS9r";

    /**
     *
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
     *
     * @param menu - inflate the menu; this adds items to the action bar if it is present.
     * @return - true if consumed etc.
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    /**
     *
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
     * Callbach from Fabric Twitter Sign In call
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.d(TAG, "requestCode: " + requestCode);
        Log.d(TAG, "resultCode: "+resultCode);
        Log.d(TAG, "data: "+data.getAction());

        Log.d(TAG, "onActivityResult called");

        // Pass the activity result to the fragment, which will then pass the result to the login button.
        Fragment fragment = getCurrentFragment();
        if (fragment != null) {
            Log.d(TAG, "Passing result back to the sign in fragment");
            fragment.onActivityResult(requestCode, resultCode, data);
        }
    }

    @VisibleForTesting
    Fragment getCurrentFragment() {
        return getFragmentManager().findFragmentById(android.R.id.content);
    }

    @VisibleForTesting
    void setCurrentFragment(Fragment fragment) {
        getFragmentManager().beginTransaction().replace(android.R.id.content, fragment).commit();;
    }
}