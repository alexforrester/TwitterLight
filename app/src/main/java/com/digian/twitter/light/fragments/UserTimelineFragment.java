package com.digian.twitter.light.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;
import android.util.Log;

import com.digian.twitter.light.presenters.UserTimelinePresenter;
import com.digian.twitter.light.presenters.UserTimelinePresenterImpl;
import com.digian.twitter.light.views.TimelineView;

/**
 * Created by forrestal on 24/08/2015.
 */
public class UserTimelineFragment extends Fragment implements TimelineView {

    private static final String TAG = UserTimelineFragment.class.getSimpleName();

    public static final String TWITTER_SESSION_USER_ID = "TWITTER_SESSION_USER_ID";
    public static final String TWITTER_SESSION_USER_NAME = "TWITTER_SESSION_USER_NAME";

    private UserTimelinePresenter userTimelinePresenter;

    /**
     * Create new instance of UserTimelineFragment passing in any args
     * @param args
     * @return new instance of UserTimelineFragment
     */
    public static UserTimelineFragment newInstance(@NonNull Bundle args) {
        Log.d(TAG, "UserTimelineFragment newInstance(@NonNull Bundle args)");
        UserTimelineFragment fragment = new UserTimelineFragment();
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Default Constructor
     */
    public UserTimelineFragment() {}

    /**
     Set-up
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate(Bundle savedInstanceState)");

        userTimelinePresenter = UserTimelinePresenterImpl.newInstance(this);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d(TAG, "onActivityCreated(Bundle savedInstanceState)");

        if (savedInstanceState != null)
            initiliseUserTimelinPresenter(savedInstanceState);
        else
            initiliseUserTimelinPresenter(getArguments());

        initiliseUserTimelinPresenter(savedInstanceState);
    }

    @VisibleForTesting
    void initiliseUserTimelinPresenter(Bundle bundle) {
        userTimelinePresenter.init(getArguments());
    }

    @VisibleForTesting
    UserTimelinePresenter getUserTimelinePresenter() {
        return userTimelinePresenter;
    }

    @Override
    public void setUpTimeline() {
    }
}
