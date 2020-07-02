package com.rstudio.knackquiz.models;

import java.io.Serializable;

public class Player implements Serializable {

    private String playerID;
    private String userName;
    private String emailID;
    private String photoURL;
    private String playerRegisterType;
    private long levelPoint;
    private String signInType;
    private long coins;
    private long diamonds;
    private String phone,dob,createdOn,lastOnline;
    private boolean isActive=false;

    public Player() {
        coins = 0;
    }


    public String getPhone() {
        return phone;
    }

    public String getDob() {
        return dob;
    }

    public String getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(String createdOn) {
        this.createdOn = createdOn;
    }

    public String getLastOnline() {
        return lastOnline;
    }

    public void setLastOnline(String lastOnline) {
        this.lastOnline = lastOnline;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUserName() {
        if(userName ==null){
            userName = "guest";
        }
        return userName;
    }


    public long getDiamonds() {
        return diamonds;
    }

    public void setDiamonds(long diamonds) {
        this.diamonds = diamonds;
    }

    public long getLevelPoint() {
        return levelPoint;
    }

    public void setLevelPoint(long levelPoint) {
        this.levelPoint = levelPoint;
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

    public String getPlayerRegisterType() {
        return playerRegisterType;
    }

    public void setPlayerRegisterType(String playerRegisterType) {
        this.playerRegisterType = playerRegisterType;
    }

    public String getPhotoURL() {
        return photoURL;
    }

    public String getSignInType() {
        return signInType;
    }

    public void setSignInType(String signInType) {
        this.signInType = signInType;
    }

    public void setPhotoURL(String photoURL) {
        this.photoURL = photoURL;
    }

    public void setPlayerID(String playerID) {
        this.playerID = playerID;
    }
}
