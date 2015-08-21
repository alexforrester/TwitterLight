package com.digian.twitter.light;

import android.app.Fragment;
import android.os.Build;

import com.digian.twitter.light.fragments.SignInFragment;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.annotation.Config;

import static org.junit.Assert.assertTrue;

/**
 * Created by forrestal on 21/08/2015.
 */
@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = Build.VERSION_CODES.LOLLIPOP)
public class MainActivityTest{

    MainActivity mClassUnderTest;

    @Before
    public void setUp() {
        mClassUnderTest = Robolectric.setupActivity(MainActivity.class);
    }

    @Test
    public void testActivitySetUpCorrectly() throws Exception {
        assertTrue(mClassUnderTest != null);
    }

    @Test
    public void testActivityHasSignInFragmentDisplayedInitially() throws Exception {
        Fragment firstFragmentAdded = mClassUnderTest.getFragmentManager().findFragmentById(R.id.fragment_container);
        assertTrue(firstFragmentAdded instanceof SignInFragment);
    }
}