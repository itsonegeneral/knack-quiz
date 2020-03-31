package com.rstudio.knackquiz.gameplay.contests;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rstudio.knackquiz.R;
import com.rstudio.knackquiz.adapters.ContestAdapter;
import com.rstudio.knackquiz.helpers.DBKeys;
import com.rstudio.knackquiz.models.Contest;

import java.util.ArrayList;

public class AllContestsFragment extends Fragment {

    private Context context;
    private ArrayList<Contest> contestsList;
    private RelativeLayout layout;
    private ContestAdapter contestAdapter;
    private RecyclerView recyclerView;


    public AllContestsFragment() {
        if (context == null) {
            this.context = getContext();
        }
    }

    public AllContestsFragment(Context context) {
        this.context = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        layout = (RelativeLayout) inflater.inflate(R.layout.fragment_my_contest, container, false);

        initValues();
        getData();

        return layout;
    }

    private void getData() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference(DBKeys.KEY_CONTESTS);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Contest contest = snapshot.getValue(Contest.class);
                        contestsList.add(contest);
                    }

                    if (contestsList.isEmpty()) {

                    } else {
                        contestAdapter = new ContestAdapter(context, contestsList);
                        recyclerView.setAdapter(contestAdapter);
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void initValues() {
        contestsList = new ArrayList<>();
        recyclerView = layout.findViewById(R.id.rView_myContests);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        //Hide add contest
        ExtendedFloatingActionButton fab = layout.findViewById(R.id.fab_hostContest);
        fab.setVisibility(View.GONE);
    }
}
