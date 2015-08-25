package com.digian.twitter.light.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;

import com.digian.twitter.light.views.TimelineView;

/**
 * Created by forrestal on 25/08/2015.
 */
public class HomeTimelineSubFragment extends HomeTimelineFragment implements TimelineView {

    private Bundle testBundle;
    private boolean createHomeTimelineCalled = false;

    public static HomeTimelineSubFragment newInstance(@NonNull Bundle args) {
        HomeTimelineSubFragment fragment = new HomeTimelineSubFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public HomeTimelineSubFragment() {}

    @Override
    void createHomeTimeline() {
        createHomeTimelineCalled = true;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    void initialiseUserTimelinePresenter(Bundle bundle) {
        testBundle = bundle;
    }

    Bundle getTestBundle() {
        return testBundle;
    }

    boolean isCreateHomeTimelineCalled() {
        return createHomeTimelineCalled;
    }
}
