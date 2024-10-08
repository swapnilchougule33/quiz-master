package com.infowithvijay.triviaquizapp2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ChapterList extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private ListView chapterListView;
    private String scholarshipName;
    private String subjectName;
    private TextView subjectTitleTextView; // TextView for displaying subject title

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chapter_list);

        chapterListView = findViewById(R.id.chapterListView);
        subjectTitleTextView = findViewById(R.id.subjectTitle);

        // Get the scholarship name and subject from the intent
        scholarshipName = getIntent().getStringExtra("scholarship");
        subjectName = getIntent().getStringExtra("subject");

        // Set the subject title in the TextView
        subjectTitleTextView.setText("विषय: " + subjectName);

        // Define chapter lists based on scholarship name and subject
        String[] chapters = getChaptersForSubject(scholarshipName, subjectName);

        // Create an adapter for the ListView
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, chapters);

        // Set the adapter to the ListView
        chapterListView.setAdapter(adapter);

        // Set the listener for item clicks
        chapterListView.setOnItemClickListener(this);
    }

    private String[] getChaptersForSubject(String scholarshipName, String subjectName) {
        if (scholarshipName.equals("इ.५वी स्कॉलरशिप परीक्षा") || scholarshipName.equals("इ.५वी नवोदय परीक्षा") || scholarshipName.equals("इ.८वी स्कॉलरशिप परीक्षा") || scholarshipName.equals("इ.८वी नवोदय परीक्षा")) {
            if (subjectName.equals("गणित")) {
                return new String[]{"संख्या", "भूमिती", "मापन", "बीजगणित"};
            } else if (subjectName.equals("विज्ञान")) {
                return new String[]{"वनस्पती", "प्राणी", "पदार्थ", "ऊर्जा"};
            } else if (subjectName.equals("English")) {
                return new String[]{"Grammar", "Vocabulary", "Comprehension"};
            } else if (subjectName.equals("हिन्दी")) {
                return new String[]{"वर्णमाला", "शब्द", "वाक्य", "कविता"};
            }
        } else if (scholarshipName.equals("इ.६वी होमी भाभा स्पर्धा परीक्षा") || scholarshipName.equals("इ.९वी होमी भाभा स्पर्धा परीक्षा")) {
            if (subjectName.equals("विज्ञान")) {
                return new String[]{"गती", "शक्ती", "कार्य आणि ऊर्जा", "प्रकाश"};
            } else if (subjectName.equals("गणित")) {
                return new String[]{"अपूर्णांक", "दशांश", "टक्केवारी", "बीजगणित"};
            }
        } else if (scholarshipName.equals("NMMS स्पर्धा परीक्षा")) {
            if (subjectName.equals("गणित")) {
                return new String[]{"संख्या", "बीजगणित", "भूमिती", "मापन"};
            } else if (subjectName.equals("विज्ञान")) {
                return new String[]{"भौतिकशास्त्र", "रसायनशास्त्र", "जीवशास्त्र"};
            } else if (subjectName.equals("सामाजिक अभ्यास")) {
                return new String[]{"इतिहास", "भूगोल", "नागरिकशास्त्र"};
            } else if (subjectName.equals("English")) {
                return new String[]{"Grammar", "Vocabulary", "Comprehension", "Writing"};
            } else if (subjectName.equals("हिन्दी")) {
                return new String[]{"वर्णमाला", "शब्द", "वाक्य", "कविता", "गद्य"};
            }
        } else if (scholarshipName.equals("MTS स्पर्धा परीक्षा")) {
            if (subjectName.equals("सामान्य जागरूकता")) {
                return new String[]{"इतिहास", "भूगोल", "विज्ञान", "चालू घडामोडी"};
            } else if (subjectName.equals("गणित")) {
                return new String[]{"मूलभूत अंकगणित", "टक्केवारी", "नफा आणि तोटा"};
            } else if (subjectName.equals("तर्क")) {
                return new String[]{"तार्किक तर्क", "मौखिक तर्क", "गैर शाब्दिक तर्क"};
            }
        }
        return new String[]{"कोणतेही अध्याय आढळले नाहीत"}; // Handle unknown scholarship levels and subjects
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String selectedChapter = parent.getItemAtPosition(position).toString();

        // Pass the scholarshipName, subjectName, and selectedChapter to quiz_game activity
        Intent intent = new Intent(this, quiz_game.class);
        intent.putExtra("SCHOLARSHIP_NAME", scholarshipName);
        intent.putExtra("subjectName", subjectName);
        intent.putExtra("chapterName", selectedChapter); // Pass the selected chapter
        startActivity(intent);
    }
}