package com.digian.twitter.light.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.digian.twitter.light.R;
import com.digian.twitter.light.TwitterSignInCallback;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;

/**
 * Created by forrestal on 21/08/2015.
 * Allow user to sign in using Twitter's OAuth service. The result from the
 * launched activity is passed back to the fragment from the activity
 */
public class SignInFragment extends Fragment {

    private static final String TAG = SignInFragment.class.getSimpleName();

    private TwitterLoginButton loginButton;
    private TwitterSignInCallback twitterSignInCallback;

    /**
     * @return SignInFragment from factory method
     */
    @NonNull
    @CheckResult
    public static SignInFragment newInstance() {
        return new SignInFragment();
    }

    /**
     *  Required default public constructor
     */
    public SignInFragment() {}

    /**
     * Create view for fragment
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_signin, container, false);
    }

    /**
     * Attach parent activity with check for implementation of interface
     * @param activity
     */
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            twitterSignInCallback = (TwitterSignInCallback) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement TwitterSignInCallback");
        }
    }

    /**
     * Callback that view has been created
     * @param view
     * @param savedInstanceState
     */
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        loginButton = (TwitterLoginButton) getActivity().findViewById(R.id.twitter_login_button);
        loginButton.setCallback(new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {
                Log.d(TAG, "Success - I have signed in yippee!");
                twitterSignInCallback.signInSuccess(result);
            }

            @Override
            public void failure(TwitterException exception) {
                Log.e(TAG, "Failure - something went wrong yikes!", exception);
                twitterSignInCallback.signInFailure(exception);
            }
        });
    }

    /**
     * Result returned from parent activity
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "onActivityResult" );
        super.onActivityResult(requestCode, resultCode, data);

        // Pass the activity result to the login button.
        loginButton.onActivityResult(requestCode, resultCode, data);
    }

    @VisibleForTesting
    void setLoginButton(TwitterLoginButton loginButton) {
        this.loginButton = loginButton;
    }

    @VisibleForTesting
    TwitterLoginButton getLoginButton() {
        return loginButton;
    }
}
