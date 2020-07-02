package com.rstudio.knackquiz.modules.friends.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.DateSorter;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.rstudio.knackquiz.AlertActivity;
import com.rstudio.knackquiz.LoginActivity;
import com.rstudio.knackquiz.R;
import com.rstudio.knackquiz.SelectCategoryActivity;
import com.rstudio.knackquiz.datastore.DataStore;
import com.rstudio.knackquiz.gameplay.QuestionActivity;
import com.rstudio.knackquiz.helpers.DBClass;
import com.rstudio.knackquiz.helpers.DBKeys;
import com.rstudio.knackquiz.helpers.DateHelper;
import com.rstudio.knackquiz.helpers.KeyStore;
import com.rstudio.knackquiz.models.Category;
import com.rstudio.knackquiz.models.Challenge;
import com.rstudio.knackquiz.models.Player;
import com.rstudio.knackquiz.models.Question;
import com.rstudio.knackquiz.modules.friends.adapters.MyFriendAdapter;
import com.rstudio.knackquiz.services.OnlineManagerService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MyFriendsFragment extends Fragment {

    private Context context;
    private RelativeLayout layout;
    private RecyclerView rViewOnlineFriends, rViewMyFriends;
    private TextView tvNoFriends, tvNoOnline;
    private MyFriendAdapter friendAdapter;
    private static final int CATEGORY_RESULT = 14;
    public static final int RESULT_SUCCESS = 20;
    private Player mPlayer;
    private ArrayList<Question> questions = new ArrayList<>();
    private Category category;
    private Challenge challenge;
    AlertDialog.Builder builder;
    AlertDialog alertDialog;

    private static final String TAG = "MyFriendsFragment";

    public MyFriendsFragment() {
        if (context == null) {
            context = getContext();
        }
    }

    public MyFriendsFragment(Context context) {
        this.context = context;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        layout = (RelativeLayout) inflater.inflate(R.layout.fragment_my_friends, container, false);
        return layout;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initValues();
        loadData();

    }

    ArrayList<Player> onlinePlayers = new ArrayList<>();

    private void loadData() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child(DBKeys.KEY_ONLINE_PLAYERS);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                onlinePlayers.clear();
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        Player player = snapshot.getValue(Player.class);
                        if (player.getPlayerRegisterType().equals(DBKeys.KEY_REGISTERED)) {
                            if (!player.getPlayerID().equals(DataStore.getCurrentPlayerID(getContext())))
                                onlinePlayers.add(player);
                        }

                        if (onlinePlayers.isEmpty()) {
                            tvNoOnline.setVisibility(View.VISIBLE);
                            rViewOnlineFriends.setVisibility(View.GONE);
                        } else {
                            tvNoOnline.setVisibility(View.GONE);
                            rViewOnlineFriends.setVisibility(View.VISIBLE);
                        }

                        friendAdapter = new MyFriendAdapter(context, onlinePlayers);
                        rViewOnlineFriends.setAdapter(friendAdapter);
                        setListeners();
                    }
                } else {
                    tvNoOnline.setVisibility(View.VISIBLE);
                    rViewOnlineFriends.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void setListeners() {
        if (friendAdapter != null) {
            friendAdapter.setOnItemClickListener(new MyFriendAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(Player player) {
                    startActivityForResult(new Intent(context, SelectCategoryActivity.class), CATEGORY_RESULT);
                    mPlayer = player;
                }
            });
        }
    }

    private void initValues() {
        rViewMyFriends = layout.findViewById(R.id.rView_myFriends);
        rViewOnlineFriends = layout.findViewById(R.id.rView_onlineFriends);
        rViewOnlineFriends.setLayoutManager(new LinearLayoutManager(getContext()));
        rViewMyFriends.setLayoutManager(new LinearLayoutManager(getContext()));

        tvNoFriends = layout.findViewById(R.id.tv_noFriendsAdded);
        tvNoOnline = layout.findViewById(R.id.tv_noFriendsOnline);
        tvNoOnline.setVisibility(View.GONE);
        tvNoFriends.setVisibility(View.GONE);

        builder = new AlertDialog.Builder(getContext());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CATEGORY_RESULT && resultCode == RESULT_SUCCESS && data != null) {
            createChallenge(data);
        }
    }

    private void createChallenge(Intent data) {
        category = (Category) data.getSerializableExtra(KeyStore.CATEGORY_SERIAL);
        challenge = new Challenge();

        challenge.setCategory(category.getCategory());
        challenge.setCategoryId(category.getId());
        challenge.setAccepted(false);
        challenge.setChallengerPlayer(DataStore.getCurrentPlayer(getContext()));
        challenge.setReceivedPlayer(mPlayer);
        challenge.setMessage(category.getCategory() + " challenge from " + DataStore.getCurrentPlayer(getContext()).getUserName());

        challenge.setCreatedOn(DateHelper.getCurrentFormattedDate());
        challenge.setRejected(false);

        getQuestions(category.getCategory());
    }

    private void getQuestions(final String cat) {
        showLoadingAlert();
        String url = DBClass.urlGetQuestions + "?category=" + cat + "&limit=" + 10;
        Log.d(TAG, "getQuestions: " + url);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    questions.clear();
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray array = jsonObject.getJSONArray("data");
                    Gson gson = new Gson();
                    for (int i = 0; i < array.length(); i++) {
                        Question question = gson.fromJson(array.getJSONObject(i).toString(), Question.class);
                        questions.add(question);
                    }
                    if (questions.isEmpty()) {
                        Toast.makeText(context, "No Questions Found", Toast.LENGTH_SHORT).show();
                    } else {
                        challenge.setQuestions(questions);
                        uploadData();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "onErrorResponse: " + error.getLocalizedMessage());
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("category", cat);
                params.put("limit", "10");
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.add(stringRequest);
    }

    private void uploadData() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference(DBKeys.KEY_CHALLENGES);
        String key = ref.push().getKey();
        challenge.setChallengeId(key);
        showLoadingAlert();
        ref.child(challenge.getChallengeId()).setValue(challenge).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                alertDialog.dismiss();
                if (task.isSuccessful()) {
                    Toast.makeText(context, "Challenge Sent!", Toast.LENGTH_SHORT).show();
                    ArrayList<Question> questions = challenge.getQuestions();
                    Gson gson = new Gson();
                    String StringQuest = gson.toJson(questions);
                    Intent intent = new Intent(context, QuestionActivity.class);
                    intent.putExtra(KeyStore.QUESTION_SERIAL, StringQuest);
                    intent.putExtra(KeyStore.CHALLENGE_SERIAL, challenge);
                    startActivity(intent);
                }
            }
        });
    }

    private void showLoadingAlert() {
        View customView = LayoutInflater.from(context).inflate(R.layout.alert_loading_view, null);
        TextView tvLoadingText = customView.findViewById(R.id.tv_loadingTextLoadingAlert);
        tvLoadingText.setText("Sending challenge...");
        LottieAnimationView lottieView = customView.findViewById(R.id.lottieViewLoadingAlert);
        lottieView.setAnimation(R.raw.loading_bouncing);
        lottieView.loop(true);
        lottieView.playAnimation();
        builder.setView(customView);
        alertDialog = builder.create();
        alertDialog.show();
    }

}
