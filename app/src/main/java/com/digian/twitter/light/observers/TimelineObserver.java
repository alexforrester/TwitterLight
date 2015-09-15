package com.digian.twitter.light.observers;

import android.widget.ListAdapter;

/**
 * Created by forrestal on 15/09/2015.
 */
public interface TimelineObserver {
    void createUserTweetList(ListAdapter adapter);
    void updateUserTweetList(ListAdapter adapter);
    void processError(String string);
}
