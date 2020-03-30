package com.rstudio.knackquiz;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.easyandroidanimations.library.FadeInAnimation;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rstudio.knackquiz.datastore.DataStore;
import com.rstudio.knackquiz.helpers.KeyStore;
import com.rstudio.knackquiz.models.Player;

import java.util.UUID;

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
                startActivity(new Intent(IntroActivity.this, IntroFavouriteActivity.class));
            }
        });


        //Assign a new id to user
        String userID = UUID.randomUUID().toString().replace("-", "");
        Player player = new Player();
        player.setPlayerID(userID);
        player.setPlayerRegisterType(KeyStore.UNREGISTERED_USER);
        player.setDiamonds(0);
        player.setCoins(0);
        player.setUserName("Guest Player");
        DataStore.setCurrentPlayer(player, this);

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("users");
        ref.child(userID).setValue(player);


        SharedPreferences sharedPreferences = getSharedPreferences(DataStore.FIRSTTIME, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(DataStore.STATUS, "NOT");
        editor.apply();


    }

}
