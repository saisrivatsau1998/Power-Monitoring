package com.example.testingproject;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.Toast;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.StaticLabelsFormatter;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;

public class Graph extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);

        /*float sec = getIntent().getIntExtra("SEC",0);
        int min = getIntent().getIntExtra("MIN",0);*/
        float res = getIntent().getFloatExtra("RES",0);
        float res1 = getIntent().getFloatExtra("RES1",0);



        GraphView graph = (GraphView) findViewById(R.id.graph);
        StaticLabelsFormatter staticLabelsFormatter = new StaticLabelsFormatter(graph);
        staticLabelsFormatter.setHorizontalLabels(new String[] {"BULB","FAN"});
        graph.getGridLabelRenderer().setLabelFormatter(staticLabelsFormatter);

        BarGraphSeries<DataPoint> series = new BarGraphSeries<>(new DataPoint[] {

                new DataPoint(1,res),
                new DataPoint(2,res1),


        });
        graph.addSeries(series);
        graph.getViewport().setMinY(0);
        graph.getViewport().setMaxY(0.09);



        graph.getViewport().setYAxisBoundsManual(true);

        series.setDrawValuesOnTop(true);
        series.setValuesOnTopColor(Color.RED);
        series.setSpacing(50);
    }

}
