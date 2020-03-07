package com.rstudio.knackquiz.models;

import java.io.Serializable;

public class QuizOption implements Serializable {

    private String id, title, catid, rewardcoins, description, entrycoins, gametime, questionsize, difficulty;
    private String categoryName,parentCategory,iconLink,type;

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

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getParentCategory() {
        return parentCategory;
    }

    public void setParentCategory(String parentCategory) {
        this.parentCategory = parentCategory;
    }

    public String getIconLink() {
        return iconLink;
    }

    public void setIconLink(String iconLink) {
        this.iconLink = iconLink;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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
