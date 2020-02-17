package com.rstudio.knackquiz.gameplay;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.widget.FrameLayout;

import com.rstudio.knackquiz.R;
import com.rstudio.knackquiz.fragments.gameplayfragments.QuestionFragment;

public class QuestionActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_question);

        FrameLayout frameLayout = findViewById(R.id.frame_fragHolderQuestion);

        QuestionFragment questionFragment= new QuestionFragment(this);


        getSupportFragmentManager().beginTransaction().replace(R.id.frame_fragHolderQuestion,questionFragment).commit();

        MediaPlayer mediaPlayer = MediaPlayer.create(this,R.raw.clock_ticking);
       // mediaPlayer.start();

        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mp.start();
            }
        });


    }
}
