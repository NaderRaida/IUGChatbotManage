package com.example.nadir.finalproject;

import java.util.ArrayList;
import java.util.List;

public class Question {
    private List<String> listOfWords = new ArrayList<>();
    private String questionText;
    private String answerText;

    public Question(){

    }
    public Question(List<String> listOfWords, String questionText, String answerText) {
        this.listOfWords = listOfWords;
        this.questionText = questionText;
        this.answerText = answerText;
    }

    public List<String> getListOfWords() {
        return listOfWords;
    }

    public void setListOfWords(List<String> listOfWords) {
        this.listOfWords = listOfWords;
    }

    public String getQuestionText() {
        return questionText;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    public String getAnswerText() {
        return answerText;
    }

    public void setAnswerText(String answerText) {
        this.answerText = answerText;
    }
}
