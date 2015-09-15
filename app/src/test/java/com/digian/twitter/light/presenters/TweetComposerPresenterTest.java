package com.digian.twitter.light.presenters;

import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.util.Log;

import com.digian.twitter.light.BuildConfig;
import com.digian.twitter.light.CustomRobolectricRunner;
import com.digian.twitter.light.OutlineShadow;
import com.digian.twitter.light.R;
import com.digian.twitter.light.TestStatusServices;
import com.digian.twitter.light.views.TweetComposerView;
import com.twitter.sdk.android.core.services.StatusesService;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowLog;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by forrestal on 27/08/2015.
 */
@RunWith(CustomRobolectricRunner.class)
@Config(constants = BuildConfig.class, shadows = OutlineShadow.class, sdk = Build.VERSION_CODES.LOLLIPOP)
public class TweetComposerPresenterTest extends TestCase {

    public static final String TWEET_OVER_140_CHARACTERS_LONG = "This is a tweet which is over 140 characters long and therefore should fail to be posted to the home timeline as it is too long. It should instead display an error message about the tweet being too long";
    public static final String TWEET_MESSAGE_THAT_SHOULD_BE_SENT_TO_VIEW = "tweet message that should be sent to view";
    TweetComposerPresenter mClassUnderTest;

    TweetComposerView mTweetComposerView;
    Context mContext;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        mTweetComposerView = mock(TweetComposerView.class);
        mContext = mock(Context.class);
        mClassUnderTest = TweetComposerPresenter.newInstance(mContext);
        ShadowLog.stream = System.out;
    }

    @Test
    public void testNewInstanceAndMembersInitialisedCorrectly() throws Exception {
        assertNotNull("presenter should have been created", mClassUnderTest);
        assertNotNull("mContext should have been passed in correctly and set", mClassUnderTest.getContext());
        assertNotNull("tweet composer observers should be initialised", mClassUnderTest.getTweetComposerObservers());
    }

    @Test
    public void testNewInstanceParametersIsSetAsMember() throws Exception {
        assertSame("mContext passed in should be set as mContext member", mContext, mClassUnderTest.getContext());
    }

    @Test
    public void testErrorShownIfNoTweetEntered() throws Exception {

        final Context contextToRetrieveStrings = RuntimeEnvironment.application;
        mClassUnderTest = spy(TweetComposerPresenter.newInstance(contextToRetrieveStrings));
        mClassUnderTest.createTweet("");

        verify(mClassUnderTest,times(1)).notifyTweetError(contextToRetrieveStrings.getString(R.string.tweet_not_entered));
        verify(mClassUnderTest, never()).getStatusesService();
    }

    @Test
    public void testErrorShownIfTweetExceedsMaxLength() throws Exception {

        final Context contextToRetrieveStrings = RuntimeEnvironment.application;
        mClassUnderTest = spy(TweetComposerPresenter.newInstance(contextToRetrieveStrings));
        mClassUnderTest.createTweet(TWEET_OVER_140_CHARACTERS_LONG);

        verify(mClassUnderTest,times(1)).notifyTweetError(contextToRetrieveStrings.getString(R.string.tweet_is_too_long));
        verify(mClassUnderTest, never()).getStatusesService();
    }

    @Test
    public void testObserversNotifiedAfterBeingLegitimateTweetSubmitted() throws Exception {

        StatusesService spyStatusServices = spy(new TestStatusServices.TestStatusServiceSuccess());

        mClassUnderTest = spy(TweetComposerPresenterSubImpl.newInstance(mContext));
        when(mClassUnderTest.getStatusesService()).thenReturn(spyStatusServices);

        mClassUnderTest.createTweet(TWEET_MESSAGE_THAT_SHOULD_BE_SENT_TO_VIEW);

        verify(mClassUnderTest,times(1)).notifyTweetSent();
    }

    @Test
    public void testObserversNotifiedIfExceptionThrown() throws Exception {

        StatusesService spyStatusServices = spy(new TestStatusServices.TestStatusServiceFailure());

        mClassUnderTest = spy(TweetComposerPresenterSubImpl.newInstance(mContext));
        when(mClassUnderTest.getStatusesService()).thenReturn(spyStatusServices);

        mClassUnderTest.createTweet(TWEET_MESSAGE_THAT_SHOULD_BE_SENT_TO_VIEW);

        verify(mClassUnderTest,times(1)).notifyTweetError(TestStatusServices.SOME_TWITTER_EXCEPTION);
    }

    private static class TweetComposerPresenterSubImpl extends TweetComposerPresenter {
        private static final String TAG = HomeTimelinePresenter.class.getSimpleName();

        public static TweetComposerPresenterSubImpl newInstance(@NonNull Context context) {
            return new TweetComposerPresenterSubImpl(context);
        }

        TweetComposerPresenterSubImpl(@NonNull Context context  ) {
            super(context);
            Log.d(TAG, "TweetComposerPresenterSubImpl(@NonNull TweetComposerView mTweetComposerView,@NonNull Context mContext ");
        }

        @Override
        public StatusesService getStatusesService() {
            return new TestStatusServices.TestStatusServiceSuccess();
        }
    }
}