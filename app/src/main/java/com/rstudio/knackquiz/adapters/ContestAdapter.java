package com.rstudio.knackquiz.adapters;

import android.content.Context;
import android.content.Intent;
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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rstudio.knackquiz.R;
import com.rstudio.knackquiz.datastore.DataStore;
import com.rstudio.knackquiz.gameplay.QuestionActivity;
import com.rstudio.knackquiz.helpers.DBClass;
import com.rstudio.knackquiz.helpers.DBKeys;
import com.rstudio.knackquiz.helpers.DateHelper;
import com.rstudio.knackquiz.helpers.KeyStore;
import com.rstudio.knackquiz.models.Challenge;
import com.rstudio.knackquiz.models.Contest;

import java.util.ArrayList;
import java.util.Date;

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
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        final Contest contest = contestsList.get(position);

        holder.tvDate.setText(DateHelper.getStartDate(contest.getStartTime()));
        holder.tvTime.setText(DateHelper.getStartTime(contest.getStartTime()));
        holder.tvCategory.setText(contest.getCategory());
        holder.tvPrize.setText(String.valueOf(contest.getRewardValue()));
        holder.tvEntry.setText(String.valueOf(contest.getEntryValue()));
        holder.tvQuestionTime.setText(String.valueOf(contest.getQuestionTime()));
        //holder.tvDuration.setText(contest.);
        holder.tvJoinedPlayers.setText(contest.getJoinedCount() + " Players joined");
        holder.tvTotalPlayers.setText(contest.getTotalPlayers() + " Players");
        holder.pgBarPlayers.setProgress(contest.getJoinedCount());
        holder.pgBarPlayers.setMax(contest.getTotalPlayers());
        if (contest.getPlayers().contains(DataStore.getCurrentPlayerID(context))) {
            holder.btJoin.setEnabled(false);
            holder.btJoin.setText("Joined");
            Date date = new Date(contest.getStartTime());
            if (new Date().after(date)) {
                holder.btJoin.setText("Play");
                holder.btJoin.setEnabled(true);
                holder.btJoin.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, QuestionActivity.class);
                        intent.putExtra(DBKeys.KEY_CONTESTS,"CONTEST");
                        intent.putExtra(KeyStore.CONTEST_SERIAL,contest);

                        context.startActivity(intent);
                    }
                });
            }
        } else {
            holder.btJoin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    joinContest(position);
                }
            });
        }


    }

    private void joinContest(int position) {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("contests");
        contestsList.get(position).addPlayer(DataStore.getCurrentPlayerID(context));
        ref.child(contestsList.get(position).getId()).setValue(contestsList.get(position));
    }

    @Override
    public int getItemCount() {
        return contestsList.size();
    }

}
