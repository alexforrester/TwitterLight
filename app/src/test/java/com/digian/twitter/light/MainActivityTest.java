package com.digian.twitter.light;

import android.app.Fragment;
import android.content.Intent;
import android.os.Build;

import com.digian.twitter.light.fragments.HomeTimelineFragment;
import com.digian.twitter.light.fragments.HomeTimelineSubFragment;
import com.digian.twitter.light.fragments.SignInFragment;
import com.digian.twitter.light.fragments.TweetComposerFragment;
import com.twitter.sdk.android.core.TwitterException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.annotation.Config;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Created by forrestal on 21/08/2015.
 * IllegalArgumentException: path must be convex #1810
 * See <a href="https://github.com/robolectric/robolectric/issues/1810">IllegalArgumentException: path must be convex #1810</a>
 */
@RunWith(CustomRobolectricRunner.class)
@Config(constants = BuildConfig.class, shadows = OutlineShadow.class, sdk = Build.VERSION_CODES.LOLLIPOP)
public class MainActivityTest {

    public static final String ERROR_SIGNING_IN = "Error Signing In";
    MainActivity mClassUnderTest;

    @Before
    public void setUp() {
        mClassUnderTest = Robolectric.setupActivity(MainActivity.class);
    }

    @Test
    public void testActivitySetUpCorrectly() {
        assertTrue(mClassUnderTest != null);
    }

    @Test
    public void testActivityHasSignInFragmentDisplayedInitially() {
        Fragment firstFragmentAdded = mClassUnderTest.getFragmentManager().findFragmentById(R.id.fragment_container);
        assertTrue(firstFragmentAdded instanceof SignInFragment);
    }

    @Test
    public void testOnActivityResultInActivityCallsSignInFragmentOnActivityResult() {
        Fragment signInFragment = spy(mClassUnderTest.getCurrentFragment());
        Intent intent = mock(Intent.class);
        mClassUnderTest.setCurrentFragment(signInFragment);

        mClassUnderTest.onActivityResult(TestConstants.REQUEST_CODE, TestConstants.RESULT_CODE, intent);
        verify(signInFragment, times(1)).onActivityResult(TestConstants.REQUEST_CODE, TestConstants.RESULT_CODE, intent);
    }

    @Test
    public void testSignInFragmentReplacedWithHomeTimelineFragmentOnSignInSuccess() {
        mClassUnderTest = spy(Robolectric.setupActivity(MainActivity.class));

        HomeTimelineFragment homeTimelineSubFragment = HomeTimelineSubFragment.newInstance();

        when(mClassUnderTest.getHomeTimelineFragment()).thenReturn(homeTimelineSubFragment);

        mClassUnderTest.signInSuccess(null);
        Fragment addedFragment = mClassUnderTest.getFragmentManager().findFragmentById(R.id.fragment_container);
        assertTrue(addedFragment instanceof HomeTimelineFragment);
    }

    @Test
    public void testToastMessageWithErrorShownIfProblemSigningIn() {
        mClassUnderTest = spy(Robolectric.setupActivity(MainActivity.class));

        HomeTimelineFragment homeTimelineSubFragment = HomeTimelineSubFragment.newInstance();

        when(mClassUnderTest.getHomeTimelineFragment()).thenReturn(homeTimelineSubFragment);

        TwitterException twitterException = new TwitterException(ERROR_SIGNING_IN);
        mClassUnderTest.signInFailure(twitterException);

        verify(mClassUnderTest, times(1)).displayToast(twitterException);
    }

    @Test
    public void testTweetComposerFragmentDisplayedWhenTweetComposerCallbackReceived() {
        mClassUnderTest = Robolectric.setupActivity(MainActivity.class);

        mClassUnderTest.displayTweetComposer();

        Fragment addedFragment = mClassUnderTest.getFragmentManager().findFragmentById(R.id.fragment_container);
        assertTrue(addedFragment instanceof TweetComposerFragment);
    }

    @Test
    public void testNewHomeTimelineFragmentAddedWhenShowUpdatedCallbackMade() {

        mClassUnderTest = spy(Robolectric.setupActivity(MainActivity.class));

        HomeTimelineFragment homeTimelineSubFragment = HomeTimelineSubFragment.newInstance();
        when(mClassUnderTest.getHomeTimelineFragment()).thenReturn(homeTimelineSubFragment);

        Fragment currentFragment = mClassUnderTest.getFragmentManager().findFragmentById(R.id.fragment_container);
        assertTrue(currentFragment instanceof SignInFragment);

        mClassUnderTest.showUpdatedTimeline();
        Fragment addedFragment = mClassUnderTest.getFragmentManager().findFragmentById(R.id.fragment_container);
        assertTrue(addedFragment instanceof HomeTimelineFragment);
    }

    @Test
    public void testPoppingFragmentsOffTheStack() {

        mClassUnderTest = spy(Robolectric.setupActivity(MainActivity.class));

        HomeTimelineFragment homeTimelineSubFragment = HomeTimelineSubFragment.newInstance();
        when(mClassUnderTest.getHomeTimelineFragment()).thenReturn(homeTimelineSubFragment);

        Fragment currentFragment = mClassUnderTest.getFragmentManager().findFragmentById(R.id.fragment_container);
        assertTrue(currentFragment instanceof SignInFragment);

        mClassUnderTest.displayTweetComposer();
        Fragment addedFragment = mClassUnderTest.getFragmentManager().findFragmentById(R.id.fragment_container);
        assertTrue(addedFragment instanceof TweetComposerFragment);

        mClassUnderTest.onBackPressed();
        Fragment fragmentAfterPop = mClassUnderTest.getFragmentManager().findFragmentById(R.id.fragment_container);
        assertTrue(fragmentAfterPop instanceof SignInFragment);
    }

    @Test
    public void testMainActivityImplementsTwitterSignInCallback() {
        assertTrue(mClassUnderTest instanceof TwitterSignInCallback);
    }

    @Test
    public void testMainActivityImplementsTweetComposerCallback() {
        assertTrue(mClassUnderTest instanceof TweetComposerCallback);
    }
}