package com.pennapps.calmer;

import android.app.Activity;
import android.app.Application;
import android.app.Service;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.Wearable;

import com.parse.Parse;


public class MainHomeActivity extends Activity {

    private static final String TAG = "CalmerPhoneActivity";

    private Switch onOffSwitch;
    private GoogleApiClient mGoogleApiClient;
    Intent intent;
    private TextView titleText;
    private Button calibrationButton;
    private Button viewHeartrateButton;
    private Button viewTrendsButton;
    private Calmer calmer = (Calmer) this.getApplication();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        titleText = (TextView) findViewById(R.id.titleTextView);
        Typeface font = Typeface.createFromAsset(getAssets(), "CanelaBarkPersonal.ttf");
        titleText.setTypeface(font);

        Parse.enableLocalDatastore(this);
        Parse.initialize(this);

        configureButtons();
        configureWearableApi();
        configureSwitch();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        MainDataListenerService.setHandler(null);
        stopService(intent);
        Log.d(TAG, "trying to destroy service");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void configureButtons() {
        calibrationButton = (Button) findViewById(R.id.calibrationButton);
        viewHeartrateButton = (Button) findViewById(R.id.viewHeartrateButton);
        viewHeartrateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ViewCurrentHeartrateActivity.class);
                startActivity(intent);
            }
        });
        viewTrendsButton = (Button) findViewById(R.id.viewTrendsButton);
    }

    private void configureWearableApi() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
                    @Override
                    public void onConnected(Bundle connectionHint) {
                        Log.d(TAG, "onConnected: " + connectionHint);
                    }

                    @Override
                    public void onConnectionSuspended(int cause) {
                        Log.d(TAG, "onConnectionSuspended: " + cause);
                    }
                })
                .addOnConnectionFailedListener(new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(ConnectionResult result) {
                        Log.d(TAG, "onConnectionFailed: " + result);
                    }
                })
                .addApi(Wearable.API)
                .build();
    }

    private void configureSwitch() {
        intent = new Intent(getApplicationContext(), MainDataListenerService.class);
        onOffSwitch = (Switch) findViewById(R.id.onOffSwitch);
        onOffSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    Log.d(TAG, "trying to start service");
                    calmer.setMainServiceStatus(true);
                    startService(intent);
                    mGoogleApiClient.connect();
                } else {
                    Log.d(TAG, "trying to disconnect service");
                    calmer.setMainServiceStatus(false);
                    stopService(intent);
                    mGoogleApiClient.disconnect();
                }
            }
        });
    }

}
