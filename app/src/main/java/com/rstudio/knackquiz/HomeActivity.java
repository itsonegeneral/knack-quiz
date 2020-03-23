package com.rstudio.knackquiz;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityOptionsCompat;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.multidex.MultiDex;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.DataSetObserver;
import android.graphics.Color;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;
import android.service.autofill.Dataset;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.airbnb.lottie.animation.LPaint;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rstudio.knackquiz.adapters.CategoryAdapter;
import com.rstudio.knackquiz.adapters.ViewPagerAdapter;
import com.rstudio.knackquiz.datastore.DataStore;
import com.rstudio.knackquiz.fragments.bottomnav.FragmentContest;
import com.rstudio.knackquiz.fragments.bottomnav.FragmentFriends;
import com.rstudio.knackquiz.fragments.bottomnav.FragmentHome;
import com.rstudio.knackquiz.fragments.bottomnav.FragmentLeaderboard;
import com.rstudio.knackquiz.fragments.bottomnav.FragmentProfile;
import com.rstudio.knackquiz.helpers.CategoryHelper;
import com.rstudio.knackquiz.helpers.DBKeys;
import com.rstudio.knackquiz.helpers.KeyStore;
import com.rstudio.knackquiz.models.Category;
import com.rstudio.knackquiz.models.Player;

import java.io.DataInputStream;
import java.util.ArrayList;

import devlight.io.library.ntb.NavigationTabBar;

public class HomeActivity extends AppCompatActivity {


    private TextView tvCoins, tvUserName;
    private Player player;
    private static final String TAG = "HomeActivity";
    private ImageView imgProfileToolbar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        MultiDex.install(this);
        CategoryHelper.init(this);
        checkFirstTime();


        initUI();
        setToolbar();
        loadData();

    }


    private void checkFirstTime() {
        SharedPreferences sharedPreferences = getSharedPreferences(DataStore.FIRSTTIME, MODE_PRIVATE);
        String status = sharedPreferences.getString(DataStore.STATUS, "");
        if (status.isEmpty()) {
            finish();
            startActivity(new Intent(this, IntroActivity.class));
        }
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
        DatabaseReference ref;
        Player tplr = DataStore.getCurrentPlayer(this);
        if (!tplr.getPlayerRegisterType().equalsIgnoreCase(KeyStore.UNREGISTERED_USER)) {
            ref = FirebaseDatabase.getInstance().getReference(DBKeys.KEY_USERS).child(DBKeys.KEY_REGISTERED);
        } else {
            ref = FirebaseDatabase.getInstance().getReference(DBKeys.KEY_USERS).child(DBKeys.KEY_UNREGISTERED);
        }

        ref.child(DataStore.getCurrentPlayerID(this)).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    player = dataSnapshot.getValue(Player.class);

                    tvCoins.setText(String.valueOf(player.getCoins()));
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
        tvCoins = findViewById(R.id.tb_tvcoinsMainCommon);
        imgProfileToolbar = findViewById(R.id.img_toolbarProfile);
    }

}
