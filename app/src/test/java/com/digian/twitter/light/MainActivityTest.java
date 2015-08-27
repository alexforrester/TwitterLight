package com.digian.twitter.light;

import android.app.Fragment;
import android.content.Intent;
import android.os.Build;

import com.digian.twitter.light.fragments.SignInFragment;

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

/**
 * Created by forrestal on 21/08/2015.
 * IllegalArgumentException: path must be convex #1810
 * See <a href="https://github.com/robolectric/robolectric/issues/1810">IllegalArgumentException: path must be convex #1810</a>
 */
@RunWith(CustomRobolectricRunner.class)
@Config(constants = BuildConfig.class, shadows = OutlineShadow.class, sdk = Build.VERSION_CODES.LOLLIPOP )
public class MainActivityTest{

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
    public void testMainActivityImplementsTwitterSignInCallback()  {
        assertTrue(mClassUnderTest instanceof TwitterSignInCallback);
    }

    @Test
    public void testMainActivityImplementsTweetComposerCallback()  {
        assertTrue(mClassUnderTest instanceof TweetComposerCallback);
    }
}