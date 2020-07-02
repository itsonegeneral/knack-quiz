package com.rstudio.knackquiz.datastore;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.rstudio.knackquiz.helpers.KeyStore;
import com.rstudio.knackquiz.models.Challenge;
import com.rstudio.knackquiz.models.Player;
import com.rstudio.knackquiz.models.Question;

import java.lang.reflect.Type;
import java.util.ArrayList;

public abstract class DataStore {
    private static Player player = new Player();
    private static SharedPreferences sharedPreferences;
    private static SharedPreferences.Editor editor;
    private static final String PLAYER = "PLAYER", USER = "USER";
    public static final String FIRSTTIME = "FIRSTTIME", STATUS = "STATUS";
    public static final String CHALLENGES = "CHALLENGES";
    private static final String TAG = "DataStore";


    public DataStore() {

    }

    public static void setCurrentPlayer(Player player, Context context) {
        sharedPreferences = context.getSharedPreferences(USER, Context.MODE_PRIVATE);
        DataStore.player = player;
        Gson gson = new Gson();
        String json = gson.toJson(player);
        editor = sharedPreferences.edit();
        editor.putString(PLAYER, json);
        editor.apply();
    }

    public static String getCurrentPlayerID(Context context) {
        sharedPreferences = context.getSharedPreferences(USER, Context.MODE_PRIVATE);
        String json = sharedPreferences.getString(PLAYER, "");
        Gson gson = new Gson();
        player = gson.fromJson(json, Player.class);
        return player.getPlayerID();
    }

    public static Player getCurrentPlayer(Context context) {
        sharedPreferences = context.getSharedPreferences(USER, Context.MODE_PRIVATE);
        String json = sharedPreferences.getString(PLAYER, "");
        Gson gson = new Gson();
        player = gson.fromJson(json, Player.class);
        return player;
    }

    public static boolean isChallengeShown(String challengeID, Context context) {
        sharedPreferences = context.getSharedPreferences(USER, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString(CHALLENGES, "");
        ArrayList<String> list = new ArrayList<>();
        Type type = new TypeToken<ArrayList<String>>() {
        }.getType();
        if (!json.isEmpty()) {
            list = gson.fromJson(json, type);
        } else {
            list = new ArrayList<>();
        }
        if (list.contains(challengeID)) {
            return true;
        }
        return false;
    }

    public static void addChallengeShown(String challengeID, Context context) {
        sharedPreferences = context.getSharedPreferences(USER, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = sharedPreferences.getString(CHALLENGES, "");
        ArrayList<String> list = new ArrayList<>();
        Type type = new TypeToken<ArrayList<String>>() {
        }.getType();
        list = gson.fromJson(json, type);
        if (json.isEmpty())
            list = new ArrayList<>();
        list.add(challengeID);
        String backString = gson.toJson(list);
        editor.putString(CHALLENGES, backString);
        editor.apply();
    }
}
