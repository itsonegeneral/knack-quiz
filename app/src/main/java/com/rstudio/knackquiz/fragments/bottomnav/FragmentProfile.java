package com.rstudio.knackquiz.fragments.bottomnav;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.transition.TransitionInflater;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.rstudio.knackquiz.HomeActivity;
import com.rstudio.knackquiz.LoginActivity;
import com.rstudio.knackquiz.R;
import com.rstudio.knackquiz.gameplay.QuestionActivity;

public class FragmentProfile extends Fragment {

    private Context context;
    private RelativeLayout layout;
    private LinearLayout llSignInLayout;
    private TextView tvTest;
    private HomeActivity homeActivity;
    private static final String TAG = "FragmentProfile";
    private MaterialButton btSignin;
    private FirebaseAuth mAuth;

    public FragmentProfile() {
        if (context == null)
            context = getContext();
    }

    public FragmentProfile(Context context) {
        this.context = context;
        homeActivity = (HomeActivity) context;

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        layout = (RelativeLayout) inflater.inflate(R.layout.fragment_profile, container, false);
        mAuth = FirebaseAuth.getInstance();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setSharedElementEnterTransition(TransitionInflater.from(getContext()).inflateTransition(android.R.transition.move));
        }

        btSignin = layout.findViewById(R.id.bt_signinFragProfile);
        tvTest = layout.findViewById(R.id.tv_testlogin);
        tvTest.setVisibility(View.GONE);
        llSignInLayout = layout.findViewById(R.id.ll_profileSignInLayout);
        llSignInLayout.setVisibility(View.GONE);

        if(mAuth.getCurrentUser()==null){
            llSignInLayout.setVisibility(View.VISIBLE);
        }else{
            tvTest.setVisibility(View.VISIBLE);
            tvTest.setText(mAuth.getUid());
        }


        btSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context, LoginActivity.class));
            }
        });

        return layout;
    }

}
