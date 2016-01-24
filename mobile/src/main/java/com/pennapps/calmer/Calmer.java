package com.pennapps.calmer;

import android.app.Application;

/**
 * Created by klyde on 1/23/16.
 */
public class Calmer extends Application {

    private boolean isMainServiceOn;

    public boolean getMainServiceStatus() {
        return isMainServiceOn;
    }

    public void setMainServiceStatus(boolean serviceStatus) {
        isMainServiceOn = serviceStatus;
    }
}
