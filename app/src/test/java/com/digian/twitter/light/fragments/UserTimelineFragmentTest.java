package com.digian.twitter.light.fragments;

import android.os.Build;
import android.os.Bundle;

import com.digian.twitter.light.BuildConfig;
import com.digian.twitter.light.CustomRobolectricRunner;
import com.digian.twitter.light.OutlineShadow;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.annotation.Config;
import org.robolectric.shadows.ShadowLog;

import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.robolectric.util.FragmentTestUtil.startFragment;
import static org.robolectric.util.FragmentTestUtil.startVisibleFragment;

/**
 * Created by forrestal on 24/08/2015.
 */
@RunWith(CustomRobolectricRunner.class)
@Config(constants = BuildConfig.class, shadows = OutlineShadow.class, sdk = Build.VERSION_CODES.LOLLIPOP)
public class UserTimelineFragmentTest extends TestCase {

    private static final String A_SAMPLE_STRING_KEY = "A_SAMPLE_STRING_KEY";
    private static final String A_SAMPLE_STRING_VALUE = "A_SAMPLE_STRING_VALUE";

    UserTimelineFragment mClassUnderTest;

    @Before
    public void setUp() throws Exception {
        super.setUp();
        ShadowLog.stream = System.out;
    }

    @Test
    public void fragmentIsCreatedCorrectly() throws Exception {
        mClassUnderTest = UserTimelineFragment.newInstance(new Bundle());
        startVisibleFragment(mClassUnderTest);
        assertNotNull("Fragment should not be null at this point",mClassUnderTest);
    }

    @Test
    public void callingUserTimelineFragmentNewInstanceSetsFragmentArgumentsCorrectly() throws Exception {
        Bundle bundle = new Bundle();
        bundle.putString(A_SAMPLE_STRING_KEY,A_SAMPLE_STRING_VALUE);

        mClassUnderTest = UserTimelineFragment.newInstance(bundle);

        assertEquals("key A_SAMPLE_STRING_KEY should have been set in the bundle", A_SAMPLE_STRING_VALUE, mClassUnderTest.getArguments().getString(A_SAMPLE_STRING_KEY));
    }

    @Test
    public void onCreateCallbackSetsPresenter() throws Exception {
        mClassUnderTest = UserTimelineFragment.newInstance(new Bundle());
        startVisibleFragment(mClassUnderTest);

        assertTrue("Presenter should be non null", mClassUnderTest.getUserTimelinePresenter() != null);
    }

    @Test
    public void verifyOnActivityCreatedPassesSameBundleToPresenterInitMethod() throws Exception {
        Bundle bundle = new Bundle();

        mClassUnderTest = spy(UserTimelineFragment.newInstance(bundle));
        startFragment(mClassUnderTest);

        verify(mClassUnderTest,times(1)).initiliseUserTimelinPresenter(bundle);
    }
}