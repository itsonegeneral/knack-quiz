package com.rstudio.knackquiz.helpers;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.rstudio.knackquiz.R;
import com.rstudio.knackquiz.models.Category;

import java.lang.reflect.Type;
import java.util.ArrayList;

public abstract class CategoryHelper {

    private static ArrayList<Category> subCategories, parentCategories, favCategories;
    private static Context context;
    public static final String CACHE = "CACHE";
    public static final String FAVOURITE_LIST = "FAVLIST";
    private static final String TAG = "CategoryHelper";


    public static void init(Context context){
        CategoryHelper.context = context;
        favCategories = new ArrayList<>();
    }
    public static void initCategories(Context context, ArrayList<Category> subCategories, ArrayList<Category> parentCategories) {
        CategoryHelper.context = context;
        CategoryHelper.subCategories = subCategories;
        CategoryHelper.parentCategories = parentCategories;
    }


    public static ArrayList<Category> getSubCategories(String category) {
        ArrayList<Category> filtered = new ArrayList<>();

        for (Category category1 : subCategories) {
            if (category1.getParentCategory().equalsIgnoreCase(category)) {
                filtered.add(category1);
            }
        }
        return filtered;
    }

    public static void addFavouriteCategory(Category category) {
        favCategories.add(category);
    }
    public static  void removeFavouriteCategory(Category category){
        favCategories.remove(category);
    }
    public static void saveFavourites() {
        saveCategoriesData();
    }

    private static void saveCategoriesData() {
        Log.d(TAG, "saveCategoriesData: ");
        SharedPreferences sharedPreferences = context.getSharedPreferences(CACHE, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(favCategories);
        editor.putString(FAVOURITE_LIST, json);
        editor.apply();
    }

    public static ArrayList<Category> getFavCategories() {
        ArrayList<Category> fav = new ArrayList<>();
        Gson gson = new Gson();
        SharedPreferences sharedPreferences = context.getSharedPreferences(CACHE, Context.MODE_PRIVATE);
        String json = sharedPreferences.getString(FAVOURITE_LIST, "");
        if(json.isEmpty()){
            return  new ArrayList<>();
        }
        Type type = new TypeToken<ArrayList<Category>>() {
        }.getType();
        fav = gson.fromJson(json, type);
        return fav;
    }
}
