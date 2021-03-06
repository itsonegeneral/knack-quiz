package com.rstudio.knackquiz.adapters;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.github.twocoffeesoneteam.glidetovectoryou.GlideToVectorYou;
import com.google.android.material.textview.MaterialTextView;
import com.rstudio.knackquiz.R;
import com.rstudio.knackquiz.helpers.CategoryHelper;
import com.rstudio.knackquiz.models.Category;
import com.thekhaeng.pushdownanim.PushDownAnim;

import java.util.ArrayList;

public class ListCategoryAdapter extends RecyclerView.Adapter<ListCategoryAdapter.MyViewHolder> {
    private Context context;
    private ArrayList<Category> categories;
    private static final String TAG = "CategoryAdapter";

    public ListCategoryAdapter(Context context, ArrayList<Category> categories) {
        this.context = context;
        this.categories = categories;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        MaterialTextView tvName;
        RelativeLayout relativeLayout;
        ImageView imgIcon, imgTick;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            tvName = itemView.findViewById(R.id.tv_categoryNameList);
            imgIcon = itemView.findViewById(R.id.img_iconCategoryList);
            imgTick = itemView.findViewById(R.id.img_tickCategoryList);
            relativeLayout = itemView.findViewById(R.id.rl_categoryListHolder);
        }
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_category_list_view,
                parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final  MyViewHolder holder, int position) {
        final Category category = categories.get(position);


        holder.tvName.setText(category.getCategory());
        Log.d(TAG, "onBindViewHolder: " + category.getCategory() + category.getIconLink());

        GlideToVectorYou.justLoadImage((Activity) context,Uri.parse(category.getIconLink()),holder.imgIcon);


        PushDownAnim.setPushDownAnimTo(holder.relativeLayout);
        if (category.isSelected()) {
            holder.imgTick.setVisibility(View.VISIBLE);
        }else{
            holder.imgTick.setVisibility(View.GONE);
        }
        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                category.setSelected(!category.isSelected());
                if (category.isSelected()) {
                    CategoryHelper.addFavouriteCategory(category);
                    holder.imgTick.setVisibility(View.VISIBLE);
                }else{
                    CategoryHelper.removeFavouriteCategory(category);
                    holder.imgTick.setVisibility(View.GONE);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }


}
