package com.rstudio.knackquiz.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.rstudio.knackquiz.R;
import com.rstudio.knackquiz.gameplay.QuestionActivity;
import com.rstudio.knackquiz.models.QuizOption;

import java.util.ArrayList;

public class QuizOptionAdapter extends RecyclerView.Adapter<QuizOptionAdapter.MyViewHolder> {
    private ArrayList<QuizOption> quizOptions;
    private Context mContext;

    public QuizOptionAdapter(Context context, ArrayList<QuizOption> quizOptions) {
        this.mContext = context;
        this.quizOptions = quizOptions;
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tvTitle, tvDescription, tvRewardCoins, tvEntryCoins;
        MaterialCardView cardView, cardCoins;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            tvEntryCoins = itemView.findViewById(R.id.tv_entryCoinQuizOption);
            tvRewardCoins = itemView.findViewById(R.id.tv_rewardCoinsQuizOption);
            tvDescription = itemView.findViewById(R.id.tv_quizOptionsDescriptionList);
            tvTitle = itemView.findViewById(R.id.tv_quizOptionsTitle);
            cardCoins = itemView.findViewById(R.id.card_quizOptionCoins);
            cardView = itemView.findViewById(R.id.card_quizOptionItem);

        }
    }

    @NonNull
    @Override
    public QuizOptionAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_view_quiz_options, parent, false);
        return new QuizOptionAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull QuizOptionAdapter.MyViewHolder holder, int position) {
        final QuizOption quizOption = quizOptions.get(position);

        holder.tvTitle.setText(quizOption.getTitle());
        holder.tvDescription.setText(quizOption.getDescription());
        holder.tvRewardCoins.setText(quizOption.getRewardcoins());
        holder.tvEntryCoins.setText(quizOption.getEntrycoins());


        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(mContext, "Clicked", Toast.LENGTH_SHORT).show();
            }
        });

        holder.cardCoins.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, QuestionActivity.class);
                intent.putExtra("options", quizOption);
                mContext.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return quizOptions.size();
    }

}
