package com.digian.twitter.light.presenters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.ListAdapter;

import com.digian.twitter.light.R;
import com.digian.twitter.light.observers.TimelineObserver;
import com.digian.twitter.light.subjects.HomeTimelineSubject;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.core.services.StatusesService;
import com.twitter.sdk.android.tweetui.FixedTweetTimeline;
import com.twitter.sdk.android.tweetui.TweetTimelineListAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by forrestal on 24/08/2015.
 * Handle interaction between model and view in the home timeline
 */
public class HomeTimelinePresenter extends BasePresenter implements HomeTimelineSubject {

    private static final String TAG = HomeTimelinePresenter.class.getSimpleName();
    private static final int TWEET_LIST_LENGTH = 21;

    List<TimelineObserver> mTimelineObservers;

    public static HomeTimelinePresenter newInstance() {
        return new HomeTimelinePresenter();
    }

    HomeTimelinePresenter() {
        Log.d(TAG, "HomeTimelinePresenter()");
        mTimelineObservers = new ArrayList<TimelineObserver>();
    }

    public void createTimeline(final Context context) {
        Log.d(TAG, "createTimeline(final Context mContext)");
        pollTimeline(context, false);
    }

    public void updateTimeline(final Context context) {
        Log.d(TAG, "updateTimeline(final Context context)");
        pollTimeline(context, true);
    }

    @Override
    public void registerObserver(TimelineObserver timelineObserver) {
        Log.d(TAG,"registerObserver(TimelineObserver timelineObserver)");
        mTimelineObservers.add(timelineObserver);
    }

    @Override
    public void removeObserver(TimelineObserver timelineObserver) {
        int i = mTimelineObservers.indexOf(timelineObserver);
        if (i >= 0) {
            Log.d(TAG,"removeObserver(TimelineObserver timelineObserver)");
            mTimelineObservers.remove(i);
        }
    }

    @Override
    public void notifyUserTweetListCreated(ListAdapter adapter) {
        if (mTimelineObservers != null) {
            for (TimelineObserver timelineObserver : mTimelineObservers) {
                Log.d(TAG,"notifyUserTweetListCreated(ListAdapter adapter)");
                timelineObserver.createUserTweetList(adapter);
            }
        }
    }

    @Override
    public void notifyUserTweetListUpdated(ListAdapter adapter) {
        if (mTimelineObservers != null) {
            for (TimelineObserver timelineObserver : mTimelineObservers) {
                Log.d(TAG,"notifyUserTweetListUpdated(ListAdapter adapter)");
                timelineObserver.updateUserTweetList(adapter);
            }
        }
    }

    @Override
    public void notifyError(String errorMessage) {
        if (mTimelineObservers != null) {
            for (TimelineObserver timelineObserver : mTimelineObservers) {
                Log.d(TAG,"notifyError(String errorMessage)");
                timelineObserver.processError(errorMessage);
            }
        }
    }

    private void pollTimeline(Context context, boolean isUpdate) {
        StatusesService statusesService = getStatusesService();
        //The count specified is up to the amount of the specified count number. In practice it returns -1 (less than) the specified count number
        statusesService.homeTimeline(TWEET_LIST_LENGTH, null, null, null, null, null, null, getTwitterCallback(context, isUpdate));
    }

    @NonNull
    private TwitterCallback getTwitterCallback(@NonNull Context context, boolean isUpdate) {
        return new TwitterCallback(context, isUpdate, this);
    }

    private static final class TwitterCallback extends Callback<List<Tweet>> {

        private final String TAG = TwitterCallback.class.getSimpleName();

        private Context mContext;
        private boolean mIsUpdate;
        private HomeTimelineSubject mHomeTimelineSubject;

        TwitterCallback(Context context, boolean isUpdate, HomeTimelineSubject homeTimelineSubject) {
            mContext = context;
            mIsUpdate = isUpdate;
            mHomeTimelineSubject = homeTimelineSubject;
        }

        @Override
        public void success(Result<List<Tweet>> result) {
            Log.d(TAG, "success(Result<List<Tweet>> result)");

            List<Tweet> tweets = result.data;

            if (result == null || result.data == null) {
                Log.d(TAG, "Tweets is null");
                mHomeTimelineSubject.notifyError(mContext.getResources().getString(R.string.no_tweet_data_returned));
                return;
            }

            FixedTweetTimeline fixedTweetTimeline = new FixedTweetTimeline.Builder().setTweets(tweets).build();
            TweetTimelineListAdapter adapter = new TweetTimelineListAdapter(mContext, fixedTweetTimeline);

            if (mIsUpdate) {
                Log.d(TAG, "update user tweet list");
                mHomeTimelineSubject.notifyUserTweetListUpdated(adapter);
            } else {
                Log.d(TAG, "create user tweet list");
                mHomeTimelineSubject.notifyUserTweetListCreated(adapter);
            }

            resetMembers();
        }

        @Override
        public void failure(TwitterException exception) {
            Log.e(TAG, "failure(TwitterException exception)", exception);
            mHomeTimelineSubject.notifyError(exception.getMessage());
            resetMembers();
        }

        private void resetMembers() {
            mContext = null;
            mHomeTimelineSubject = null;
        }
    }
}