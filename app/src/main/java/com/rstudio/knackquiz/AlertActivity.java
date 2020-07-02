package com.rstudio.knackquiz;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.rstudio.knackquiz.gameplay.QuestionActivity;
import com.rstudio.knackquiz.helpers.DBKeys;
import com.rstudio.knackquiz.helpers.KeyStore;
import com.rstudio.knackquiz.models.Challenge;
import com.rstudio.knackquiz.models.Question;

import java.util.ArrayList;

public class AlertActivity extends AppCompatActivity {
    Challenge challenge;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alert);

        challenge = (Challenge) getIntent().getSerializableExtra(KeyStore.CHALLENGE_SERIAL);

        TextView tvTitle, tvMessage;
        Button btAccept, btReject;
        tvTitle = findViewById(R.id.tv_TitleAlertDefault);
        tvMessage = findViewById(R.id.tv_MessageAlertDefault);
        btAccept = findViewById(R.id.bt_YesAlertDefault);
        btReject = findViewById(R.id.bt_NoAlertDefault);
        tvTitle.setText("Challenge");
        tvMessage.setText(challenge.getMessage());
        btAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<Question> questions = challenge.getQuestions();
                Gson gson = new Gson();
                String StringQuest = gson.toJson(questions);

                Intent intent = new Intent(AlertActivity.this, QuestionActivity.class);
                intent.putExtra(KeyStore.QUESTION_SERIAL, StringQuest);
                intent.putExtra(KeyStore.CHALLENGE_SERIAL, challenge);
                startActivity(intent);
                finish();

                DatabaseReference ref= FirebaseDatabase.getInstance().getReference(DBKeys.KEY_CHALLENGES).child(challenge.getChallengeId());
                ref.child("accepted").setValue(true);

            }
        });
        btReject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rejectChallenge();
            }
        });
    }

    private void rejectChallenge() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference(DBKeys.KEY_CHALLENGES).child(challenge.getChallengeId());
        challenge.setRejected(true);
        ref.setValue(challenge).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    finish();    
                }else{
                    Toast.makeText(AlertActivity.this, "Failed to reject", Toast.LENGTH_SHORT).show();
                }
                
            }
        });
    }
}
