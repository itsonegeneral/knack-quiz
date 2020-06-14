package com.rstudio.knackquiz.network;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.rstudio.knackquiz.R;

public class NoNetworkActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_no_network);
    }
}
