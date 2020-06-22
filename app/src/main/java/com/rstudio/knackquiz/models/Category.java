package com.rstudio.knackquiz.models;

import android.graphics.drawable.Drawable;

import java.io.Serializable;

public class Category implements Serializable {
    private String categoryName,parentCategory,id,iconLink,type;
    private boolean isSelected=false;
    private int homeClicks,favClicks;

    public Category() {

    }


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public int getHomeClicks() {
        return homeClicks;
    }

    public void setHomeClicks(int homeClicks) {
        this.homeClicks = homeClicks;
    }

    public int getFavClicks() {
        return favClicks;
    }

    public void setFavClicks(int favClicks) {
        this.favClicks = favClicks;
    }

    public String getCategory() {
        return categoryName;
    }

    public void setCategory(String category) {
        this.categoryName = category;
    }

    public String getParentCategory() {
        return parentCategory;
    }

    public void setParentCategory(String parentCategory) {
        this.parentCategory = parentCategory;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIconLink() {
        return iconLink;
    }

    public void setIconLink(String iconLink) {
        this.iconLink = iconLink;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }
}
