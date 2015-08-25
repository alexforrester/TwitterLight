package com.digian.twitter.light.fragments;

import android.content.Intent;
import android.os.Build;

import com.digian.twitter.light.BuildConfig;
import com.digian.twitter.light.TestConstants;
import com.digian.twitter.light.CustomRobolectricRunner;
import com.digian.twitter.light.MainActivity;
import com.digian.twitter.light.OutlineShadow;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowLog;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.robolectric.util.FragmentTestUtil.startFragment;

/**
 * Created by forrestal on 23/08/2015.
 */
@RunWith(CustomRobolectricRunner.class)
@Config(constants = BuildConfig.class, shadows = OutlineShadow.class, sdk = Build.VERSION_CODES.LOLLIPOP)
public class SignInFragmentTest extends TestCase {

    SignInFragment mClassUnderTest;

    @Before
    public void setUp() {
        mClassUnderTest = SignInFragment.newInstance();
        ShadowLog.stream = System.out;
    }

    @Test
    public void basicTestToCheckFragmentIsInitialisedCorrectly() {
        //Cannot test Fragment in Isolation as Fabric needs to have been initialised
        startFragment(mClassUnderTest, Robolectric.setupActivity(MainActivity.class).getClass());
        assertNotNull("Fragment has not been created correctly", mClassUnderTest);
    }

    @Test
    public void verifyUIDisplayedCorrectlyWithTwitterLoginButtonInflated() {
        //Cannot test Fragment in Isolation as Fabric needs to have been initialised
        startFragment(mClassUnderTest, Robolectric.setupActivity(MainActivity.class).getClass());
        assertNotNull(mClassUnderTest.getLoginButton());
    }

    @Test
    public void onActivityResultInFragmentCallsLoginButtonOnActivityResult() throws Exception {
        //Cannot test Fragment in Isolation as Fabric needs to have been initialised
        startFragment(mClassUnderTest, Robolectric.setupActivity(MainActivity.class).getClass());
        TwitterLoginButton twitterLoginButton = spy(new TwitterLoginButton(mClassUnderTest.getActivity()));
        mClassUnderTest.setLoginButton(twitterLoginButton);
        Intent intent = mock(Intent.class);

        mClassUnderTest.onActivityResult(TestConstants.REQUEST_CODE, TestConstants.RESULT_CODE, intent);

        verify(twitterLoginButton, times(1)).onActivityResult(TestConstants.REQUEST_CODE, TestConstants.RESULT_CODE, intent);
    }

    @Test(expected = ClassCastException.class)
    public void onAttachThrowsClassCastExceptionIfParentActivityDoesntImplementTwitterSignInCallback(){
        startFragment(mClassUnderTest);
    }
}