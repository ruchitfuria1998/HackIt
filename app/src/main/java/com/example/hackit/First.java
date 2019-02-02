package com.example.hackit;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class First extends AppCompatActivity {

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
                String income_src = e1.getText().toString();
                EditText e2 = findViewById(R.id.incomeamt);
                String income_amt = e2.getText().toString();
            }
        });


        Button expendituresub = findViewById(R.id.expendituresub);
        expendituresub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText e3 = findViewById(R.id.expendituresrc);
                String expenditure_src = e3.getText().toString();
                EditText e4 = findViewById(R.id.expenditureamt);
                String expenditure_amt = e4.getText().toString();
            }
        });


    }

}
