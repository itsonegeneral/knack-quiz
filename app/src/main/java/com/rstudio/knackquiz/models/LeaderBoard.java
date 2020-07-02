package com.rstudio.knackquiz.models;

import java.io.Serializable;

public class LeaderBoard implements Serializable {
    private String profileURL,playerName,totalScore,weeklyScore,playerID,level;

    public LeaderBoard() {
    }

    public String getProfileURL() {
        return profileURL;
    }

    public void setProfileURL(String profileURL) {
        this.profileURL = profileURL;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public String getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(String totalScore) {
        this.totalScore = totalScore;
    }

    public String getWeeklyScore() {
        return weeklyScore;
    }

    public void setWeeklyScore(String weeklyScore) {
        this.weeklyScore = weeklyScore;
    }

    public String getPlayerID() {
        return playerID;
    }

    public void setPlayerID(String playerID) {
        this.playerID = playerID;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }
}
