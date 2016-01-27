package com.pennapps.calmly;


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

    private JSONArray bpmBuffer;
    private ArrayList<Integer> bpmData;

    public DataAnalytics() {
        bpmData = new ArrayList<>();
    }

    public void grabDataInTimeInterval(Date startDate, Date endDate) {
        try {

            ParseQuery<ParseObject> query = ParseQuery.getQuery("BPM");
                query.whereEqualTo("username", ParseUser.getCurrentUser().getUsername());

            query.whereGreaterThanOrEqualTo("createdAt", startDate);
            query.whereLessThanOrEqualTo("createdAt", endDate);
            query.orderByAscending("createdAt");

            List<ParseObject> objs = query.find();
            for (ParseObject o : objs) {
                //Log.d("Parse", "getting row");
                bpmBuffer = o.getJSONArray("BPMData");
                for (int i = 0; i < bpmBuffer.length(); i++) {
                    bpmData.add(bpmBuffer.getInt(i));
                }
            }
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }

    public ArrayList<Integer> getBpmData() {
        return bpmData;
    }


}
