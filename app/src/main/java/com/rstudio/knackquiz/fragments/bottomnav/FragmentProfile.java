package com.rstudio.knackquiz.fragments.bottomnav;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.button.MaterialButton;
import com.rstudio.knackquiz.HomeActivity;
import com.rstudio.knackquiz.R;
import com.rstudio.knackquiz.gameplay.QuestionActivity;

public class FragmentProfile extends Fragment {

    private Context context;
    private LinearLayout layout;
    private HomeActivity homeActivity;
    private static final String TAG = "FragmentProfile";


    public FragmentProfile() {
        context = getContext();
    }

    public FragmentProfile(Context context) {
        this.context = context;
        homeActivity = (HomeActivity) context;

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        layout = (LinearLayout) inflater.inflate(R.layout.fragment_profile, container, false);


        return layout;
    }

    @Override
    public void onResume() {
        super.onResume();

        Log.d(TAG, "onResume: ");

        if (homeActivity != null)
            try {
                homeActivity.getSupportActionBar().hide();
            } catch (NullPointerException e) {
                e.printStackTrace();
            }

    }
}
