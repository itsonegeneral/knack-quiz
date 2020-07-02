package com.rstudio.knackquiz.fragments.bottomnav;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.rstudio.knackquiz.LoginActivity;
import com.rstudio.knackquiz.R;
import com.rstudio.knackquiz.adapters.ViewPagerAdapter;
import com.rstudio.knackquiz.gameplay.QuestionActivity;
import com.rstudio.knackquiz.gameplay.contests.AllContestsFragment;
import com.rstudio.knackquiz.gameplay.contests.MyContestFragment;

public class FragmentContest extends Fragment {

    private static final String TAG = "FragmentContest";
    private Context context;
    private RelativeLayout layout;
    private MaterialButton btnPlay; //TODO remove this

    public FragmentContest() {
        if (context == null)
            context = getContext();
    }

    public FragmentContest(Context context) {
        this.context = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        layout = (RelativeLayout) inflater.inflate(R.layout.fragment_contest, container, false);

        Log.d(TAG, "onCreateView: ");

        return layout;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            setTabLayout();
            layout.findViewById(R.id.ll_loggedInContestsFragment).setVisibility(View.VISIBLE);
            layout.findViewById(R.id.ll_contestsSignInLayout).setVisibility(View.GONE);
        } else {
            layout.findViewById(R.id.ll_loggedInContestsFragment).setVisibility(View.GONE);
            layout.findViewById(R.id.ll_contestsSignInLayout).setVisibility(View.VISIBLE);
            layout.findViewById(R.id.bt_signinFragContest).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(context, LoginActivity.class));
                }
            });
        }
    }

    private void setTabLayout() {
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getChildFragmentManager());
        TabLayout tabLayout = layout.findViewById(R.id.tabLayout_contestFrag);
        ViewPager viewPager = layout.findViewById(R.id.viewPager_contestFrag);

        viewPagerAdapter.addFrag(new AllContestsFragment(context), "Contests");
        viewPagerAdapter.addFrag(new MyContestFragment(context), "My Contests");
        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
    }
}
