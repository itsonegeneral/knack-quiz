package com.rstudio.knackquiz.gameplay;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.MutableData;
import com.firebase.client.Transaction;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rstudio.knackquiz.R;
import com.rstudio.knackquiz.datastore.DataStore;
import com.rstudio.knackquiz.helpers.DBKeys;
import com.rstudio.knackquiz.models.GameSession;
import com.rstudio.knackquiz.models.Player;
import com.rstudio.knackquiz.models.QuizOption;

public class QuizFinishActivity extends AppCompatActivity {


    private TextView tvCorrect, tvCoins, tvWinType;
    private ImageView imgIcReward;

    private GameSession gameSession;
    private QuizOption quizOption;
    private static final String TAG = "QuizFinishActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_finish);
        int c = getIntent().getIntExtra("result", 0);
        int coins = getIntent().getIntExtra("coins", 0);
        quizOption = (QuizOption) getIntent().getSerializableExtra("option");
        tvCorrect = findViewById(R.id.tv_correctAnswersFinishActivity);
        tvCoins = findViewById(R.id.tv_prizeCoinsQuizFinishActivity);
        tvWinType = findViewById(R.id.tv_winTypeQuizFinishActivity);
        imgIcReward = findViewById(R.id.img_icCoinQuizFinishActivity);


        tvCorrect.setText(String.valueOf(c) + " Correct Answers");
        tvCoins.setText(String.valueOf(coins));

        if (quizOption.getRewardType().equalsIgnoreCase("diamond")) {
            addDiamonds(coins);
            imgIcReward.setBackground(getResources().getDrawable(R.drawable.ic_diamond));
            tvWinType.setText("Diamond(s)");
        } else {
            addCoins(coins);
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
