package com.rstudio.knackquiz.modules.friends.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.client.Firebase;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rstudio.knackquiz.R;
import com.rstudio.knackquiz.helpers.DBKeys;
import com.rstudio.knackquiz.models.Challenge;
import com.rstudio.knackquiz.models.Player;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MyFriendAdapter extends RecyclerView.Adapter<MyFriendAdapter.MyViewHolder> {

    private Context context;
    private ArrayList<Player> players;
    private OnItemClickListener mListener;

    public MyFriendAdapter(Context context, ArrayList<Player> players) {
        this.context = context;
        this.players = players;
    }

    public interface OnItemClickListener {
        public void onItemClick(Player player);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mListener = onItemClickListener;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tvName;
        Button btChallenge;
        ImageView imgProfile;
        FrameLayout flOnline;

        public MyViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_nameFriendList);
            btChallenge = itemView.findViewById(R.id.bt_challengeFriend);
            imgProfile = itemView.findViewById(R.id.img_friendList);
            flOnline = itemView.findViewById(R.id.fl_onlineIndicatorFriendLisr);
            btChallenge.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int pos = getAdapterPosition();
                        if (pos != RecyclerView.NO_POSITION) {
                            listener.onItemClick(players.get(pos));
                        }
                    }
                }
            });
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_view_friend, parent, false);
        return new MyViewHolder(view, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final Player player = players.get(position);
        holder.tvName.setText(player.getUserName());
        Picasso.get().load(player.getPhotoURL()).into(holder.imgProfile);

        if (player.isActive()) {
            holder.flOnline.setVisibility(View.VISIBLE);
        } else {
            holder.flOnline.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        return players.size();
    }


}
