package com.digian.twitter.light.presenters;

import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.services.StatusesService;

/**
 * Created by forrestal on 26/08/2015.
 */
public class BasePresenter  {

    protected StatusesService getStatusesService() {
        return TwitterCore.getInstance().getApiClient().getStatusesService();
    }
}
