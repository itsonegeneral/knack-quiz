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
import com.rstudio.knackquiz.models.Contest;
import com.rstudio.knackquiz.models.LeaderBoard;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class LeaderBoardAdapter extends RecyclerView.Adapter<LeaderBoardAdapter.MyViewHolder> {

    private Context context;
    private ArrayList<LeaderBoard> leaderBoards;

    public LeaderBoardAdapter(Context context, ArrayList<LeaderBoard> leaderBoards) {
        this.context = context;
        this.leaderBoards = leaderBoards;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_view_leaderboard,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.tvPosition.setText(String.valueOf(position+1));
        Picasso.get().load(leaderBoards.get(position).getProfileURL()).into(holder.imgProfile);
        holder.tvName.setText(leaderBoards.get(position).getPlayerName());
        holder.tvSore.setText(leaderBoards.get(position).getTotalScore());
    }

    @Override
    public int getItemCount() {
        return leaderBoards.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        CircleImageView imgProfile;
        TextView tvName,tvPosition,tvSore;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imgProfile = itemView.findViewById(R.id.img_leaderboardList);
            tvName = itemView.findViewById(R.id.tv_nameLeaderboardList);
            tvPosition = itemView.findViewById(R.id.tv_positionLeaderboardList);
            tvSore= itemView.findViewById(R.id.tv_scoreLeaderboardList);
        }
    }



}
