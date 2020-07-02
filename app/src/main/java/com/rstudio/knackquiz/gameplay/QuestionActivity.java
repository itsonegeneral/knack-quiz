package com.rstudio.knackquiz.gameplay;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.session.MediaSession;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
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
import com.google.firebase.database.util.GAuthToken;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.rstudio.knackquiz.R;
import com.rstudio.knackquiz.datastore.DataStore;
import com.rstudio.knackquiz.fragments.gameplayfragments.QuestionFragment;
import com.rstudio.knackquiz.helpers.DBClass;
import com.rstudio.knackquiz.helpers.DBKeys;
import com.rstudio.knackquiz.helpers.KeyStore;
import com.rstudio.knackquiz.models.Challenge;
import com.rstudio.knackquiz.models.Contest;
import com.rstudio.knackquiz.models.GameSession;
import com.rstudio.knackquiz.models.Player;
import com.rstudio.knackquiz.models.Question;
import com.rstudio.knackquiz.models.QuizOption;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.security.Key;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class QuestionActivity extends AppCompatActivity {

    private ArrayList<Question> questions = new ArrayList<>();
    private static final String TAG = "QuestionActivity";
    private int index = 0;
    private int correct = 0;
    private TextView tvCoins;
    private Player player;
    private QuizOption quizOption;
    private Activity questionActivty;
    private int coins = 0;
    private GameSession gameSession;
    private String category;
    private Challenge challenge;
    private Contest contest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_question);
        Firebase.setAndroidContext(this);
        gameSession = new GameSession();


        if (getIntent().hasExtra("options") && getIntent().hasExtra("category")) {
            category = getIntent().getStringExtra("category");
            quizOption = (QuizOption) getIntent().getSerializableExtra("options");
            gameSession.setTYPE(GameSession.TYPE_NORMAL);
            getQuestions();
        } else if (getIntent().hasExtra(DBKeys.KEY_CONTESTS)) {
            contest = (Contest) getIntent().getSerializableExtra(KeyStore.CONTEST_SERIAL);
            gameSession.setTYPE(GameSession.TYPE_CONTEST);
            quizOption = new QuizOption();
            quizOption.setQuestionsize(String.valueOf(contest.getQuestions().size()));
            quizOption.setCatid(contest.getCategoryId());
            quizOption.setGametime(String.valueOf(contest.getQuestionTime()));
            questions = contest.getQuestions();
            quizOption.setRewardType("diamond");
            quizOption.setRewardcoins("0");
            quizOption.setRewardcoins(String.valueOf(contest.getRewardValue()));
            category = contest.getCategory();

            showNextQuestion();
        } else if (getIntent().hasExtra(KeyStore.QUESTION_SERIAL)) {
            playMultiplayerMode();
            showNextQuestion();
        } else {
            finish();
        }


        setToolbar();
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

    private void playMultiplayerMode() {
        Gson gson = new Gson();
        String q = getIntent().getStringExtra(KeyStore.QUESTION_SERIAL);
        challenge = (Challenge) getIntent().getSerializableExtra(KeyStore.CHALLENGE_SERIAL);
        Type type = new TypeToken<ArrayList<Question>>() {
        }.getType();
        questions = gson.fromJson(q, type);
        gameSession.setTYPE(GameSession.TYPE_MULTIPLAYER);
        quizOption = new QuizOption();
        quizOption.setCategoryName(challenge.getCategory());
        quizOption.setCatid(challenge.getCategoryId());
        quizOption.setGametime("10");
        quizOption.setRewardType("diamond");
        quizOption.setRewardcoins("100");
        quizOption.setQuestionsize("10");
        category = challenge.getCategory();
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
                    //   Toast.makeText(QuestionActivity.this, "Transaction Complete", Toast.LENGTH_SHORT).show();
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
            intent.putExtra("session", gameSession);
            intent.putExtra("coins", coins);
            intent.putExtra("option", quizOption);

            if (gameSession.getTYPE().equals(GameSession.TYPE_MULTIPLAYER)) {
                intent.putExtra(KeyStore.CHALLENGE_SERIAL, challenge);
            } else if (gameSession.getTYPE().equals((GameSession.TYPE_CONTEST))) {
                intent.putExtra(KeyStore.CONTEST_SERIAL, contest);
            }

            startActivity(intent);
            //Show final result
        }
    }

    public void addGameActivity(Question question, boolean isCorrect) {
        if (isCorrect) {
            coins += Integer.parseInt(quizOption.getRewardcoins()) / Integer.parseInt(quizOption.getQuestionsize());
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
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("category", quizOption.getCategoryName());
                params.put("limit", "10");
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }

    private void setToolbar() {
        MaterialToolbar toolbar = findViewById(R.id.tb_activityQuestion);
        tvCoins = findViewById(R.id.tb_tvcoinsMainCommon);
        TextView tv = findViewById(R.id.tv_toolbarHeadingSimple);
        tv.setText(category + " Quiz");
        setSupportActionBar(toolbar);
        ImageView img = findViewById(R.id.imgBtn_tb_close);
        img.setBackground(getResources().getDrawable(R.drawable.ic_close_white_24dp));
        getSupportActionBar().setTitle("");
    }

}

