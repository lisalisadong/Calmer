package com.pennapps.calmer;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;

import com.google.android.gms.wearable.MessageApi;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.WearableListenerService;
import com.parse.ParseObject;
import com.google.android.gms.common.api.GoogleApiClient;
import android.os.Bundle;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.wearable.Wearable;

/**
 * Created by QingxiaoDong on 1/23/16.
 */
public class MainDataListenerService extends WearableListenerService implements MessageApi.MessageListener{
    private boolean isMainServiceOn = false;
    private static final String LOG_TAG = "CalmerPhoneService";
    public static final String HEARTBEAT = "HEARTBEAT";

    private static Handler handler;
    private static int currentValue=0;

    private int bpmBufferSize = 20;
    private int[] bpmBuffer = new int[bpmBufferSize];
    private int bpmBufferIndex = 0;
    protected GoogleApiClient mGoogleApiClient;
    //protected MessageApi.MessageListener messageListener;


    public MainDataListenerService() {}
    public boolean getServiceStatus() {
        return isMainServiceOn;
    }
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
        isMainServiceOn = true;
        // We want this service to continue running until it is explicitly
        // stopped, so return sticky.

        //Wearable.MessageApi.addListener(mGoogleApiClient, messageListener);
        Log.d(LOG_TAG, "Started service");
        return Service.START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        isMainServiceOn = false;
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
        //updateBpmBuffer(currentValue);
        if(handler!=null) {
            // if a handler is registered, send the value as new message
            handler.sendEmptyMessage(currentValue);
        }
    }

    private void updateBpmBuffer(int value) {
        if (bpmBufferIndex < bpmBufferSize - 1) {
            bpmBuffer[bpmBufferIndex] = value;
            bpmBufferIndex++;
        } else {

            //buffer is filled, time to upload to Parse!
            ParseObject obj = new ParseObject("BPM");
            obj.put("BPMData", bpmBuffer);
            obj.saveInBackground();

            bpmBufferIndex = 0;
            bpmBuffer = new int[bpmBufferSize];
            bpmBuffer[bpmBufferIndex] = value;
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
}
