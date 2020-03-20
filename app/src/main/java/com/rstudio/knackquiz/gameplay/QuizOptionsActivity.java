package com.rstudio.knackquiz.gameplay;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.rstudio.knackquiz.R;
import com.rstudio.knackquiz.adapters.QuizOptionAdapter;
import com.rstudio.knackquiz.datastore.DataStore;
import com.rstudio.knackquiz.helpers.DBClass;
import com.rstudio.knackquiz.models.Player;
import com.rstudio.knackquiz.models.QuizOption;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class QuizOptionsActivity extends AppCompatActivity {

    private static final String TAG = "QuizOptionsActivity";
    private String cat;
    private QuizOptionAdapter quizOptionAdapter;
    private RecyclerView recyclerView;
    private ArrayList<QuizOption> quizOptions;
    private TextView tvCoins;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_options);
        initValues();
        setToolbar();
        loadData();
        loadCoins();



    }

    private void loadData() {
        String url = DBClass.urlGetQuizOptions + "?category=" + cat;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            Gson gson = new Gson();
                            if (jsonObject.getString("status").equalsIgnoreCase("success")) {
                                JSONArray data = jsonObject.getJSONArray("data");
                                for (int i = 0; i < data.length(); i++) {
                                    QuizOption quizOption = gson.fromJson(data.getJSONObject(i).toString(), QuizOption.class);
                                    quizOptions.add(quizOption);
                                    Log.d(TAG, "onResponse: " + data.getJSONObject(i).toString());
                                }
                                quizOptionAdapter = new QuizOptionAdapter(QuizOptionsActivity.this, quizOptions);
                                recyclerView.setAdapter(quizOptionAdapter);
                            } else {
                                Toast.makeText(QuizOptionsActivity.this, "Server side error", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(QuizOptionsActivity.this, "Failed loading data", Toast.LENGTH_SHORT).show();
                finish();
            }
        });


        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }

    private void initValues() {
        cat = getIntent().getStringExtra("cat");
        quizOptions = new ArrayList<>();
        recyclerView = findViewById(R.id.rView_quizOptions);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
    }



    private void loadCoins() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("users");
        ref.child(DataStore.getCurrentPlayerID(this)).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    long coins = dataSnapshot.child("coins").getValue(Long.class);

                    tvCoins.setText(String.valueOf(coins));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        finish();
        overridePendingTransition(0,0);
        return super.onOptionsItemSelected(item);
    }

    private void setToolbar() {
        Toolbar toolbar = findViewById(R.id.tb_quizOptionsActivity);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        TextView tv = findViewById(R.id.tv_toolbarHeadingSimple);
        ImageView imgIcon = findViewById(R.id.imgBtn_tb_close);
        tvCoins = findViewById(R.id.tb_tvcoinsMainCommon);

        imgIcon.setImageBitmap((Bitmap) getIntent().getParcelableExtra("bitmap"));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        tv.setText(cat + " Challenges");
    }

}
