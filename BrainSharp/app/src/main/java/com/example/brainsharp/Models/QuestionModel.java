package com.example.brainsharp.Models;

public class QuestionModel {
    private String question;
    private String optionA;
    private String optionB;
    private String optionC;
    private String  optionD;
    private int correctAns;

    public int getTopScore() {
        return topScore;
    }

    public void setTopScore(int topScore) {
        this.topScore = topScore;
    }

    private int slectedAns;

    private int topScore = 0;

    public void setSlectedAns(int slectedAns) {
        this.slectedAns = slectedAns;
    }

    public QuestionModel(String question, String optionA, String optionB, String optionC, String optionD, int correctAns) {
        this.question = question;
        this.optionA = optionA;
        this.optionB = optionB;
        this.optionC = optionC;
        this.optionD = optionD;
        this.correctAns = correctAns;
    }

    public QuestionModel(String question, String optionA, String optionB, String optionC, String optionD, int correctAns, int slectedAns) {
        this.question = question;
        this.optionA = optionA;
        this.optionB = optionB;
        this.optionC = optionC;
        this.optionD = optionD;
        this.correctAns = correctAns;
        this.slectedAns =slectedAns;
    }

    public int getSlectedAns() {
        return slectedAns;
    }



    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getOptionA() {
        return optionA;
    }

    public void setOptionA(String optionA) {
        this.optionA = optionA;
    }

    public String getOptionB() {
        return optionB;
    }

    public void setOptionB(String optionB) {
        this.optionB = optionB;
    }

    public String getOptionC() {
        return optionC;
    }

    public void setOptionC(String optionC) {
        this.optionC = optionC;
    }

    public String getOptionD() {
        return optionD;
    }

    public void setOptionD(String optionD) {
        this.optionD = optionD;
    }

    public int getCorrectAns() {
        return correctAns;
    }

    public void setCorrectAns(int correctAns) {
        this.correctAns = correctAns;
    }





}
