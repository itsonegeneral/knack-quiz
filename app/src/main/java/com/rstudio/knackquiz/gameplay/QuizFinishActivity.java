package com.rstudio.knackquiz.gameplay;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.rstudio.knackquiz.R;
import com.rstudio.knackquiz.models.GameSession;

public class QuizFinishActivity extends AppCompatActivity {


    private TextView tvCorrect;
    private GameSession gameSession;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_finish);
        int c =  getIntent().getIntExtra("result",0);
        tvCorrect = findViewById(R.id.tv_correctAnswersFinishActivity);
        tvCorrect.setText(String.valueOf(c) + " Correct Answers");
        


    }
}
