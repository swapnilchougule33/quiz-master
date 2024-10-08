package com.infowithvijay.triviaquizapp2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView; // Import TextView
import androidx.appcompat.app.AppCompatActivity;

public class SubjectList extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private ListView subjectListView;
    private String scholarshipName;
    private TextView scholarshipTitleTextView; // Add TextView to display title

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subject_list);

        subjectListView = findViewById(R.id.subjectListView);
        scholarshipTitleTextView = findViewById(R.id.scholarshipTitle); // Get reference to TextView

        // Get the scholarship name from the intent
        scholarshipName = getIntent().getStringExtra("SCHOLARSHIP_NAME");

        // Set the scholarship title in the TextView
        scholarshipTitleTextView.setText("विषय: " + scholarshipName);

        // Define subject lists based on scholarship name
        String[] subjects = getSubjectsForScholarship(scholarshipName);

        // Create an adapter for the ListView
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, subjects);

        // Set the adapter to the ListView
        subjectListView.setAdapter(adapter);

        // Set the listener for item clicks
        subjectListView.setOnItemClickListener(this);
    }

    private String[] getSubjectsForScholarship(String scholarshipName) {
        if (scholarshipName.equals("इ.५वी स्कॉलरशिप परीक्षा")) {
            return new String[]{"Maths", "Science", "English", "Hindi"};
        } else if (scholarshipName.equals("इ.५वी नवोदय परीक्षा")) {
            return new String[]{"Maths", "Science", "English", "Hindi"};
        } else if (scholarshipName.equals("इ.६वी होमी भाभा स्पर्धा परीक्षा")) {
            return new String[]{"Science", "Maths"};
        } else if (scholarshipName.equals("इ.८वी स्कॉलरशिप परीक्षा")) {
            return new String[]{"Maths", "Science", "English", "Hindi"};
        } else if (scholarshipName.equals("इ.८वी नवोदय परीक्षा")) {
            return new String[]{"Maths", "Science", "English", "Hindi"};
        } else if (scholarshipName.equals("इ.९वी होमी भाभा स्पर्धा परीक्षा")) {
            return new String[]{"Science", "Maths"};
        } else if (scholarshipName.equals("NMMS स्पर्धा परीक्षा")) {
            return new String[]{"Maths", "Science", "Social Studies", "English", "Hindi"};
        } else if (scholarshipName.equals("MTS स्पर्धा परीक्षा")) {
            return new String[]{"General Awareness", "Maths", "Reasoning"};
        } else {
            return new String[]{"No Subjects Found"};
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String selectedSubject = parent.getItemAtPosition(position).toString();

        // Start the ChapterListActivity with the selected subject and scholarship name
        Intent intent = new Intent(this, ChapterList.class);
        intent.putExtra("subject", selectedSubject);
        intent.putExtra("scholarship", scholarshipName);
        startActivity(intent);
    }
}