package com.rstudio.knackquiz.gameplay;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.rstudio.knackquiz.R;
import com.rstudio.knackquiz.models.GameSession;

public class QuizFinishActivity extends AppCompatActivity {


    private TextView tvCorrect;
    private GameSession gameSession;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_finish);
        gameSession = (GameSession) getIntent().getSerializableExtra("result");
        tvCorrect = findViewById(R.id.tv_correctAnswersFinishActivity);
        //tvCorrect.setText(gameSession.);

    }
}
