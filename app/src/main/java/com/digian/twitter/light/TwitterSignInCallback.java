package com.digian.twitter.light;

import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;

/**
 * Created by forrestal on 24/08/2015.
 */
public interface TwitterSignInCallback {
    void signInSuccess(Result<TwitterSession> result);
    void signInFailure(TwitterException exception);
}
