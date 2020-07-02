package com.rstudio.knackquiz.modules.friends.fragments;

import android.content.Context;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rstudio.knackquiz.R;
import com.rstudio.knackquiz.datastore.DataStore;
import com.rstudio.knackquiz.helpers.DBKeys;
import com.rstudio.knackquiz.models.Challenge;
import com.rstudio.knackquiz.modules.friends.adapters.ChallengeAdapter;

import java.util.ArrayList;

public class FriendChallengesFragment extends Fragment {

    private LinearLayout layout;
    private Context context;
    private RecyclerView rViewChallenges;
    private ArrayList<Challenge> challenges;
    private ChallengeAdapter adapter;

    public FriendChallengesFragment() {
        if (context == null) {
            context = getContext();
        }
    }

    public FriendChallengesFragment(Context context) {
        this.context = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        layout = (LinearLayout) inflater.inflate(R.layout.fragment_challenges, container, false);
        return layout;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initValues();
        loadData();
    }

    private void loadData() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference(DBKeys.KEY_CHALLENGES);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                challenges.clear();
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Challenge challenge = snapshot.getValue(Challenge.class);
                        if (challenge.getChallengerPlayer().getPlayerID().equals(DataStore.getCurrentPlayerID(context))
                                || challenge.getReceivedPlayer().getPlayerID().equals(DataStore.getCurrentPlayerID(context))) {
                            if (!DataStore.isChallengeShown(challenge.getChallengeId(), context)) {
                                challenges.add(challenge);
                                DataStore.addChallengeShown(challenge.getChallengeId(), context);
                            }
                        }
                    }
                    if (!challenges.isEmpty()) {
                        adapter = new ChallengeAdapter(challenges, context);
                        rViewChallenges.setAdapter(adapter);
                    } else {

                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void initValues() {
        rViewChallenges = layout.findViewById(R.id.rView_challenges);
        rViewChallenges.setLayoutManager(new LinearLayoutManager(context));
        challenges = new ArrayList<>();

    }
}
