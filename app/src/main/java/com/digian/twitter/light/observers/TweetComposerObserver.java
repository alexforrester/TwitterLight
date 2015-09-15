package com.digian.twitter.light.observers;

/**
 * Created by forrestal on 14/09/2015.
 */
public interface TweetComposerObserver {
    void tweetSent();
    void tweetError(String errorMessage);
}
