package com.digian.twitter.light.fragments;

import android.app.ListFragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ProgressBar;

import com.digian.twitter.light.R;
import com.digian.twitter.light.presenters.HomeTimelinePresenter;
import com.digian.twitter.light.presenters.HomeTimelinePresenterImpl;
import com.digian.twitter.light.views.TimelineView;

/**
 * Created by forrestal on 24/08/2015.
 */
public class HomeTimelineFragment extends ListFragment implements TimelineView {

    private static final String TAG = HomeTimelineFragment.class.getSimpleName();

    private HomeTimelinePresenter mHomeTimelinePresenter;
    private ProgressBar mProgressBar;

    /**
     * Create new instance of UserTimelineFragment passing in any args
     * @param args
     * @return new instance of UserTimelineFragment
     */
    public static HomeTimelineFragment newInstance(@NonNull Bundle args) {
        Log.d(TAG, "HomeTimelineFragment newInstance(@NonNull Bundle args)");
        HomeTimelineFragment fragment = new HomeTimelineFragment();
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Default Constructor
     */
    public HomeTimelineFragment() {}

    /**
     Set-up
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate(Bundle savedInstanceState)");

        mHomeTimelinePresenter = HomeTimelinePresenterImpl.newInstance(this);
    }

    /**
     * Return view for the fragment's home timeline
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_timeline, container, false);
    }

    /**
     * Takes care of initialising the presenter depending on whether this is the first time
     * it has been created or whether it is done from savedInstanceState
     *
     * @param savedInstanceState
     */
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d(TAG, "onActivityCreated(Bundle savedInstanceState)");

        if (savedInstanceState != null) {
            Log.d(TAG, "savedInstanceState != null");
            initialiseUserTimelinePresenter(savedInstanceState);
        }
        else {
            Log.d(TAG, "savedInstanceState == null");
            initialiseUserTimelinePresenter(getArguments());
        }
    }

    /**
     * When presenter initialisation has been completed and fragment is viewable
     * then create the home timeline
     */
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume()");
        createHomeTimeline();
    }

    /**
     * This is the overridden method from the TimelineView so the presenter can update the view
     * through the interface
     * @param adapter
     */
    @Override
    public void displayUserTweetList(ListAdapter adapter) {
        Log.d(TAG, "displayUserTweetList(ListAdapter adapter)");
        setListAdapter(adapter);
    }

    @VisibleForTesting
    HomeTimelinePresenter getHomeTimelinePresenter() {
        Log.d(TAG, "getHomeTimelinePresenter()");
        return mHomeTimelinePresenter;
    }

    @VisibleForTesting
    void createHomeTimeline() {
        Log.d(TAG,"createHomeTimeline()");
        mHomeTimelinePresenter.createTimeline(this.getActivity());
    }

    @VisibleForTesting
    void initialiseUserTimelinePresenter(Bundle bundle) {
        Log.d(TAG,"initialiseUserTimelinePresenter(Bundle bundle)");
        mHomeTimelinePresenter.init(bundle);
    }
}