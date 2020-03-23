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
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;

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
                showLoadingAlert();
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
        if (result.isSuccess()) {
            GoogleSignInAccount account = result.getSignInAccount();
            Toast.makeText(this, "Success", Toast.LENGTH_SHORT).show();
          /*  idToken = account.getIdToken();
            name = account.getDisplayName();
            email = account.getEmail();
            // you can store user data to SharedPreference
            AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
            firebaseAuthWithGoogle(credential);*/
        } else {
            // Google Sign In failed, update UI appropriately
            Log.e(TAG, "Login Unsuccessful. " + result);
            Toast.makeText(this, "Login Unsuccessful", Toast.LENGTH_SHORT).show();
        }
    }

    /* private void firebaseAuthWithGoogle(AuthCredential credential){

         firebaseAuth.signInWithCredential(credential)
                 .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                     @Override
                     public void onComplete(@NonNull Task<AuthResult> task) {
                         Log.d(TAG, "signInWithCredential:onComplete:" + task.isSuccessful());
                         if(task.isSuccessful()){
                             Toast.makeText(MainActivity.this, "Login successful", Toast.LENGTH_SHORT).show();
                             gotoProfile();
                         }else{
                             Log.w(TAG, "signInWithCredential" + task.getException().getMessage());
                             task.getException().printStackTrace();
                             Toast.makeText(MainActivity.this, "Authentication failed.",
                                     Toast.LENGTH_SHORT).show();
                         }

                     }
                 });
     }


 */
    private boolean validateInput() {
        String email = etEmail.getText().toString();
        String pass = etEmail.getText().toString();

        if (email.isEmpty()) {
            etEmail.setError("Enter email");
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError("Invalid email");
        } else if (pass.isEmpty()) {
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

        FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(LoginActivity.this, "Login Success", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(LoginActivity.this, "Login Failed " + task.getException().toString(), Toast.LENGTH_SHORT).show();
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
