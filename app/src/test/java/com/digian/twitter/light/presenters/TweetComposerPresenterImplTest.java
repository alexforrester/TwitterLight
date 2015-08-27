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
public class TweetComposerPresenterImplTest extends TestCase {

    public static final String TWEET_OVER_140_CHARACTERS_LONG = "This is a tweet which is over 140 characters long and therefore should fail to be posted to the home timeline as it is too long. It should instead display an error message about the tweet being too long";
    public static final String TWEET_MESSAGE_THAT_SHOULD_BE_SENT_TO_VIEW = "tweet message that should be sent to view";
    TweetComposerPresenterImpl mClassUnderTest;

    TweetComposerView tweetComposerView;

    Context context;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        tweetComposerView = mock(TweetComposerView.class);
        context = mock(Context.class);
        mClassUnderTest = TweetComposerPresenterImpl.newInstance(tweetComposerView,context);
        ShadowLog.stream = System.out;
    }

    @Test
    public void testNewInstanceAndMembersInitialisedCorrectly() throws Exception {
        assertNotNull("presenter should have been created", mClassUnderTest);
        assertNotNull("view should have been passed in correctly and set", mClassUnderTest.getTweetComposerView());
        assertNotNull("context should have been passed in correctly and set", mClassUnderTest.getContext());
    }

    @Test
    public void testNewInstanceParametersAreSetAsMembers() throws Exception {
        assertSame("view passed in should be set as view member", tweetComposerView, mClassUnderTest.getTweetComposerView());
        assertSame("context passed in should be set as context member", context, mClassUnderTest.getContext());
    }

    @Test
    public void testErrorShownIfNoTweetEntered() throws Exception {

        final Context contextToRetrieveStrings = RuntimeEnvironment.application;
        mClassUnderTest = spy(TweetComposerPresenterImpl.newInstance(tweetComposerView, contextToRetrieveStrings));
        mClassUnderTest.createTweet("");

        verify(tweetComposerView,times(1)).tweetError(contextToRetrieveStrings.getString(R.string.tweet_not_entered));
        verify(mClassUnderTest, never()).getStatusesService();
    }

    @Test
    public void testErrorShownIfTweetExceedsMaxLength() throws Exception {

        final Context contextToRetrieveStrings = RuntimeEnvironment.application;
        mClassUnderTest = spy(TweetComposerPresenterImpl.newInstance(tweetComposerView, contextToRetrieveStrings));
        mClassUnderTest.createTweet(TWEET_OVER_140_CHARACTERS_LONG);

        verify(tweetComposerView,times(1)).tweetError(contextToRetrieveStrings.getString(R.string.tweet_is_too_long));
        verify(mClassUnderTest, never()).getStatusesService();
    }

    @Test
    public void testTweetSentBackToViewAfterBeingSubmitted() throws Exception {

        StatusesService spyStatusServices = spy(new TestStatusServices.TestStatusServiceSuccess());

        mClassUnderTest = spy(TweetComposerPresenterSubImpl.newInstance(tweetComposerView, context));
        when(mClassUnderTest.getStatusesService()).thenReturn(spyStatusServices);

        mClassUnderTest.createTweet(TWEET_MESSAGE_THAT_SHOULD_BE_SENT_TO_VIEW);

        verify(tweetComposerView,times(1)).tweetSent();
    }

    @Test
    public void testTweetExceptionSentBackToViewIfExceptionThrown() throws Exception {

        StatusesService spyStatusServices = spy(new TestStatusServices.TestStatusServiceFailure());

        mClassUnderTest = spy(TweetComposerPresenterSubImpl.newInstance(tweetComposerView, context));
        when(mClassUnderTest.getStatusesService()).thenReturn(spyStatusServices);

        mClassUnderTest.createTweet(TWEET_MESSAGE_THAT_SHOULD_BE_SENT_TO_VIEW);

        verify(tweetComposerView,times(1)).tweetError(TestStatusServices.SOME_TWITTER_EXCEPTION);
    }

    private static class TweetComposerPresenterSubImpl extends TweetComposerPresenterImpl{
        private static final String TAG = HomeTimelinePresenterImpl.class.getSimpleName();

        public static TweetComposerPresenterSubImpl newInstance(@NonNull TweetComposerView tweetComposerView, @NonNull Context context) {
            return new TweetComposerPresenterSubImpl(tweetComposerView, context);
        }

        TweetComposerPresenterSubImpl(@NonNull TweetComposerView tweetComposerView,@NonNull Context context  ) {
            super(tweetComposerView,context);
            Log.d(TAG, "TweetComposerPresenterSubImpl(@NonNull TweetComposerView tweetComposerView,@NonNull Context context ");
        }

        @Override
        public StatusesService getStatusesService() {
            return new TestStatusServices.TestStatusServiceSuccess();
        }
    }
}