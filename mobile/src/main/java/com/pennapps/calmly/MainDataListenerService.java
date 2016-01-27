package com.pennapps.calmly;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.WearableListenerService;
import com.parse.ParseObject;
import com.google.android.gms.common.api.GoogleApiClient;
import com.parse.ParseUser;

import java.util.List;
import java.util.ArrayList;
import java.util.Random;

/**
 * Created by QingxiaoDong on 1/23/16.
 */
public class MainDataListenerService extends WearableListenerService implements MessageApi.MessageListener{

    private static final String LOG_TAG = "CalmerPhoneService";
    public static final String HEARTBEAT = "HEARTBEAT";

    private static Handler handler;
    private static int currentValue=0;

    private final int bpmBufferSize = 60;
    private List<Integer> bpmBuffer = new ArrayList<>();
    private int bpmBufferIndex = 0;

    private final String[] TITLES = new String[] {"Hmmmmm", "Hey", "I think..."};
    private final String[] TEXTS = new String[] {"You should have a rest :)", "Don't be too serious =)", "You might need to calm down :|"};

    static boolean calibrating = false;
    static int calibrationCounter = 0;
    private int calibrationSum = 0;
    private User user;

    protected GoogleApiClient mGoogleApiClient;
    //protected MessageApi.MessageListener messageListener;

    public static Handler getHandler() {
        return handler;
    }

    public static void setHandler(Handler handler) {
        MainDataListenerService.handler = handler;
        // send current value as initial value.
        if(handler!=null)
            handler.sendEmptyMessage(currentValue);

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        ((Calmly) this.getApplication()).setMainServiceStatus(true);
        user = new User();
        // We want this service to continue running until it is explicitly
        // stopped, so return sticky.

        //Wearable.MessageApi.addListener(mGoogleApiClient, messageListener);
        Log.d(LOG_TAG, "Started service");
        return Service.START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ((Calmly) this.getApplication()).setMainServiceStatus(false);
        Log.d(LOG_TAG, "service destroyed");
    }

    @Override
    public void onPeerConnected(Node peer) {
        super.onPeerConnected(peer);

        String id = peer.getId();
        String name = peer.getDisplayName();

        Log.d(LOG_TAG, "Connected peer name & ID: " + name + "|" + id);
    }
    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        super.onMessageReceived(messageEvent);
        Log.d(LOG_TAG, "received a message from wear: " + messageEvent.getPath());
        // save the new heartbeat value
        currentValue = Integer.parseInt(messageEvent.getPath());

        //if the service is "on"
        if (((Calmly) this.getApplication()).getMainServiceStatus()) {
            if (currentValue > user.getExcitedBPM()) {
                sendNotification();
            }
            updateBpmBuffer(currentValue);
            if (calibrating) {
                calibrate(currentValue);
            }
            //Log.d(LOG_TAG, "calibrating: " + calibrating);
            //Log.d(LOG_TAG, "counter: " + calibrationCounter);
            if (handler != null) {
                // if a handler is registered, send the value as new message
                handler.sendEmptyMessage(currentValue);
            }
        }
    }

    private void calibrate(int value) {
        if (calibrationCounter < 20) {
            calibrationSum += value;
            calibrationCounter++;
        } else {
            try {
                user = (User) ParseUser.getCurrentUser();
                user.setExcitedBPM(calibrationSum / calibrationCounter);
                user.saveInBackground();
                Toast.makeText(getApplicationContext(),
                        "Your excited heart rate is updated to " + calibrationSum / calibrationCounter + " bpm.",
                        Toast.LENGTH_LONG).show();
                calibrating = false;
                calibrationCounter = 0;
                Log.d(LOG_TAG, "uploaded active bpm to parse");

            }
            catch (Exception e) {
                Log.d(LOG_TAG, e.toString());
            }
        }

    }

    private void updateBpmBuffer(int value) {
        if (bpmBufferIndex < bpmBufferSize - 1) {
            bpmBuffer.add(value);
            bpmBufferIndex++;
        } else {
            //buffer is filled, time to upload to Parse!
            try {

                ParseObject obj = ParseObject.create("BPM");
                obj.put("BPMData", bpmBuffer);
                obj.put("username", ParseUser.getCurrentUser().getUsername());
                obj.saveInBackground();
                Log.d(LOG_TAG, "uploaded data to Parse");
            }
            catch (Exception e) {
                Log.d(LOG_TAG, e.getMessage());
            }

            bpmBufferIndex = 0;
            bpmBuffer = new ArrayList<>();
            bpmBuffer.add(value);
            bpmBufferIndex++;
        }
    }

    /*
    private void sendNotification() {

        if (mGoogleApiClient.isConnected()) {
            PutDataMapRequest dataMapRequest = PutDataMapRequest.create(Constants.NOTIFICATION_PATH);
            // Make sure the data item is unique. Usually, this will not be required, as the payload
            // (in this case the title and the content of the notification) will be different for almost all
            // situations. However, in this example, the text and the content are always the same, so we need
            // to disambiguate the data item by adding a field that contains teh current time in milliseconds.
            dataMapRequest.getDataMap().putDouble(Constants.NOTIFICATION_TIMESTAMP, System.currentTimeMillis());
            dataMapRequest.getDataMap().putString(Constants.NOTIFICATION_TITLE, "This is the title");
            dataMapRequest.getDataMap().putString(Constants.NOTIFICATION_CONTENT, "This is a notification with some text.");
            PutDataRequest putDataRequest = dataMapRequest.asPutDataRequest();
            Wearable.DataApi.putDataItem(mGoogleApiClient, putDataRequest);
        }
        else {
            Log.e(TAG, "No connection to wearable available!");
        }
    }
    */

    private void sendNotification() {
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
