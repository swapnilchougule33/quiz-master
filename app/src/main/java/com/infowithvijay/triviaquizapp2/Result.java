package com.infowithvijay.triviaquizapp2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Result extends AppCompatActivity {

    Button btPlayAgain, btPlayScreen;
    TextView txtTotalQuestion, txtCoins, txtWrongQues, txtCorrectQues;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        // Initialize views
        btPlayAgain = findViewById(R.id.bt_PlayAgainR);
        btPlayScreen = findViewById(R.id.bt_PlayScreenR);

        txtCoins = findViewById(R.id.txtCoinsR);
        txtCorrectQues = findViewById(R.id.txtCorrectR);
        txtWrongQues = findViewById(R.id.txtWrongR);
        txtTotalQuestion = findViewById(R.id.txtTotalQuestionsR);

        // Get the result data from the Intent
        Intent intent = getIntent();
        int totalQuestions = intent.getIntExtra(Constants.TOTAL_QUESTIONS, 0);
        int coins = intent.getIntExtra(Constants.COINS, 0);
        int correct = intent.getIntExtra(Constants.CORRECT, 0);
        int wrong = intent.getIntExtra(Constants.WRONG, 0);

        // Display the result data in the TextViews
        txtTotalQuestion.setText("Total Questions: " + totalQuestions);
        txtCoins.setText("Coins Earned: " + coins);
        txtCorrectQues.setText("Correct Answers: " + correct);
        txtWrongQues.setText("Wrong Answers: " + wrong);

        // Play screen button click listener
        btPlayScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Result.this, PlayScreen.class);
                startActivity(intent);
            }
        });

        // Play again button click listener
        btPlayAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Result.this, QuizActivity.class);
                startActivity(intent);
            }
        });
    }
}
