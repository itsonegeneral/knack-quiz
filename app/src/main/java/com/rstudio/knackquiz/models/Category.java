package com.rstudio.knackquiz.models;

import android.graphics.drawable.Drawable;

import java.io.Serializable;

public class Category implements Serializable {
    private String name;
    private int id;
    private Drawable icDrawable;
    private boolean isSelected=false;

    public Category() {
    }

    public String getName() {
        return name;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Drawable getIcDrawable() {
        return icDrawable;
    }

    public void setIcDrawable(Drawable icDrawable) {
        this.icDrawable = icDrawable;
    }
}
