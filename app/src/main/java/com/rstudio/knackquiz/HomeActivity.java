package com.rstudio.knackquiz;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.multidex.MultiDex;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rstudio.knackquiz.adapters.ViewPagerAdapter;
import com.rstudio.knackquiz.datastore.DataStore;
import com.rstudio.knackquiz.fragments.bottomnav.FragmentContest;
import com.rstudio.knackquiz.fragments.bottomnav.FragmentFriends;
import com.rstudio.knackquiz.fragments.bottomnav.FragmentHome;
import com.rstudio.knackquiz.fragments.bottomnav.FragmentLeaderboard;
import com.rstudio.knackquiz.fragments.bottomnav.FragmentProfile;
import com.rstudio.knackquiz.helpers.CategoryHelper;
import com.rstudio.knackquiz.helpers.DBClass;
import com.rstudio.knackquiz.helpers.DBKeys;
import com.rstudio.knackquiz.helpers.KeyStore;
import com.rstudio.knackquiz.models.Player;
import com.rstudio.knackquiz.services.OnlineManagerService;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import devlight.io.library.ntb.NavigationTabBar;

public class HomeActivity extends AppCompatActivity {


    private TextView tvCoins, tvUserName, tvDiamonds;
    private Player player;
    private static final String TAG = "HomeActivity";
    private CircleImageView imgProfileToolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        MultiDex.install(this);
        CategoryHelper.init(this);
        FirebaseApp.initializeApp(this);

        initUI();
        setToolbar();

        if (!checkFirstTime()) {
            loadData();
        }

        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            startService(new Intent(getApplicationContext(), OnlineManagerService.class));
            createLeaderBoard();
        }


    }

    public void createLeaderBoard() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, DBClass.urlCreateLeaderboard, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<>();
                params.put("playerid", player.getPlayerID());
                params.put("name", player.getUserName());
                params.put("profileurl", player.getPhotoURL());
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }


    private void setPlayerData() {
        if (player.getPhotoURL() != null) {
            Picasso.get().load(player.getPhotoURL()).into(imgProfileToolbar);
        }
        tvUserName.setText(player.getUserName());
        tvCoins.setText(String.valueOf(player.getCoins()));
        tvDiamonds.setText(String.valueOf(player.getDiamonds()));
    }


    private boolean checkFirstTime() {
        SharedPreferences sharedPreferences = getSharedPreferences(DataStore.FIRSTTIME, MODE_PRIVATE);
        String status = sharedPreferences.getString(DataStore.STATUS, "");
        if (status.isEmpty()) {
            finish();
            startActivity(new Intent(this, IntroActivity.class));
            return true;
        } else {
            if (getIntent().hasExtra(KeyStore.PLAYER_SERIAL)) {
                player = (Player) getIntent().getSerializableExtra(KeyStore.PLAYER_SERIAL);
                setPlayerData();
            } else {
                showLoadingActivity();
            }
            return false;
        }
    }

    private void showLoadingActivity() {
        finish();
        startActivity(new Intent(this, LaunchLoadingActivity.class));
        overridePendingTransition(0, 0);
    }

    private void initUI() {
        setNavBar();
    }

    private void setNavBar() {
        final ViewPager viewPager = (ViewPager) findViewById(R.id.vp_horizontal_ntb);
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        viewPagerAdapter.addFrag(new FragmentHome(this), "Home");
        viewPagerAdapter.addFrag(new FragmentContest(this), "Contest");
        viewPagerAdapter.addFrag(new FragmentLeaderboard(this), "Leaderboard");
        viewPagerAdapter.addFrag(new FragmentFriends(this), "Friends");
        viewPagerAdapter.addFrag(new FragmentProfile(this), "Profile");

        viewPager.setAdapter(viewPagerAdapter);

        final String[] colors = getResources().getStringArray(R.array.default_preview);

        final NavigationTabBar navigationTabBar = (NavigationTabBar) findViewById(R.id.ntb_horizontal);
        final ArrayList<NavigationTabBar.Model> models = new ArrayList<>();
        models.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.drawable.ic_first),
                        Color.parseColor(colors[0]))
                        .selectedIcon(getResources().getDrawable(R.drawable.ic_first))
                        .title("Home")
                        .build()
        );
        models.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.drawable.ic_second),
                        Color.parseColor(colors[1]))
//                        .selectedIcon(getResources().getDrawable(R.drawable.ic_eighth))
                        .title("Contests")
                        .build()
        );
        models.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.drawable.ic_leaderboard),
                        Color.parseColor(colors[2]))
                        .selectedIcon(getResources().getDrawable(R.drawable.ic_leaderboard))
                        .title("Leaderboard")
                        .build()
        );
        models.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.drawable.ic_friends),
                        Color.parseColor(colors[3]))
                        .title("Friends")
                        .build()
        );
        models.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.drawable.ic_profile),
                        Color.parseColor(colors[4]))
                        .title("Profile")
                        .build()
        );


        navigationTabBar.setModels(models);
        navigationTabBar.setViewPager(viewPager, 2);
        navigationTabBar.setIsTitled(true);


        //navigationTabBar.setTitleMode(NavigationTabBar.TitleMode.ACTIVE);
        //navigationTabBar.setTypeface("fonts/custom_font.ttf");
        navigationTabBar.setIsBadged(false);
        navigationTabBar.setIsSwiped(true);


        navigationTabBar.setModelIndex(0);

        navigationTabBar.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(final int position, final float positionOffset, final int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(final int position) {
                navigationTabBar.getModels().get(position).hideBadge();
                if (position == 4) {
                    getSupportActionBar().hide();
                  /*  getSupportFragmentManager().beginTransaction()
                            //.addSharedElement(imgProfileToolbar, "imagetransition")
                            .replace(R.id.frame_fragHolderHome, new FragmentProfile(HomeActivity.this))
                            .commit();*/
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        postponeEnterTransition();
                    }


                } else {
                    getSupportActionBar().show();
                }
            }

            @Override
            public void onPageScrollStateChanged(final int state) {

            }
        });

        navigationTabBar.postDelayed(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < navigationTabBar.getModels().size(); i++) {
                    final NavigationTabBar.Model model = navigationTabBar.getModels().get(i);
                    navigationTabBar.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            model.showBadge();
                        }
                    }, i * 100);
                }
            }
        }, 500);
    }

    private void loadData() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference(DBKeys.KEY_USERS);


        ref.child(DataStore.getCurrentPlayerID(this)).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    player = dataSnapshot.getValue(Player.class);
                    DataStore.setCurrentPlayer(player, HomeActivity.this);
                    setPlayerData();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    private void setToolbar() {
        MaterialToolbar toolbar = findViewById(R.id.tb_activityHome);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        tvUserName = findViewById(R.id.tb_tvusernameMainCommmon);
        tvCoins = findViewById(R.id.tb_tvcoinsMainCommon);
        tvDiamonds = findViewById(R.id.tb_tvDiamondsMainCommon);

        imgProfileToolbar = findViewById(R.id.img_toolbarProfile);
    }

}
