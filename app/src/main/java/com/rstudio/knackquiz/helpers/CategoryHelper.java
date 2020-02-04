package com.rstudio.knackquiz.helpers;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;

import com.rstudio.knackquiz.R;
import com.rstudio.knackquiz.models.Category;

import java.util.ArrayList;

public abstract class CategoryHelper {

    private static ArrayList<Category> categoryArrayList;
    private static Context context;


    public static void initCategories(Context context) {
        categoryArrayList = new ArrayList<>();
        categoryArrayList.add(getCatElement("Cricket", 1, context.getResources().getDrawable(R.drawable.ic_cat_cricket)));
        categoryArrayList.add(getCatElement("Badminton", 2, context.getResources().getDrawable(R.drawable.ic_cat_badminton)));
        categoryArrayList.add(getCatElement("Olympics", 3, context.getResources().getDrawable(R.drawable.ic_cat_olympic)));
        categoryArrayList.add(getCatElement("Python", 4, context.getResources().getDrawable(R.drawable.ic_cat_python)));
        categoryArrayList.add(getCatElement("Football", 5, context.getResources().getDrawable(R.drawable.ic_cat_soccer)));
        categoryArrayList.add(getCatElement("Mind Twisters", 6, context.getResources().getDrawable(R.drawable.ic_cat_process)));
        categoryArrayList.add(getCatElement("Interview", 7, context.getResources().getDrawable(R.drawable.ic_cat_interview)));
        categoryArrayList.add(getCatElement("Node js", 8, context.getResources().getDrawable(R.drawable.ic_cat_nodejs)));
        categoryArrayList.add(getCatElement("Java", 9, context.getResources().getDrawable(R.drawable.ic_cat_java)));
        categoryArrayList.add(getCatElement("CSS", 10, context.getResources().getDrawable(R.drawable.ic_cat_css)));
        categoryArrayList.add(getCatElement("PhP", 11, context.getResources().getDrawable(R.drawable.ic_cat_php)));



    }

    private static Category getCatElement(String name, int id, Drawable drawable) {
        Category category = new Category();
        category.setName(name);
        category.setId(id);
        category.setIcDrawable(drawable);
        return category;
    }

    public static ArrayList<Category> getCategoryArrayList(Context context) {
        initCategories(context);
        if (categoryArrayList != null) {

            return categoryArrayList;
        } else {
            return new ArrayList<>();
        }
    }
}
