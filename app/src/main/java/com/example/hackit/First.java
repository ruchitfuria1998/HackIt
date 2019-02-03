package com.example.hackit;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class First extends AppCompatActivity {

    int size;
    String amtstr = null;
    int sumincome = 0;
    int total=0,amt=0,bal=0;
    int amt1=0,tot = 0,exp=0,inc1=0;
    String income_amt = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);
        final TextView income = findViewById(R.id.income);
        final TextView expense = findViewById(R.id.expenditure);
        final TextView balance = findViewById(R.id.balance);
        Button analysis = findViewById(R.id.checkanalysis);
        analysis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(First.this, MainActivity.class);
                startActivity(intent);

            }
        });
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver, new IntentFilter("message"));

        Button incomesub = findViewById(R.id.incomesub);
        incomesub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                EditText e1 = findViewById(R.id.incomesrc);
//                final String income_src = e1.getText().toString();
                EditText e2 = findViewById(R.id.incomeamt);
                income_amt = e2.getText().toString();
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy-hh-mm-ss");
                String format = simpleDateFormat.format(new Date());
                final DatabaseReference ref = FirebaseDatabase.getInstance().getReference("/Income/");
                ref.child(format).setValue(income_amt);
                Intent intent= new Intent("message");
                intent.putExtra("inc",income_amt);
                LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
                Toast.makeText(getApplicationContext(),"Income added Rs."+ income_amt,
                        Toast.LENGTH_LONG).show();
                income.setText(""+(amt+Integer.parseInt(income_amt)));

            }
        });
        final DatabaseReference ref0 = FirebaseDatabase.getInstance().getReference("/Income/");
        ref0.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot childSnapshot : dataSnapshot.getChildren())
                {
                    Log.d("oops",""+childSnapshot.getValue());
                    amt += Integer.parseInt(childSnapshot.getValue().toString());
                    Log.d("First", amt+"amount");
                    String amtstr = String.valueOf(amt);
                    Intent intent= new Intent("message");
                    intent.putExtra("amt",amtstr);

                    LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
                }
                income.setText(""+ amt);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        final DatabaseReference ref = FirebaseDatabase.getInstance().getReference("/Expenditure/");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
                                               @Override
                                               public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                   for (DataSnapshot childSnapshot : dataSnapshot.getChildren())
                                                   {
                                                       int amount=0;
                                                       for (DataSnapshot childSnapshot1 : childSnapshot.getChildren())
                                                       {
                                                           Log.d("HELLO",""+childSnapshot1.getValue());
                                                           amount+= Integer.parseInt(childSnapshot1.getValue().toString());

                                                       }
                                                       total+=amount;
                                                   }
                                                Log.d("First",total+"hi");
                                                   expense.setText(""+total);
                                                   Log.d("First",amt +"ouuuu" + total);
                                                   String totstr = String.valueOf(total);
                                                   Intent intent= new Intent("message");
                                                   intent.putExtra("total",totstr);
                                                   LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
                                                   //bal = amt - total;
                                                   //balance.setText(""+bal);
                                               }
                                               @Override
                                               public void onCancelled(@NonNull DatabaseError databaseError) {

                                               }

                                           });


                Button expendituresub = findViewById(R.id.expendituresub);
        expendituresub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int i;
                EditText e3 = findViewById(R.id.expendituresrc);
                String expenditure_src = e3.getText().toString();
                EditText e4 = findViewById(R.id.expenditureamt);
                String expenditure_amt = e4.getText().toString();


                List<String[]> csvContent = readCVSFromAssetFolder();
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy-hh-mm-ss");
                String format = simpleDateFormat.format(new Date());
                for (i=0;i<csvContent.size();i++)
                {
                    String [] rows = csvContent.get(i);
                    Log.d("First",rows[0]+rows[1]);
                    if(expenditure_src.equalsIgnoreCase(rows[0]))
                    {
                        expenditure_src= rows[1];
                    }
                }
                final DatabaseReference ref = FirebaseDatabase.getInstance().getReference("/Expenditure/"+expenditure_src);
                ref.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override

                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        size = (int) dataSnapshot.getChildrenCount();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

                ref.child(format).setValue(expenditure_amt);
                Toast.makeText(getApplicationContext(),"Money spent Rs."+ expenditure_amt,
                        Toast.LENGTH_LONG).show();
                        Log.d("First",total+"hi");
                        expense.setText(""+(total+Integer.parseInt(expenditure_amt)));
                        String totstr = String.valueOf(total);
                        Intent intent= new Intent("message");
                        intent.putExtra("total",totstr);
                        intent.putExtra("exp",expenditure_amt);
                        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);

            }
        });

    }
    private List<String[]> readCVSFromAssetFolder(){
        List<String[]> csvLine = new ArrayList<>();
        String[] content;
        try {
            InputStream inputStream = getResources().getAssets().open("expenditureSource.csv");
            BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
            String line = "";
            while((line = br.readLine()) != null){
                content = line.split(",");
                csvLine.add(content);
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return csvLine;
    }
    public BroadcastReceiver mMessageReceiver=new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int balance;
            if(intent.getStringExtra("amt") != null) {
                amt1 = Integer.parseInt(intent.getStringExtra("amt"));
            }
            if(intent.getStringExtra("total") !=null){tot = Integer.parseInt(intent.getStringExtra("total"));}
            balance = amt1 - tot;
            if(intent.getStringExtra("exp")!=null) {
                exp = Integer.parseInt(intent.getStringExtra("exp"));
                balance = amt1 - tot - exp;
            }
            if(intent.getStringExtra("inc") != null){
                inc1=Integer.parseInt(intent.getStringExtra("inc"));
                balance = amt1 - tot + inc1;
            }
            TextView bal= findViewById(R.id.balance);
            bal.setText(""+ balance);
        }
    };
}