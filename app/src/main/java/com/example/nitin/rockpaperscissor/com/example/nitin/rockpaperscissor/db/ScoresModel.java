package com.example.nitin.rockpaperscissor.com.example.nitin.rockpaperscissor.db;

/**
 * Created by nitin on 9/20/14.
 */
public class ScoresModel {


    public int wins=0;
    public int losses=0;
    public ScoresModel(){}

    public void setLosses(int losses) {
        this.losses = losses;
    }

    public void setWins(int wins) {

        this.wins = wins;
    }

    public int getWins() {
        return wins;
    }

    public int getLosses() {
        return losses;
    }





}
