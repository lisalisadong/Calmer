package com.pennapps.calmer;

import android.util.Log;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by klyde on 1/23/16.
 */
public class DataAnalytics {

    private final int bpmBufferSize = 20;
    private JSONArray bpmBuffer;
    private List<Integer> bpmData;
    public DataAnalytics() {
        bpmData = new ArrayList<>();
    }

    private void grabDataInTimeInterval(Date d1, Date d2) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("GameScore");
        query.whereEqualTo("user", ParseUser.getCurrentUser());
        query.whereGreaterThan("createdAt", d1);
        query.whereLessThan("createdAt", d2);
        query.findInBackground(new FindCallback<ParseObject>() {
            public void done(List<ParseObject> objs, ParseException pe) {
                if (pe == null) { //found items
                    try {
                        for (ParseObject o : objs) {
                            bpmBuffer = o.getJSONArray("BPMData");
                            for (int i = 0; i < bpmBufferSize; i++) {
                                bpmData.add(bpmBuffer.getInt(i));
                            }
                        }
                    }
                    catch (Exception e) {
                        System.out.println(e.getMessage());
                    }
                } else {
                }
            }
        });
    }
}
