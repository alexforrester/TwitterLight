package com.digian.twitter.light.presenters;

import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.services.StatusesService;

/**
 * Created by forrestal on 26/08/2015.
 * Used a base class for other presenters
 */
public abstract class BasePresenter  {

    /**
     * @return API to use for TwitterService
     */
    protected StatusesService getStatusesService() {
        return TwitterCore.getInstance().getApiClient().getStatusesService();
    }
}
