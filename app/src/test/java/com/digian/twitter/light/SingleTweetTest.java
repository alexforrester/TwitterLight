package com.digian.twitter.light;

import android.content.Context;
import android.os.Build;

import junit.framework.TestCase;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

@RunWith(CustomRobolectricRunner.class)
@Config(constants = BuildConfig.class, shadows = OutlineShadow.class, sdk = Build.VERSION_CODES.LOLLIPOP)
public class SingleTweetTest extends TestCase {

    private SingleTweet mClassUnderTest;

     @Test
     public void testValidTweetDoesntHaveError() throws Exception {

        String validTweet = "This is a valid tweet";
        mClassUnderTest = SingleTweet.newInstance(validTweet, RuntimeEnvironment.application);

        assertFalse("No error should be reported", mClassUnderTest.hasTweetError());
    }

    @Test
    public void testValidTweetSetsContentCorrectly() throws Exception {

        String validTweet = "This is a valid tweet";
        mClassUnderTest = SingleTweet.newInstance(validTweet, RuntimeEnvironment.application);

        assertEquals("Tweet content should have been set correctly", validTweet, mClassUnderTest.getTweetContents());
    }

    @Test
    public void testNullTweetSetsCorrectErrorMessage() throws Exception {
        String invalidTweet = null;
        Context context = RuntimeEnvironment.application;
        mClassUnderTest = SingleTweet.newInstance(invalidTweet, context);
        mClassUnderTest.hasTweetError();
        assertEquals("The tweet should return error message for empty", context.getResources().getString(R.string.tweet_not_entered),mClassUnderTest.getTweetErrorMessage());
    }

    @Test
    public void testEmptyTweetWithZeroLengthStringSetsCorrectErrorMessage() throws Exception {

        String invalidTweet = "";
        Context context = RuntimeEnvironment.application;
        mClassUnderTest = SingleTweet.newInstance(invalidTweet, context);
        mClassUnderTest.hasTweetError();

        assertEquals("The tweet should return error message for empty", context.getResources().getString(R.string.tweet_not_entered),mClassUnderTest.getTweetErrorMessage());

    }

    @Test
    public void testTweetWhichIsTooLongSetsCorrectErrorMessage() throws Exception {

        String invalidTweet = "This is a tweet which should fail the validity check as it is definitely over 140 characters long. In fact it is one hundred and ninety nine characters long and so by definition is not a valid tweet";

        Context context = RuntimeEnvironment.application;
        mClassUnderTest = SingleTweet.newInstance(invalidTweet, context);
        mClassUnderTest.hasTweetError();

        assertEquals("The tweet should return error code for too long", context.getResources().getString(R.string.tweet_is_too_long),mClassUnderTest.getTweetErrorMessage());

    }
}