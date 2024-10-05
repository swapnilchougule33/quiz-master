
package com.infowithvijay.triviaquizapp2;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.widget.Toast;

public class OptionQuiz extends AppCompatActivity {

    private TextView questionTextView;
    private TextView questionNumberTextView;
    private RadioGroup optionsGroup;
    private RadioButton option1RadioButton;
    private RadioButton option2RadioButton;
    private RadioButton option3RadioButton;
    private RadioButton option4RadioButton;
    private Button submitButton;
    private Button quitButton;
    private Button previousButton;
    private Button nextButton;
    private TextView timerTextView;
    private int currentQuestionIndex;
    private int score;
    private CountDownTimer countDownTimer;
    private static final long TIMER_DURATION = 60000;
    private static final long TIMER_INTERVAL = 1000;
    private DatabaseHelper databaseHelper;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_option_quiz);

        questionTextView = findViewById(R.id.questionTextView);
        questionNumberTextView = findViewById(R.id.questionNumberTextView);
        optionsGroup = findViewById(R.id.optionsGroup1);
        option1RadioButton = findViewById(R.id.option1RadioButton);
        option2RadioButton = findViewById(R.id.option2RadioButton);
        option3RadioButton = findViewById(R.id.option3RadioButton);
        option4RadioButton = findViewById(R.id.option4RadioButton);
        submitButton = findViewById(R.id.submitButton);
        quitButton = findViewById(R.id.quitButton);
        previousButton = findViewById(R.id.previousButton);
        nextButton = findViewById(R.id.nextButton);
        timerTextView = findViewById(R.id.timerTextView);

        databaseHelper = new DatabaseHelper(this);



        submitButton.setOnClickListener(v -> {
            int selectedOptionId = optionsGroup.getCheckedRadioButtonId();
            if (selectedOptionId == -1) {
                Toast.makeText(OptionQuiz.this, "Please select an option.", Toast.LENGTH_SHORT).show();
                return;
            }

            RadioButton selectedRadioButton = findViewById(selectedOptionId);
            String selectedAnswer = selectedRadioButton.getText().toString();dismissKeyboardShortcutsHelper();
            Toast.makeText(this, ""+selectedAnswer, Toast.LENGTH_SHORT).show();
            handleSubmitAnswer(selectedAnswer);
        });

        previousButton.setOnClickListener(v -> loadPreviousQuestion());
        nextButton.setOnClickListener(v -> loadNextQuestion());

        quitButton.setOnClickListener(v -> quitQuiz());

        loadQuestion(1);
    }

    private void loadQuestion(int questionNumber) {
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        Cursor questionCursor = db.rawQuery("SELECT * FROM " + DatabaseHelper.TABLE_QUESTIONS +
                " WHERE " + DatabaseHelper.COLUMN_QUESTION_NUMBER + " = ?", new String[]{String.valueOf(questionNumber)});

        if (questionCursor != null && questionCursor.moveToFirst()) {
            @SuppressLint("Range") String questionText = questionCursor.getString(questionCursor.getColumnIndex(DatabaseHelper.COLUMN_QUESTION_TEXT));
            @SuppressLint("Range") String option1 = questionCursor.getString(questionCursor.getColumnIndex(DatabaseHelper.COLUMN_OPTION1));
            @SuppressLint("Range") String option2 = questionCursor.getString(questionCursor.getColumnIndex(DatabaseHelper.COLUMN_OPTION2));
            @SuppressLint("Range") String option3 = questionCursor.getString(questionCursor.getColumnIndex(DatabaseHelper.COLUMN_OPTION3));
            @SuppressLint("Range") String option4 = questionCursor.getString(questionCursor.getColumnIndex(DatabaseHelper.COLUMN_OPTION4));


            for (int i = 0; i < optionsGroup.getChildCount(); i++) {
                View child = optionsGroup.getChildAt(i);
                if (child instanceof RadioButton) {
                    RadioButton radioButton = (RadioButton) child;
                    radioButton.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                }
            }

            questionTextView.setText(questionText);
            option1RadioButton.setText(option1);
            option2RadioButton.setText(option2);
            option3RadioButton.setText(option3);
            option4RadioButton.setText(option4);

            int optionCount = 0;
//            if (!option1.isEmpty()) {
//                option1RadioButton.setVisibility(View.VISIBLE);
//                optionCount++;
//            } else {
//                option1RadioButton.setVisibility(View.GONE);
//            }
//
//            if (!option2.isEmpty()) {
//                option2RadioButton.setVisibility(View.VISIBLE);
//                optionCount++;
//            } else {
//                option2RadioButton.setVisibility(View.GONE);
//            }

            if (!option3.isEmpty()) {
                option3RadioButton.setVisibility(View.VISIBLE);
                optionCount++;
            } else {
                option3RadioButton.setVisibility(View.GONE);
            }

            if (!option4.isEmpty()) {
                option4RadioButton.setVisibility(View.VISIBLE);
                optionCount++;
            } else {
                option4RadioButton.setVisibility(View.GONE);
            }



            questionNumberTextView.setText("Q " + questionNumber + ".");

            this.currentQuestionIndex = questionNumber;

            previousButton.setEnabled(questionNumber > 1);
        } else {
            Toast.makeText(this, "No question found.", Toast.LENGTH_SHORT).show();
        }
        if (questionCursor != null) {
            questionCursor.close();
        }
        db.close();
    }

    private void loadNextQuestion() {
        loadQuestion(currentQuestionIndex + 1);

    }

    private void loadPreviousQuestion() {
        loadQuestion(currentQuestionIndex - 1);
    }

    private void handleSubmitAnswer(String answer) {
        String correctAnswer = getCorrectAnswer(currentQuestionIndex);

        Log.d("OptionQuiz", "Submitted Answer: '" + answer + "'");
        Log.d("OptionQuiz", "Correct Answer: '" + correctAnswer + "'");

        // Reset background color for all RadioButtons
        for (int i = 0; i < optionsGroup.getChildCount(); i++) {
            View child = optionsGroup.getChildAt(i);
            if (child instanceof RadioButton) {
                RadioButton radioButton = (RadioButton) child;
                radioButton.setBackgroundColor(getResources().getColor(android.R.color.transparent));
            }
        }

        RadioButton selectedRadioButton = findViewById(optionsGroup.getCheckedRadioButtonId());
        if (selectedRadioButton != null) {
            String selectedAnswerText = selectedRadioButton.getText().toString().trim();

            // Use equalsIgnoreCase for comparison
            if (selectedAnswerText.equalsIgnoreCase(correctAnswer)) {
                score++;
                selectedRadioButton.setBackgroundColor(getResources().getColor(R.color.green));
                Toast.makeText(this, "Correct!", Toast.LENGTH_SHORT).show();

                submitButton.postDelayed(this::loadNextQuestion, 1000);

            } else {
                selectedRadioButton.setBackgroundColor(getResources().getColor(R.color.red2));
                Toast.makeText(this, "Incorrect! The correct answer is: " + correctAnswer, Toast.LENGTH_SHORT).show();
            }
        }


    }


    @SuppressLint("Range")
    private String getCorrectAnswer(int questionNumber) {
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + DatabaseHelper.COLUMN_CORRECT_ANSWER + " FROM " + DatabaseHelper.TABLE_QUESTIONS +
                " WHERE " + DatabaseHelper.COLUMN_QUESTION_NUMBER + " = ?", new String[]{String.valueOf(questionNumber)});

        String correctAnswer = "";
        if (cursor != null && cursor.moveToFirst()) {
            correctAnswer = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_CORRECT_ANSWER));
            cursor.close();
        }
        db.close();
        return correctAnswer != null ? correctAnswer : "";
    }

    private void quitQuiz() {
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}

