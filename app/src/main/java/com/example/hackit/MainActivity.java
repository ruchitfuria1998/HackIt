package com.example.hackit;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.Highlight;
import com.github.mikephil.charting.utils.PercentFormatter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements OnChartValueSelectedListener {

    int amtt=0;
    vevals global = vevals.getInstance();
    int tott=0;
    int amt=0;
    final ArrayList<BarEntry> valueSet1 = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        HorizontalBarChart chart = (HorizontalBarChart) findViewById(R.id.chart);


        BarData data = new BarData(getXAxisValues(), getDataSet());
        chart.setData(data);
        chart.animateXY(100, 100);
        chart.invalidate();

//       LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver, new IntentFilter("message1"));
        final PieChart pieChart = (PieChart) findViewById(R.id.piechart);
        pieChart.setUsePercentValues(true);

        final ArrayList<Entry> yvalues = new ArrayList<Entry>();
        final ArrayList<String> xVals = new ArrayList<String>();
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Expenditure");

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override

            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    int total=0;
                int i=0;

                for (DataSnapshot childSnapshot : dataSnapshot.getChildren())
                {
                    int amount=0;


                    for (DataSnapshot childSnapshot1 : childSnapshot.getChildren())
                    {
                        Log.d("HELLO",""+childSnapshot1.getValue());
                        amount+= Integer.parseInt(childSnapshot1.getValue().toString());

                    }
                    total+=amount;
                    yvalues.add(new Entry(amount, i++));
                    xVals.add(childSnapshot.getKey());
                    global.setV1e2(total);
                }

                PieDataSet dataSet = new PieDataSet(yvalues, "Expenditure");
                Log.d("DATASET",""+yvalues);
                PieData data = new PieData(xVals, dataSet);
                data.setValueFormatter(new PercentFormatter());
                dataSet.setColors(ColorTemplate.JOYFUL_COLORS);
                Legend legend = pieChart.getLegend();
                legend.setTextSize(12);
                legend.setPosition(Legend.LegendPosition.BELOW_CHART_CENTER);

                pieChart.bringToFront();
                pieChart.setData(data);
                pieChart.invalidate();
                pieChart.setDrawHoleEnabled(true);
                pieChart.setCenterText("Total Expenses:" +total);
                pieChart.setCenterTextSize(10);
                pieChart.setTransparentCircleRadius(30f);
                data.setValueTextSize(13f);
                data.setValueTextColor(Color.DKGRAY);

            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        pieChart.setOnChartValueSelectedListener(this);
//
//        GraphView graph = findViewById(R.id.graph);
//        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(new DataPoint[] {
//                new DataPoint(0, 1),
//                new DataPoint(1, 5),
//                new DataPoint(2, 3),
//                new DataPoint(3, 2),
//                new DataPoint(4, 6)
//        });
//        series.setTitle("Random Curve 1");
//        series.setColor(Color.GREEN);
//        series.setDrawDataPoints(true);
//        series.setDataPointsRadius(10);
//        series.setThickness(8);
//        graph.addSeries(series);
    }
    public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {

        if (e == null)
            return;
        Toast.makeText(getApplicationContext(),"Total Expendtiure: "+ e.getVal(),
                Toast.LENGTH_LONG).show();
        //Log.d("VAL SELECTED",
        //    "Value: " + e.getVal() + ", xIndex: " + e.getXIndex()
        //            + ", DataSet index: " + dataSetIndex);
    }

    @Override
    public void onNothingSelected() {
        Log.i("PieChart", "nothing selected");
    }

    public ArrayList<BarDataSet> getDataSet() {

        ArrayList<BarDataSet> dataSets = new ArrayList<>();
        final ArrayList<BarEntry> valueSet1 = new ArrayList<>();
        final int income=0;
        final DatabaseReference ref0 = FirebaseDatabase.getInstance().getReference("/Income/");
        ref0.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot childSnapshot : dataSnapshot.getChildren())
                {
                    Log.d("oops",""+childSnapshot.getValue());
                    amt += Integer.parseInt(childSnapshot.getValue().toString());

                }


                global.setV1e1(amt);

//                Intent intent= new Intent("message1");
//                intent.putExtra("amt",amt);
//                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        Log.d("Last","ajb" + global.getV1e1());
        amtt = global.getV1e1();
        tott = global.getV1e2();
        Log.d("try",""+amtt);
        BarEntry v1e1 = new BarEntry(amtt, 0); // Jan
        Log.d("SECOND", income+"amount");
        valueSet1.add(v1e1);
        BarEntry v1e2 = new BarEntry(tott, 1); // Feb
        valueSet1.add(v1e2);
        BarDataSet barDataSet1 = new BarDataSet(valueSet1, "Income vs Expenditure");
        Log.d("Last11",valueSet1.toString());
        barDataSet1.setColor(Color.rgb(0, 155, 0));
        dataSets.add(barDataSet1);
        return dataSets;
    }

    private ArrayList<String> getXAxisValues() {
        ArrayList<String> xAxis = new ArrayList<>();
        xAxis.add("Income");
        xAxis.add("Expenditure");
        return xAxis;
    }

//    public BroadcastReceiver mMessageReceiver=new BroadcastReceiver() {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            int amt1 = intent.getIntExtra("amt",0);
//
//            //vv.setV1e1(amt1);
//
//            Log.d("Last",amt1+"ajb" + vv.getV1e1());
//        }
//    };


}

