package com.rstudio.knackquiz.fragments.bottomnav;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.transition.TransitionInflater;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rstudio.knackquiz.HomeActivity;
import com.rstudio.knackquiz.LoginActivity;
import com.rstudio.knackquiz.R;
import com.rstudio.knackquiz.datastore.DataStore;
import com.rstudio.knackquiz.gameplay.QuestionActivity;
import com.rstudio.knackquiz.helpers.DBKeys;
import com.rstudio.knackquiz.models.Player;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;

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
    private TextView tvUserName, tvEmail, tvProgressLevelStart, tvProgressLevelEnd, tvDob, tvPhone;
    private String phoneFormat = "xxxx-xxxxxx";
    private Player player;
    private ProgressBar pgBar;

    DatePickerDialog picker;


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
        tvEmail = layout.findViewById(R.id.tv_emailProfilFragment);
        tvPhone = layout.findViewById(R.id.tv_phoneProfilFragment);
        tvProgressLevelStart = layout.findViewById(R.id.tv_startLevelProfilFragment);
        tvProgressLevelEnd = layout.findViewById(R.id.tv_endProfilFragment);
        tvDob = layout.findViewById(R.id.tv_dobProfilFragment);


        pgBar = layout.findViewById(R.id.pgBar_levelProfileFragment);


        if (mAuth.getCurrentUser() == null) {
            llSignInLayout.setVisibility(View.VISIBLE);
            llLoggedInLayout.setVisibility(View.GONE);
        } else {
            //   tvUserName.setText(mAuth.getCurrentUser().getEmail());
            getPlayerData();
            llSignInLayout.setVisibility(View.GONE);
            llLoggedInLayout.setVisibility(View.VISIBLE);
        }
    }


    private void getPlayerData() {
        player = DataStore.getCurrentPlayer(context);
        tvEmail.setText(player.getEmailID());
        tvUserName.setText(player.getUserName());
        if (!player.getPhotoURL().isEmpty()) {
            Picasso.get().load(player.getPhotoURL()).into(imgProfile);
        }

        setFormatedPhone();
        pgBar.setProgress((int) player.getLevelPoint());
        tvProgressLevelStart.setText("Solder");
        tvProgressLevelEnd.setText("Guard");


        if (player.getDob() != null) {
            tvDob.setText(player.getDob());
        } else {
            tvDob.setText("Set DOB");
            tvDob.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    picker = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

                            Calendar calendar = null;
                            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                                calendar = Calendar.getInstance();
                                calendar.set(year, month, dayOfMonth);
                                SimpleDateFormat format = new SimpleDateFormat("dd-MMM-yyyy");
                                String date = format.format(calendar.getTime()).replace("-", " ");
                                tvDob.setText(date);
                                player.setDob(date);
                                updatePlayer();
                            }

                        }
                    }, 2004, 1, 1);
                    picker.show();
                }
            });
        }

    }

    private void updatePlayer() {

        DatabaseReference ref;
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            ref = FirebaseDatabase.getInstance().getReference(DBKeys.KEY_USERS).child(DBKeys.KEY_REGISTERED);
        } else {
            ref = FirebaseDatabase.getInstance().getReference(DBKeys.KEY_USERS).child(DBKeys.KEY_UNREGISTERED);
        }

        ref.child(player.getPlayerID()).setValue(player);
    }

    private void setFormatedPhone() {
        Log.d(TAG, "setFormatedPhone: " + player.getPhone());
        tvPhone.setText(player.getPhone());
    }

}
