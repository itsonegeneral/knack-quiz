package com.rstudio.knackquiz.models;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class Contest implements Serializable {

    private String id, title, startTime, endTime, entryType, rewardType, category, categoryId;
    private int entryValue, rewardValue, questionTime, totalPlayers, playerCount;
    private ArrayList<String> players = new ArrayList<>();
    private ArrayList<Question> questions = new ArrayList<>();

    public Contest() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStartDate() {
        Date date = new Date(startTime);
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/YYYY");
        return format.format(date);
    }

    public String getStartTime() {
        Date date = new Date(startTime);
        SimpleDateFormat format = new SimpleDateFormat("hh:mm a");
        return format.format(date);
    }
    /*
    public int getDuration(){

    }*/

    public int getJoinedCount(){
        return players.size();
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

    public int getPlayerCount() {
        return playerCount;
    }

    public void setPlayerCount(int playerCount) {
        this.playerCount = playerCount;
    }
}
