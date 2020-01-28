package com.rstudio.knackquiz;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

public class IntroFavouriteActivity extends AppCompatActivity {

    private TextView tvSKip ;
    private ImageButton btnNext;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro_favourite);
        initValues();


        tvSKip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(getApplicationContext(),HomeActivity.class));
            }
        });

    }

    private void initValues(){
        btnNext = findViewById(R.id.imgBtn_submitFavourites);
        tvSKip = findViewById(R.id.tv_skipFavourites);
    }
}
