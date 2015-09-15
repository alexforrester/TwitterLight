package com.digian.twitter.light.presenters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;
import android.util.Log;

import com.digian.twitter.light.SingleTweet;
import com.digian.twitter.light.observers.TweetComposerObserver;
import com.digian.twitter.light.subjects.TweetComposerSubject;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.core.services.StatusesService;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by forrestal on 26/08/2015.
 * Handle interaction between model and view on the Tweet Composer screen
 */
public class TweetComposerPresenter extends BasePresenter implements TweetComposerSubject {
    private static final String TAG = TweetComposerPresenter.class.getSimpleName();

    Context mContext;

    List<TweetComposerObserver> mTweetComposerObservers;

    public static TweetComposerPresenter newInstance(@NonNull Context context) {
        return new TweetComposerPresenter(context);
    }

    TweetComposerPresenter(Context context) {
        mContext = context;
        mTweetComposerObservers = new ArrayList<TweetComposerObserver>();
    }

    @Override
    public void registerObserver(TweetComposerObserver tweetComposerObserver) {
        Log.d(TAG,"registerObserver(TweetComposerObserver tweetComposerObserver)");
        mTweetComposerObservers.add(tweetComposerObserver);
    }

    @Override
    public void removeObserver(TweetComposerObserver tweetComposerObserver) {
        int i = mTweetComposerObservers.indexOf(tweetComposerObserver);
        if (i >= 0) {
            Log.d(TAG,"removeObserver(TweetComposerObserver tweetComposerObserver)");
            mTweetComposerObservers.remove(i);
        }
    }

    @Override
    public void notifyTweetSent() {
        if (mTweetComposerObservers != null) {
            for (TweetComposerObserver tweetComposerObserver : mTweetComposerObservers) {
                Log.d(TAG,"notifyTweetSent()");
                tweetComposerObserver.tweetSent();
            }
        }
    }

    @Override
    public void notifyTweetError(String errorMessage) {
        if (mTweetComposerObservers != null) {
            for (TweetComposerObserver tweetComposerObserver : mTweetComposerObservers) {
                Log.d(TAG,"notifyTweetError(String errorMessage)");
                tweetComposerObserver.tweetError(errorMessage);
            }
        }
    }

    public void createTweet(String tweetContent) {
        Log.d(TAG, "createTweet");

        SingleTweet tweet = SingleTweet.newInstance(tweetContent, mContext);

        if(tweet.hasTweetError()) {
            notifyTweetError(tweet.getTweetErrorMessage());
            return;
        }

        String tweetContents = tweet.getTweetContents();

        StatusesService statusesService = getStatusesService();
        statusesService.update(tweetContents.substring(0, tweetContents.length()), null, null, null, null, null, null, null, getTweetBuilderCallback());
    }

    Callback<Tweet> getTweetBuilderCallback() {
        return new TweetBuilderCallback(this);
    }

    @VisibleForTesting
    Context getContext() {
        return mContext;
    }

    @VisibleForTesting
    List<TweetComposerObserver> getTweetComposerObservers() {
        return mTweetComposerObservers;
    }

    private static final class TweetBuilderCallback extends Callback<Tweet> {
        private final String TAG = TweetBuilderCallback.class.getSimpleName();

        TweetComposerSubject mTweetComposerSubject;

        TweetBuilderCallback(TweetComposerSubject tweetComposerSubject) {
            mTweetComposerSubject = tweetComposerSubject;
        };

        @Override
        public void success(Result<Tweet> result) {
            Log.d(TAG,"Tweet has been sent");
            mTweetComposerSubject.notifyTweetSent();
        }

        @Override
        public void failure(TwitterException e) {
            Log.e(TAG,"Twitter exception thrown",e);
            mTweetComposerSubject.notifyTweetError(e.getMessage());
        }
    }
}
