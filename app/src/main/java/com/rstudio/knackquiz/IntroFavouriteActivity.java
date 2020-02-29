package com.rstudio.knackquiz;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.gson.Gson;
import com.rstudio.knackquiz.adapters.CategoryAdapter;
import com.rstudio.knackquiz.adapters.ParentCategoryAdapter;
import com.rstudio.knackquiz.helpers.CategoryHelper;
import com.rstudio.knackquiz.helpers.DBClass;
import com.rstudio.knackquiz.models.Category;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class IntroFavouriteActivity extends AppCompatActivity {

    private TextView tvSKip;
    private ImageButton btnNext;
    private ParentCategoryAdapter categoryAdapter;
    private static final String TAG = "IntroFavouriteActivity";
    private ArrayList<Category> categories, parentCategories,favCats = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro_favourite);
        initValues();
        CategoryHelper.init(this);
        final String[] colors = getResources().getStringArray(R.array.default_preview);

        tvSKip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                startActivity(new Intent(getApplicationContext(), HomeActivity.class));
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*if(checkSelections()){
                }*/

                CategoryHelper.saveFavourites();

                finish();
                startActivity(new Intent(getApplicationContext(), HomeActivity.class));
            }
        });


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
                                JSONArray jsonParents = jsonObject.getJSONArray("parentCategories");


                                for (int i = 0; i < jsonCats.length(); i++) {
                                    Category category = gson.fromJson(jsonCats.getJSONObject(i).toString(), Category.class);
                                    categories.add(category);
                                }
                                for (int i = 0; i < jsonParents.length(); i++) {
                                    Category category = gson.fromJson(jsonParents.getJSONObject(i).toString(), Category.class);
                                    parentCategories.add(category);
                                }


                                CategoryHelper.initCategories(IntroFavouriteActivity.this,categories,parentCategories);


                                categoryAdapter = new ParentCategoryAdapter(colors,parentCategories,IntroFavouriteActivity.this);
                                RecyclerView recyclerView = findViewById(R.id.rView_introCategories);
                                recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                                recyclerView.setAdapter(categoryAdapter);





                            } else {
                                //Databse error
                                Toast.makeText(IntroFavouriteActivity.this, "error", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(IntroFavouriteActivity.this, "Er", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(IntroFavouriteActivity.this, "ERRROR", Toast.LENGTH_SHORT).show();
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }



    private void initValues() {
        btnNext = findViewById(R.id.imgBtn_submitFavourites);
        tvSKip = findViewById(R.id.tv_skipFavourites);
        categories = new ArrayList<>();
        parentCategories = new ArrayList<>();
    }
}
