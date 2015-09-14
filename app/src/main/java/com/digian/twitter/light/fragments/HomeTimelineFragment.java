package com.digian.twitter.light.fragments;

import android.app.Activity;
import android.app.ListFragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.VisibleForTesting;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.Toast;

import com.digian.twitter.light.R;
import com.digian.twitter.light.TweetComposerCallback;
import com.digian.twitter.light.presenters.HomeTimelinePresenter;
import com.digian.twitter.light.views.TimelineView;
import com.twitter.sdk.android.tweetui.TweetTimelineListAdapter;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by forrestal on 24/08/2015.
 * Display with user's home timeline with the option to refresh and tweet
 *
 */
public class HomeTimelineFragment extends ListFragment implements TimelineView {

    private static final String TAG = HomeTimelineFragment.class.getSimpleName();

    private HomeTimelinePresenter mHomeTimelinePresenter;

    @Bind(R.id.refresh_list) Button mRefreshButton;
    @Bind(R.id.post_tweet) Button mTweetButton;

    private TweetComposerCallback mTweetComposerCallback;

    public static HomeTimelineFragment newInstance() {
        Log.d(TAG, "HomeTimelineFragment newInstance()");
        HomeTimelineFragment fragment = new HomeTimelineFragment();
        return fragment;
    }

    public HomeTimelineFragment() {}

    //Attach parent activity with check for implementation of interfaces
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        Log.d(TAG, "onAttach(Activity activity)");
        try {
                mTweetComposerCallback = (TweetComposerCallback) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement TweetComposerCallback");
        }
    }

    //Attach parent activity with check for implementation of interfaces
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.d(TAG, "onAttach(Context context)");
        try {
            if (context instanceof Activity)
                mTweetComposerCallback = (TweetComposerCallback) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement TweetComposerCallback");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate(Bundle savedInstanceState) " + savedInstanceState);

        mHomeTimelinePresenter = HomeTimelinePresenter.newInstance(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)");
        View view = inflater.inflate(R.layout.fragment_timeline, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d(TAG, "onActivityCreated(Bundle savedInstanceState)");
    }

    @OnClick(R.id.post_tweet)
    public void composeTweet() {
        mTweetComposerCallback.displayTweetComposer();
    }

    @OnClick(R.id.refresh_list)
    public void refreshList() {
        mHomeTimelinePresenter.updateTimeline(getActivity());
    }

    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d(TAG, "onViewCreated (View view, Bundle savedInstanceState)");
    }

    public void onViewStateRestored(Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        Log.d(TAG, "onViewStateRestored()");
    }

    //When presenter initialisation has been completed and fragment is viewable
    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume()");
        createHomeTimeline();
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause()");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop()");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d(TAG, "onDestroyView()");
        ButterKnife.unbind(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy()");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.d(TAG, "onDetach()");
    }

    @Override
    public void displayUserTweetList(ListAdapter adapter) {
        Log.d(TAG, "displayUserTweetList(ListAdapter adapter)");
        setListAdapter(adapter);
    }

    @Override
    public void updateUserTweetList(ListAdapter adapter) {
        Log.d(TAG, "updateUserTweetList(ListAdapter adapter)");
        setListAdapter(adapter);

        TweetTimelineListAdapter tweetTimelineListAdapter = (TweetTimelineListAdapter) getListAdapter();
        tweetTimelineListAdapter.notifyDataSetChanged();
    }

    @Override
    public void displayError(String error) {
        Log.d(TAG, "Display Error: " + error);
        Toast.makeText(getActivity(),error,Toast.LENGTH_LONG).show();
    }

    @VisibleForTesting
    void createHomeTimeline() {
        Log.d(TAG, "createHomeTimeline()");
        mHomeTimelinePresenter.createTimeline(this.getActivity());
    }

    @VisibleForTesting
    HomeTimelinePresenter getHomeTimelinePresenter() {
        Log.d(TAG, "getHomeTimelinePresenter()");
        return mHomeTimelinePresenter;
    }
}