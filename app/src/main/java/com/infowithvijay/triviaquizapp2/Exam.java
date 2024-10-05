package com.infowithvijay.triviaquizapp2;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;

public class Exam extends AppCompatActivity {
    private DatabaseHelper databaseHelper;
    private TextView questionNumberTextView;
    private TextView questionTextView;
    private RadioGroup optionsGroup;
    private RadioButton option1RadioButton, option2RadioButton, option3RadioButton, option4RadioButton;
    private Button submitButton;
    private Button skipButton;
    private Button finishButton;
    private GridLayout questionButtonsLayout;
    private TextView timerTextView;
    private int currentQuestionIndex = 1;
    private int totalQuestions = 30;
    private int correctAnswers = 0;
    private int answeredQuestions = 0;
    private CountDownTimer countDownTimer;
    private long timeLeftInMillis = 30 * 60 * 1000;
    private String[] selectedAnswers;
    private TextView scoreTextView;
    private Animation blinkAnimation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exam);


        questionNumberTextView = findViewById(R.id.questionNumberTextView);
        questionTextView = findViewById(R.id.questionTextView);
        scoreTextView = findViewById(R.id.finalScoreTextView);
        scoreTextView = findViewById(R.id.finalScoreTextView);
        optionsGroup = findViewById(R.id.optionsGroup);
        option1RadioButton = findViewById(R.id.option1RadioButton);
        option2RadioButton = findViewById(R.id.option2RadioButton);
        option3RadioButton = findViewById(R.id.option3RadioButton);
        option4RadioButton = findViewById(R.id.option4RadioButton);
        submitButton = findViewById(R.id.buttonSubmit);
        skipButton = findViewById(R.id.buttonSkip);
        finishButton = findViewById(R.id.buttonFinish);
        questionButtonsLayout = findViewById(R.id.questionButtonsLayout);
        timerTextView = findViewById(R.id.timerTextView);
        selectedAnswers = new String[totalQuestions];

        blinkAnimation = AnimationUtils.loadAnimation(this, R.anim.blink);

        databaseHelper = new DatabaseHelper(this);

        startTimer();

        createQuestionButtons();

        loadQuestion(currentQuestionIndex);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleSubmit();
            }
        });

        skipButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                skipButton();

            }
        });

        finishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishQuiz();
            }
        });
    }

    private void skipButton() {

        if (currentQuestionIndex < totalQuestions){
            loadNextQuestion();
        } else {
            Toast.makeText(this, "No more questions to skip", Toast.LENGTH_SHORT).show();
        }
    }

    private void startTimer() {
        countDownTimer = new CountDownTimer(timeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeftInMillis = millisUntilFinished;
                updateTimer();
            }

            @Override
            public void onFinish() {
                Toast.makeText(Exam.this, "Time's up!", Toast.LENGTH_SHORT).show();

                timerTextView.clearAnimation();
                finishQuiz();
            }
        }.start();
    }

    private void updateTimer() {
        int minutes = (int) (timeLeftInMillis / 1000) / 60;
        int seconds = (int) (timeLeftInMillis / 1000) % 60;

        String timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
        timerTextView.setText("" + timeLeftFormatted);

        if (timeLeftInMillis <= 300000) { // 300,000 milliseconds = 5 minutes
            timerTextView.setTextColor(getResources().getColor(R.color.red2)); // Change to your desired color
        } else {
            timerTextView.setTextColor(getResources().getColor(android.R.color.black)); // Default color
        }

        //timerTextView.startAnimation(blinkAnimation);
    }

    private void finishQuiz() {

        if (countDownTimer != null) {
            countDownTimer.cancel();
        }

        timerTextView.clearAnimation();

        submitButton.setEnabled(false);
        skipButton.setEnabled(false);
        finishButton.setEnabled(false);

        String finalScore = "Score: " + correctAnswers + " / " + totalQuestions;
        scoreTextView.setText(finalScore);
        scoreTextView.setVisibility(View.VISIBLE);


        for (int i = 1; i <= totalQuestions; i++) {
            SQLiteDatabase db = databaseHelper.getReadableDatabase();
            Cursor answerCursor = db.rawQuery("SELECT " + DatabaseHelper.COLUMN_CORRECT_ANSWER +
                    " FROM " + DatabaseHelper.TABLE_QUESTIONS +
                    " WHERE " + DatabaseHelper.COLUMN_QUESTION_NUMBER + " = ?", new String[]{String.valueOf(i)});

            if (answerCursor != null && answerCursor.moveToFirst()) {
                @SuppressLint("Range") String correctAnswer = answerCursor.getString(answerCursor.getColumnIndex(DatabaseHelper.COLUMN_CORRECT_ANSWER));

                String selectedOption = selectedAnswers[i - 1];

                if (selectedOption != null) {

                    if (selectedOption.equals(correctAnswer)) {
                        updateQuestionButtonColor(i, true);
                    } else {
                        updateQuestionButtonColor(i, false);
                    }
                } else {

                    updateQuestionButtonColor(i, false);
                }
            }

            if (answerCursor != null) {
                answerCursor.close();
            }
            db.close();
        }


        Toast.makeText(this, "Quiz Finished! Score: " + correctAnswers + " / " + totalQuestions, Toast.LENGTH_LONG).show();

//        showViewAnswersDialog();

    }

//    private void showViewAnswersDialog() {
//        new AlertDialog.Builder(this)
//                .setTitle("View Answers")
//                .setMessage("Exam Completed! " +
//                        " Do you want to view your answers?")
//                .setPositiveButton("Yes", (dialog, which) -> {
//
//                    highlightAllAnswers();
//                    dialog.dismiss();
//                })
//                .setNegativeButton("No", (dialog, which) -> {
//                    dialog.dismiss();
//                })
//                .show();
//
//    }

    private void highlightAllAnswers() {
        for (int i = 1; i <= totalQuestions; i++) {
            String correctAnswer = getCorrectAnswer(i);
            String selectedAnswer = selectedAnswers[i - 1];

            for (int j = 0; j < optionsGroup.getChildCount(); j++) {
                View child = optionsGroup.getChildAt(j);
                if (child instanceof RadioButton) {
                    RadioButton radioButton = (RadioButton) child;
                    radioButton.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                }
            }

            for (int j = 0; j < optionsGroup.getChildCount(); j++) {
                View child = optionsGroup.getChildAt(j);
                if (child instanceof RadioButton) {
                    RadioButton radioButton = (RadioButton) child;

                    if (radioButton.getText().toString().equals(correctAnswer)) {
                        radioButton.setBackgroundColor(getResources().getColor(R.color.green));
                    }

                    if (radioButton.getText().toString().equals(selectedAnswer)) {
                        if (!selectedAnswer.equals(correctAnswer)) {
                            radioButton.setBackgroundColor(getResources().getColor(R.color.red2));
                        }
                    }
                }
            }
        }
    }




    private String getCorrectAnswer(int questionNumber) {
        SQLiteDatabase db = databaseHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + DatabaseHelper.COLUMN_CORRECT_ANSWER + " FROM " + DatabaseHelper.TABLE_QUESTIONS +
                " WHERE " + DatabaseHelper.COLUMN_QUESTION_NUMBER + " = ?", new String[]{String.valueOf(questionNumber)});

        String correctAnswer = "";
        if (cursor != null && cursor.moveToFirst()) {
            @SuppressLint("Range") String answer = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_CORRECT_ANSWER));
            correctAnswer = answer != null ? answer : "";
            cursor.close();
        }
        db.close();
        return correctAnswer;
    }


    private void createQuestionButtons() {
        for (int i = 1; i <= totalQuestions; i++) {
            Button questionButton = new Button(this);
            questionButton.setText("Q" + i);

            GridLayout.LayoutParams params = new GridLayout.LayoutParams(
                    GridLayout.spec((i - 1) / 6),
                    GridLayout.spec((i - 1) % 6)
            );


            questionButton.setPadding(6, 6, 6, 6);


            params.width = 0;
            params.height = GridLayout.LayoutParams.WRAP_CONTENT;
            params.columnSpec = GridLayout.spec((i - 1) % 6, 1f);


            params.setMargins(5, 5, 5, 5);

            questionButton.setLayoutParams(params);

            final int questionNumber = i;

            questionButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    loadQuestion(questionNumber);
                }
            });
            questionButtonsLayout.addView(questionButton);
        }
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

            questionTextView.setText(questionText);
            option1RadioButton.setText(option1);
            option2RadioButton.setText(option2);
            option3RadioButton.setText(option3);
            option4RadioButton.setText(option4);

            int optionCount = 0;
            if (!option1.isEmpty()) {
                option1RadioButton.setVisibility(View.VISIBLE);
                optionCount++;
            } else {
                option1RadioButton.setVisibility(View.GONE);
            }

            if (!option2.isEmpty()) {
                option2RadioButton.setVisibility(View.VISIBLE);
                optionCount++;
            } else {
                option2RadioButton.setVisibility(View.GONE);
            }

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

            questionNumberTextView.setText("Q " + questionNumber +".");

            this.currentQuestionIndex = questionNumber;
        } else {
            Toast.makeText(this, "No question found.", Toast.LENGTH_SHORT).show();
        }
        if (questionCursor != null) {
            questionCursor.close();
        }
        db.close();
    }

    private void handleSubmit() {
        int selectedId = optionsGroup.getCheckedRadioButtonId();
        if (selectedId != -1) {

            RadioButton selectedRadioButton = findViewById(selectedId);
            String selectedOption = selectedRadioButton.getText().toString();

            selectedAnswers[currentQuestionIndex - 1] = selectedOption;

            SQLiteDatabase db = databaseHelper.getReadableDatabase();
            Cursor answerCursor = db.rawQuery("SELECT " + DatabaseHelper.COLUMN_CORRECT_ANSWER +
                    " FROM " + DatabaseHelper.TABLE_QUESTIONS +
                    " WHERE " + DatabaseHelper.COLUMN_QUESTION_NUMBER + " = ?", new String[]{String.valueOf(currentQuestionIndex)});

            if (answerCursor != null && answerCursor.moveToFirst()) {
                @SuppressLint("Range") String correctAnswer = answerCursor.getString(answerCursor.getColumnIndex(DatabaseHelper.COLUMN_CORRECT_ANSWER));
                if (selectedOption.equals(correctAnswer)) {
                    correctAnswers++;

                    updateQuestionButtonColor(currentQuestionIndex, true);
                } else {

                    updateQuestionButtonColor(currentQuestionIndex, false);
                }
                answeredQuestions++;
                scoreTextView.setText("Score: " + correctAnswers + " / " + answeredQuestions);
            }

            updateAttendedQuestionColor(currentQuestionIndex);

            if (answerCursor != null) {
                answerCursor.close();
            }

            loadNextQuestion();
        } else {
            Toast.makeText(this, "Please select an answer.", Toast.LENGTH_SHORT).show();
        }
    }

    private void updateAttendedQuestionColor(int questionNumber) {
        Button questionButton = (Button) questionButtonsLayout.getChildAt(questionNumber - 1);
        questionButton.setBackgroundColor(getResources().getColor(android.R.color.holo_orange_light));
    }

    private void loadNextQuestion() {
        if (currentQuestionIndex < totalQuestions) {
            currentQuestionIndex++;
            loadQuestion(currentQuestionIndex);
        } else {
            Toast.makeText(this, "Quiz completed!", Toast.LENGTH_SHORT).show();
            submitButton.setEnabled(false);
            skipButton.setEnabled(false);
        }
    }



    private void updateQuestionButtonColor(int questionNumber, boolean isCorrect) {
        Button questionButton = (Button) questionButtonsLayout.getChildAt(questionNumber - 1);
        if (isCorrect) {
            questionButton.setBackgroundColor(getResources().getColor(android.R.color.holo_green_light));
        } else {
            questionButton.setBackgroundColor(getResources().getColor(R.color.pastel_red));

        }

    }
}
