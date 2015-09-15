package com.digian.twitter.light.subjects;

import com.digian.twitter.light.observers.TweetComposerObserver;

/**
 * Created by forrestal on 15/09/2015.
 */
public interface TweetComposerSubject {
        void registerObserver(TweetComposerObserver tweetComposerObserver);
        void removeObserver(TweetComposerObserver tweetComposerObserver);
        void notifyTweetSent();
        void notifyTweetError(String errorMessage);
}
