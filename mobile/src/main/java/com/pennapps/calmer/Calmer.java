package com.pennapps.calmer;

import com.parse.Parse;
import com.parse.ParseObject;

/**
 * Created by Yujie on 1/23/16.
 */
public class Calmer extends android.app.Application {

        private final String applicationID = "TZwcUfExXMmc52um2z9ZHqPuACi6QBYYE7rUHatf";
        private final String clientKey = "AQLBUXcT4El6kXDHAh1MUmzrl45wTb2yZ4ltpAR8";

        /** Enable Parse on all app pages */
        @Override
        public void onCreate() {
                super.onCreate();

                // Register ParseObject subclasses
                ParseObject.registerSubclass(User.class);
                Parse.enableLocalDatastore(this);
                Parse.initialize(this, applicationID, clientKey);
        }
}