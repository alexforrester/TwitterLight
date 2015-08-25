package com.digian.twitter.light.fragments;

import android.app.Activity;
import android.app.ListFragment;
import android.os.Build;
import android.os.Bundle;

import com.digian.twitter.light.BuildConfig;
import com.digian.twitter.light.CustomRobolectricRunner;
import com.digian.twitter.light.OutlineShadow;
import com.digian.twitter.light.presenters.HomeTimelinePresenter;
import com.digian.twitter.light.views.TimelineView;
import com.twitter.sdk.android.tweetui.TweetViewAdapter;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowLog;

import static org.robolectric.util.FragmentTestUtil.startFragment;
import static org.robolectric.util.FragmentTestUtil.startVisibleFragment;

/**
 * Created by forrestal on 24/08/2015.
 */
@RunWith(CustomRobolectricRunner.class)
@Config(constants = BuildConfig.class, shadows = OutlineShadow.class, sdk = Build.VERSION_CODES.LOLLIPOP)
public class HomeTimelineFragmentTest extends TestCase {

    private static final String A_SAMPLE_STRING_KEY = "A_SAMPLE_STRING_KEY";
    private static final String A_SAMPLE_STRING_VALUE = "A_SAMPLE_STRING_VALUE";
    private static final String FRAGMENT_TAG = "UserTimelineFragment";

    HomeTimelineFragment mClassUnderTest;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        ShadowLog.stream = System.out;
    }

    @Test
    public void fragmentIsCreatedCorrectly() throws Exception {
        mClassUnderTest = HomeTimelineSubFragment.newInstance(new Bundle());

        startVisibleFragment(mClassUnderTest);
        assertNotNull("Fragment should not be null at this point", mClassUnderTest);
    }

    @Test
    public void callingUserTimelineFragmentNewInstanceSetsFragmentArgumentsCorrectly() throws Exception {
        Bundle bundle = new Bundle();
        bundle.putString(A_SAMPLE_STRING_KEY, A_SAMPLE_STRING_VALUE);

        mClassUnderTest = HomeTimelineFragment.newInstance(bundle);

        assertEquals("key A_SAMPLE_STRING_KEY should have been set in the bundle", A_SAMPLE_STRING_VALUE, mClassUnderTest.getArguments().getString(A_SAMPLE_STRING_KEY));
    }

    @Test
    public void onCreateCallbackSetsPresenter() throws Exception {
        mClassUnderTest = HomeTimelineSubFragment.newInstance(new Bundle());
        startVisibleFragment(mClassUnderTest);

        assertTrue("Presenter should be non null", mClassUnderTest.getHomeTimelinePresenter() != null);
    }

    @Test
    public void testOnCreateHomeTimelineCalled() throws Exception {
        HomeTimelineSubFragment classUnderTest = HomeTimelineSubFragment.newInstance(new Bundle());

        assertFalse("create home time should not have been called yet", classUnderTest.isCreateHomeTimelineCalled());

        startVisibleFragment(classUnderTest);
        assertTrue("home time line should now have been called", classUnderTest.isCreateHomeTimelineCalled());
    }

    @Test
    public void verifyOnActivityCreatedPassesSameBundleToPresenterInitMethod() throws Exception {
        Bundle bundle = new Bundle();
        HomeTimelineSubFragment classUnderTest = HomeTimelineSubFragment.newInstance(bundle);
        startFragment(classUnderTest);
        assertSame("The bundle passed into HomeTimeline should be the same as passed to the presenter", bundle, classUnderTest.getTestBundle());
    }

    @Test
    public void testFragmentSavesAndRestoresBundleData() throws Exception {

        Bundle bundle = new Bundle();
        bundle.putString(HomeTimelinePresenter.TWITTER_SESSION_USER_NAME, "username");
        bundle.putLong(HomeTimelinePresenter.TWITTER_SESSION_USER_ID, 1L);

        //Creating subclass to get round issues with activity.recreate() ignoring mocks set on previous fragment etc.
        HomeTimelineSubFragment classUnderTest = HomeTimelineSubFragment.newInstance(bundle);

        startVisibleFragment(classUnderTest);
        Activity activity = classUnderTest.getActivity();

        activity.recreate();

        HomeTimelineFragment recreatedFragment = ((HomeTimelineSubFragment)activity.getFragmentManager().findFragmentById(1));

        assertNotNull("fragment should be recreated correctly",recreatedFragment);
        assertNotSame("the two fragments although having same restored data should be two distinct objects", classUnderTest, recreatedFragment);
        assertSame("The arguments/savedInstanceState of two objects should be the same", classUnderTest.getArguments(), recreatedFragment.getArguments());

        assertSame("Twitter username should be the same", classUnderTest.getArguments().getString(HomeTimelinePresenter.TWITTER_SESSION_USER_NAME), recreatedFragment.getArguments().getString(HomeTimelinePresenter.TWITTER_SESSION_USER_NAME));
        assertSame("Twitter user id should be the same", classUnderTest.getArguments().getLong(HomeTimelinePresenter.TWITTER_SESSION_USER_ID), recreatedFragment.getArguments().getLong(HomeTimelinePresenter.TWITTER_SESSION_USER_ID));
    }

    @Test
    public void fragmentIsSetupAsListFragment() throws Exception {

        HomeTimelineSubFragment classUnderTest = HomeTimelineSubFragment.newInstance(new Bundle());
        startVisibleFragment(classUnderTest);

        assertTrue("fragment should be instanceof ListFragment", classUnderTest instanceof ListFragment);
        assertNull("adapter should initially be null", classUnderTest.getListAdapter());
    }

    @Test
    public void testTimelineViewSetUpCorrectly(){

        HomeTimelineSubFragment classUnderTest = HomeTimelineSubFragment.newInstance(new Bundle());
        startVisibleFragment(classUnderTest);

        TimelineView timelineView = ((TimelineView) classUnderTest);
        timelineView.displayUserTweetList( new TweetViewAdapter(RuntimeEnvironment.application));
        assertNotNull("adapter should now be not null", classUnderTest.getListAdapter());
    }
}