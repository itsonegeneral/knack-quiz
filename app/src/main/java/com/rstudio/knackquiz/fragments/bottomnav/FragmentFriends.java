package com.rstudio.knackquiz.fragments.bottomnav;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rstudio.knackquiz.HomeActivity;
import com.rstudio.knackquiz.LoginActivity;
import com.rstudio.knackquiz.R;
import com.rstudio.knackquiz.adapters.ViewPagerAdapter;
import com.rstudio.knackquiz.gameplay.QuestionActivity;
import com.rstudio.knackquiz.gameplay.contests.AllContestsFragment;
import com.rstudio.knackquiz.gameplay.contests.MyContestFragment;
import com.rstudio.knackquiz.models.Player;
import com.rstudio.knackquiz.modules.friends.adapters.AddFriendAdapter;
import com.rstudio.knackquiz.modules.friends.fragments.FriendChallengesFragment;
import com.rstudio.knackquiz.modules.friends.fragments.MyFriendsFragment;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FragmentFriends extends Fragment {

    private Context context;
    private RelativeLayout layout;
    private static final String TAG = "FragmentFriends";


    public FragmentFriends() {
        context = getContext();
    }

    public FragmentFriends(Context context) {
        this.context = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        layout = (RelativeLayout) inflater.inflate(R.layout.fragment_friends, container, false);
        return layout;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            setTabLayout();
            layout.findViewById(R.id.ll_loggedInFriendsFragment).setVisibility(View.VISIBLE);
            layout.findViewById(R.id.ll_friendsSignInLayout).setVisibility(View.GONE);
        } else {
            layout.findViewById(R.id.ll_loggedInFriendsFragment).setVisibility(View.GONE);
            layout.findViewById(R.id.ll_friendsSignInLayout).setVisibility(View.VISIBLE);
            layout.findViewById(R.id.bt_signinFragFriends).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(context, LoginActivity.class));
                }
            });
        }
    }

    private void setTabLayout() {
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getChildFragmentManager());
        TabLayout tabLayout = layout.findViewById(R.id.tabLayout_friendsFrag);
        ViewPager viewPager = layout.findViewById(R.id.viewPager_friendsFrag);

        viewPagerAdapter.addFrag(new MyFriendsFragment(context), "My Friends");
        viewPagerAdapter.addFrag(new FriendChallengesFragment(context), "Challenges");
        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
    }


}
