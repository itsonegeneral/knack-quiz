package com.rstudio.knackquiz.helpers;

import android.os.Handler;
import android.view.View;

import com.google.android.material.card.MaterialCardView;

public class DelayAnimator implements Runnable {

    private Handler handler;
    private MaterialCardView view;
    private long seconds;


    public DelayAnimator(){

    }

    public DelayAnimator(MaterialCardView cardView,long seconds){
        this.view = cardView;
        this.seconds = seconds;

    }
    @Override
    public void run() {
        try {
            Thread.sleep(seconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        view.setVisibility(View.VISIBLE);
    }
}
