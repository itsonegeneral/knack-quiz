package com.rstudio.knackquiz.helpers;

public class DBClass {
    public static final String testURL = "http://10.0.2.2:8080/";
    public static final String url = "http://node-server-quiz.herokuapp.com/";
    public static final String urlGetQuestions = url +"getquestions";
    public static final String urlGetCategories = url + "getcategories";
    public static final String urlGetQuizOptions = url + "getquizoptions";
    public static final String urlGetSubCategories = url + "getsubcategories";
    public static final String urlSendFriendRequest = url +"friends/sendrequest";
    public static final String urlCreatePaymentRequest = testURL + "payments/createrequest";
    public static final String urlSetLeaderboard  = testURL + "setleaderboard";
    public static final String urlCreateLeaderboard = url + "createleaderboard";
}
