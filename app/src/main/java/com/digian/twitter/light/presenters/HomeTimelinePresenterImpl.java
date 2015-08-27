package com.digian.twitter.light.presenters;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;

import com.digian.twitter.light.views.TimelineView;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.core.services.StatusesService;
import com.twitter.sdk.android.tweetui.TweetViewAdapter;

import java.util.List;

/**
 * Created by forrestal on 24/08/2015.
 * Handle interaction between model and view in the home timeline
 */
public class HomeTimelinePresenterImpl extends BasePresenter implements HomeTimelinePresenter {

    private static final String TAG = HomeTimelinePresenterImpl.class.getSimpleName();

    private long mUserId;
    private String mUserName;
    private TimelineView timelineView;

    /**
     * Create new UserTimelinePresenter
     *
     * @param timelineView
     * @return
     */
    public static HomeTimelinePresenterImpl newInstance(@NonNull TimelineView timelineView) {
        return new HomeTimelinePresenterImpl(timelineView);
    }

    /**
     * Private Constructor used by factory method which creates object setting timeLineView
     *
     * @param timelineView
     */
    HomeTimelinePresenterImpl(@NonNull TimelineView timelineView) {
        Log.d(TAG, "UserTimelinePresenterImpl(@NonNull TimelineView timelineView)");
        this.timelineView = timelineView;
    }

    /**
     * Set user details up
     */
    @Override
    public void init(@NonNull Bundle bundle) {
        Log.d(TAG, "init(@NonNull Bundle bundle)");
        setUserDetails(bundle);
    }

    /**
     * Create Timeline
     *
     * @param context - used by TweetViewAdapter
     */
    @Override
    public void createTimeline(final Context context) {
        Log.d(TAG, "createTimeline(Context context)");

        StatusesService statusesService = getStatusesService();
        //The count specified is up to the amount of the specified count number. In practice it returns -1 (less than) the specified count number
        final int count = 21;
        statusesService.homeTimeline(count, null, null, null, null, null, null, getTwitterCallback(context, false));
    }

    /**
     * Update Timeline
     * @param context -used by TweetViewAdapter
     */
    @Override
    public void updateTimeline(final Context context) {
        Log.d(TAG, "createTimeline(Context context)");

        StatusesService statusesService = getStatusesService();
        //The count specified is up to the amount of the specified count number. In practice it returns -1 (less than) the specified count number
        final int count = 21;
        statusesService.homeTimeline(count, null, null, null, null, null, null, getTwitterCallback(context, true));
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

    /**
     * Setting bundle of user details (userId and userName (Twitter screenName))
     *
     * @param bundle
     */
    private void setUserDetails(@NonNull Bundle bundle) {
        Log.d(TAG, "setUserDetails(@NonNull Bundle bundle)");
        if (bundle != null) {
            mUserId = bundle.getLong(TWITTER_SESSION_USER_ID);
            mUserName = bundle.getString(TWITTER_SESSION_USER_NAME);
        }
    }

    @NonNull
    private TwitterCallback getTwitterCallback(@NonNull Context context, boolean isUpdate) {
        return new TwitterCallback(context, isUpdate, timelineView);
    }

    private static final class TwitterCallback extends Callback<List<Tweet>> {

        private final String TAG = TwitterCallback.class.getSimpleName();

        private Context context;
        private boolean isUpdate;
        private TimelineView timelineView;

        TwitterCallback(Context context, boolean isUpdate, TimelineView timelineView) {
            this.context = context;
            this.isUpdate = isUpdate;
            this.timelineView = timelineView;
        }

        @Override
        public void success(Result<List<Tweet>> result) {
            Log.d(TAG, "success(Result<List<Tweet>> result)");
            final TweetViewAdapter adapter = getTweetViewAdapter(result);

            if (isUpdate) {
                Log.d(TAG, "update user tweet list");
                timelineView.updateUserTweetList(adapter);
            } else {
                Log.d(TAG, "create user tweet list");
                timelineView.displayUserTweetList(adapter);
            }

            resetMembers();
        }

        @NonNull
        TweetViewAdapter getTweetViewAdapter(@NonNull Result<List<Tweet>> result) {
            //Suggested to use 'FixedTweetTime' however this class is not in the Fabric sdk although it is mentioned here:
            // - http://docs.fabric.io/android/twitter/show-tweets.html#tweet-list-adapters
            final TweetViewAdapter adapter = new TweetViewAdapter(context);

            if (result != null) {
                Log.e(TAG,"Adapter set with tweet data");
                adapter.setTweets(result.data);
            }
            else
                Log.e(TAG,"Adapter passed to getTweetViewAdapter is null");

            return adapter;
        }

        @Override
        public void failure(TwitterException exception) {
            Log.e(TAG, "failure(TwitterException exception)", exception);
            timelineView.displayError(exception.getMessage());
            resetMembers();
        }

        private void resetMembers() {
            context = null;
            timelineView = null;
        }
    }
}