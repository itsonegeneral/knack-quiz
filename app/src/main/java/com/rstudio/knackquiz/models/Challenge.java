package com.rstudio.knackquiz.models;

import java.io.Serializable;
import java.util.ArrayList;

public class Challenge implements Serializable {
    private Player challengerPlayer,receivedPlayer;
    private String challengeId;
    private ArrayList<Question> questions;
    private String createdOn,message,categoryId,category;
    private boolean isAccepted,isRejected;
    private int challengerScore,receiverScore;

    public Challenge() {
    }


    public ArrayList<Question> getQuestions() {
        return questions;
    }

    public void setQuestions(ArrayList<Question> questions) {
        this.questions = questions;
    }

    public int getChallengerScore() {
        return challengerScore;
    }

    public void setChallengerScore(int challengerScore) {
        this.challengerScore = challengerScore;
    }

    public int getReceiverScore() {
        return receiverScore;
    }

    public void setReceiverScore(int receiverScore) {
        this.receiverScore = receiverScore;
    }

    public Player getChallengerPlayer() {
        return challengerPlayer;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setChallengerPlayer(Player challengerPlayer) {
        this.challengerPlayer = challengerPlayer;
    }

    public Player getReceivedPlayer() {
        return receivedPlayer;
    }

    public void setReceivedPlayer(Player receivedPlayer) {
        this.receivedPlayer = receivedPlayer;
    }

    public String getChallengeId() {
        return challengeId;
    }

    public void setChallengeId(String challengeId) {
        this.challengeId = challengeId;
    }

    public String getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(String createdOn) {
        this.createdOn = createdOn;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isAccepted() {
        return isAccepted;
    }

    public void setAccepted(boolean accepted) {
        isAccepted = accepted;
    }

    public boolean isRejected() {
        return isRejected;
    }

    public void setRejected(boolean rejected) {
        isRejected = rejected;
    }
}
