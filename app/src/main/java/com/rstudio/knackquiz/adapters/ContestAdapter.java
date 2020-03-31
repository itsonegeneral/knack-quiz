package com.rstudio.knackquiz.adapters;

import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rstudio.knackquiz.R;
import com.rstudio.knackquiz.models.Contest;

import java.util.ArrayList;

public class ContestAdapter extends RecyclerView.Adapter<ContestAdapter.MyViewHolder> {

    private Context context;
    private ArrayList<Contest> contestsList;

    public ContestAdapter(Context context, ArrayList<Contest> contestsList) {
        this.context = context;
        this.contestsList = contestsList;
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tvCategory, tvDate, tvTime, tvPrize, tvEntry;
        TextView tvDuration, tvJoinedPlayers, tvTotalPlayers, tvQuestionTime;
        ProgressBar pgBarPlayers;
        Button btJoin;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCategory = itemView.findViewById(R.id.tv_categoryContestList);
            tvDate = itemView.findViewById(R.id.tv_dateContestList);
            tvTime = itemView.findViewById(R.id.tv_timeContestList);
            tvPrize = itemView.findViewById(R.id.tv_prizeContestList);
            tvEntry = itemView.findViewById(R.id.tv_entryContestList);
            tvQuestionTime = itemView.findViewById(R.id.tv_questionTimeContestList);
            tvDuration = itemView.findViewById(R.id.tv_durationContestList);
            tvJoinedPlayers = itemView.findViewById(R.id.tv_joinedPlayersContestList);
            tvTotalPlayers = itemView.findViewById(R.id.tv_totalPlayersContestList);
            pgBarPlayers = itemView.findViewById(R.id.pgBar_playersContestList);
            btJoin = itemView.findViewById(R.id.bt_joinContestList);
        }
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_view_contest, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Contest contest = contestsList.get(position);

        holder.tvDate.setText(contest.getStartDate());
        holder.tvTime.setText(contest.getStartTime());
        holder.tvCategory.setText(contest.getCategory());
        holder.tvPrize.setText(contest.getRewardValue());
        holder.tvEntry.setText(contest.getEntryValue());
        holder.tvQuestionTime.setText(contest.getQuestionTime());
        //holder.tvDuration.setText(contest.);
        holder.tvJoinedPlayers.setText(String.valueOf(contest.getJoinedCount()));
        holder.tvTotalPlayers.setText(String.valueOf(contest.getTotalPlayers()));

        holder.btJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "Clicked", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return contestsList.size();
    }

}
