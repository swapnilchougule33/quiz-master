package com.infowithvijay.triviaquizapp2;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class MainActivity extends AppCompatActivity {
    private CardView scholarship1, scholarship2, scholarship3, scholarship4, scholarship5, scholarship6, scholarship7, scholarship8;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize CardViews
        scholarship1 = findViewById(R.id.Scholarship_1);
        scholarship2 = findViewById(R.id.Scholarship_2);
        scholarship3 = findViewById(R.id.Scholarship_3);
        scholarship4 = findViewById(R.id.Scholarship_4);
        scholarship5 = findViewById(R.id.Scholarship_5);
        scholarship6 = findViewById(R.id.Scholarship_6);
        scholarship7 = findViewById(R.id.Scholarship_7);
        scholarship8 = findViewById(R.id.Scholarship_8);

        // Set up click listeners for each CardView
        scholarship1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openScholarshipDetails("इ.५वी स्कॉलरशिप परीक्षा");
            }
        });

        scholarship2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openScholarshipDetails("इ.५वी नवोदय परीक्षा");
            }
        });

        scholarship3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openScholarshipDetails("इ.६वी होमी भाभा स्पर्धा परीक्षा");
            }
        });

        scholarship4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openScholarshipDetails("इ.८वी स्कॉलरशिप परीक्षा");
            }
        });

        scholarship5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openScholarshipDetails("इ.८वी नवोदय परीक्षा");
            }
        });

        scholarship6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openScholarshipDetails("इ.९वी होमी भाभा स्पर्धा परीक्षा");
            }
        });

        scholarship7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openScholarshipDetails("NMMS स्पर्धा परीक्षा");
            }
        });

        scholarship8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openScholarshipDetails("MTS स्पर्धा परीक्षा");
            }
        });
    }

    // Method to handle scholarship details page or action
    private void openScholarshipDetails(String scholarshipName) {
        Toast.makeText(this, "" + scholarshipName, Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(MainActivity.this, SubjectList.class);
        intent.putExtra("SCHOLARSHIP_NAME", scholarshipName);
        startActivity(intent);
    }
}