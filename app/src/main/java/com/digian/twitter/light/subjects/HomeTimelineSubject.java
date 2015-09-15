package com.digian.twitter.light.subjects;

import android.widget.ListAdapter;

import com.digian.twitter.light.observers.TimelineObserver;

/**
 * Created by forrestal on 15/09/2015.
 */
public interface HomeTimelineSubject {
    void registerObserver(TimelineObserver timelineObserver);
    void removeObserver(TimelineObserver timelineObserver);
    void notifyUserTweetListCreated(ListAdapter adapter);
    void notifyUserTweetListUpdated(ListAdapter adapter);
    void notifyError(String error);
}
