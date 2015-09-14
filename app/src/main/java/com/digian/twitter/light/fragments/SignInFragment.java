package com.digian.twitter.light.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
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

import butterknife.Bind;
import butterknife.ButterKnife;
/**
 * Created by forrestal on 21/08/2015.
 * Allow user to sign in using Twitter's OAuth service. The result from the
 * launched activity is passed back to the fragment from the activity
 */
public class SignInFragment extends Fragment {

    private static final String TAG = SignInFragment.class.getSimpleName();

    @Bind(R.id.twitter_login_button) TwitterLoginButton mLoginButton;

    private TwitterSignInCallback mTwitterSignInCallback;

    @NonNull
    @CheckResult
    public static SignInFragment newInstance() {
        return new SignInFragment();
    }

    public SignInFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG,"onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) " + savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_signin, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        Log.d(TAG, "onAttach(Activity activity)");

        try {
            mTwitterSignInCallback = (TwitterSignInCallback) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement TwitterSignInCallback");
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.d(TAG, "onAttach(Context context)");

        try {
            if (context instanceof Activity)
                mTwitterSignInCallback = (TwitterSignInCallback) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement TwitterSignInCallback");
        }
    }

    /**
     * Callback that view has been created
     * @param view
     * @param savedInstanceState
     */
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.d(TAG, "onViewCreated (View view, Bundle savedInstanceState)");

        mLoginButton.setCallback(new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {
                Log.d(TAG, "Success - I have signed in yippee!");
                mTwitterSignInCallback.signInSuccess(result);
            }

            @Override
            public void failure(TwitterException exception) {
                Log.e(TAG, "Failure - something went wrong yikes!", exception);
                mTwitterSignInCallback.signInFailure(exception);
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
        Log.d(TAG, "onActivityResult");
        super.onActivityResult(requestCode, resultCode, data);

        // Pass the activity result to the login button.
        mLoginButton.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d(TAG, "onDestroyView()");
        ButterKnife.unbind(this);
    }

    @VisibleForTesting
    void setLoginButton(TwitterLoginButton mLoginButton) {
        this.mLoginButton = mLoginButton;
    }

    @VisibleForTesting
    TwitterLoginButton getLoginButton() {
        return mLoginButton;
    }
}