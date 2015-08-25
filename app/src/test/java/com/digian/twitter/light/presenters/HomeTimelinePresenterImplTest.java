package com.digian.twitter.light.presenters;

import android.os.Build;
import android.os.Bundle;

import com.digian.twitter.light.BuildConfig;
import com.digian.twitter.light.CustomRobolectricRunner;
import com.digian.twitter.light.OutlineShadow;
import com.digian.twitter.light.views.TimelineView;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.robolectric.annotation.Config;

/**
 * Created by forrestal on 24/08/2015.
 */
@RunWith(CustomRobolectricRunner.class)
@Config(constants = BuildConfig.class, shadows = OutlineShadow.class, sdk = Build.VERSION_CODES.LOLLIPOP)
public class HomeTimelinePresenterImplTest extends TestCase {

    @Mock
    private TimelineView timelineView;
    private HomeTimelinePresenter mClassUnderTest;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        mClassUnderTest = HomeTimelinePresenterImpl.newInstance(timelineView);
    }

    @Test
    public void testNewInstanceIsSetUpCorrectlyWithTimelineViewSet() throws Exception {
        assertNotNull("timeline view should have been created now",((HomeTimelinePresenterImpl)mClassUnderTest).getTimelineView() != null);
        assertEquals("Timeline view should be same as passed in", timelineView, ((HomeTimelinePresenterImpl)mClassUnderTest).getTimelineView());
    }

    @Test
    public void testUserDetailsSetCorrectly() throws Exception {

        Bundle bundle = new Bundle();
        bundle.putString(HomeTimelinePresenter.TWITTER_SESSION_USER_NAME, "username");
        bundle.putLong(HomeTimelinePresenter.TWITTER_SESSION_USER_ID, 1L);
        mClassUnderTest.init(bundle);

        assertEquals("User id should not be default value of 0L", 1L, ((HomeTimelinePresenterImpl) mClassUnderTest).getUserId());
        assertNotNull("User name should not be null", ((HomeTimelinePresenterImpl) mClassUnderTest).getUserName());
        assertEquals("User name should not be same as set from bundle", "username", ((HomeTimelinePresenterImpl)mClassUnderTest).getUserName());
    }
}