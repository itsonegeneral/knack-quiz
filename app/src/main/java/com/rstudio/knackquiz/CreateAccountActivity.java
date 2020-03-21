package com.rstudio.knackquiz;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.util.Patterns;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.github.florent37.materialtextfield.MaterialTextField;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
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

import java.util.regex.Pattern;

public class CreateAccountActivity extends AppCompatActivity {


    private TextInputEditText etPass1,etPass2,etEmail,etUserName;
    private Button btSignUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
        setToolbar();
        initValues();

        btSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validateInput()){
                    checkUserNameExists();
                }
            }
        });
        checkUserNameExists();

    }

    private void checkUserNameExists(){
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("users");
        ref.orderByChild("userName").equalTo("hello").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    Toast.makeText(CreateAccountActivity.this, String.valueOf(dataSnapshot.getValue()), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    private void createAccount(){
        String email= etEmail.getText().toString();
        String pass = etPass1.getText().toString();

        FirebaseAuth mAuth= FirebaseAuth.getInstance();
        mAuth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){

                }else{

                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if(e instanceof FirebaseAuthUserCollisionException){
                    Snackbar.make(findViewById(android.R.id.content),"Email already in use",Snackbar.LENGTH_SHORT).show();
                }else{
                    Snackbar.make(findViewById(android.R.id.content),"Account creation failed",Snackbar.LENGTH_SHORT).show();
                }
            }
        });
    }

    private boolean validateInput(){
        String username = etUserName.getText().toString();
        String email = etEmail.getText().toString();
        String pass1 = etPass1.getText().toString();
        String pass2 = etPass2.getText().toString();
        if(username.isEmpty()){
            etUserName.setError("Enter Username");
        }else if(username.length()>15){
            etUserName.setError("Max length is 15");
        }else if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            etEmail.setError("Invalid Email");
        }else if(pass1.isEmpty()){
            etPass1.setError("Enter Password");
        }else if (!pass1.equals(pass2)){
            etPass2.setError("Passwords do not match");
        }else{
            return true;
        }
        return false;
    }

    private void initValues(){
        etUserName = findViewById(R.id.et_usernameCreateAccount);
        etPass1 = findViewById(R.id.et_pass1CreateAccount);
        etPass2 = findViewById(R.id.et_pass2CreateAccount);
        etEmail = findViewById(R.id.et_emailCreateAccount);


        btSignUp = findViewById(R.id.bt_signUp);
    }



    private void setToolbar(){
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
        overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
    }
}
