package com.pennapps.calmer;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.Wearable;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import com.google.android.gms.wearable.DataEventBuffer;
import com.google.android.gms.wearable.DataEvent;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import android.hardware.SensorEventListener;
import android.hardware.SensorEvent;
import android.hardware.Sensor;

/**
 * Created by QingxiaoDong on 1/23/16.
 */
public class HeartbeatListenerActivity extends Activity implements SensorEventListener  {

    private static final String LOG_TAG = "MyHeart";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_HEART_RATE) {
            //TODO: do something here
        }
    }


}
