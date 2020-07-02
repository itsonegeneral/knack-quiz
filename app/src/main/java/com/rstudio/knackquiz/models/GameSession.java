package com.rstudio.knackquiz.models;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class GameSession implements Serializable {

    private QuizOption quizOption;
    private String userId;
    private String TYPE;
    private HashMap<Question, Boolean> sessionData = new HashMap<>();
    public static final String TYPE_NORMAL = "normal", TYPE_MULTIPLAYER = "multiplayer", TYPE_CONTEST = "contest";


    public GameSession() {
    }

    public String getTYPE() {
        return TYPE;
    }

    public void setTYPE(String TYPE) {
        this.TYPE = TYPE;
    }

    public QuizOption getQuizOption() {
        return quizOption;
    }

    public void setQuizOption(QuizOption quizOption) {
        this.quizOption = quizOption;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public HashMap<Question, Boolean> getSessionData() {
        return sessionData;
    }

    public int getCorrectAnswers() {
        int i = 0;
        for (Map.Entry<Question, Boolean> entry : sessionData.entrySet()) {
            if (entry.getValue()) {
                i++;
            }
        }
        return i;
    }


    public void addQuestionActivity(Question question, boolean isCorrect) {
        this.sessionData.put(question, isCorrect);


    }

    public void setSessionData(HashMap<Question, Boolean> sessionData) {
        this.sessionData = sessionData;
    }
}
