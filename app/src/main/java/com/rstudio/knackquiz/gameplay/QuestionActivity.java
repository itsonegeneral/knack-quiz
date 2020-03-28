package com.rstudio.knackquiz.gameplay;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.MutableData;
import com.firebase.client.Transaction;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.rstudio.knackquiz.R;
import com.rstudio.knackquiz.datastore.DataStore;
import com.rstudio.knackquiz.fragments.gameplayfragments.QuestionFragment;
import com.rstudio.knackquiz.helpers.DBClass;
import com.rstudio.knackquiz.models.GameSession;
import com.rstudio.knackquiz.models.Player;
import com.rstudio.knackquiz.models.Question;
import com.rstudio.knackquiz.models.QuizOption;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Method;
import java.util.ArrayList;

public class QuestionActivity extends AppCompatActivity {

    private ArrayList<Question> questions = new ArrayList<>();
    private static final String TAG = "QuestionActivity";
    private int index = 0;
    private int correct = 0;
    private TextView tvCoins;
    private String cat;
    private Player player;
    private QuizOption quizOption;
    private Activity questionActivty;
    private int coins = 0;
    private GameSession gameSession;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_question);
        Firebase.setAndroidContext(this);
        setToolbar();
        gameSession = new GameSession();


        if (getIntent().hasExtra("options")) {
            quizOption = (QuizOption) getIntent().getSerializableExtra("options");
        } else {
            finish();
        }


/*
        MediaPlayer mediaPlayer = MediaPlayer.create(this,R.raw.clock_ticking);
       // mediaPlayer.start();

        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mp.start();
            }
        });
        */

        getQuestions();


        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("users");
        ref.child(DataStore.getCurrentPlayerID(getApplicationContext())).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    player = dataSnapshot.getValue(Player.class);
                    tvCoins.setText(String.valueOf(player.getCoins()));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    public void addCoins(final int coins) {

        String url = "https://knack-quiz-b57d5.firebaseio.com/users/" + DataStore.getCurrentPlayerID(this);
        Firebase database = new Firebase(url);
        database.runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
                Player player = new Player();
                if (mutableData.getValue() != null) {
                    player = mutableData.getValue(Player.class);
                    player.setCoins(player.getCoins() + coins);
                    Log.d(TAG, "doTransaction: Completed Trascation");
                }
                mutableData.setValue(player);
                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(FirebaseError firebaseError, boolean b, com.firebase.client.DataSnapshot dataSnapshot) {
                if (b) {
                    Toast.makeText(QuestionActivity.this, "Transaction Complete", Toast.LENGTH_SHORT).show();
                }
            }


        });
    }


    public void showNextQuestion() {
        Log.d(TAG, "showNextQuestion: " + index);
        if (index < questions.size()) {
            showFragment(index);
            index++;
        } else {
            Toast.makeText(this, "Quiz finished", Toast.LENGTH_SHORT).show();
            finish();
            Intent intent = new Intent(getApplicationContext(), QuizFinishActivity.class);
            intent.putExtra("result", gameSession.getCorrectAnswers());
            intent.putExtra("coins",coins);
            startActivity(intent);
            //Show final result
        }
    }

    public void addGameActivity(Question question, boolean isCorrect) {
        if (isCorrect) {
            coins += Integer.parseInt(quizOption.getRewardcoins()) / Integer.parseInt(quizOption.getQuestionsize());
            Toast.makeText(questionActivty, coins + "", Toast.LENGTH_SHORT).show();
        }
        gameSession.addQuestionActivity(question, isCorrect);
    }

    private void showFragment(int i) {
        QuestionFragment questionFragment = new QuestionFragment(questionActivty, questions.get(i), i + 1, quizOption);
        FragmentTransaction fr = getSupportFragmentManager().beginTransaction();
        //   fr.setCustomAnimations(R.anim.slide_in_right,R.anim.slide_in_right);
        try {
            fr.replace(R.id.frame_fragHolderQuestion, questionFragment).commit();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        }
    }


    private void getQuestions() {
        String url = DBClass.urlGetQuestions + "?category=" + quizOption.getCategoryName() + "&limit=" + quizOption.getQuestionsize();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    Log.d(TAG, "onResponse: " + jsonObject);
                    JSONArray array = jsonObject.getJSONArray("data");
                    Gson gson = new Gson();
                    for (int i = 0; i < array.length(); i++) {
                        Question question = gson.fromJson(array.getJSONObject(i).toString(), Question.class);
                        questions.add(question);
                    }
                    if (questions.isEmpty()) {
                        Toast.makeText(QuestionActivity.this, "No Questions Found", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        questionActivty = QuestionActivity.this;
                        showNextQuestion();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.d(TAG, "onErrorResponse: " + error);
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }

    private void setToolbar() {
        MaterialToolbar toolbar = findViewById(R.id.tb_activityQuestion);
        tvCoins = findViewById(R.id.tb_tvcoinsMainCommon);
        setSupportActionBar(toolbar);
        ImageView img = findViewById(R.id.imgBtn_tb_close);
        img.setBackground(getResources().getDrawable(R.drawable.ic_close_white_24dp));
        getSupportActionBar().setTitle("");
    }

}

