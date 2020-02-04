package com.rstudio.knackquiz;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.rstudio.knackquiz.adapters.CategoryAdapter;
import com.rstudio.knackquiz.helpers.CategoryHelper;

public class IntroFavouriteActivity extends AppCompatActivity {

    private TextView tvSKip ;
    private ImageButton btnNext;
    private CategoryAdapter categoryAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro_favourite);
        initValues();

        categoryAdapter = new CategoryAdapter(this);
        RecyclerView recyclerView = findViewById(R.id.rView_introCategories);
        recyclerView.setLayoutManager(new GridLayoutManager(this,4));
        recyclerView.setAdapter(categoryAdapter);

        tvSKip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(getApplicationContext(),HomeActivity.class));
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*if(checkSelections()){

                }*/
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
