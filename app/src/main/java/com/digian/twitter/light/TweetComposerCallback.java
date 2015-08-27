package com.digian.twitter.light;

/**
 * Created by forrestal on 26/08/2015.
 * Callbacks which fragment makes on the activity usually to ask activity to replace a fragment
 */
public interface TweetComposerCallback {
    void displayTweetComposer();
    void showUpdatedTimeline();
}
