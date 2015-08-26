package com.digian.twitter.light.presenters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;

import com.digian.twitter.light.R;
import com.digian.twitter.light.views.TweetComposerView;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.core.services.StatusesService;

/**
 * Created by forrestal on 26/08/2015.
 */
public class TweetComposerPresenterImpl extends BasePresenter implements TweetComposerPresenter {
    private static final String TAG = TweetComposerPresenterImpl.class.getSimpleName();
    public static final int TWEET_MAX_LENGTH = 140;
    TweetComposerView tweetComposerView;
    Context context;

    private TweetComposerPresenterImpl(TweetComposerView tweetComposerView, Context context) {
        this.tweetComposerView = tweetComposerView;
        this.context = context;
    }

    public static TweetComposerPresenterImpl newInstance(@NonNull TweetComposerView tweetComposerView, @NonNull Context context) {
        return new TweetComposerPresenterImpl(tweetComposerView, context);
    }

    @Override
    public void createTweet(String tweet) {
        Log.d(TAG, "createTweet");

        int tweetLength = getTweetLength(tweet);

        if (tweetLength == -1)  {
            return; ////There has been an error
        }

        StatusesService statusesService = getStatusesService();
        statusesService.update(tweet.substring(0, tweetLength), null, null, null, null, null, null, null, getTweetBuilderCallback(tweetComposerView));
    }

    int getTweetLength(String tweet) {
        if (TextUtils.isEmpty(tweet.trim())) {
            tweetComposerView.tweetError(context.getString(R.string.tweet_not_entered));
            return -1;
        }

        int tweetLength = tweet.length();

        if (tweetLength > TWEET_MAX_LENGTH) {
            tweetComposerView.tweetError(context.getString(R.string.tweet_is_too_long));
            return -1;
        }

        return tweetLength;
    }

    public Callback<Tweet> getTweetBuilderCallback(TweetComposerView tweetComposerView) {
        return new TweetBuilderCallback(tweetComposerView);
    }

    private static final class TweetBuilderCallback extends Callback<Tweet> {
        private final String TAG = TweetBuilderCallback.class.getSimpleName();

        TweetComposerView tweetComposerView;

        TweetBuilderCallback(TweetComposerView tweetComposerView) {
            this.tweetComposerView = tweetComposerView;
        }

        @Override
        public void success(Result<Tweet> result) {
            tweetComposerView.tweetSent();
        }

        @Override
        public void failure(TwitterException e) {
            tweetComposerView.tweetError(e.getMessage());
        }

    }
}
