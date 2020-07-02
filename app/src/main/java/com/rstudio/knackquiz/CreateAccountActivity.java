package com.rstudio.knackquiz;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.Activity;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.firebase.client.Firebase;
import com.github.florent37.materialtextfield.MaterialTextField;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.rstudio.knackquiz.datastore.DataStore;
import com.rstudio.knackquiz.helpers.DBKeys;
import com.rstudio.knackquiz.models.Player;

import java.util.regex.Pattern;

public class CreateAccountActivity extends AppCompatActivity {


    private static final String TAG = "CreateAccountActivity";
    private TextInputEditText etPass1, etPass2, etEmail, etUserName;
    private Button btSignUp;
    private AlertDialog alertDialog;
    private LottieAnimationView lottieView;
    private AlertDialog.Builder builder;
    private TextView tvLoadingText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
        setToolbar();
        initValues();

        btSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateInput()) {
                    checkUserNameExists();
                }
            }
        });


        Log.d(TAG, "askForAccountSync: " + DataStore.getCurrentPlayerID(this));

    }

    private void checkUserNameExists() {
        String username = etUserName.getText().toString();
        showLoadingAlert(); //First showing alert
        tvLoadingText.setText("Creating your account...");
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("users");
        ref.orderByChild("userName").equalTo(username).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    Snackbar.make(findViewById(android.R.id.content), "Username is taken", Snackbar.LENGTH_SHORT).show();
                    alertDialog.dismiss();
                } else {
                    askForAccountSync();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                alertDialog.dismiss();
                Toast.makeText(CreateAccountActivity.this, "Error occurred!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    //Ask user to sync current progress with account
    private void askForAccountSync() {
        Player player = DataStore.getCurrentPlayer(this);
        if (player.getCoins() > 0) {
            askMigration();
        } else {
            //create account if no coin balance in account
            //TODO check for also level progress in future
            createAccount();
        }
    }

    private void askMigration() {
        View customView = LayoutInflater.from(this).inflate(R.layout.alert_custom_default, null);

        TextView tvTitle, tvMessage;
        MaterialButton btOK, btNo;
        //init views
        btOK = customView.findViewById(R.id.bt_YesAlertDefault);
        btNo = customView.findViewById(R.id.bt_NoAlertDefault);
        tvTitle = customView.findViewById(R.id.tv_TitleAlertDefault);
        tvMessage = customView.findViewById(R.id.tv_MessageAlertDefault);


        //Set TextView Values
        tvTitle.setText("Sync");
        tvMessage.setText("Do you want to sync current progress?");

        btNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        btOK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        builder.setView(customView);
        builder.show();
    }

    private void createAccount() {
        final String email = etEmail.getText().toString();
        String pass = etPass1.getText().toString();
        updateLoadingAlertText("Creating your account...");
        final FirebaseAuth mAuth = FirebaseAuth.getInstance();
        mAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Player player = new Player();
                    player.setCoins(0);
                    player.setEmailID(email);
                    player.setUserName(etUserName.getText().toString());
                    player.setPlayerID(mAuth.getUid());
                    player.setPlayerRegisterType(DBKeys.KEY_REGISTERED);

                    uploadPlayerData(player);

                } else {
                    alertDialog.dismiss();
                    Snackbar.make(findViewById(android.R.id.content), "Faield to create account", Snackbar.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                alertDialog.dismiss();
                if (e instanceof FirebaseAuthUserCollisionException) {
                    Snackbar.make(findViewById(android.R.id.content), "Email already in use", Snackbar.LENGTH_SHORT).show();
                } else {
                    Log.d(TAG, "onFailure: " + e.getMessage());
                    Snackbar.make(findViewById(android.R.id.content), "Account creation failed", Snackbar.LENGTH_SHORT).show();
                }
            }
        });
    }


    private void uploadPlayerData(final Player player) {
        updateLoadingAlertText("Setting up your account...");
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("users");
        ref.child(player.getPlayerID()).setValue(player).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    DataStore.setCurrentPlayer(player, getApplicationContext());
                    Snackbar.make(findViewById(android.R.id.content), "Account Created", Snackbar.LENGTH_SHORT).show();
                    showSuccessAlert();
                } else {
                    Snackbar.make(findViewById(android.R.id.content), "Account creation failed", Snackbar.LENGTH_SHORT).show();
                    //TODO delete user account
                    alertDialog.dismiss();
                }
            }
        });
    }

    private boolean validateInput() {
        String username = etUserName.getText().toString();
        String email = etEmail.getText().toString();
        String pass1 = etPass1.getText().toString();
        String pass2 = etPass2.getText().toString();
        if (username.isEmpty()) {
            etUserName.setError("Enter Username");
            etUserName.requestFocus();
        } else if (username.length() < 4) {
            etUserName.setError("Too short");
            etUserName.requestFocus();
        } else if (username.length() > 15) {
            etUserName.setError("Max length is 15");
            etUserName.requestFocus();
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError("Invalid Email");
            etEmail.requestFocus();
        } else if (pass1.isEmpty()) {
            etPass1.setError("Enter Password");
            etPass1.requestFocus();
        } else if (pass1.length() < 6) {
            etPass1.setError("Too short");
            etPass1.requestFocus();
        } else if (!pass1.equals(pass2)) {
            etPass2.requestFocus();
            etPass2.setError("Passwords do not match");
        } else {
            return true;
        }
        return false;
    }

    private void initValues() {
        etUserName = findViewById(R.id.et_usernameCreateAccount);
        etPass1 = findViewById(R.id.et_pass1CreateAccount);
        etPass2 = findViewById(R.id.et_pass2CreateAccount);
        etEmail = findViewById(R.id.et_emailCreateAccount);


        btSignUp = findViewById(R.id.bt_signUp);


        //Setup Alert Dialogue
        builder = new AlertDialog.Builder(CreateAccountActivity.this);


    }

    private void showLoadingAlert() {
        View customView = LayoutInflater.from(CreateAccountActivity.this).inflate(R.layout.alert_loading_view, null);
        tvLoadingText = customView.findViewById(R.id.tv_loadingTextLoadingAlert);
        lottieView = customView.findViewById(R.id.lottieViewLoadingAlert);
        lottieView.setAnimation(R.raw.loading_bouncing);
        lottieView.loop(true);
        lottieView.playAnimation();
        builder.setView(customView);
        alertDialog = builder.create();
        alertDialog.show();
    }

    private void showSuccessAlert() {
        lottieView.setAnimation(R.raw.anim_green_tick);
        lottieView.loop(false);
        lottieView.playAnimation();
        tvLoadingText.setText("Account created successfully!");


    }

    private void updateLoadingAlertText(String loadingText) {
        tvLoadingText.setText(loadingText);
    }

    private void setToolbar() {
        Toolbar toolbar = findViewById(R.id.tb_createAccount);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        TextView tv = findViewById(R.id.tv_toolbarHeadingSimple);
        tv.setText("Create Account");
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        finish();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

}
