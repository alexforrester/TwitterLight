package com.digian.twitter.light.fragments;

import com.digian.twitter.light.views.TimelineView;

/**
 * Created by forrestal on 25/08/2015.
 */
public class HomeTimelineSubFragment extends HomeTimelineFragment implements TimelineView {


    private boolean createHomeTimelineCalled = false;

    public static HomeTimelineSubFragment newInstance() {
        HomeTimelineSubFragment fragment = new HomeTimelineSubFragment();

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

    boolean isCreateHomeTimelineCalled() {
        return createHomeTimelineCalled;
    }
}
