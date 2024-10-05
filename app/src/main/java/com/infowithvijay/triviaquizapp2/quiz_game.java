package com.infowithvijay.triviaquizapp2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class quiz_game extends AppCompatActivity {

    private CardView cardViewLudo, cardViewKBC, cardViewPracticeTest, cardViewMainTest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_game);

        cardViewLudo = findViewById(R.id.cardViewLudo);
        cardViewKBC = findViewById(R.id.cardViewKBC);
        cardViewPracticeTest = findViewById(R.id.cardViewPracticeTest);
        cardViewMainTest = findViewById(R.id.cardViewMainTest);

        cardViewLudo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(quiz_game.this, "Ludo Clicked", Toast.LENGTH_SHORT).show();
                // Intent intent = new Intent(quiz_game.this, LudoActivity.class);
                // startActivity(intent);
            }
        });

        cardViewKBC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(quiz_game.this, PlayScreen.class);
                startActivity(intent);
                Toast.makeText(quiz_game.this, "Opened PlayScreen", Toast.LENGTH_SHORT).show();  // For Debugging
            }
        });


        cardViewPracticeTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(quiz_game.this, "Practice Test Clicked", Toast.LENGTH_SHORT).show();
                 Intent intent = new Intent(quiz_game.this, OptionQuiz.class);
                 startActivity(intent);
            }
        });

        cardViewMainTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(quiz_game.this, "Main Test Clicked", Toast.LENGTH_SHORT).show();
                 Intent intent = new Intent(quiz_game.this, Exam.class);
                 startActivity(intent);
            }
        });
    }
}
