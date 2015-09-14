package com.digian.twitter.light.fragments;

import android.os.Build;
import android.widget.Button;
import android.widget.EditText;

import com.digian.twitter.light.BuildConfig;
import com.digian.twitter.light.CustomRobolectricRunner;
import com.digian.twitter.light.MainActivity;
import com.digian.twitter.light.OutlineShadow;
import com.digian.twitter.light.R;
import com.digian.twitter.light.TweetComposerCallback;
import com.digian.twitter.light.presenters.TweetComposerPresenter;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowLog;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.robolectric.util.FragmentTestUtil.startVisibleFragment;

/**
 * Created by forrestal on 26/08/2015.
 */
@RunWith(CustomRobolectricRunner.class)
@Config(constants = BuildConfig.class, shadows = OutlineShadow.class, sdk = Build.VERSION_CODES.LOLLIPOP)
public class TweetComposerFragmentTest {

    public static final String SOME_SAMPLE_TEXT = "some sample text";
    public static final String ERROR_STRING = "Error String";
    TweetComposerFragment mClassUnderTest;

    @Before
    public void setUp() {
        mClassUnderTest = TweetComposerFragment.newInstance();
        ShadowLog.stream = System.out;
    }

    @Test
    public void basicTestToCheckFragmentIsInitialisedCorrectly() {
        //Cannot test Fragment in Isolation as Fabric needs to have been initialised
        startVisibleFragment(mClassUnderTest, Robolectric.setupActivity(MainActivity.class).getClass(), R.id.fragment_container);
        assertNotNull("Fragment has not been created correctly", mClassUnderTest);
    }

    @Test(expected = ClassCastException.class)
    public void verifyTweetComposerCallbackMustBeImplementedByParentActivity() {
        //Cannot test Fragment in Isolation as Fabric needs to have been initialised
        startVisibleFragment(mClassUnderTest);
    }

    @Test
    public void checkTweetComposerPresenterInitialised() {
        startVisibleFragment(mClassUnderTest, Robolectric.setupActivity(MainActivity.class).getClass(), R.id.fragment_container);
        assertNotNull("TweetComposerPresenter should have been initialised", mClassUnderTest.getmTweetComposerPresenter());
    }

    @Test
    public void verifyTextButtonAndCallbackAreInitialisedCorrectly() {
        //Cannot test Fragment in Isolation as Fabric needs to have been initialised
        startVisibleFragment(mClassUnderTest,  Robolectric.setupActivity(MainActivity.class).getClass(), R.id.fragment_container);
        assertNotNull(mClassUnderTest.getmEditText());
        assertNotNull(mClassUnderTest.getmTweetButton());
        assertNotNull(mClassUnderTest.getmTweetComposerCallback());
    }

    @Test
    public void verifyClickingTweetButtonCallsPresenterCreateTweetMethod() {
        startVisibleFragment(mClassUnderTest, Robolectric.setupActivity(MainActivity.class).getClass(), R.id.fragment_container);

        TweetComposerPresenter tweetComposerPresenter = mock(TweetComposerPresenter.class);
        mClassUnderTest.setmTweetComposerPresenter(tweetComposerPresenter);

        EditText tweetText = mClassUnderTest.getmEditText();
        tweetText.setText(SOME_SAMPLE_TEXT);

        Button tweetButton = mClassUnderTest.getmTweetButton();
        tweetButton.performClick();

        verify(tweetComposerPresenter,times(1)).createTweet(SOME_SAMPLE_TEXT);
    }

    @Test
    public void verifyTimelineUpdatedAfterTweetSent() {
        startVisibleFragment(mClassUnderTest, Robolectric.setupActivity(MainActivity.class).getClass(), R.id.fragment_container);

        TweetComposerCallback tweetComposerCallback = mock(TweetComposerCallback.class);
        mClassUnderTest.setmTweetComposerCallback(tweetComposerCallback);
        mClassUnderTest.tweetSent();

        verify(tweetComposerCallback,times(1)).showUpdatedTimeline();
    }

    @Test
    public void verifyToastDisplayedIfTweetError() {

        mClassUnderTest = spy(TweetComposerFragment.newInstance());
        mClassUnderTest.tweetError(ERROR_STRING);

        verify(mClassUnderTest,times(1)).displayToast(ERROR_STRING);
    }

}

