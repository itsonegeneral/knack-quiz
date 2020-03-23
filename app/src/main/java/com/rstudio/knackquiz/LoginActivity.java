package com.rstudio.knackquiz;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.PatternMatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.firebase.client.Firebase;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rstudio.knackquiz.datastore.DataStore;
import com.rstudio.knackquiz.helpers.DBClass;
import com.rstudio.knackquiz.helpers.DBKeys;
import com.rstudio.knackquiz.models.Player;

import java.util.regex.Pattern;

public class LoginActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener {

    private MaterialButton btnLogin;
    private SignInButton signInButton;
    private EditText etEmail, etPassword;
    private GoogleApiClient googleApiClient;
    private static final String TAG = "LoginActivity";
    private TextView tvCreateAccount;
    private FirebaseAuth mAuth;
    private final int RC_SIGN_IN = 13;
    private LottieAnimationView lottieView;
    private AlertDialog.Builder builder;
    private TextView tvLoadingText;
    private AlertDialog alertDialog;
    private Player player=new Player();
    private FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initValues();

        setUpGoogleApi();


        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validateInput()) {
                    login();
                }
            }
        });

        tvCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, CreateAccountActivity.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                googleSignIn();

            }
        });

    }

    private void setUpGoogleApi() {
        mAuth = FirebaseAuth.getInstance();
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        googleApiClient = new GoogleApiClient.Builder(getApplicationContext())
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
    }

    private void googleSignIn() {


        Intent intent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        startActivityForResult(intent, RC_SIGN_IN);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }

    private void handleSignInResult(GoogleSignInResult result) {
        String idToken,name;
        if (result.isSuccess()) {
            GoogleSignInAccount account = result.getSignInAccount();
            idToken = account.getIdToken();
            name = account.getDisplayName();
            player.setEmailID(account.getEmail());
            player.setUserName(account.getEmail().substring(0,account.getEmail().indexOf("@")));
            // you can store user data to SharedPreference
            Log.d(TAG, "handleSignInResult: UserName" + player.getUserName());
            AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
            firebaseAuthWithGoogle(credential);
        } else {
            // Google Sign In failed, update UI appropriately
            Log.e(TAG, "Login Unsuccessful. " + result);
            Toast.makeText(this, "Login Unsuccessful", Toast.LENGTH_SHORT).show();
        }
    }

     private void firebaseAuthWithGoogle(AuthCredential credential){
        showLoadingAlert();
         firebaseAuth.signInWithCredential(credential)
                 .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                     @Override
                     public void onComplete(@NonNull Task<AuthResult> task) {
                         String id = firebaseAuth.getUid();
                         player.setPlayerID(id);
                         Log.d(TAG, "onComplete:handle " + id);
                         if(task.isSuccessful()){
                             getPlayerData(player);
                         }else{
                             Log.w(TAG, "signInWithCredential" + task.getException().getMessage());
                             Toast.makeText(LoginActivity.this, "Authentication failed.",
                                     Toast.LENGTH_SHORT).show();
                             alertDialog.dismiss();
                         }

                     }
                 });
     }

    private void getPlayerData(final Player player) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference(DBKeys.KEY_USERS).child(DBKeys.KEY_REGISTERED);
        ref.child(player.getPlayerID()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    getExistingPlayerData();
                }else{
                    uploadPlayerData(player);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(LoginActivity.this, "Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void uploadPlayerData(final Player player) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference(DBKeys.KEY_USERS).child(DBKeys.KEY_REGISTERED);
        ref.child(player.getPlayerID()).setValue(player).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    DataStore.setCurrentPlayer(player, getApplicationContext());
                    Snackbar.make(findViewById(android.R.id.content), "Welcome "+ player.getUserName(), Snackbar.LENGTH_SHORT).show();
                } else {
                    Snackbar.make(findViewById(android.R.id.content), "Account creation failed", Snackbar.LENGTH_SHORT).show();
                }
                alertDialog.dismiss();
            }
        });
    }

    private void getExistingPlayerData(){
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference(DBKeys.KEY_USERS)
                .child(DBKeys.KEY_REGISTERED);
        ref.child(mAuth.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    player = dataSnapshot.getValue(Player.class);
                    Snackbar.make(findViewById(android.R.id.content),"Welcome " + player.getUserName(),Snackbar.LENGTH_SHORT).show();
                }else{
                    mAuth.signOut();
                    Toast.makeText(LoginActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                }
                alertDialog.dismiss();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    private boolean validateInput() {
        String email = etEmail.getText().toString();
        String pass = etEmail.getText().toString();

        if (email.isEmpty()) {
            etEmail.setError("Enter email");
            etEmail.requestFocus();
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError("Invalid email");
            etEmail.requestFocus();
        } else if (pass.isEmpty()) {
            etPassword.requestFocus();
            etPassword.setError("Enter Password");
        } else {
            return true;
        }

        return false;
    }

    private void login() {
        Toast.makeText(this, "Logging in", Toast.LENGTH_SHORT).show();

        String email = etEmail.getText().toString();
        String pass = etPassword.getText().toString();
        showLoadingAlert();
        FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    getExistingPlayerData();
                    Toast.makeText(LoginActivity.this, "Login Success", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(LoginActivity.this, "Login Failed " + task.getException().toString(), Toast.LENGTH_SHORT).show();
                    alertDialog.dismiss();
                }
            }
        });

    }

    private void initValues() {
        btnLogin = findViewById(R.id.bt_login);
        signInButton = findViewById(R.id.gBtn_signin);
        tvCreateAccount = findViewById(R.id.tv_createAnAccountLogin);
        etEmail = findViewById(R.id.et_emailLoginPage);
        etPassword = findViewById(R.id.et_passwordLoginPage);
        builder = new AlertDialog.Builder(this);
        firebaseAuth = FirebaseAuth.getInstance();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


    private void showLoadingAlert() {
        View customView = LayoutInflater.from(LoginActivity.this).inflate(R.layout.alert_loading_view, null);
        tvLoadingText = customView.findViewById(R.id.tv_loadingTextLoadingAlert);
        tvLoadingText.setText("Logging in...");
        lottieView = customView.findViewById(R.id.lottieViewLoadingAlert);
        lottieView.setAnimation(R.raw.loading_bouncing);
        lottieView.loop(true);
        lottieView.playAnimation();
        builder.setView(customView);
        alertDialog = builder.create();
        alertDialog.show();
    }

}
