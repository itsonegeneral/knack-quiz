package com.rstudio.knackquiz;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.easyandroidanimations.library.FadeInAnimation;
import com.google.android.material.button.MaterialButton;

public class IntroActivity extends AppCompatActivity {

    private ImageView imgIcon;
    private TextView tvAppName;
    private MaterialButton btnStarted;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);


        imgIcon = findViewById(R.id.img_iconIntro);
        tvAppName = findViewById(R.id.tv_animtextNameIntro);
        btnStarted = findViewById(R.id.btn_GetStarted);
        tvAppName.setVisibility(View.GONE);
        btnStarted.setVisibility(View.GONE);

        new FadeInAnimation(imgIcon).setDuration(2000).animate();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                tvAppName.setVisibility(View.VISIBLE);
                new FadeInAnimation(tvAppName).setDuration(1500).animate();
            }
        }, 600);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                btnStarted.setVisibility(View.VISIBLE);
                new FadeInAnimation(btnStarted).setDuration(1000).animate();
            }
        }, 1500);

        btnStarted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(IntroActivity.this,IntroFavouriteActivity.class));

            }
        });

    }

}
