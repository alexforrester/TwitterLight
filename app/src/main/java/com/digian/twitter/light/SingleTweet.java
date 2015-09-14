package com.digian.twitter.light;

import android.content.Context;
import android.support.annotation.Nullable;
import android.text.TextUtils;

/**
 * Created by forrestal on 07/09/2015.
 */
public class SingleTweet {

    public static final int TWEET_MAX_LENGTH = 140;

    private final String mTweetContents;
    private final Context mContext;
    private String errorMessage;

    public static SingleTweet newInstance(String tweetContents, Context context) {
     return new SingleTweet(tweetContents, context);
    }

    private SingleTweet(String tweetContents, Context context) {
        mTweetContents = tweetContents;
        mContext = context;
    }

    public boolean hasTweetError() {

        if (TextUtils.isEmpty(mTweetContents)) {
            errorMessage = mContext.getResources().getString(R.string.tweet_not_entered);
            return true;
        }

        if (mTweetContents.length() > TWEET_MAX_LENGTH) {
            errorMessage = mContext.getResources().getString(R.string.tweet_is_too_long);
            return true;
        }

        return false;
    }

    @Nullable
    public String getTweetErrorMessage() {
        return errorMessage;
    }

    public String getTweetContents() {
        return mTweetContents;
    }
}