package com.digian.twitter.light.presenters;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;

import com.digian.twitter.light.views.TimelineView;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.core.services.StatusesService;
import com.twitter.sdk.android.tweetui.TweetViewAdapter;

import java.util.List;

/**
 * Created by forrestal on 24/08/2015.
 */
public class HomeTimelinePresenterImpl implements HomeTimelinePresenter {

    private static final String TAG = HomeTimelinePresenterImpl.class.getSimpleName();

    private long mUserId;
    private String mUserName;
    private TimelineView timelineView;

    /**
     * Create new UserTimelinePresenter
     * @param timelineView
     * @return
     */
    public static HomeTimelinePresenterImpl newInstance(@NonNull TimelineView timelineView) {
        return new HomeTimelinePresenterImpl(timelineView);
    }

    /**
     * Private Constructor used by factory method which creates object setting timeLineView
     * @param timelineView
     */
    private HomeTimelinePresenterImpl(@NonNull TimelineView timelineView){
        Log.d(TAG, "UserTimelinePresenterImpl(@NonNull TimelineView timelineView)");
        this.timelineView = timelineView;
    }

    /**
     * Set user details up
     *
     */
    @Override
    public void init(@NonNull Bundle bundle) {
        Log.d(TAG, "init(@NonNull Bundle bundle)");
        setUserDetails(bundle);
    }

    /**
     *  Create Timeline
     *  @param context - used by TweetViewAdapter
     */
    @Override
    public void createTimeline(final Context context) {
        Log.d(TAG, "createTimeline(Context context)");

        StatusesService statusesService = TwitterCore.getInstance().getApiClient().getStatusesService();
        final int count = 21;
        //The count specified is up to the amount of the specified count number. In practice it returns -1 (less than) the specified count number
        statusesService.homeTimeline(count, null, null, null, null, null, null, new Callback<List<Tweet>>() {
                    @Override
                    public void success(Result<List<Tweet>> result) {
                        Log.d(TAG, "success(Result<List<Tweet>> result)");

                        //Suggested to use 'FixedTweetTime' however this class is not in the Fabric sdk although it is mentioned here:
                        // - http://docs.fabric.io/android/twitter/show-tweets.html#tweet-list-adapters
                        final TweetViewAdapter adapter = new TweetViewAdapter(context);
                        adapter.setTweets(result.data);
                        timelineView.displayUserTweetList(adapter);
                    }

                    @Override
                    public void failure(TwitterException exception) {
                        Log.e(TAG, "failure(TwitterException exception)", exception);
                    }
                }
        );

    }


    /**
     * Setting bundle of user details (userId and userName (Twitter screenName)
     * @param bundle
     */
    private void setUserDetails(@NonNull Bundle bundle) {
        Log.d(TAG, "setUserDetails(@NonNull Bundle bundle)");
        if (bundle != null) {
            mUserId = bundle.getLong(TWITTER_SESSION_USER_ID);
            mUserName = bundle.getString(TWITTER_SESSION_USER_NAME);
        }
    }

    public long getUserId() {
        return mUserId;
    }

    public String getUserName() {
        return mUserName;
    }

    public TimelineView getTimelineView() {
        return timelineView;
    }
}