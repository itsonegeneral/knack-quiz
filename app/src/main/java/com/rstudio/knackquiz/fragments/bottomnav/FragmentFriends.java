package com.rstudio.knackquiz.fragments.bottomnav;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.button.MaterialButton;
import com.rstudio.knackquiz.R;
import com.rstudio.knackquiz.gameplay.QuestionActivity;

public class FragmentFriends extends Fragment {

    private Context context;
    private LinearLayout layout;
    private MaterialButton btnPlay; //TODO remove this


    public FragmentFriends(Context context) {
        this.context = context;

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        layout = (LinearLayout) inflater.inflate(R.layout.fragment_friends, container, false);

        return layout;
    }
}
