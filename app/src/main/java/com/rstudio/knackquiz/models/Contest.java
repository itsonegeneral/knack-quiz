package com.rstudio.knackquiz.models;

import java.io.Serializable;
import java.util.ArrayList;

public class Contest implements Serializable {

    private String id, title, startTime, endTime, entryType, rewardType, category, categoryId, hostUserId;
    private int entryValue, rewardValue, questionTime, totalPlayers, winnerCount;
    private ArrayList<String> players = new ArrayList<>();
    private ArrayList<Question> questions = new ArrayList<>();
    private ArrayList<ContestPlayer> playedPLayers = new ArrayList<>();

    public Contest() {
    }

    public String getHostUserId() {
        return hostUserId;
    }

    public void setHostUserId(String hostUserId) {
        this.hostUserId = hostUserId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ArrayList<ContestPlayer> getPlayedPLayers() {
        return playedPLayers;
    }

    public void setPlayedPLayers(ArrayList<ContestPlayer> playedPLayers) {
        this.playedPLayers = playedPLayers;
    }

    public void addPlayedPlayer(ContestPlayer player) {
        this.playedPLayers.add(player);
    }

    /*

    public int getDuration(){

    }*/

    public int getJoinedCount() {
        return players.size();
    }

    public void addPlayer(String playerUserId) {
        this.players.add(playerUserId);
    }


    public ArrayList<String> getPlayers() {
        return players;
    }

    public void setPlayers(ArrayList<String> players) {
        this.players = players;
    }

    public ArrayList<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(ArrayList<Question> questions) {
        this.questions = questions;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getEntryType() {
        return entryType;
    }

    public void setEntryType(String entryType) {
        this.entryType = entryType;
    }

    public String getRewardType() {
        return rewardType;
    }

    public void setRewardType(String rewardType) {
        this.rewardType = rewardType;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public int getEntryValue() {
        return entryValue;
    }

    public void setEntryValue(int entryValue) {
        this.entryValue = entryValue;
    }

    public int getRewardValue() {
        return rewardValue;
    }

    public void setRewardValue(int rewardValue) {
        this.rewardValue = rewardValue;
    }

    public int getQuestionTime() {
        return questionTime;
    }

    public void setQuestionTime(int questionTime) {
        this.questionTime = questionTime;
    }

    public int getTotalPlayers() {
        return totalPlayers;
    }

    public void setTotalPlayers(int totalPlayers) {
        this.totalPlayers = totalPlayers;
    }

    public int getWinnerCount() {
        return winnerCount;
    }

    public void setWinnerCount(int winnerCount) {
        this.winnerCount = winnerCount;
    }
}
