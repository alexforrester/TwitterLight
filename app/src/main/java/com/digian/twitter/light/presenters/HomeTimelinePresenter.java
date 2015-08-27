package com.digian.twitter.light.presenters;

import android.content.Context;
import android.os.Bundle;

/**
 * Created by forrestal on 24/08/2015.
 *  * Presenter interface for home timeline
 */
public interface HomeTimelinePresenter {

    String TWITTER_SESSION_USER_ID = "TWITTER_SESSION_USER_ID";
    String TWITTER_SESSION_USER_NAME = "TWITTER_SESSION_USER_NAME";

    void init(Bundle bundle);
    void createTimeline(Context context);
    void updateTimeline(Context context);
}
