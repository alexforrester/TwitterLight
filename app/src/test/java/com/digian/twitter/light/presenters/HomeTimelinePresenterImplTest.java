package com.digian.twitter.light.presenters;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.ListAdapter;

import com.digian.twitter.light.BuildConfig;
import com.digian.twitter.light.CustomRobolectricRunner;
import com.digian.twitter.light.OutlineShadow;
import com.digian.twitter.light.views.TimelineView;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.core.services.StatusesService;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * Created by forrestal on 24/08/2015.
 */
@RunWith(CustomRobolectricRunner.class)
@Config(constants = BuildConfig.class, shadows = OutlineShadow.class, sdk = Build.VERSION_CODES.LOLLIPOP)
public class HomeTimelinePresenterImplTest extends TestCase {

    public static final String USERNAME = "username";
    public static final long VALUE = 1L;
    public static final String SOME_TWITTER_EXCEPTION = "some twitter exception";

    @Mock
    private TimelineView mockTimelineView;

    private HomeTimelinePresenter mClassUnderTest;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        mClassUnderTest = HomeTimelinePresenterImpl.newInstance(mockTimelineView);
    }

    @Test
    public void testNewInstanceIsSetUpCorrectlyWithTimelineViewSet() throws Exception {
        assertNotNull("timeline view should have been created now", ((HomeTimelinePresenterImpl) mClassUnderTest).getTimelineView() != null);
        assertEquals("Timeline view should be same as passed in", mockTimelineView, ((HomeTimelinePresenterImpl)mClassUnderTest).getTimelineView());
    }

    @Test
    public void testUserDetailsSetCorrectly() throws Exception {

        Bundle bundle = new Bundle();
        bundle.putString(HomeTimelinePresenter.TWITTER_SESSION_USER_NAME, USERNAME);
        bundle.putLong(HomeTimelinePresenter.TWITTER_SESSION_USER_ID, VALUE);
        mClassUnderTest.init(bundle);

        assertEquals("User id should not be default value of 0L", 1L, ((HomeTimelinePresenterImpl) mClassUnderTest).getUserId());
        assertNotNull("User name should not be null", ((HomeTimelinePresenterImpl) mClassUnderTest).getUserName());
        assertEquals("User name should be same as set from bundle", USERNAME, ((HomeTimelinePresenterImpl)mClassUnderTest).getUserName());
    }

    @Test
    public void testPresenterPassesTheAdapterToTheViewToCreate() {

        TimelineView timelineView = mock(TimelineView.class);
        HomeTimelinePresenter homeTimelinePresenter = HomeTimelineSubPresenterImpl.newInstance(timelineView);

        homeTimelinePresenter.createTimeline(RuntimeEnvironment.application);

        verify(timelineView,times(1)).displayUserTweetList(any(ListAdapter.class));

    }

    @Test
    public void testPresenterPassesTheAdapterToTheViewToUpdate() {

        TimelineView timelineView = mock(TimelineView.class);
        HomeTimelinePresenter homeTimelinePresenter = HomeTimelineSubPresenterImpl.newInstance(timelineView);

        homeTimelinePresenter.updateTimeline(RuntimeEnvironment.application);

        verify(timelineView,times(1)).updateUserTweetList(any(ListAdapter.class));
    }

    @Test
    public void testPresenterPassesTheExcepptionToTheViewToDisplay() {

        TimelineView timelineView = mock(TimelineView.class);
        HomeTimelinePresenter homeTimelinePresenter = HomeTimelineSubPresenterImplFailure.newInstance(timelineView);

        homeTimelinePresenter.updateTimeline(RuntimeEnvironment.application);

        verify(timelineView,times(1)).displayError(any(String.class));
    }

    private static final class HomeTimelineSubPresenterImpl extends HomeTimelinePresenterImpl{
        private static final String TAG = HomeTimelinePresenterImpl.class.getSimpleName();

        public static HomeTimelineSubPresenterImpl newInstance(@NonNull TimelineView timelineView) {
            return new HomeTimelineSubPresenterImpl(timelineView);
        }

        private HomeTimelineSubPresenterImpl(@NonNull TimelineView timelineView) {
            super(timelineView);
            Log.d(TAG, "HomeTimelineSubPresenterImpl(@NonNull TimelineView timelineView)");
        }

        @Override
        public StatusesService getStatusesService() {
            return new TestStatusServiceSuccess();
        }
    }

    private static final class HomeTimelineSubPresenterImplFailure extends HomeTimelinePresenterImpl{
        private static final String TAG = HomeTimelinePresenterImpl.class.getSimpleName();

        public static HomeTimelineSubPresenterImplFailure newInstance(@NonNull TimelineView timelineView) {
            return new HomeTimelineSubPresenterImplFailure(timelineView);
        }

        private HomeTimelineSubPresenterImplFailure(@NonNull TimelineView timelineView) {
            super(timelineView);
            Log.d(TAG, "HomeTimelineSubPresenterImpl(@NonNull TimelineView timelineView)");
        }

        @Override
        public StatusesService getStatusesService() {
            return new TestStatusServiceFailure();
        }
    }

    private static final class TestStatusServiceFailure implements StatusesService
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

    private static final class TestStatusServiceSuccess implements StatusesService
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