package com.rstudio.knackquiz.adapters;

import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rstudio.knackquiz.R;
import com.rstudio.knackquiz.models.ContestPlayer;

import java.util.ArrayList;

public class ContestScoreAdapter extends RecyclerView.Adapter<ContestScoreAdapter.MyViewHolder> {
    private Context context;
    private ArrayList<ContestPlayer> contestPlayers;

    public ContestScoreAdapter(Context context, ArrayList<ContestPlayer> contestPlayers) {
        this.context = context;
        this.contestPlayers = contestPlayers;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_contest_score, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.tvName.setText(contestPlayers.get(position).getPlayerName());
        holder.tvPosition.setText(String.valueOf(position + 1));
        holder.tvScore.setText(String.valueOf(contestPlayers.get(position).getScore()));
    }

    @Override
    public int getItemCount() {
        return contestPlayers.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tvPosition, tvName, tvScore;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            tvName = itemView.findViewById(R.id.tv_nameContestScoreList);
            tvPosition = itemView.findViewById(R.id.tv_positionContestScoreList);
            tvScore = itemView.findViewById(R.id.tv_scoreContestScoreList);

        }
    }

}
