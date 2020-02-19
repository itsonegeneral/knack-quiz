package com.rstudio.knackquiz.gameplay;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;
import com.rstudio.knackquiz.R;
import com.rstudio.knackquiz.fragments.gameplayfragments.QuestionFragment;
import com.rstudio.knackquiz.helpers.DBClass;
import com.rstudio.knackquiz.models.Question;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Method;
import java.util.ArrayList;

public class QuestionActivity extends AppCompatActivity {

    private ArrayList<Question> questions=new ArrayList<>();
    private static final String TAG = "QuestionActivity";
    private int index=0;
    private int correct = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_question);



/*
        MediaPlayer mediaPlayer = MediaPlayer.create(this,R.raw.clock_ticking);
       // mediaPlayer.start();

        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mp.start();
            }
        });
        */

        getQuestions();



    }

    public void showNextQuestion(){
        Log.d(TAG, "showNextQuestion: " + index);
        if(index<questions.size()){
            showFragment(index);
            index++;
        }else{
            Toast.makeText(this, "Quiz finished", Toast.LENGTH_SHORT).show();
            //Show final result
        }
    }

    private void showFragment(int i){
        QuestionFragment questionFragment= new QuestionFragment(QuestionActivity.this,questions.get(i),i+1);
        FragmentTransaction fr = getSupportFragmentManager().beginTransaction();
     //   fr.setCustomAnimations(R.anim.slide_in_right,R.anim.slide_in_right);
        fr.replace(R.id.frame_fragHolderQuestion,questionFragment).commit();
    }

    private void startGamePlay(){
        showFragment(index);
    }

    private void getQuestions(){
        String url = DBClass.urlGetQuestions + "?category=Cricket&limit=10";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    Log.d(TAG, "onResponse: " + jsonObject);
                    JSONArray array = jsonObject.getJSONArray("data");
                    Gson gson = new Gson();
                    for(int i =0;i<array.length();i++){
                        Question question = gson.fromJson(array.getJSONObject(i).toString(),Question.class);
                        questions.add(question);
                    }


                    showNextQuestion();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.d(TAG, "onErrorResponse: " + error);
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }
}

