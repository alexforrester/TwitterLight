package com.digian.twitter.light.presenters;

import android.os.Build;
import android.util.Log;
import android.widget.ListAdapter;

import com.digian.twitter.light.BuildConfig;
import com.digian.twitter.light.CustomRobolectricRunner;
import com.digian.twitter.light.OutlineShadow;
import com.digian.twitter.light.TestStatusServices;
import com.twitter.sdk.android.core.services.StatusesService;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

/**
 * Created by forrestal on 24/08/2015.
 */
@RunWith(CustomRobolectricRunner.class)
@Config(constants = BuildConfig.class, shadows = OutlineShadow.class, sdk = Build.VERSION_CODES.LOLLIPOP)
public class HomeTimelinePresenterTest extends TestCase {

    private HomeTimelinePresenter mClassUnderTest;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        mClassUnderTest = spy(HomeTimelineSubPresenter.newInstance());
    }

    @Test
    public void testObserversNotifiedWhenUserTweetListCreated() {
        mClassUnderTest.createTimeline(RuntimeEnvironment.application);
        verify(mClassUnderTest,times(1)).notifyUserTweetListCreated(any(ListAdapter.class));
    }

    @Test
    public void testObserversNotifiedWhenUserTweetListCreatedUpdated() {
        mClassUnderTest.updateTimeline(RuntimeEnvironment.application);
        verify(mClassUnderTest,times(1)).notifyUserTweetListUpdated(any(ListAdapter.class));
    }

    @Test
    public void testObserversNotifiedWhenThereIsAnException() {
        mClassUnderTest = spy(HomeTimelineSubPresenterFailure.newInstance());
        mClassUnderTest.updateTimeline(RuntimeEnvironment.application);
        verify(mClassUnderTest,times(1)).notifyError(any(String.class));
    }

    private static class HomeTimelineSubPresenter extends HomeTimelinePresenter {
        private static final String TAG = HomeTimelinePresenter.class.getSimpleName();

        public static HomeTimelineSubPresenter newInstance() {
            return new HomeTimelineSubPresenter();
        }

        private HomeTimelineSubPresenter() {
            super();
            Log.d(TAG, "HomeTimelineSubPresenterImpl()");
        }

        @Override
        public StatusesService getStatusesService() {
            return new TestStatusServices.TestStatusServiceSuccess();
        }
    }

    private static class HomeTimelineSubPresenterFailure extends HomeTimelinePresenter {
        private static final String TAG = HomeTimelinePresenter.class.getSimpleName();

        public static HomeTimelineSubPresenterFailure newInstance() {
            return new HomeTimelineSubPresenterFailure();
        }

        private HomeTimelineSubPresenterFailure() {
            super();
            Log.d(TAG, "HomeTimelineSubPresenterImpl()");
        }

        @Override
        public StatusesService getStatusesService() {
            return new TestStatusServices.TestStatusServiceFailure();
        }
    }
}