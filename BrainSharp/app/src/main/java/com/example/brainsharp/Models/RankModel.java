package com.example.brainsharp.Models;

public class RankModel {
    private int score;
    private int rank;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private String name;

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public RankModel(String name, int score, int rank) {
        this.score = score;
        this.rank = rank;
        this.name = name;
    }
}
