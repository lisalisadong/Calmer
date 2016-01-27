package com.pennapps.calmly;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.androidplot.xy.CatmullRomInterpolator;
import com.androidplot.xy.LineAndPointFormatter;
import com.androidplot.xy.PointLabelFormatter;
import com.androidplot.xy.SimpleXYSeries;
import com.androidplot.xy.XYPlot;
import com.androidplot.xy.XYSeries;

import java.util.ArrayList;

public class DataPlotActivity extends Activity {

    private XYPlot plot;
    private LineAndPointFormatter plotFormatter;
    private ArrayList<Integer> bpmData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_plot);
        Intent intent = getIntent();
        bpmData = intent.getIntegerArrayListExtra("bpmData");
        configurePlot();
        plot();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_data_plot, menu);
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

    private void configurePlot() {
        // initialize our XYPlot reference:
        plot = (XYPlot) findViewById(R.id.plot);

        // create formatters to use for drawing a series using LineAndPointRenderer
        // and configure them from xml:
        plotFormatter = new LineAndPointFormatter();
        plotFormatter.setPointLabelFormatter(new PointLabelFormatter());
        plotFormatter.configure(getApplicationContext(),
                R.xml.line_point_formatter_with_labels);

        // just for fun, add some smoothing to the lines:
        // see: http://androidplot.com/smooth-curves-and-androidplot/
        plotFormatter.setInterpolationParams(
                new CatmullRomInterpolator.Params(10, CatmullRomInterpolator.Type.Centripetal));

        // rotate domain labels 45 degrees to make them more compact horizontally:
        //plot.getGraphWidget().setDomainLabelOrientation(-45);

        // reduce the number of range labels
        plot.setTicksPerRangeLabel(3);


    }

    private void plot() {
        // turn the above arrays into XYSeries':
        // (Y_VALS_ONLY means use the element index as the x value)
        XYSeries series1 = new SimpleXYSeries(bpmData,
                SimpleXYSeries.ArrayFormat.Y_VALS_ONLY, "BPM");

        // add a new series' to the xyplot:
        plot.addSeries(series1, plotFormatter);
    }
}
