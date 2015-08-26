package com.digian.twitter.light.fragments;

import android.os.Build;

import com.digian.twitter.light.BuildConfig;
import com.digian.twitter.light.CustomRobolectricRunner;
import com.digian.twitter.light.MainActivity;
import com.digian.twitter.light.OutlineShadow;
import com.digian.twitter.light.R;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowLog;

import static org.junit.Assert.assertNotNull;
import static org.robolectric.util.FragmentTestUtil.startVisibleFragment;

/**
 * Created by forrestal on 26/08/2015.
 */
@RunWith(CustomRobolectricRunner.class)
@Config(constants = BuildConfig.class, shadows = OutlineShadow.class, sdk = Build.VERSION_CODES.LOLLIPOP)
public class TweetComposerFragmentTest {

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
    public void verifyTextButtonAndCallbackAreInitialisedCorrectly() {
        //Cannot test Fragment in Isolation as Fabric needs to have been initialised
        startVisibleFragment(mClassUnderTest,  Robolectric.setupActivity(MainActivity.class).getClass(), R.id.fragment_container);
        assertNotNull(mClassUnderTest.getEditText());
        assertNotNull(mClassUnderTest.getTweetButton());
        assertNotNull(mClassUnderTest.getTweetComposerCallback());
    }

}

