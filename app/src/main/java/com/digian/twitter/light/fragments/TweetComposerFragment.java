package com.digian.twitter.light.fragments;

import android.app.Activity;
import android.app.Fragment;
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
import com.digian.twitter.light.presenters.TweetComposerPresenterImpl;
import com.digian.twitter.light.views.TweetComposerView;

/**
 * Created by forrestal on 26/08/2015.
 *
 * UI with associated functionality to send a tweet
 */
public class TweetComposerFragment extends Fragment implements TweetComposerView {

    private static final String TAG = TweetComposerFragment.class.getSimpleName();

    private Button tweetButton;
    private EditText editText;
    private TweetComposerCallback tweetComposerCallback;
    private TweetComposerPresenter tweetComposerPresenter;

    public TweetComposerFragment() { }

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

        tweetComposerPresenter = TweetComposerPresenterImpl.newInstance(this,getActivity());
    }

    /**
     * Attach parent activity with check for implementation of interface
     * @param activity
     */
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            tweetComposerCallback = (TweetComposerCallback) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement TweetComposerCallback");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_tweet, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.d(TAG, "onActivityCreated(Bundle savedInstanceState)");

        tweetButton = (Button) getActivity().findViewById(R.id.tweet_button);
        editText = (EditText) getActivity().findViewById(R.id.tweet_box);
        editText.requestFocus();

        tweetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG,"click before building tweet");

                tweetComposerPresenter.createTweet(editText.getText().toString());
                //Hide the keyboard
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(getActivity().INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(),0);
            }
        });

        //Show the keyboard
        InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(getActivity().INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
    }

    /**
     * Update the timeline after the tweet sent overriding view interface
     */
    @Override
    public void tweetSent() {
        tweetComposerCallback.showUpdatedTimeline();
    }

    /**
     * Display error from trying to send a tweet overriding view interface
     */
    @Override
    public void tweetError(String error) {
        displayToast(error);
    }

    @VisibleForTesting
    void displayToast(String error) {
        Toast.makeText(getActivity(), error, Toast.LENGTH_LONG).show();
    }

    @VisibleForTesting
    Button getTweetButton() {
        return tweetButton;
    }

    @VisibleForTesting
    EditText getEditText() {
        return editText;
    }

    @VisibleForTesting
    TweetComposerCallback getTweetComposerCallback() {
        return tweetComposerCallback;
    }

    @VisibleForTesting
    TweetComposerPresenter getTweetComposerPresenter() {
        return tweetComposerPresenter;
    }

    @VisibleForTesting
    void setTweetComposerPresenter(TweetComposerPresenter tweetComposerPresenter) {
        this.tweetComposerPresenter = tweetComposerPresenter;
    }

    @VisibleForTesting
    void setTweetComposerCallback(TweetComposerCallback tweetComposerCallback) {
        this.tweetComposerCallback = tweetComposerCallback;
    }
}
