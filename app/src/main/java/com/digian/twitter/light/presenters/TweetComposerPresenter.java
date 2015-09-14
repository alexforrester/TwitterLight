package com.digian.twitter.light.presenters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;
import android.util.Log;

import com.digian.twitter.light.SingleTweet;
import com.digian.twitter.light.views.TweetComposerView;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.core.services.StatusesService;

/**
 * Created by forrestal on 26/08/2015.
 * Handle interaction between model and view on the Tweet Composer screen
 */
public class TweetComposerPresenter extends BasePresenter {
    private static final String TAG = TweetComposerPresenter.class.getSimpleName();

    TweetComposerView mTweetComposerView;
    Context mContext;

    TweetComposerPresenter(TweetComposerView tweetComposerView, Context context) {
        this.mTweetComposerView = tweetComposerView;
        this.mContext = context;
    }

    public static TweetComposerPresenter newInstance(@NonNull TweetComposerView tweetComposerView, @NonNull Context context) {
        return new TweetComposerPresenter(tweetComposerView, context);
    }

    public void createTweet(String tweetContent) {
        Log.d(TAG, "createTweet");

        SingleTweet tweet = SingleTweet.newInstance(tweetContent, mContext);

        if(tweet.hasTweetError()) {
            mTweetComposerView.tweetError(tweet.getTweetErrorMessage());
            return;
        }

        String tweetContents = tweet.getTweetContents();

        StatusesService statusesService = getStatusesService();
        statusesService.update(tweetContents.substring(0, tweetContents.length()), null, null, null, null, null, null, null, getTweetBuilderCallback(mTweetComposerView));
    }

    Callback<Tweet> getTweetBuilderCallback(TweetComposerView tweetComposerView) {
        return new TweetBuilderCallback(tweetComposerView);
    }

    @VisibleForTesting
    TweetComposerView getTweetComposerView() {
        return mTweetComposerView;
    }

    @VisibleForTesting
    Context getmContext() {
        return mContext;
    }

    private static final class TweetBuilderCallback extends Callback<Tweet> {
        private final String TAG = TweetBuilderCallback.class.getSimpleName();

        TweetComposerView tweetComposerView;

        TweetBuilderCallback(TweetComposerView tweetComposerView) {
            this.tweetComposerView = tweetComposerView;
        }

        @Override
        public void success(Result<Tweet> result) {
            Log.d(TAG,"Tweet has been sent");
            tweetComposerView.tweetSent();
        }

        @Override
        public void failure(TwitterException e) {
            Log.e(TAG,"Twitter exception thrown",e);
            tweetComposerView.tweetError(e.getMessage());
        }
    }
}
