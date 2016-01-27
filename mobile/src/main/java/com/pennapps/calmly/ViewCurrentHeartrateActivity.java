package com.pennapps.calmly;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;
import android.widget.TextView;

public class ViewCurrentHeartrateActivity extends Activity {

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
//        ActionBar actionBar = getSupportActionBar();
//        actionBar.setLogo(R.mipmap.ic_launcher);
//        actionBar.setDisplayUseLogoEnabled(true);
//        actionBar.setDisplayShowHomeEnabled(true);

        heartrateStaticText = (TextView) findViewById(R.id.heartrateStaticText);
        Typeface font = Typeface.createFromAsset(getBaseContext().getAssets(), "CanelaBarkPersonal.ttf");
        heartrateStaticText.setTypeface(font);


        bpmText = (TextView) findViewById(R.id.heartrateTextView);
        MainDataListenerService.setHandler(handler);

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
