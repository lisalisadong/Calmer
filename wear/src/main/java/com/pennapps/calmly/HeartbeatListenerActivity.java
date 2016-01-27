package com.pennapps.calmly;

import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;


/**
 * Created by QingxiaoDong on 1/23/16.
 */
public class HeartbeatListenerActivity extends Activity  {

    private static final String LOG_TAG = "CalmerMyHeart";

    Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);

        intent = new Intent(getApplicationContext(), HeartbeatTrackingService.class);
        startService(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopService(intent);
    }


}
