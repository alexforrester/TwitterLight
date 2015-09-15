package com.digian.twitter.light.views;

/**
 * Created by forrestal on 26/08/2015.
 * interface Presenter users to communicate with the view (fragment)
 */
public interface TweetComposerView {
    void displayUpdatedTimeline();
    void displayError(String string);
}
