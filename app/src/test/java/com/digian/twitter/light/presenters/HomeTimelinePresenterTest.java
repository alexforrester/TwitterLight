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
public class HomeTimelinePresenterTest extends TestCase {

    @Mock
    private TimelineView mMockTimelineView;
    private HomeTimelinePresenter mClassUnderTest;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        mMockTimelineView = mock(TimelineView.class);
        mClassUnderTest = HomeTimelineSubPresenter.newInstance(mMockTimelineView);
    }

    @Test
    public void testPresenterPassesTheAdapterToTheViewToCreate() {
        mClassUnderTest.createTimeline(RuntimeEnvironment.application);
        verify(mMockTimelineView,times(1)).displayUserTweetList(any(ListAdapter.class));
    }

    @Test
    public void testPresenterPassesTheAdapterToTheViewToUpdate() {
        mClassUnderTest.updateTimeline(RuntimeEnvironment.application);
        verify(mMockTimelineView,times(1)).updateUserTweetList(any(ListAdapter.class));
    }

    @Test
    public void testPresenterPassesTheExceptionToTheViewToDisplay() {
        HomeTimelinePresenter homeTimelinePresenter = HomeTimelineSubPresenterFailure.newInstance(mMockTimelineView);
        homeTimelinePresenter.updateTimeline(RuntimeEnvironment.application);
        verify(mMockTimelineView,times(1)).displayError(any(String.class));
    }

    private static final class HomeTimelineSubPresenter extends HomeTimelinePresenter {
        private static final String TAG = HomeTimelinePresenter.class.getSimpleName();

        public static HomeTimelineSubPresenter newInstance(@NonNull TimelineView timelineView) {
            return new HomeTimelineSubPresenter(timelineView);
        }

        private HomeTimelineSubPresenter(@NonNull TimelineView timelineView) {
            super(timelineView);
            Log.d(TAG, "HomeTimelineSubPresenterImpl(@NonNull TimelineView timelineView)");
        }

        @Override
        public StatusesService getStatusesService() {
            return new TestStatusServices.TestStatusServiceSuccess();
        }
    }

    private static final class HomeTimelineSubPresenterFailure extends HomeTimelinePresenter {
        private static final String TAG = HomeTimelinePresenter.class.getSimpleName();

        public static HomeTimelineSubPresenterFailure newInstance(@NonNull TimelineView timelineView) {
            return new HomeTimelineSubPresenterFailure(timelineView);
        }

        private HomeTimelineSubPresenterFailure(@NonNull TimelineView timelineView) {
            super(timelineView);
            Log.d(TAG, "HomeTimelineSubPresenterImpl(@NonNull TimelineView timelineView)");
        }

        @Override
        public StatusesService getStatusesService() {
            return new TestStatusServices.TestStatusServiceFailure();
        }
    }
}