package com.rstudio.knackquiz.models;

import java.io.Serializable;

public class Player implements Serializable {
    private String playerID;
    private long coins;


    public Player() {
        coins = 0;
    }


    public long getCoins() {
        return coins;
    }

    public void setCoins(long coins) {
        this.coins = coins;
    }

    public String getPlayerID() {
        return playerID;
    }

    public void setPlayerID(String playerID) {
        this.playerID = playerID;
    }
}
