package com.pennapps.calmer;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import com.androidplot.xy.CatmullRomInterpolator;
import com.androidplot.xy.LineAndPointFormatter;
import com.androidplot.xy.PointLabelFormatter;
import com.androidplot.xy.SimpleXYSeries;
import com.androidplot.xy.XYPlot;
import com.androidplot.xy.XYSeries;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ViewTrendsActivity extends FragmentActivity {

    protected static int startYear, startMonth, startDay, startHour, startMin;
    protected static int endYear, endMonth, endDay, endHour, endMin;
    protected static TextView startDateView;
    protected static TextView endDateView;
    protected static TextView startTimeView;
    protected static TextView endTimeView;

    protected static boolean isStartDate = true;
    protected static boolean isStartTime = true;

    private Button findTrendButton;

    private Calendar calendar;

    private DataAnalytics dataAnalytics;
    private ArrayList<Integer> bpmData;

    private Date startDate;
    private Date endDate;

    private TextView titleText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_trends);

        titleText = (TextView) findViewById(R.id.trendsTitleTextView);
        Typeface font = Typeface.createFromAsset(getAssets(), "CanelaBarkPersonal.ttf");
        titleText.setTypeface(font);

        startDateView = (TextView) findViewById(R.id.startDateTextView);
        endDateView = (TextView) findViewById(R.id.endDateTextView);
        startTimeView = (TextView) findViewById(R.id.startTimeTextView);
        endTimeView = (TextView) findViewById(R.id.endTimeTextView);

        calendar = Calendar.getInstance();
        startYear = calendar.get(Calendar.YEAR);
        startMonth = calendar.get(Calendar.MONTH);
        startDay = calendar.get(Calendar.DAY_OF_MONTH);
        startHour = calendar.get(Calendar.HOUR_OF_DAY);
        startMin = calendar.get(Calendar.MINUTE);
        endYear = startYear; endMonth = startMonth; endDay = startDay; endHour = startHour; endMin = startMin;


        showStartDate(startYear, startMonth, startDay);
        showEndDate(endYear, endMonth, endDay);
        showStartTime(endHour, endMin);
        showEndTime(startHour + 1, endMin);

        dataAnalytics = new DataAnalytics();

        findTrendButton = (Button) findViewById(R.id.findTrendButton);
        Typeface sansFont = Typeface.createFromAsset(getAssets(), "FiraSans-Regular.otf");
        findTrendButton.setTypeface(sansFont);
        findTrendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startDate = new Date(startYear-1900,startMonth,startDay,startHour,startMin);
                endDate = new Date(endYear-1900,endMonth,endDay,endHour,endMin);

                dataAnalytics.grabDataInTimeInterval(startDate, endDate);
                bpmData = dataAnalytics.getBpmData();
                StringBuilder stringBuilder = new StringBuilder();
                for (int i: bpmData) {
                    stringBuilder.append(i).append(", ");
                }
                Log.d("Time Series Data", stringBuilder.toString());

                Intent intent = new Intent(getApplicationContext(), DataPlotActivity.class);
                intent.putIntegerArrayListExtra("bpmData", bpmData);
                startActivity(intent);
            }
        });
    }


    public void showStartDatePickerDialog(View v) {
        isStartDate = true;
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getFragmentManager(), "startDatePicker");
    }

    public void showEndDatePickerDialog(View v) {
        isStartDate = false;
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getFragmentManager(), "endDatePicker");
    }

    private static void showStartDate(int year, int month, int day) {
        startDateView.setText(new StringBuilder().append(month + 1).append("/")
                .append(day).append("/").append(year));
    }

    private static void showEndDate(int year, int month, int day) {
        endDateView.setText(new StringBuilder().append(month + 1).append("/")
                .append(day).append("/").append(year));
    }

    public void showStartTimePickerDialog(View v) {
        isStartTime = true;
        DialogFragment newFragment = new TimePickerFragment();
        newFragment.show(getFragmentManager(), "startTimePicker");
    }

    public void showEndTimePickerDialog(View v) {
        isStartTime = false;
        DialogFragment newFragment = new TimePickerFragment();
        newFragment.show(getFragmentManager(), "endTimePicker");
    }

    private static void showStartTime(int hourOfDay, int minute) {
        String hour, min;
        if (hourOfDay < 10) {
            hour = "0" + Integer.toString(hourOfDay);
        } else {
            hour = Integer.toString(hourOfDay);
        }
        if (minute < 10) {
            min = "0" + Integer.toString(minute);
        } else {
            min = Integer.toString(minute);
        }
        startTimeView.setText(new StringBuilder().append(hour).append(":").append(min));
    }

    private static void showEndTime(int hourOfDay, int minute) {
        if(hourOfDay > 23) hourOfDay = 0;
        String hour, min;
        if (hourOfDay < 10) {
            hour = "0" + Integer.toString(hourOfDay);
        } else {
            hour = Integer.toString(hourOfDay);
        }
        if (minute < 10) {
            min = "0" + Integer.toString(minute);
        } else {
            min = Integer.toString(minute);
        }
        endTimeView.setText(new StringBuilder().append(hour).append(":").append(min));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_view_trends, menu);
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

    public static class DatePickerFragment extends DialogFragment
            implements DatePickerDialog.OnDateSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }

        public void onDateSet(DatePicker view, int year, int month, int day) {
            if (isStartDate) {
                showStartDate(year, month, day);
                startYear = year; startMonth = month; startDay = day;
            } else {
                showEndDate(year, month, day);
                endYear = year; endMonth = month; endDay = day;
            }
        }


    }

    public static class TimePickerFragment extends DialogFragment
            implements TimePickerDialog.OnTimeSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current time as the default values for the picker
            final Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);

            // Create a new instance of TimePickerDialog and return it
            return new TimePickerDialog(getActivity(), this, hour, minute,
                    DateFormat.is24HourFormat(getActivity()));
        }

        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            if (isStartTime) {
                showStartTime(hourOfDay, minute);
                startHour = hourOfDay; startMin = minute;
            } else {
                showEndTime(hourOfDay, minute);
                endHour = hourOfDay; endMin = minute;
            }
        }
    }


}
