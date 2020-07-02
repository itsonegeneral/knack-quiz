package com.rstudio.knackquiz.services;

import android.app.AlertDialog;
import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rstudio.knackquiz.AlertActivity;
import com.rstudio.knackquiz.R;
import com.rstudio.knackquiz.datastore.DataStore;
import com.rstudio.knackquiz.helpers.DBKeys;
import com.rstudio.knackquiz.helpers.DateHelper;
import com.rstudio.knackquiz.helpers.KeyStore;
import com.rstudio.knackquiz.models.Challenge;
import com.rstudio.knackquiz.models.Player;

import java.util.ArrayList;

public class OnlineManagerService extends Service {

    public static final int TIME_INTERVAL = 10000;
    Handler handler = new Handler();
    private Player player;
    private ValueEventListener valueEventListener;
    private DatabaseReference challengesRef;
    private ArrayList<String> shownChallenges = new ArrayList<>();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        player = DataStore.getCurrentPlayer(this);
        player.setActive(true);
        player.setLastOnline(DateHelper.getCurrentFormattedDate());
        setOnlineOnInterval();
        if (player == null) {
        }
      //  checkReceivedChallenges();
        return START_NOT_STICKY;
    }

    private void checkReceivedChallenges() {
        challengesRef = FirebaseDatabase.getInstance().getReference(DBKeys.KEY_CHALLENGES);
        valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Challenge challenge = snapshot.getValue(Challenge.class);
                        if (challenge.getReceivedPlayer().getPlayerID().equals(player.getPlayerID()) && !challenge.isRejected() && !challenge.isAccepted()) {
                            if (!shownChallenges.contains(challenge)) {
                                shownChallenges.add(challenge.getChallengeId());
                                showChallengeReceivedWindow(challenge);
                            }
                        }
                    }
                } else {
                    //Do Nothing
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        challengesRef.addValueEventListener(valueEventListener);
    }


    AlertDialog dialog;

    private void showChallengeReceivedWindow(Challenge challenge) {
        Intent intent = new Intent(getApplicationContext(), AlertActivity.class);
        intent.putExtra(KeyStore.CHALLENGE_SERIAL, challenge);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private void setOnlineOnInterval() {
        final DatabaseReference ref = FirebaseDatabase.getInstance().getReference(DBKeys.KEY_ONLINE_PLAYERS);
        // Define the code block to be executed
        Runnable runnableCode = new Runnable() {
            @Override
            public void run() {
                // Do something here on the main thread
                Log.d("Handlers", "Called on main thread");
                player.setLastOnline(DateHelper.getCurrentFormattedDate());
                ref.child(player.getPlayerID()).setValue(player);
                // Repeat this the same runnable code block again another 2 seconds
                // 'this' is referencing the Runnable object
                handler.postDelayed(this, TIME_INTERVAL);
            }
        };
        // Start the initial runnable task by posting through the handler
        handler.post(runnableCode);
    }

    @Override
    public void onDestroy() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference(DBKeys.KEY_ONLINE_PLAYERS);
        ref.child(player.getPlayerID()).removeValue();
        challengesRef.removeEventListener(valueEventListener);
        super.onDestroy();
    }


}