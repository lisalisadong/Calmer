package com.pennapps.calmer;

import android.app.Activity;
import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.Wearable;

import com.parse.Parse;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.app.PendingIntent;
import android.content.Context;
import android.app.NotificationManager;

import java.util.Random;
import com.parse.ParseQuery;
import com.parse.ParseObject;

public class MainHomeActivity extends Activity {

    private static final String TAG = "CalmerPhoneActivity";

    private Switch onOffSwitch;
    private TextView bpmText;
    private GoogleApiClient mGoogleApiClient;
    Intent intent;
    private final String[] TITLES = new String[] {"Hmmmmm", "Hey", "I think..."};
    private final String[] TEXTS = new String[] {"You should have a rest :)", "Don't be too serious =)", "You might need to calm down :|"};


    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            // message from API client! message from wear! The contents is the heartbeat.

            if(bpmText!=null)
                bpmText.setText(Integer.toString(msg.what));

            if (msg.what >= 90 && msg.what <= 93) {
                Random random = new Random();
                int titlePicker = random.nextInt(3);
                int textPicker = random.nextInt(3);
                NotificationCompat.Builder mBuilder =
                        new NotificationCompat.Builder(getApplicationContext())
                                .setSmallIcon(R.mipmap.ic_launcher)
                                .setContentTitle(TITLES[titlePicker])
                                .setContentText(TEXTS[textPicker]);
                // Creates an explicit intent for an Activity in your app
                Intent resultIntent = new Intent(getApplicationContext(), MainHomeActivity.class);

                // The stack builder object will contain an artificial back stack for the
                // started Activity.
                // This ensures that navigating backward from the Activity leads out of
                // your application to the Home screen.
                TaskStackBuilder stackBuilder = TaskStackBuilder.create(getApplicationContext());
                // Adds the back stack for the Intent (but not the Intent itself)
                stackBuilder.addParentStack(MainHomeActivity.class);
                // Adds the Intent that starts the Activity to the top of the stack
                stackBuilder.addNextIntent(resultIntent);
                PendingIntent resultPendingIntent =
                        stackBuilder.getPendingIntent(
                                0,
                                PendingIntent.FLAG_UPDATE_CURRENT
                        );
                mBuilder.setContentIntent(resultPendingIntent);
                NotificationManager mNotificationManager =
                        (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                // mId allows you to update the notification later on.
                int mId = 0;
                mNotificationManager.notify(mId, mBuilder.build());
            }
        }
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bpmText = (TextView) findViewById(R.id.heartbeat);

        // Parse.enableLocalDatastore(this);
        // Parse.initialize(this);

        intent = new Intent(getApplicationContext(), MainDataListenerService.class);
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

        onOffSwitch = (Switch) findViewById(R.id.onOffSwitch);
        onOffSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    Log.d(TAG, "trying to start service");
                    startService(intent);
                    mGoogleApiClient.connect();
                } else {
                    Log.d(TAG, "trying to disconnect service");
                    mGoogleApiClient.disconnect();
                    stopService(intent);
                }
            }
        });


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopService(intent);
        Log.d(TAG, "trying to destroy service");
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_main, menu);
//        return true;
//    }

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

    @Override
    protected void onResume() {
        super.onResume();
        // register our handler with the DataLayerService. This ensures we get messages whenever the service receives something.
        MainDataListenerService.setHandler(handler);
    }

    @Override
    protected void onPause() {
        // unregister our handler so the service does not need to send its messages anywhere.
        MainDataListenerService.setHandler(null);
        super.onPause();
    }
}
