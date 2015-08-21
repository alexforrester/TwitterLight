package com.digian.twitter.light.fragments;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.digian.twitter.light.R;
import com.twitter.sdk.android.core.Callback;
import com.twitter.sdk.android.core.Result;
import com.twitter.sdk.android.core.TwitterException;
import com.twitter.sdk.android.core.TwitterSession;
import com.twitter.sdk.android.core.identity.TwitterLoginButton;

/**
 * Created by forrestal on 21/08/2015.
 */
public class SignInFragment extends Fragment {

    private static final String TAG = SignInFragment.class.getSimpleName();

    private TwitterLoginButton loginButton;

    /**
     *
     * @return SignInFragment from factory method
     */
    public static SignInFragment newInstance() {
        return new SignInFragment();
    }

    /**
     * Required empty public constructor
     */
    public SignInFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_signin, container, false);
    }


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        loginButton = (TwitterLoginButton) getActivity().findViewById(R.id.twitter_login_button);
        loginButton.setCallback(new Callback<TwitterSession>() {
            @Override
            public void success(Result<TwitterSession> result) {
                Log.d(TAG, "Success - I have signed in yippee!");
            }

            @Override
            public void failure(TwitterException exception) {
                Log.e(TAG, "Failure - something went wrong yikes!", exception);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "onActivityResult" );
        super.onActivityResult(requestCode, resultCode, data);

        // Pass the activity result to the login button.
        loginButton.onActivityResult(requestCode, resultCode, data);
    }


}
