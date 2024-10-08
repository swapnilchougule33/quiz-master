package com.infowithvijay.triviaquizapp2;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Quiz_Question extends AppCompatActivity {

    private static final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int REQUEST_IMAGE_GALLERY = 2;

    private TextView titleTextView, textViewQuestionNumber;
    private EditText editTextQuestion, editTextOption1, editTextOption2, editTextOption3, editTextOption4;
    private Spinner spinnerName, spinnerAnswer;
    private Button saveButton, viewButton, finishButton;
    private View optionalOptionsLayout;
    private DatabaseHelper databaseHelper; // Database helper object for SQLite
    private int currentQuestionNumber;

    private ImageButton buttonCamera, buttonUpload, buttonShow;
    private ImageView imageViewQuestion; // ImageView to display the image
    private Bitmap selectedImageBitmap; // Store the selected image bitmap

    private ActivityResultLauncher<Intent> cameraLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK) {
                    Intent data = result.getData();
                    if (data != null) {
                        Bundle extras = data.getExtras();
                        if (extras != null) {
                            selectedImageBitmap = (Bitmap) extras.get("data");
                            imageViewQuestion.setImageBitmap(selectedImageBitmap);
                        }
                    }
                }
            }
    );

    private ActivityResultLauncher<Intent> galleryLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK) {
                    Intent data = result.getData();
                    if (data != null) {
                        Uri selectedImageUri = data.getData();
                        try {
                            selectedImageBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImageUri);
                            imageViewQuestion.setImageBitmap(selectedImageBitmap);
                        } catch (IOException e) {
                            Toast.makeText(this, "Failed to load image", Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
                    }
                }
            }
    );

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_questions);

        databaseHelper = new DatabaseHelper(this); // Initialize database helper

        String standard = getIntent().getStringExtra("SCHOLARSHIP_NAME");

        Toast.makeText(this, "" + standard, Toast.LENGTH_SHORT).show();

        titleTextView = findViewById(R.id.titleTextView);
        textViewQuestionNumber = findViewById(R.id.textViewQuestionNumber);
        editTextQuestion = findViewById(R.id.editTextQuestion);
        editTextOption1 = findViewById(R.id.editTextOption1);
        editTextOption2 = findViewById(R.id.editTextOption2);
        editTextOption3 = findViewById(R.id.editTextOption3);
        editTextOption4 = findViewById(R.id.editTextOption4);
        spinnerName = findViewById(R.id.spinnerName);
        spinnerAnswer = findViewById(R.id.spinnerAnswer);
        saveButton = findViewById(R.id.saveButton);
        viewButton = findViewById(R.id.viewButton);
        finishButton = findViewById(R.id.finishButton);
        optionalOptionsLayout = findViewById(R.id.optionalOptionsLayout);
        buttonCamera = findViewById(R.id.buttonCamera);
        buttonUpload = findViewById(R.id.buttonUpload);
        buttonShow = findViewById(R.id.buttonShow); // Initialize the ImageView

        titleTextView.setText(" " + standard);
        currentQuestionNumber = getNextQuestionNumber();
        textViewQuestionNumber.setText(getString(R.string.question_number, currentQuestionNumber));

        saveButton.setOnClickListener(v -> saveQuestion());
        viewButton.setOnClickListener(v -> {
            Intent intent = new Intent(Quiz_Question.this, ViewQuestionsActivity.class);
            startActivity(intent);
        });
        finishButton.setOnClickListener(v -> finishQuiz());

        setupQuestionTypeSpinner();

        buttonCamera.setOnClickListener(v -> {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (intent.resolveActivity(getPackageManager()) != null) {
                cameraLauncher.launch(intent);
            }
        });

        buttonUpload.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            if (intent.resolveActivity(getPackageManager()) != null) {
                galleryLauncher.launch(intent);
            }
        });

        buttonShow.setOnClickListener(v -> {
            if (selectedImageBitmap != null) {
                imageViewQuestion.setImageBitmap(selectedImageBitmap);
            } else {
                Toast.makeText(this, "No image selected", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupQuestionTypeSpinner() {
        List<String> questionTypes = new ArrayList<>();
        questionTypes.add("द्विपर्यायी");
        questionTypes.add("बहुपर्यायी");

        ArrayAdapter<String> questionTypeAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, questionTypes);
        questionTypeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerName.setAdapter(questionTypeAdapter);

        spinnerName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                updateOptionsVisibility(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void updateOptionsVisibility(int position) {
        List<String> answerOptions = new ArrayList<>();

        if (position == 0) {
            optionalOptionsLayout.setVisibility(View.GONE);
            editTextOption3.setVisibility(View.GONE);
            editTextOption4.setVisibility(View.GONE);


            answerOptions.add("Option 1");
            answerOptions.add("Option 2");

        } else if (position == 1) {
            optionalOptionsLayout.setVisibility(View.VISIBLE);
            editTextOption3.setVisibility(View.VISIBLE);
            editTextOption4.setVisibility(View.VISIBLE);

            answerOptions.add("Option 1");
            answerOptions.add("Option 2");
            answerOptions.add("Option 3");
            answerOptions.add("Option 4");
        }

        updateAnswerSpinner(answerOptions);
    }

    private void updateAnswerSpinner(List<String> options) {
        ArrayAdapter<String> answerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, options);
        answerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerAnswer.setAdapter(answerAdapter);
    }

    private void saveQuestion() {
        String question = editTextQuestion.getText().toString().trim();
        String option1 = editTextOption1.getText().toString().trim();
        String option2 = editTextOption2.getText().toString().trim();
        String option3 = editTextOption3.getText().toString().trim();
        String option4 = editTextOption4.getText().toString().trim();
        String correctAnswer = "";

        if (question.isEmpty() || option1.isEmpty() || option2.isEmpty()) {
            Toast.makeText(this, "Please fill all mandatory fields", Toast.LENGTH_SHORT).show();
            return;
        }

        if (spinnerName.getSelectedItemPosition() == 1 && (option3.isEmpty() || option4.isEmpty())) {
            Toast.makeText(this, "Please fill all options for a multiple-choice question", Toast.LENGTH_SHORT).show();
            return;
        }

        if (spinnerAnswer.getSelectedItemPosition() == 0) {
            correctAnswer = option1;
        } else if (spinnerAnswer.getSelectedItemPosition() == 1) {
            correctAnswer = option2;
        } else if (spinnerAnswer.getSelectedItemPosition() == 2) {
            correctAnswer = option3;
        } else if (spinnerAnswer.getSelectedItemPosition() == 3) {
            correctAnswer = option4;
        }

        String testNumber = getIntent().getStringExtra("परीक्षा नं.");

        saveQuestionInDatabase(question, option1, option2, option3, option4, correctAnswer, testNumber);
        currentQuestionNumber++;
        textViewQuestionNumber.setText(getString(R.string.question_number, currentQuestionNumber)); // Update the displayed question number
        saveQuestionInSharedPreferences(question, option1, option2, option3, option4, testNumber);

        Toast.makeText(this, "Question saved successfully!", Toast.LENGTH_SHORT).show();
        clearFields();
    }

    private int getNextQuestionNumber() {
        SQLiteDatabase db = null;
        Cursor cursor = null;
        int nextQuestionNumber = 1;

        try {
            db = databaseHelper.getReadableDatabase();
            cursor = db.rawQuery("SELECT MAX(" + DatabaseHelper.COLUMN_QUESTION_NUMBER + ") FROM " + DatabaseHelper.TABLE_QUESTIONS, null);

            if (cursor != null && cursor.moveToFirst()) {
                nextQuestionNumber = cursor.getInt(0) + 1;
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (db != null) {
                db.close();
            }
        }

        return nextQuestionNumber;
    }

    // Save the question in SQLite database
    private void saveQuestionInDatabase(String question, String option1, String option2, String option3, String option4, String correctAnswer,  String testNumber) {
        int questionNumber = currentQuestionNumber;

        SQLiteDatabase db = databaseHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_QUESTION_NUMBER, questionNumber);
        values.put(DatabaseHelper.COLUMN_QUESTION_TEXT, question);
        values.put(DatabaseHelper.COLUMN_OPTION1, option1);
        values.put(DatabaseHelper.COLUMN_OPTION2, option2);
        values.put(DatabaseHelper.COLUMN_OPTION3, option3);
        values.put(DatabaseHelper.COLUMN_OPTION4, option4);
        values.put(DatabaseHelper.COLUMN_CORRECT_ANSWER, correctAnswer); // Store the correct answer

        // Convert image to byte array if it's available
        byte[] imageData = null;
        if (selectedImageBitmap != null) {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            selectedImageBitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
            imageData = outputStream.toByteArray();
        }
        values.put(DatabaseHelper.COLUMN_IMAGE, imageData);

        // Insert a new question in the database
        long newRowId = db.insert(DatabaseHelper.TABLE_QUESTIONS, null, values);
        if (newRowId != -1) {
            Toast.makeText(this, "Question saved to database successfully!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Failed to save question to database.", Toast.LENGTH_SHORT).show();
        }
    }


    // Save the question in SharedPreferences
    private void saveQuestionInSharedPreferences(String question, String option1, String option2, String option3, String option4, String testNumber) {
        SharedPreferences sharedPreferences = getSharedPreferences("quiz_data", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        // Store the question data (increment key for multiple questions)
        int questionCount = sharedPreferences.getInt("questionCount", 0);
        questionCount++;

        // Save question and options with the test number
        editor.putString("question_" + questionCount, "Test Number: " + testNumber + " | " + question);
        editor.putString("option1_" + questionCount, option1);
        editor.putString("option2_" + questionCount, option2);
        editor.putString("option3_" + questionCount, option3);
        editor.putString("option4_" + questionCount, option4);
        editor.putInt("questionCount", questionCount);

        editor.apply();
    }


    private void clearFields() {

        editTextQuestion.setText("");
        editTextOption1.setText("");
        editTextOption2.setText("");
        editTextOption3.setText("");
        editTextOption4.setText("");
        spinnerName.setSelection(0);
        spinnerAnswer.setSelection(0);
        imageViewQuestion.setImageResource(android.R.color.transparent); // Clear the image view
        selectedImageBitmap = null; // Reset the image bitmap
        Toast.makeText(this, "Fields cleared", Toast.LENGTH_SHORT).show();
    }

    private void finishQuiz() {
        Toast.makeText(this, "Quiz finished", Toast.LENGTH_SHORT).show();

//        Intent intent = new Intent(this, quiz_game.class);
//        startActivity(intent);

        finish();
    }
}