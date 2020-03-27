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
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.auth.FirebaseAuth;
import com.rstudio.knackquiz.HomeActivity;
import com.rstudio.knackquiz.LoginActivity;
import com.rstudio.knackquiz.R;
import com.rstudio.knackquiz.datastore.DataStore;
import com.rstudio.knackquiz.gameplay.QuestionActivity;
import com.rstudio.knackquiz.models.Player;

import de.hdodenhof.circleimageview.CircleImageView;

public class FragmentProfile extends Fragment {

    private Context context;
    private RelativeLayout layout;
    private LinearLayout llSignInLayout;
    private HomeActivity homeActivity;
    private static final String TAG = "FragmentProfile";
    private MaterialButton btSignin;
    private FirebaseAuth mAuth;
    private LinearLayout llLoggedInLayout;
    private CircleImageView imgProfile;
    private TextView tvUserName;
    private Player player;

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
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            setSharedElementEnterTransition(TransitionInflater.from(getContext()).inflateTransition(android.R.transition.move));
        }

        initValues();
        getPlayerData();


        btSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context, LoginActivity.class));
            }
        });

        return layout;
    }

    private void initValues() {
        mAuth = FirebaseAuth.getInstance();
        btSignin = layout.findViewById(R.id.bt_signinFragProfile);
        llSignInLayout = layout.findViewById(R.id.ll_profileSignInLayout);
        llSignInLayout.setVisibility(View.GONE);
        llSignInLayout.setVisibility(View.VISIBLE);
        llLoggedInLayout = layout.findViewById(R.id.ll_loggedInProfileFragment);


        imgProfile = layout.findViewById(R.id.img_profileFragment);
        tvUserName = layout.findViewById(R.id.tv_userNameProfileFragment);


        if (mAuth.getCurrentUser() == null) {
            llSignInLayout.setVisibility(View.VISIBLE);
            llLoggedInLayout.setVisibility(View.GONE);
        } else {
         //   tvUserName.setText(mAuth.getCurrentUser().getEmail());
            llSignInLayout.setVisibility(View.GONE);
            llLoggedInLayout.setVisibility(View.VISIBLE);
        }
    }


    private void getPlayerData(){
        player = DataStore.getCurrentPlayer(context);

    }

}
