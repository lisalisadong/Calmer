package com.pennapps.calmer;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.Random;

public class ViewCurrentHeartrateActivity extends AppCompatActivity {

    private TextView bpmText;
    private TextView heartrateStaticText;


    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            // message from API client! message from wear! The contents is the heartbeat.

            if(bpmText!=null)
                bpmText.setText(Integer.toString(msg.what));

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_current_heartrate);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setLogo(R.mipmap.ic_launcher);
        actionBar.setDisplayUseLogoEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

        heartrateStaticText = (TextView) findViewById(R.id.heartrateStaticText);
        Typeface font = Typeface.createFromAsset(getBaseContext().getAssets(), "CanelaBarkPersonal.ttf");
        heartrateStaticText.setTypeface(font);


        bpmText = (TextView) findViewById(R.id.heartrateTextView);
        MainDataListenerService.setHandler(handler);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_view_current_heartrate, menu);
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

//    private void sendNotification() {
//        Random random = new Random();
//        int titlePicker = random.nextInt(3);
//        int textPicker = random.nextInt(3);
//        NotificationCompat.Builder mBuilder =
//                new NotificationCompat.Builder(getApplicationContext())
//                        .setSmallIcon(R.mipmap.ic_launcher)
//                        .setContentTitle(TITLES[titlePicker])
//                        .setContentText(TEXTS[textPicker]);
//        // Creates an explicit intent for an Activity in your app
//        Intent resultIntent = new Intent(getApplicationContext(), MainHomeActivity.class);
//
//        // The stack builder object will contain an artificial back stack for the
//        // started Activity.
//        // This ensures that navigating backward from the Activity leads out of
//        // your application to the Home screen.
//        TaskStackBuilder stackBuilder = TaskStackBuilder.create(getApplicationContext());
//        // Adds the back stack for the Intent (but not the Intent itself)
//        stackBuilder.addParentStack(MainHomeActivity.class);
//        // Adds the Intent that starts the Activity to the top of the stack
//        stackBuilder.addNextIntent(resultIntent);
//        PendingIntent resultPendingIntent =
//                stackBuilder.getPendingIntent(
//                        0,
//                        PendingIntent.FLAG_UPDATE_CURRENT
//                );
//        mBuilder.setContentIntent(resultPendingIntent);
//        NotificationManager mNotificationManager =
//                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//        // mId allows you to update the notification later on.
//        int mId = 0;
//        mNotificationManager.notify(mId, mBuilder.build());
//    }
}
