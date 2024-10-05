package com.infowithvijay.triviaquizapp2;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "QuizApp.db";

    // Columns for quiz questions
    private static final String COL_1 = "ID";
    private static final String COL_2 = "QUESTION_NUMBER";
    private static final String COL_3 = "QUESTION_TEXT";
    private static final String COL_4 = "OPTION1";
    private static final String COL_5 = "OPTION2";
    private static final String COL_6 = "OPTION3";
    private static final String COL_7 = "OPTION4";
    private static final String COL_8 = "CORRECT_ANSWER";
    private static final String COL_9 = "SHOW_IMAGE";

    // Quiz questions table
    public static final String TABLE_QUESTIONS = "questions";
    public static final String COLUMN_QUESTION_NUMBER = COL_2;
    public static final String COLUMN_QUESTION_TEXT = COL_3;
    public static final String COLUMN_OPTION1 = COL_4;
    public static final String COLUMN_OPTION2 = COL_5;
    public static final String COLUMN_OPTION3 = COL_6;
    public static final String COLUMN_OPTION4 = COL_7;
    public static final String COLUMN_CORRECT_ANSWER = COL_8;
    public static final String COLUMN_IMAGE = COL_9;




    // Additional table for quiz data (standard, subject, test number)
    private static final String TABLE_NAME = "quiz_data";
    private static final String COL_STANDARD = "STANDARD";
    private static final String COL_SUBJECT = "SUBJECT";
    private static final String COL_TEST_NUMBER = "TEST_NUMBER";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create questions table
        db.execSQL("CREATE TABLE " + TABLE_QUESTIONS + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "QUESTION_NUMBER INTEGER, " +
                "QUESTION_TEXT TEXT, " +
                "OPTION1 TEXT, " +
                "OPTION2 TEXT, " +
                "OPTION3 TEXT, " +
                "OPTION4 TEXT, " +
                "CORRECT_ANSWER TEXT) ");

        // Create quiz data table (for standard, subject, and test number)
        db.execSQL("CREATE TABLE " + TABLE_NAME + " (ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "STANDARD TEXT, " +
                "SUBJECT TEXT, " +
                "TEST_NUMBER TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop old tables if they exist and create them again
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_QUESTIONS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    // Method to add a question to the questions table
    public boolean addQuestion(int questionNumber, String questionText, String option1, String option2, String option3, String option4, String correctAnswer) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2, questionNumber);
        contentValues.put(COL_3, questionText);
        contentValues.put(COL_4, option1);
        contentValues.put(COL_5, option2);
        contentValues.put(COL_6, option3);
        contentValues.put(COL_7, option4);
        contentValues.put(COL_8, correctAnswer);


        long result = db.insert(TABLE_QUESTIONS, null, contentValues);
        return result != -1;
    }

    // Method to insert data into the quiz_data table (standard, subject, and test number)
    public boolean insertData(String standard, String subject, String testNumber) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_STANDARD, standard);
        contentValues.put(COL_SUBJECT, subject);
        contentValues.put(COL_TEST_NUMBER, testNumber);

        long result = db.insert(TABLE_NAME, null, contentValues);
        return result != -1;
    }

    // Getter for table questions (in case you need it elsewhere)
    public String getTableQuestions() {

        return TABLE_QUESTIONS;
    }
}
