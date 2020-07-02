package com.rstudio.knackquiz.gameplay;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rstudio.knackquiz.R;
import com.rstudio.knackquiz.adapters.ContestScoreAdapter;
import com.rstudio.knackquiz.datastore.DataStore;
import com.rstudio.knackquiz.helpers.DBClass;
import com.rstudio.knackquiz.helpers.DBKeys;
import com.rstudio.knackquiz.helpers.KeyStore;
import com.rstudio.knackquiz.models.Challenge;
import com.rstudio.knackquiz.models.Contest;
import com.rstudio.knackquiz.models.ContestPlayer;
import com.rstudio.knackquiz.models.GameSession;
import com.rstudio.knackquiz.models.Player;
import com.rstudio.knackquiz.models.QuizOption;

import java.util.HashMap;
import java.util.Map;

public class QuizFinishActivity extends AppCompatActivity {


    private TextView tvCorrect, tvCoins, tvWinType;
    private ImageView imgIcReward;

    private GameSession gameSession;
    private Challenge challenge;
    private QuizOption quizOption;
    private Contest contest;
    private static final String TAG = "QuizFinishActivity";
    int c;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_finish);
        gameSession = (GameSession) getIntent().getSerializableExtra("session");
        c = gameSession.getCorrectAnswers();
        int coins = getIntent().getIntExtra("coins", 0);
        quizOption = (QuizOption) getIntent().getSerializableExtra("option");
        tvCorrect = findViewById(R.id.tv_correctAnswersFinishActivity);
        tvCoins = findViewById(R.id.tv_prizeCoinsQuizFinishActivity);
        tvWinType = findViewById(R.id.tv_winTypeQuizFinishActivity);
        imgIcReward = findViewById(R.id.img_icCoinQuizFinishActivity);


        tvCorrect.setText(c + " Correct Answers");
        tvCoins.setText(String.valueOf(coins));

        if (gameSession.getTYPE().equals(GameSession.TYPE_MULTIPLAYER)) {
            challenge = (Challenge) getIntent().getSerializableExtra(KeyStore.CHALLENGE_SERIAL);
            addPlayerPoints();
        } else if (gameSession.getTYPE().equals(GameSession.TYPE_CONTEST)) {
            contest = (Contest) getIntent().getSerializableExtra(KeyStore.CONTEST_SERIAL);
            showContestResult();
        } else {
            if (quizOption.getRewardType().equalsIgnoreCase("diamond")) {
                addDiamonds(coins);
                imgIcReward.setBackground(getResources().getDrawable(R.drawable.ic_diamond));
                tvWinType.setText("Diamond(s)");
            } else {
                addCoins(coins);
            }
        }



        findViewById(R.id.bt_homeQuizFinish).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void showContestResult() {
        TextView tvScore = findViewById(R.id.tv_scoreQuizFinishActivity);
        tvScore.setText(c + "");
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference(DBKeys.KEY_CONTESTS);
        ref.child(contest.getId()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    contest = dataSnapshot.getValue(Contest.class);
                    ContestPlayer player = new ContestPlayer();
                    player.setPlayerID(DataStore.getCurrentPlayerID(getApplicationContext()));
                    player.setPlayerName(DataStore.getCurrentPlayer(getApplicationContext()).getUserName());
                    player.setScore(c);
                    contest.addPlayedPlayer(player);
                    Log.d(TAG, "onDataChange: " + contest.getId());
                    updateData();
                    showScoreList();
                } else {

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void showScoreList() {
        ContestScoreAdapter adapter = new ContestScoreAdapter(this, contest.getPlayedPLayers());
        RecyclerView rView = findViewById(R.id.rView_scoresListQuizFinish);
        rView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        rView.setAdapter(adapter);
    }

    private void updateData() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference(DBKeys.KEY_CONTESTS).child(contest.getId());
        ref.setValue(contest);
    }

    private void addLeaderBoardEntry() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, DBClass.urlSetLeaderboard, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("playerid", DataStore.getCurrentPlayerID(getApplicationContext()));
                params.put("score",String.valueOf(c));
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void addPlayerPoints() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference(DBKeys.KEY_CHALLENGES).child(challenge.getChallengeId());
        if (DataStore.getCurrentPlayerID(getApplicationContext()).equals(challenge.getChallengerPlayer().getPlayerID())) {
            ref.child("challengerScore").setValue(c);
        } else {
            ref.child("receiverScore").setValue(c);
        }
    }

    public void addDiamonds(final int diamonds) {
        String url;
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            url = "https://knack-quiz-b57d5.firebaseio.com/users/registered/" + DataStore.getCurrentPlayerID(this);
        } else {
            url = "https://knack-quiz-b57d5.firebaseio.com/users/unregistered/" + DataStore.getCurrentPlayerID(this);
        }

        Firebase database = new Firebase(url);
        database.runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
                Player player = new Player();
                if (mutableData.getValue() != null) {
                    player = mutableData.getValue(Player.class);
                    player.setDiamonds(player.getDiamonds() + diamonds);
                    Log.d(TAG, "doTransaction: Completed Trascation");
                }
                mutableData.setValue(player);
                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(FirebaseError firebaseError, boolean b, com.firebase.client.DataSnapshot dataSnapshot) {
                if (b) {
                    Toast.makeText(QuizFinishActivity.this, "Transaction Complete", Toast.LENGTH_SHORT).show();
                }
            }


        });
    }

    public void addCoins(final int coins) {
        String url;
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            url = "https://knack-quiz-b57d5.firebaseio.com/users/registered/" + DataStore.getCurrentPlayerID(this);
        } else {
            url = "https://knack-quiz-b57d5.firebaseio.com/users/unregistered/" + DataStore.getCurrentPlayerID(this);
        }

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
                    Toast.makeText(QuizFinishActivity.this, "Transaction Complete", Toast.LENGTH_SHORT).show();
                }
            }


        });
    }


}
