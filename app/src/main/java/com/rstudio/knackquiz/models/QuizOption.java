package com.rstudio.knackquiz.models;

public class QuizOption {

    private String id, title, description, parentCategory,entrycoins;
    private String category, rewardcoins, time, questionno, difficulty,expiry,createdon;

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }



    public String getParentCategory() {
        return parentCategory;
    }

    public void setParentCategory(String parentCategory) {
        this.parentCategory = parentCategory;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getRewardcoins() {
        return rewardcoins;
    }

    public void setRewardcoins(String rewardcoins) {
        this.rewardcoins = rewardcoins;
    }
}
