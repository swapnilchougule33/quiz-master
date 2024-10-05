package com.infowithvijay.triviaquizapp2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class GameOver extends AppCompatActivity {


    TextView txtCorrectAnstoShow;
    Button bt_ok;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gameover);

        txtCorrectAnstoShow = findViewById(R.id.txtCoorectAnsGameOver);
        bt_ok = findViewById(R.id.bt_gameOver);


        Intent intent = getIntent();
        int correctAns = intent.getIntExtra("CorrectAns", -1); // Default is -1 if no value is passed

        // Display the correct answer to the player
        if (correctAns != -1) {
            txtCorrectAnstoShow.setText("Correct Answer: " + correctAns);
        } else {
            txtCorrectAnstoShow.setText("No correct answer available.");
        }
        bt_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new  Intent(GameOver.this,Result.class);
                startActivity(intent);

                }
        });

    }
}
