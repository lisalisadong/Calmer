package com.pennapps.calmer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;


/**
 * Activity for viewing a user's profle and in-depth info
 */
public class UserProfileActivity extends AppCompatActivity {

    TextView userNameText;
    TextView activeBPMText;
    TextView sittingBPMText;
    TextView standingBPMText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        final Intent INTENT = getIntent();
        String parseUserName = INTENT.getStringExtra("ParseUserName");

        Log.v("ParseUserName", parseUserName);

        queryParseUserAndSetViews(parseUserName);

        userNameText = (TextView) findViewById(R.id.userNameText);
        sittingBPMText = (TextView) findViewById(R.id.sittingBPMText);
        standingBPMText = (TextView) findViewById(R.id.standingBPMText);
        activeBPMText = (TextView) findViewById(R.id.activeBPMText);

    }

    private void queryParseUserAndSetViews(String parseUserName) {
        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.whereEqualTo("username", parseUserName);
        query.getFirstInBackground(new GetCallback<ParseUser>() {
            public void done(ParseUser parseUser, ParseException e) {
                if (e == null) {
                    setTextViews(parseUser);
                } else {
                    Log.e("ParseException", e.getMessage());
                }
            }
        });
    }

    private void setTextViews(ParseUser parseUser) {
        userNameText.setText((String) parseUser.get("username"));
        activeBPMText.setText((String) parseUser.get("activeBPM"));
        sittingBPMText.setText((String) parseUser.get("sittingBPM"));
        standingBPMText.setText((String) parseUser.get("standingBPM"));
    }



}
