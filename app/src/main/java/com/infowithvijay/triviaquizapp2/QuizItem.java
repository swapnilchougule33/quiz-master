package com.infowithvijay.triviaquizapp2;

public class QuizItem {
    private int questionNumber;
    private String questionText;
    private String option1;
    private String option2;
    private String option3;
    private String option4;
    private String correctAnswer;
    private String userAnswer; // Field to store the user's answer
    private String savedAnswer; // Field to store the saved answer

    public QuizItem(int questionNumber, String questionText, String option1, String option2, String option3, String option4, String correctAnswer) {
        this.questionNumber = questionNumber;
        this.questionText = questionText;
        this.option1 = option1;
        this.option2 = option2;
        this.option3 = option3;
        this.option4 = option4;
        this.correctAnswer = correctAnswer;
        this.userAnswer = "";
        this.savedAnswer = ""; // Initialize savedAnswer
    }

    public int getQuestionNumber() {
        return questionNumber;
    }

    public String getQuestionText() {
        return questionText;
    }

    public String getOption1() {
        return option1;
    }

    public String getOption2() {
        return option2;
    }

    public String getOption3() {
        return option3;
    }

    public String getOption4() {
        return option4;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public String getUserAnswer() {
        return userAnswer;
    }

    public void setUserAnswer(String userAnswer) {
        this.userAnswer = userAnswer;
    }

    public String getSavedAnswer() {
        return savedAnswer;
    }

    public void setSavedAnswer(String savedAnswer) {
        this.savedAnswer = savedAnswer;
    }
}
