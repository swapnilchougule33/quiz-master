package com.infowithvijay.triviaquizapp2;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;


public class ViewQuestionsActivity extends AppCompatActivity {

    private DatabaseHelper databaseHelper;
    private ListView listViewQuestions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_questions);

        databaseHelper = new DatabaseHelper(this);
        listViewQuestions = findViewById(R.id.listViewQuestions);

        loadQuestions();
    }

    private void loadQuestions() {
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + DatabaseHelper.TABLE_QUESTIONS, null);
        ArrayList<String> questionList = new ArrayList<>();

        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") String questionNumber = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_QUESTION_NUMBER));
                @SuppressLint("Range") String question = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_QUESTION_TEXT));
                @SuppressLint("Range") String option1 = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_OPTION1));
                @SuppressLint("Range") String option2 = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_OPTION2));
                @SuppressLint("Range") String option3 = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_OPTION3));
                @SuppressLint("Range") String option4 = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_OPTION4));
                @SuppressLint("Range") String correctAnswer = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_CORRECT_ANSWER));

                StringBuilder displayText = new StringBuilder("Q" + questionNumber + ": " + question + "\n");

                // Append options dynamically based on availability
                if (!option1.isEmpty()) {
                    displayText.append("1: ").append(option1).append("\n");
                }
                if (!option2.isEmpty()) {
                    displayText.append("2: ").append(option2).append("\n");
                }
                if (!option3.isEmpty()) {
                    displayText.append("3: ").append(option3).append("\n");
                }
                if (!option4.isEmpty()) {
                    displayText.append("4: ").append(option4).append("\n");
                }

                displayText.append("Correct Answer: ").append(correctAnswer);

                questionList.add(displayText.toString());
            } while (cursor.moveToNext());
        } else {
            Toast.makeText(this, "No questions found.", Toast.LENGTH_SHORT).show();
        }
        cursor.close();
        db.close();

        // Set the adapter to display questions in ListView
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, questionList);
        listViewQuestions.setAdapter(adapter);
    }
}