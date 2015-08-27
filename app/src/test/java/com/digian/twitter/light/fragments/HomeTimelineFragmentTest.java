package com.digian.twitter.light.fragments;

import android.app.ListFragment;
import android.os.Build;

import com.digian.twitter.light.BuildConfig;
import com.digian.twitter.light.CustomRobolectricRunner;
import com.digian.twitter.light.MainActivity;
import com.digian.twitter.light.OutlineShadow;
import com.digian.twitter.light.R;
import com.digian.twitter.light.views.TimelineView;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.tweetui.FixedTweetTimeline;
import com.twitter.sdk.android.tweetui.TweetTimelineListAdapter;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowLog;

import java.util.ArrayList;

import static org.robolectric.util.FragmentTestUtil.startVisibleFragment;

/**
 * Created by forrestal on 24/08/2015.
 */
@RunWith(CustomRobolectricRunner.class)
@Config(constants = BuildConfig.class, shadows = OutlineShadow.class, sdk = Build.VERSION_CODES.LOLLIPOP)
public class HomeTimelineFragmentTest extends TestCase {

    HomeTimelineFragment mClassUnderTest;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        ShadowLog.stream = System.out;
    }

    @Test
    public void fragmentIsCreatedCorrectly() throws Exception {
        mClassUnderTest = HomeTimelineSubFragment.newInstance();

        startVisibleFragment(mClassUnderTest, Robolectric.setupActivity(MainActivity.class).getClass(), R.id.fragment_container);
        assertNotNull("Fragment should not be null at this point", mClassUnderTest);
    }

    @Test
    public void onCreateCallbackSetsPresenter() throws Exception {
        mClassUnderTest = HomeTimelineSubFragment.newInstance();
        startVisibleFragment(mClassUnderTest, Robolectric.setupActivity(MainActivity.class).getClass(), R.id.fragment_container);

        assertTrue("Presenter should be non null", mClassUnderTest.getHomeTimelinePresenter() != null);
    }

    @Test
    public void testOnCreateHomeTimelineCalled() throws Exception {
        HomeTimelineSubFragment classUnderTest = HomeTimelineSubFragment.newInstance();

        assertFalse("create home time should not have been called yet", classUnderTest.isCreateHomeTimelineCalled());

        startVisibleFragment(classUnderTest,Robolectric.setupActivity(MainActivity.class).getClass(), R.id.fragment_container);
        assertTrue("home time line should now have been called", classUnderTest.isCreateHomeTimelineCalled());
    }

    @Test
    public void fragmentIsSetupAsListFragment() throws Exception {

        HomeTimelineSubFragment classUnderTest = HomeTimelineSubFragment.newInstance();
        startVisibleFragment(classUnderTest, Robolectric.setupActivity(MainActivity.class).getClass(), R.id.fragment_container);

        assertTrue("fragment should be instanceof ListFragment", classUnderTest instanceof ListFragment);
        assertNull("adapter should initially be null", classUnderTest.getListAdapter());
    }

    @Test
    public void testTimelineViewSetUpCorrectly(){

        HomeTimelineSubFragment classUnderTest = HomeTimelineSubFragment.newInstance();
        startVisibleFragment(classUnderTest, Robolectric.setupActivity(MainActivity.class).getClass(), R.id.fragment_container);

        FixedTweetTimeline fixedTweetTimeline = new FixedTweetTimeline.Builder().setTweets(new ArrayList<Tweet>()).build();
        TweetTimelineListAdapter adapter = new TweetTimelineListAdapter(RuntimeEnvironment.application, fixedTweetTimeline);

        TimelineView timelineView = ((TimelineView) classUnderTest);
        timelineView.displayUserTweetList(adapter);
        assertNotNull("adapter should now be not null", classUnderTest.getListAdapter());
    }
}