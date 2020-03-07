package com.rstudio.knackquiz.models;

public class QuizOption {

    private String id, title, catid, rewardcoins, description, entrycoins, gametime, questionsize, difficulty;

    public QuizOption() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCatid() {
        return catid;
    }

    public void setCatid(String catid) {
        this.catid = catid;
    }

    public String getRewardcoins() {
        return rewardcoins;
    }

    public void setRewardcoins(String rewardcoins) {
        this.rewardcoins = rewardcoins;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEntrycoins() {
        return entrycoins;
    }

    public void setEntrycoins(String entrycoins) {
        this.entrycoins = entrycoins;
    }

    public String getGametime() {
        return gametime;
    }

    public void setGametime(String gametime) {
        this.gametime = gametime;
    }

    public String getQuestionsize() {
        return questionsize;
    }

    public void setQuestionsize(String questionsize) {
        this.questionsize = questionsize;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }
}
