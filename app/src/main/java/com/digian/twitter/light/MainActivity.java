package com.digian.twitter.light;

/**
 * Created by forrestal on 20/08/2015.
 */

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.digian.twitter.light.fragments.SignInFragment;
import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import io.fabric.sdk.android.Fabric;

public class MainActivity extends AppCompatActivity {

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

        setContentView(R.layout.activity_main);

        //Load IntroFragment
        getFragmentManager().beginTransaction().replace(R.id.fragment_container, SignInFragment.newInstance()).commit();
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
}