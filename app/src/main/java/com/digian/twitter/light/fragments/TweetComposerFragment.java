package com.digian.twitter.light.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.VisibleForTesting;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.digian.twitter.light.R;
import com.digian.twitter.light.TweetComposerCallback;
import com.digian.twitter.light.presenters.TweetComposerPresenter;
import com.digian.twitter.light.views.TweetComposerView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by forrestal on 26/08/2015.
 *
 * UI with associated functionality to send a tweet
 */
public class TweetComposerFragment extends Fragment implements TweetComposerView {

    private static final String TAG = TweetComposerFragment.class.getSimpleName();

    @Bind(R.id.tweet_button) Button mTweetButton;
    @Bind(R.id.tweet_box) EditText mEditText;
    private TweetComposerCallback mTweetComposerCallback;
    private TweetComposerPresenter mTweetComposerPresenter;

    public TweetComposerFragment() {}

    public static TweetComposerFragment newInstance() {
        return new TweetComposerFragment();
    }

    /**
     * Set-up
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate(Bundle savedInstanceState)");

        mTweetComposerPresenter = TweetComposerPresenter.newInstance(this, getActivity());
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.d(TAG, "onAttach(Context context)");
        try {
            if (context instanceof Activity)
                mTweetComposerCallback = (TweetComposerCallback) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement TweetComposerCallback");
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        Log.d(TAG, "onAttach(Activity activity)");
        try {
            mTweetComposerCallback = (TweetComposerCallback) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement TweetComposerCallback");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)");
        View view = inflater.inflate(R.layout.fragment_tweet, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d(TAG, "onActivityCreated(Bundle savedInstanceState)");

        mEditText.requestFocus();
        //Show the keyboard
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(getActivity().INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
    }

    @OnClick(R.id.tweet_button)
    public void sendTweet(View v) {
        mTweetComposerPresenter.createTweet(mEditText.getText().toString());
        //Hide the keyboard
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(getActivity().INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause()");
    }

    @Override
    public void onStop() {
        super.onStop();
        Log.d(TAG, "onStop()");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        Log.d(TAG, "onDestroyView()");
        ButterKnife.unbind(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy()");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        Log.d(TAG, "onDetach()");
    }

    @Override
    public void tweetSent() {
        mTweetComposerCallback.showUpdatedTimeline();
    }

    @Override
    public void tweetError(String error) {
        displayToast(error);
    }

    @VisibleForTesting
    void displayToast(String error) {
        Toast.makeText(getActivity(), error, Toast.LENGTH_LONG).show();
    }

    @VisibleForTesting
    Button getmTweetButton() {
        return mTweetButton;
    }

    @VisibleForTesting
    EditText getmEditText() {
        return mEditText;
    }

    @VisibleForTesting
    TweetComposerCallback getmTweetComposerCallback() {
        return mTweetComposerCallback;
    }

    @VisibleForTesting
    TweetComposerPresenter getmTweetComposerPresenter() {
        return mTweetComposerPresenter;
    }

    @VisibleForTesting
    void setmTweetComposerPresenter(TweetComposerPresenter mTweetComposerPresenter) {
        this.mTweetComposerPresenter = mTweetComposerPresenter;
    }

    @VisibleForTesting
    void setmTweetComposerCallback(TweetComposerCallback mTweetComposerCallback) {
        this.mTweetComposerCallback = mTweetComposerCallback;
    }
}
