package com.rstudio.knackquiz.adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.rstudio.knackquiz.IntroFavouriteActivity;
import com.rstudio.knackquiz.R;
import com.rstudio.knackquiz.helpers.CategoryHelper;
import com.rstudio.knackquiz.models.Category;

import java.util.ArrayList;
import java.util.List;

public class ParentCategoryAdapter extends RecyclerView.Adapter<ParentCategoryAdapter.MyViewHolder> {

    private String[] colors;
    private ArrayList<Category> categories;
    private Context context;
    private ListCategoryAdapter categoryAdapter;
    private boolean isExpanded;

    public ParentCategoryAdapter(String[] colors, ArrayList<Category> parentCategories, Context context) {
        this.colors = colors;
        this.categories = parentCategories;
        this.context = context;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        MaterialCardView cardView;
        RecyclerView recyclerView;
        RelativeLayout relativeLayout;
        TextView tvCategory;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            tvCategory = itemView.findViewById(R.id.tv_parentCatNameList);
            cardView = itemView.findViewById(R.id.card_parentCatList);
            recyclerView = itemView.findViewById(R.id.rView_subParent);
            relativeLayout = itemView.findViewById(R.id.rl_parentList);
            recyclerView.setLayoutManager(new LinearLayoutManager(context));

            Typeface custom_font = Typeface.createFromAsset(context.getAssets(), "fonts/muli_regular.ttf");

            tvCategory.setTypeface(custom_font);
        }
    }

    @NonNull
    @Override
    public ParentCategoryAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_view_parent_category, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ParentCategoryAdapter.MyViewHolder holder, int position) {
        final Category category = categories.get(position);
        holder.tvCategory.setText(category.getCategory());
        holder.relativeLayout.setBackgroundColor(Color.parseColor(colors[position % 5]));

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                categoryAdapter = new ListCategoryAdapter(context, CategoryHelper.getSubCategories(category.getCategory()));

                holder.recyclerView.setAdapter(categoryAdapter);
                if (isExpanded) {
                    isExpanded = false;
                    holder.recyclerView.setVisibility(View.GONE);
                } else {
                    isExpanded = true;
                    holder.recyclerView.setVisibility(View.VISIBLE);
                }

            }
        });


    }

    @Override
    public int getItemCount() {
        return categories.size();
    }


}
