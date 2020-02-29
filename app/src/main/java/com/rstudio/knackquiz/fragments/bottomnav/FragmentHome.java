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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.rstudio.knackquiz.HomeActivity;
import com.rstudio.knackquiz.R;
import com.rstudio.knackquiz.adapters.CategoryAdapter;
import com.rstudio.knackquiz.gameplay.QuestionActivity;
import com.rstudio.knackquiz.helpers.CategoryHelper;
import com.rstudio.knackquiz.models.Category;

import java.util.ArrayList;

public class FragmentHome extends Fragment {

    private Context context;
    private LinearLayout layout;
    private ArrayList<Category> favCats = new ArrayList<>();
    private MaterialButton btnPlay; //TODO remove this
    private HomeActivity homeActivity;
    private static final String TAG = "FragmentHome";
    private RecyclerView rViewFavourites;
    private CategoryAdapter categoryAdapter;

    public FragmentHome(){
        context = getContext();
        homeActivity = (HomeActivity)context;
    }
    public FragmentHome(Context context) {
        this.context = context;
        homeActivity = (HomeActivity) context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        layout = (LinearLayout) inflater.inflate(R.layout.fragment_home, container, false);
        getFavourites();
        initUI();

        btnPlay = layout.findViewById(R.id.imgBtn_tempPlay);
        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(context, QuestionActivity.class));

            }
        });

        return layout;
    }

    private void getFavourites() {
        favCats = CategoryHelper.getFavCategories();
        for (Category fav : favCats) {
            Log.d(TAG, "getFavourites: " + fav.getCategory());
        }

    }


    private void initUI(){
        rViewFavourites =layout.findViewById(R.id.rView_favouritesHome);
        categoryAdapter =new CategoryAdapter(context,favCats);
        rViewFavourites.setLayoutManager(new LinearLayoutManager(getContext().getApplicationContext(),LinearLayoutManager.HORIZONTAL,false));
        rViewFavourites.setAdapter(categoryAdapter);
    }


}
