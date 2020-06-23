package com.rstudio.knackquiz.modules.friends.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.auth.FirebaseAuth;
import com.rstudio.knackquiz.R;
import com.rstudio.knackquiz.datastore.DataStore;
import com.rstudio.knackquiz.helpers.DBClass;
import com.rstudio.knackquiz.models.Player;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ChallengeFriendAdapter extends RecyclerView.Adapter<ChallengeFriendAdapter.ViewHolder> {

    private Context context;
    private ArrayList<Player> players;
    private static final String TAG = "AddFriendAdapter";

    public ChallengeFriendAdapter(Context context, ArrayList<Player> players) {
        this.context = context;
        this.players = players;
    }


    class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvName;
        Button btAddFriend;
        ImageView imgProfile;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_nameAddFriendList);
            btAddFriend = itemView.findViewById(R.id.bt_sendFriendRequest);
            imgProfile = itemView.findViewById(R.id.img_addfriendList);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_add_friend, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.tvName.setText(players.get(position).getUserName());
        Picasso.get().load(players.get(position).getPhotoURL()).into(holder.imgProfile);
        holder.btAddFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendFriendRequest(players.get(position).getPlayerID());
            }
        });
    }

    @Override
    public int getItemCount() {
        return players.size();
    }

    private void sendFriendRequest(final String playerID) {
        final String name = DataStore.getCurrentPlayer(context).getUserName();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, DBClass.urlSendFriendRequest, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "onErrorResponse: " + error);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("senderid", FirebaseAuth.getInstance().getCurrentUser().getUid());
                params.put("sendername", name);
                params.put("receiverid", playerID);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }

}
