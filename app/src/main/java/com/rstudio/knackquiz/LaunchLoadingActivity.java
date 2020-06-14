package com.rstudio.knackquiz;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rstudio.knackquiz.datastore.DataStore;
import com.rstudio.knackquiz.helpers.DBKeys;
import com.rstudio.knackquiz.helpers.KeyStore;
import com.rstudio.knackquiz.models.Player;
import com.rstudio.knackquiz.network.NetworkChecker;
import com.rstudio.knackquiz.network.NoNetworkActivity;

public class LaunchLoadingActivity extends AppCompatActivity {

    private Player player = new Player();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch_loading);

        if (NetworkChecker.isNetworkAvailable(getApplicationContext())) {
            loadData();
        } else {
            startActivity(new Intent(this, NoNetworkActivity.class));
        }

    }

    private void loadData() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference(DBKeys.KEY_USERS);


        ref.child(DataStore.getCurrentPlayerID(this)).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    player = dataSnapshot.getValue(Player.class);
                    DataStore.setCurrentPlayer(player, LaunchLoadingActivity.this);
                    startHomeActivity();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void startHomeActivity() {
        Intent intent = new Intent(this, HomeActivity.class);
        intent.putExtra(KeyStore.PLAYER_SERIAL, player);
        startActivity(intent);
        finish();
        overridePendingTransition(0, 0);
    }


}
