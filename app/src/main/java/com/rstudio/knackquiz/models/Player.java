package com.rstudio.knackquiz.models;

import java.io.Serializable;

public class Player implements Serializable {
    private String playerID;
    private String userName;
    private String emailID;
    private String photoURL;
    private long coins;

    public String getPlayerRegisterType() {
        return playerRegisterType;
    }

    public void setPlayerRegisterType(String playerRegisterType) {
        this.playerRegisterType = playerRegisterType;
    }

    public String getPhotoURL() {
        return photoURL;
    }

    public void setPhotoURL(String photoURL) {
        this.photoURL = photoURL;
    }

    private String playerRegisterType;


    public Player() {
        coins = 0;
    }


    public String getUserName() {
        return userName;
    }


    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmailID() {
        return emailID;
    }

    public void setEmailID(String emailID) {
        this.emailID = emailID;
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
