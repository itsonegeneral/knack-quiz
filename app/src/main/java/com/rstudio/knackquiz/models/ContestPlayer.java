package com.rstudio.knackquiz.models;

import java.io.Serializable;

public class ContestPlayer implements Serializable {
    private String playerID,playerName;
    private int score;

    public ContestPlayer() {
    }

    public String getPlayerID() {
        return playerID;
    }

    public void setPlayerID(String playerID) {
        this.playerID = playerID;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }
}
