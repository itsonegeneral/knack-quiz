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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.button.MaterialButton;
import com.google.gson.Gson;
import com.rstudio.knackquiz.HomeActivity;
import com.rstudio.knackquiz.IntroFavouriteActivity;
import com.rstudio.knackquiz.R;
import com.rstudio.knackquiz.adapters.CategoryAdapter;
import com.rstudio.knackquiz.adapters.ParentCategoryAdapter;
import com.rstudio.knackquiz.gameplay.QuestionActivity;
import com.rstudio.knackquiz.helpers.CategoryHelper;
import com.rstudio.knackquiz.helpers.DBClass;
import com.rstudio.knackquiz.models.Category;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Objects;

public class FragmentHome extends Fragment {

    private Context context;
    private LinearLayout layout;
    private ArrayList<Category> favCats = new ArrayList<>(), trendingCats;
    private HomeActivity homeActivity;
    private static final String TAG = "FragmentHome";
    private RecyclerView rViewFavourites, rViewTrending;
    private CategoryAdapter categoryAdapter;

    public FragmentHome() {
        context = getActivity();
        homeActivity = (HomeActivity) context;
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

        getTrendingCats();


        return layout;
    }

    private void getFavourites() {
        favCats = CategoryHelper.getFavCategories();
        for (Category fav : favCats) {
            Log.d(TAG, "getFavourites: " + fav.getCategory());
        }

    }

    private void getTrendingCats() {


        StringRequest stringRequest = new StringRequest(Request.Method.GET, DBClass.urlGetCategories,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String result = jsonObject.getString("status");
                            Gson gson = new Gson();
                            if (result.equalsIgnoreCase("success")) {
                                JSONArray jsonCats = jsonObject.getJSONArray("categories");

                                for (int i = 0; i < jsonCats.length(); i++) {
                                    Category category = gson.fromJson(jsonCats.getJSONObject(i).toString(), Category.class);
                                    trendingCats.add(category);
                                }

                                rViewTrending.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
                                categoryAdapter = new CategoryAdapter(context, trendingCats);
                                rViewTrending.setAdapter(categoryAdapter);

                            } else {
                                //Databse error

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(context, "Er", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context, "ERRROR", Toast.LENGTH_SHORT).show();
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(getContext().getApplicationContext());
        requestQueue.add(stringRequest);
    }


    private void initUI() {
        rViewFavourites = layout.findViewById(R.id.rView_favouritesHome);
        rViewTrending = layout.findViewById(R.id.rView_trendingCatsHome);
        trendingCats = new ArrayList<>();
        categoryAdapter = new CategoryAdapter(homeActivity, favCats);
        rViewFavourites.setLayoutManager(new LinearLayoutManager(getContext().getApplicationContext(), LinearLayoutManager.HORIZONTAL, false));
        rViewFavourites.setAdapter(categoryAdapter);
    }


}
