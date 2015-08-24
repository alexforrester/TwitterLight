package com.digian.twitter.light.presenters;

import android.os.Bundle;
import android.support.annotation.NonNull;

import com.digian.twitter.light.fragments.UserTimelineFragment;
import com.digian.twitter.light.views.TimelineView;

/**
 * Created by forrestal on 24/08/2015.
 */
public class UserTimelinePresenterImpl implements UserTimelinePresenter {

    private static final String TAG = UserTimelinePresenterImpl.class.getSimpleName();

    private long mUserId;
    private String mUserName;
    private TimelineView timelineView;

    public static UserTimelinePresenterImpl newInstance(@NonNull TimelineView timelineView) {
        return new UserTimelinePresenterImpl(timelineView);
    }

    private UserTimelinePresenterImpl(@NonNull TimelineView timelineView){
        this.timelineView = timelineView;
    }

    @Override
    public void init(@NonNull Bundle bundle) {
        setUserDetails(bundle);
    }

    private void setUserDetails(@NonNull Bundle bundle) {
        if (bundle != null) {
            mUserId = bundle.getLong(UserTimelineFragment.TWITTER_SESSION_USER_ID);
            mUserName = bundle.getString(UserTimelineFragment.TWITTER_SESSION_USER_NAME);
        }
    }
}
