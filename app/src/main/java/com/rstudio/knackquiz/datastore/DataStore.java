package com.rstudio.knackquiz.datastore;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;
import com.rstudio.knackquiz.models.Player;

public abstract class DataStore {
    private static Player player=new Player();
    private static SharedPreferences sharedPreferences;
    private static SharedPreferences.Editor editor;
    private static final String PLAYER = "PLAYER",USER= "USER";
    public static final String FIRSTTIME ="FIRSTTIME", STATUS= "STATUS";

    public DataStore() {

    }

    public static   void setCurrentPlayer(Player player,Context context){
        DataStore.player = player;
        Gson gson = new Gson();
        String json = gson.toJson(player);
        sharedPreferences = context.getSharedPreferences(USER,Context.MODE_PRIVATE);
        editor= sharedPreferences.edit();

        editor.putString(PLAYER,json);

        editor.apply();
    }

    public static String getCurrentPlayerID(Context context){
        sharedPreferences = context.getSharedPreferences(USER,Context.MODE_PRIVATE);
        String json = sharedPreferences.getString(PLAYER,"");
        Gson gson = new Gson();
        player = gson.fromJson(json,Player.class);
        return player.getPlayerID();
    }

    public static Player getCurrentPlayer(Context context){
        sharedPreferences = context.getSharedPreferences(USER,Context.MODE_PRIVATE);
        String json = sharedPreferences.getString(PLAYER,"");
        Gson gson = new Gson();
        player = gson.fromJson(json,Player.class);
        return player;
    }
}
