package com.digian.twitter.light;

import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;

/**
 * Created by forrestal on 24/08/2015.
 * Callbacks which fragment makes on the activity usually to ask activity to replace a fragment
 * or display an error message
 */
public interface TwitterSignInCallback {
    void signInSuccess(Result<TwitterSession> result);
    void signInFailure(TwitterException exception);
}
