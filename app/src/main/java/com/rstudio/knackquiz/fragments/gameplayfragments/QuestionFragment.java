package com.rstudio.knackquiz.fragments.gameplayfragments;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.net.IpSecManager;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
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

import com.easyandroidanimations.library.FadeInAnimation;
import com.easyandroidanimations.library.FadeOutAnimation;
import com.github.lzyzsd.circleprogress.DonutProgress;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.textview.MaterialTextView;
import com.rstudio.knackquiz.R;
import com.rstudio.knackquiz.gameplay.QuestionActivity;
import com.rstudio.knackquiz.helpers.CircleProgressBar;
import com.rstudio.knackquiz.helpers.DelayAnimator;
import com.rstudio.knackquiz.models.Question;


public class QuestionFragment extends Fragment {

    private Context context;
    private LinearLayout layout;
    private DonutProgress progress;
    private static final String TAG = "QuestionFragment";
    private CircleProgressBar circleProgressBar;
    private int time = 10;
    private TextView tvTimer, tvOption1, tvOption2, tvOption3, tvOption4;
    private MaterialCardView cardOption1, cardOption2, cardOption3, cardOption4;
    private MaterialTextView tvQuestion;
    private Question question;
    private Handler handler = new Handler();
    private CountDownTimer countDownTimer;
    private QuestionActivity questionActivity;
    private int questionNumber;
    int i = 1;

    public QuestionFragment(Context context, Question question, int number) {
        this.context = context;
        this.question = question;
        this.questionActivity = (QuestionActivity) context;
        this.questionNumber = number;
        Log.d(TAG, "QuestionFragment: " + number);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        layout = (LinearLayout) inflater.inflate(R.layout.fragment_question, container, false);

        initValues();

        Log.d(TAG, "Timer started ");


        ViewAnimator viewAnimator = new ViewAnimator(cardOption1, 800);
        new Thread(viewAnimator).start();

        ViewAnimator viewAnimator1 = new ViewAnimator(cardOption2, 1000);
        new Thread(viewAnimator1).start();
        ViewAnimator viewAnimator2 = new ViewAnimator(cardOption3, 1200);
        new Thread(viewAnimator2).start();
        ViewAnimator viewAnimator3 = new ViewAnimator(cardOption4, 1400, true);
        new Thread(viewAnimator3).start();

        setListeners();

        return layout;
    }

    private void setListeners() {
        tvOption1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAnswer(tvOption1, cardOption1);
            }
        });

        cardOption2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAnswer(tvOption2, cardOption2);
            }
        });
        cardOption3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAnswer(tvOption3, cardOption3);
            }
        });
        cardOption4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showAnswer(tvOption4, cardOption4);
            }
        });


    }

    private void showAnswer(TextView tvOption, final MaterialCardView cardOption) {


        tvOption1.setBackgroundResource(R.drawable.background_option_neutral);
        tvOption2.setBackgroundResource(R.drawable.background_option_neutral);
        tvOption3.setBackgroundResource(R.drawable.background_option_neutral);
        tvOption4.setBackgroundResource(R.drawable.background_option_neutral);

        tvOption1.setTextColor(getResources().getColor(R.color.colorTextLight));
        tvOption2.setTextColor(getResources().getColor(R.color.colorTextLight));
        tvOption3.setTextColor(getResources().getColor(R.color.colorTextLight));
        tvOption4.setTextColor(getResources().getColor(R.color.colorTextLight));

        if (!tvOption.getText().toString().equals(question.getAnswer())) {
            tvOption.setBackgroundResource(R.drawable.background_option_wrong);
            tvOption.setTextColor(getResources().getColor(R.color.colorOptionWrong));
            cardOption.setVisibility(View.VISIBLE);

        }else{
            Toast.makeText(context, "Correct", Toast.LENGTH_SHORT).show();
            questionActivity.addCoins(10);
        }
        if (question.getAnswer().equals(tvOption1.getText().toString())) {
            cardOption1.setVisibility(View.VISIBLE);
            tvOption1.setBackgroundResource(R.drawable.background_option_correct);
            tvOption1.setTextColor(getResources().getColor(R.color.colorOptionCorrect));

        } else if (question.getAnswer().equals(tvOption2.getText().toString())) {
            cardOption2.setVisibility(View.VISIBLE);
            tvOption2.setBackgroundResource(R.drawable.background_option_correct);
            tvOption2.setTextColor(getResources().getColor(R.color.colorOptionCorrect));


        } else if (question.getAnswer().equals(tvOption3.getText().toString())) {
            cardOption3.setVisibility(View.VISIBLE);
            tvOption3.setBackgroundResource(R.drawable.background_option_correct);
            tvOption3.setTextColor(getResources().getColor(R.color.colorOptionCorrect));


        } else if (question.getAnswer().equals(tvOption4.getText().toString())) {
            cardOption4.setVisibility(View.VISIBLE);
            tvOption4.setBackgroundResource(R.drawable.background_option_correct);
            tvOption4.setTextColor(getResources().getColor(R.color.colorOptionCorrect));


        }


        cardOption1.setEnabled(false);
        tvOption1.setEnabled(false);
        cardOption2.setEnabled(false);
        cardOption3.setEnabled(false);
        cardOption4.setEnabled(false);
        countDownTimer.cancel();

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                new FadeOutAnimation(tvQuestion).setDuration(500).animate();
                new FadeOutAnimation(cardOption1).setDuration(500).animate();
                new FadeOutAnimation(cardOption2).setDuration(500).animate();
                new FadeOutAnimation(cardOption3).setDuration(500).animate();
                new FadeOutAnimation(cardOption4).setDuration(500).animate();

                questionActivity.showNextQuestion();

            }
        }, 1300);

    }


    private void setProgressBar() {
        circleProgressBar.setMax(time * 2);
        circleProgressBar.setProgress(time * 2);


        countDownTimer = new CountDownTimer(time * 1000, 500) {
            @Override
            public void onTick(long millisUntilFinished) {

          /*      pgBarTime.setMax(100);
                pgBarTime.setProgress((int)seconds);*/
                circleProgressBar.setProgressWithAnimation((int) millisUntilFinished / 500);
                tvTimer.setText(String.valueOf((int) millisUntilFinished / 1000));
            }

            @Override
            public void onFinish() {
                circleProgressBar.setProgress(0);
                questionActivity.showNextQuestion();
            }
        };

        countDownTimer.start();
    }


    private void initValues() {
        circleProgressBar = layout.findViewById(R.id.circleProgressBar);
        tvTimer = layout.findViewById(R.id.tv_questionTime);
        tvTimer.setText(String.valueOf(time));


        //Options
        tvQuestion = layout.findViewById(R.id.mttv_question);
        cardOption1 = layout.findViewById(R.id.card_questionOption1);
        cardOption2 = layout.findViewById(R.id.card_questionOption2);
        cardOption3 = layout.findViewById(R.id.card_questionOption3);
        cardOption4 = layout.findViewById(R.id.card_questionOption4);


        tvOption1 = layout.findViewById(R.id.tv_questionOption1);
        tvOption2 = layout.findViewById(R.id.tv_questionOption2);
        tvOption3 = layout.findViewById(R.id.tv_questionOption3);
        tvOption4 = layout.findViewById(R.id.tv_questionOption4);


        char a = question.getQuestion().charAt(0);
        if (a == ' ') {
            tvQuestion.setText(question.getQuestion().substring(1, tvQuestion.length()));
        }

        String quest = questionNumber + "." + question.getQuestion();
        question.setQuestion(quest);

        tvQuestion.setText(question.getQuestion());
        tvOption1.setText(question.getOption1());
        tvOption2.setText(question.getOption2());
        tvOption3.setText(question.getOption3());
        tvOption4.setText(question.getOption4());
        cardOption1.setVisibility(View.INVISIBLE);
        cardOption2.setVisibility(View.INVISIBLE);
        cardOption3.setVisibility(View.INVISIBLE);
        cardOption4.setVisibility(View.INVISIBLE);


        new FadeInAnimation(tvQuestion).setDuration(500).animate();


    }


    public class ViewAnimator implements Runnable {

        private MaterialCardView cardView;
        private long milliseconds;
        private boolean isLast = false;

        public ViewAnimator(MaterialCardView cardView, long milliseconds) {
            this.cardView = cardView;
            this.milliseconds = milliseconds;
        }

        public ViewAnimator(MaterialCardView cardView, long milliseconds, boolean isLast) {
            this.cardView = cardView;
            this.milliseconds = milliseconds;
            this.isLast = isLast;
        }


        @Override
        public void run() {
            try {
                Thread.sleep(milliseconds);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            handler.post(new Runnable() {
                @Override
                public void run() {
                    cardView.setVisibility(View.VISIBLE);
                    new FadeInAnimation(cardView).setDuration(200).animate();
                    if (isLast) {
                        setProgressBar();

                    }
                }
            });
        }
    }


}
