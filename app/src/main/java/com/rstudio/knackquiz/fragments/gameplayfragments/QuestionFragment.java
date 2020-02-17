package com.rstudio.knackquiz.fragments.gameplayfragments;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.github.lzyzsd.circleprogress.DonutProgress;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textview.MaterialTextView;
import com.rstudio.knackquiz.R;
import com.rstudio.knackquiz.helpers.CircleProgressBar;


public class QuestionFragment extends Fragment {

    private Context context;
    private LinearLayout layout;
    private DonutProgress progress;
    private static final String TAG = "QuestionFragment";
    private CircleProgressBar circleProgressBar;
    private int time = 10;
    private TextView tvTimer,tvOption1,tvOption2,tvOption3,tvOption4;
    private MaterialCardView cardOption1,cardOption2,cardOption3,cardOption4;
    private MaterialTextView tvQuestion;
    CountDownTimer countDownTimer;
    int i=1;

    public QuestionFragment(Context context){
        this.context = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        layout = (LinearLayout) inflater.inflate(R.layout.fragment_question, container, false);

        initValues();

        Log.d(TAG, "Timer started ");


        setProgressBar();
        setListeners();

        return layout;
    }

    private void setListeners(){
        tvOption1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               showAnswer(tvOption1.getText().toString());
            }
        });

        cardOption2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAnswer(tvOption2.getText().toString());
            }
        });
        cardOption3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAnswer(tvOption3.getText().toString());
            }
        });
        cardOption4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAnswer(tvOption4.getText().toString());
            }
        });

    }

    private void showAnswer(String answer){
        if(answer.equals("Violet")){
            tvOption1.setBackgroundResource(R.drawable.background_option_correct);
        }else{
            tvOption1.setBackgroundResource(R.drawable.background_option_correct);
            tvOption2.setBackgroundResource(R.drawable.background_option_wrong);
            tvOption3.setBackgroundResource(R.drawable.background_option_wrong);
            tvOption4.setBackgroundResource(R.drawable.background_option_wrong);
        }

        switch (answer){
            case "":{

                break;
            }
        }
        countDownTimer.cancel();
    }

    private void setProgressBar() {
        circleProgressBar.setMax(time*2);
        circleProgressBar.setProgress(time* 2);


        countDownTimer = new CountDownTimer(time*1000,500) {
            @Override
            public void onTick(long millisUntilFinished) {

          /*      pgBarTime.setMax(100);
                pgBarTime.setProgress((int)seconds);*/
                circleProgressBar.setProgressWithAnimation((int)millisUntilFinished / 500);
                tvTimer.setText(String.valueOf((int)millisUntilFinished / 1000));
            }

            @Override
            public void onFinish() {
                circleProgressBar.setProgress(0);
            }
        };

        countDownTimer.start();
    }


    private void initValues(){
        circleProgressBar = layout.findViewById(R.id.circleProgressBar);
        tvTimer = layout.findViewById(R.id.tv_questionTime);
        tvTimer.setText(String.valueOf(time));


        //Options
        cardOption1 = layout.findViewById(R.id.card_questionOption1);
        cardOption2 = layout.findViewById(R.id.card_questionOption2);
        cardOption3 = layout.findViewById(R.id.card_questionOption3);
        cardOption4 = layout.findViewById(R.id.card_questionOption4);


        tvOption1 = layout.findViewById(R.id.tv_questionOption1);
        tvOption2 = layout.findViewById(R.id.tv_questionOption2);
        tvOption3 = layout.findViewById(R.id.tv_questionOption3);
        tvOption4 = layout.findViewById(R.id.tv_questionOption4);
    }
}
