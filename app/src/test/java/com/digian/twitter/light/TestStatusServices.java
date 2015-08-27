package com.digian.twitter.light;

import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.core.services.StatusesService;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.mock;

/**
 * Created by forrestal on 27/08/2015.
 */
public class TestStatusServices {

    public static final String SOME_TWITTER_EXCEPTION = "some twitter exception";

    public static class TestStatusServiceFailure implements StatusesService
    {

        @Override
        public void mentionsTimeline(Integer integer, Long aLong, Long aLong1, Boolean aBoolean, Boolean aBoolean1, Boolean aBoolean2, Callback<List<Tweet>> callback) {

        }

        @Override
        public void userTimeline(Long aLong, String s, Integer integer, Long aLong1, Long aLong2, Boolean aBoolean, Boolean aBoolean1, Boolean aBoolean2, Boolean aBoolean3, Callback<List<Tweet>> callback) {

        }

        @Override
        public void homeTimeline(Integer integer, Long aLong, Long aLong1, Boolean aBoolean, Boolean aBoolean1, Boolean aBoolean2, Boolean aBoolean3, Callback<List<Tweet>> callback) {
            TwitterException twitterException = new TwitterException(SOME_TWITTER_EXCEPTION);
            callback.failure(twitterException);
        }

        @Override
        public void retweetsOfMe(Integer integer, Long aLong, Long aLong1, Boolean aBoolean, Boolean aBoolean1, Boolean aBoolean2, Callback<List<Tweet>> callback) {

        }

        @Override
        public void show(Long aLong, Boolean aBoolean, Boolean aBoolean1, Boolean aBoolean2, Callback<Tweet> callback) {

        }

        @Override
        public void lookup(String s, Boolean aBoolean, Boolean aBoolean1, Boolean aBoolean2, Callback<List<Tweet>> callback) {

        }

        @Override
        public void update(String s, Long aLong, Boolean aBoolean, Double aDouble, Double aDouble1, String s1, Boolean aBoolean1, Boolean aBoolean2, Callback<Tweet> callback) {
            TwitterException twitterException = new TwitterException(SOME_TWITTER_EXCEPTION);
            callback.failure(twitterException);
        }

        @Override
        public void retweet(Long aLong, Boolean aBoolean, Callback<Tweet> callback) {

        }

        @Override
        public void destroy(Long aLong, Boolean aBoolean, Callback<Tweet> callback) {

        }

        @Override
        public void unretweet(Long aLong, Boolean aBoolean, Callback<Tweet> callback) {

        }
    }

    public static class TestStatusServiceSuccess implements StatusesService
    {

        @Override
        public void mentionsTimeline(Integer integer, Long aLong, Long aLong1, Boolean aBoolean, Boolean aBoolean1, Boolean aBoolean2, Callback<List<Tweet>> callback) {

        }

        @Override
        public void userTimeline(Long aLong, String s, Integer integer, Long aLong1, Long aLong2, Boolean aBoolean, Boolean aBoolean1, Boolean aBoolean2, Boolean aBoolean3, Callback<List<Tweet>> callback) {

        }

        @Override
        public void homeTimeline(Integer integer, Long aLong, Long aLong1, Boolean aBoolean, Boolean aBoolean1, Boolean aBoolean2, Boolean aBoolean3, Callback<List<Tweet>> callback) {
            List<Tweet> tweets = new ArrayList<Tweet>();
            Result result = new Result(tweets,null);
            callback.success(result);
        }

        @Override
        public void retweetsOfMe(Integer integer, Long aLong, Long aLong1, Boolean aBoolean, Boolean aBoolean1, Boolean aBoolean2, Callback<List<Tweet>> callback) {

        }

        @Override
        public void show(Long aLong, Boolean aBoolean, Boolean aBoolean1, Boolean aBoolean2, Callback<Tweet> callback) {

        }

        @Override
        public void lookup(String s, Boolean aBoolean, Boolean aBoolean1, Boolean aBoolean2, Callback<List<Tweet>> callback) {

        }

        @Override
        public void update(String s, Long aLong, Boolean aBoolean, Double aDouble, Double aDouble1, String s1, Boolean aBoolean1, Boolean aBoolean2, Callback<Tweet> callback) {
            Tweet tweet = mock(Tweet.class);
            Result result = new Result(tweet,null);
            callback.success(result);
        }

        @Override
        public void retweet(Long aLong, Boolean aBoolean, Callback<Tweet> callback) {

        }

        @Override
        public void destroy(Long aLong, Boolean aBoolean, Callback<Tweet> callback) {

        }

        @Override
        public void unretweet(Long aLong, Boolean aBoolean, Callback<Tweet> callback) {

        }
    }
}
