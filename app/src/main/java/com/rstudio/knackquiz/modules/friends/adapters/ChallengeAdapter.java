package com.rstudio.knackquiz.modules.friends.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.rstudio.knackquiz.R;
import com.rstudio.knackquiz.datastore.DataStore;
import com.rstudio.knackquiz.models.Challenge;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChallengeAdapter extends RecyclerView.Adapter<ChallengeAdapter.MyViewHolder>{

    private ArrayList<Challenge> challenges;
    private Context context;

    public ChallengeAdapter(ArrayList<Challenge> challenges, Context context) {
        this.challenges = challenges;
        this.context = context;
    }
    public class MyViewHolder extends RecyclerView.ViewHolder {

        CircleImageView myImage, oppImage;
        TextView tvMyScore, tvOppScore;
        TextView tvOppName;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            myImage = itemView.findViewById(R.id.img_myImageChallengeList);
            oppImage = itemView.findViewById(R.id.img_oppImageChallengeList);
            tvOppName = itemView.findViewById(R.id.tv_oppNameChallengeList);
            tvMyScore = itemView.findViewById(R.id.tv_myScoreChallengeList);
            tvOppScore = itemView.findViewById(R.id.tv_oppScoreChallengeList);

        }
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_challenge_history,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Challenge challenge = challenges.get(position);
        if(DataStore.getCurrentPlayerID(context).equals(challenge.getChallengerPlayer().getPlayerID())){
            holder.tvMyScore.setText(String.valueOf(challenge.getChallengerScore()));
            holder.tvOppScore.setText(String.valueOf(challenge.getReceiverScore()));
            holder.tvOppName.setText(challenge.getReceivedPlayer().getUserName());
            Picasso.get().load(challenge.getChallengerPlayer().getPhotoURL()).into(holder.myImage);
            Picasso.get().load(challenge.getReceivedPlayer().getPhotoURL()).into(holder.oppImage);
        }else{
            holder.tvMyScore.setText(String.valueOf(challenge.getReceiverScore()));
            holder.tvOppScore.setText(String.valueOf(challenge.getChallengerScore()));
            holder.tvOppName.setText(challenge.getChallengerPlayer().getUserName());
            Picasso.get().load(challenge.getChallengerPlayer().getPhotoURL()).into(holder.oppImage);
            Picasso.get().load(challenge.getReceivedPlayer().getPhotoURL()).into(holder.myImage);
        }
    }

    @Override
    public int getItemCount() {
        return challenges.size();
    }


}
