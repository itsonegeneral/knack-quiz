package com.rstudio.knackquiz;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.multidex.MultiDex;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.rstudio.knackquiz.adapters.CategoryAdapter;
import com.rstudio.knackquiz.helpers.DBClass;
import com.rstudio.knackquiz.helpers.KeyStore;
import com.rstudio.knackquiz.models.Category;
import com.rstudio.knackquiz.models.Player;
import com.rstudio.knackquiz.modules.friends.fragments.MyFriendsFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SelectCategoryActivity extends AppCompatActivity {

    private ArrayList<Category> categories = new ArrayList<>();
    private RecyclerView rViewCats;
    private CategoryAdapter categoryAdapter;
    private TextView tvSelected;
    private Category category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_category);
        MultiDex.install(this);
        setToolbar();
        rViewCats = findViewById(R.id.rView_selectCategory);
        rViewCats.setLayoutManager(new GridLayoutManager(this, 3));
        tvSelected = findViewById(R.id.tv_selectedCatActivity);
        loadData();

        findViewById(R.id.bt_confirmSelectCategory).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (category != null) {
                    Intent intent = new Intent();
                    intent.putExtra(KeyStore.CATEGORY_SERIAL, category);
                    setResult(MyFriendsFragment.RESULT_SUCCESS, intent);
                    finish();
                } else {
                    Snackbar.make(findViewById(android.R.id.content), "Select Category", Snackbar.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void loadData() {


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
                                    categories.add(category);
                                }

                                categoryAdapter = new CategoryAdapter(SelectCategoryActivity.this, categories);
                                rViewCats.setAdapter(categoryAdapter);
                                categoryAdapter.setOnItemClickListener(new CategoryAdapter.OnItemClickListener() {
                                    @Override
                                    public void OnItemClick(Category category) {
                                        manageSelected(category);
                                    }
                                });
                            } else {
                                //Databse error

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(), "Er", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "ERRROR", Toast.LENGTH_SHORT).show();
            }
        });

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    private void manageSelected(Category category) {
        this.category = category;
        tvSelected.setText(category.getCategory() + " Selected");
    }

    private void setToolbar() {
        Toolbar toolbar = findViewById(R.id.tb_selectCategory);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        TextView tv = findViewById(R.id.tv_toolbarHeadingSimple);
        tv.setText("Select Category");
    }
}
