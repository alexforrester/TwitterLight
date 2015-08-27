package com.digian.twitter.light.presenters;

import android.os.Build;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.ListAdapter;

import com.digian.twitter.light.BuildConfig;
import com.digian.twitter.light.CustomRobolectricRunner;
import com.digian.twitter.light.OutlineShadow;
import com.digian.twitter.light.TestStatusServices;
import com.digian.twitter.light.views.TimelineView;
import com.twitter.sdk.android.core.services.StatusesService;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

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
            return new TestStatusServices.TestStatusServiceSuccess();
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
            return new TestStatusServices.TestStatusServiceFailure();
        }
    }
}