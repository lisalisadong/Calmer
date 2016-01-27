package com.pennapps.calmly;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.dd.CircularProgressButton;
import android.view.View;
import android.util.Log;

import android.os.Handler;
/**
 * Created by QingxiaoDong on 1/24/16.
 */
public class CalibrationActivity extends AppCompatActivity{

    static final int timer = 20;
    boolean active ;
    CircularProgressButton circularProgressButton;
    private Handler mHandler = new Handler();
    int waited = 0;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calibration);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setLogo(R.mipmap.ic_launcher);
        actionBar.setDisplayUseLogoEnabled(true);
        actionBar.setDisplayShowHomeEnabled(true);

//        InputStream stream = null;
//        try {
//            stream = getAssets().open("piggy.gif");
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        WebView view = new WebView(this, "file:///android_asset    /piggy.gif");
//
//        setContentView(view);

        circularProgressButton = (CircularProgressButton) findViewById(R.id.btnWithText);
        circularProgressButton.setIndeterminateProgressMode(true);
        circularProgressButton.setProgress(0);
        circularProgressButton.setOnClickListener(new View.OnClickListener() {
        //@Override
        //public void onClick(View v) {
//                MainDataListenerService.calibrating = true;
//                while (true) {
//                    Log.d("CalibrationProgress", String.valueOf(MainDataListenerService.calibrationCounter));
//                    circularProgressButton.setProgress(100 * MainDataListenerService.calibrationCounter / 50);
//                    if(MainDataListenerService.calibrationCounter == 50) break;
//                }
            @Override
            public void onClick (View v) {
                if (circularProgressButton.getProgress() == 0) {
                    MainDataListenerService.calibrating = true;
                    new Thread(new Runnable() {
                        public void run() {
                            active = true;
                            try {
                                waited = MainDataListenerService.calibrationCounter;
                                while (active && waited < timer) {
                                    // sleep(200);
                                    if (active) {
                                        waited = MainDataListenerService.calibrationCounter;
                                        mHandler.post(new Runnable() {
                                            public void run() {
                                                updateProgress(waited);
                                            }
                                        });
                                    }
                                }
                            } catch (Exception e) {
                            } finally {
                                onContinue();
                            }
                        }
                    }).start();
                }
                if (circularProgressButton.getProgress() == 100) {
                    finish();
                }
            }
        });
    }

    public void onContinue() {
        Log.d ("abc", "abc");
    }



    public void updateProgress(final int timePassed) {
        if (circularProgressButton != null) {
            final int progress = 100 *  timePassed / timer;
            circularProgressButton.setProgress(progress);
        }
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
}
