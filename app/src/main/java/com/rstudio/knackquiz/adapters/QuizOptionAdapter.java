package com.rstudio.knackquiz.adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.MutableData;
import com.firebase.client.Transaction;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.auth.FirebaseAuth;
import com.rstudio.knackquiz.R;
import com.rstudio.knackquiz.datastore.DataStore;
import com.rstudio.knackquiz.gameplay.QuestionActivity;
import com.rstudio.knackquiz.gameplay.QuizFinishActivity;
import com.rstudio.knackquiz.models.Player;
import com.rstudio.knackquiz.models.QuizOption;

import java.util.ArrayList;

public class QuizOptionAdapter extends RecyclerView.Adapter<QuizOptionAdapter.MyViewHolder> {
    private ArrayList<QuizOption> quizOptions;
    private Context mContext;
    private Player player;
    private String category;
    public QuizOptionAdapter(Context context, ArrayList<QuizOption> quizOptions,String category) {
        this.mContext = context;
        this.quizOptions = quizOptions;
        this.player = DataStore.getCurrentPlayer(mContext);
        this.category = category;
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tvTitle, tvDescription, tvRewardCoins, tvEntryCoins;
        MaterialCardView cardView, cardCoins;
        MaterialButton btnPlay;
        ImageView imgRewardIcon, imgEntryIcon;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            tvEntryCoins = itemView.findViewById(R.id.tv_entryCoinQuizOption);
            tvRewardCoins = itemView.findViewById(R.id.tv_rewardCoinsQuizOption);
            tvDescription = itemView.findViewById(R.id.tv_quizOptionsDescriptionList);
            tvTitle = itemView.findViewById(R.id.tv_quizOptionsTitle);
            //cardCoins = itemView.findViewById(R.id.card_quizOptionCoins);
            cardView = itemView.findViewById(R.id.card_quizOptionItem);
            btnPlay = itemView.findViewById(R.id.btn_playQuizOptionList);
            imgRewardIcon = itemView.findViewById(R.id.img_icRewardQuizOptionlist);


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


        if (quizOption.getRewardType().equalsIgnoreCase("diamond")) { //change reward icon to diamond if diamond reward
            holder.imgRewardIcon.setBackground(mContext.getResources().getDrawable(R.drawable.ic_diamond)); //Diamond match
        }else{
            //coin match
        }

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        if (Integer.parseInt(quizOption.getEntrycoins()) > player.getCoins()) { //check if player has enough coins to enter the match
            holder.btnPlay.setText("Need " + String.valueOf(Integer.parseInt(quizOption.getEntrycoins()) - player.getCoins()) + " more coins");
            holder.btnPlay.setEnabled(false);
        }

        holder.btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, QuestionActivity.class);
                intent.putExtra("options", quizOption);
                intent.putExtra("category",category);
                reducePlayerCoins(quizOption.getEntrycoins());
                mContext.startActivity(intent);
            }
        });

    }

    private void reducePlayerCoins(String sCoins) {
        int coins = Integer.parseInt(sCoins);
        reduceCoins(coins);
    }

    public void reduceCoins(final int coins) {
        String url;
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            url = "https://knack-quiz-b57d5.firebaseio.com/users/registered/" + DataStore.getCurrentPlayerID(mContext);
        } else {
            url = "https://knack-quiz-b57d5.firebaseio.com/users/unregistered/" + DataStore.getCurrentPlayerID(mContext);
        }

        Firebase.setAndroidContext(mContext);
        Firebase database = new Firebase(url);
        database.runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData mutableData) {
                Player player = new Player();
                if (mutableData.getValue() != null) {
                    player = mutableData.getValue(Player.class);
                    player.setCoins(player.getCoins() - coins);
                }
                mutableData.setValue(player);
                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(FirebaseError firebaseError, boolean b, com.firebase.client.DataSnapshot dataSnapshot) {
                if (b) {
                    Toast.makeText(mContext, "Transaction Complete", Toast.LENGTH_SHORT).show();
                }
            }


        });
    }

    @Override
    public int getItemCount() {
        return quizOptions.size();
    }

}
