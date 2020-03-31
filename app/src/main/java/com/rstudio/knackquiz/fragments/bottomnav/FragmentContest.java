package com.rstudio.knackquiz.fragments.bottomnav;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.tabs.TabLayout;
import com.rstudio.knackquiz.R;
import com.rstudio.knackquiz.adapters.ViewPagerAdapter;
import com.rstudio.knackquiz.gameplay.QuestionActivity;
import com.rstudio.knackquiz.gameplay.contests.AllContestsFragment;
import com.rstudio.knackquiz.gameplay.contests.MyContestFragment;

public class FragmentContest extends Fragment {

    private Context context;
    private LinearLayout layout;
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
        layout = (LinearLayout) inflater.inflate(R.layout.fragment_contest, container, false);

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getFragmentManager());
        TabLayout tabLayout = layout.findViewById(R.id.tabLayout_contestFrag);
        ViewPager viewPager = layout.findViewById(R.id.viewPager_contestFrag);

        viewPagerAdapter.addFrag(new AllContestsFragment(context), "Contests");
        viewPagerAdapter.addFrag(new MyContestFragment(context), "My Contests");
        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);

        return layout;
    }
}
