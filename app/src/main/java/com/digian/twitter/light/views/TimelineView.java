package com.digian.twitter.light.views;

import android.widget.ListAdapter;

/**
 * Created by forrestal on 24/08/2015.
 */
public interface TimelineView {
    void displayUserTweetList(ListAdapter adapter);
    void updateUserTweetList(ListAdapter adapter);
    void displayError(String string);
}
