package com.example.hackit;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

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
    int sumincome = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);

        Button analysis = findViewById(R.id.checkanalysis);
        analysis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(First.this, MainActivity.class);
                startActivity(intent);
            }
        });


        Button incomesub = findViewById(R.id.incomesub);
        incomesub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText e1 = findViewById(R.id.incomesrc);
                final String income_src = e1.getText().toString();
                EditText e2 = findViewById(R.id.incomeamt);
                final String income_amt = e2.getText().toString();
                final DatabaseReference ref = FirebaseDatabase.getInstance().getReference("/Income/");
//                ref.addListenerForSingleValueEvent(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                        for(DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
//                            String inc = dataSnapshot1.child("Amount").getValue().toString();
//                            sumincome += Float.parseFloat(inc);
//                            TextView income = findViewById(R.id.income);
//                            income.setText("Rs " + sumincome);
//                        }
//                    }
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                    }
//                });
                ref.child("Income Source").setValue(income_src);
                ref.child("Amount").setValue(income_amt);
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
//                int sumexpenditure = 0;
//                sumexpenditure += Float.parseFloat(expenditure_amt);
//                TextView income = findViewById(R.id.expenditure);
//                income.setText("Rs " + sumexpenditure);
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
}