package com.rstudio.knackquiz.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textview.MaterialTextView;
import com.rstudio.knackquiz.R;
import com.rstudio.knackquiz.helpers.CategoryHelper;
import com.rstudio.knackquiz.models.Category;
import com.thekhaeng.pushdownanim.PushDownAnim;

import java.util.ArrayList;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.MyViewHolder> {
    private Context context;
    private ArrayList<Category> categories;

    public CategoryAdapter(Context context) {
        this.context = context;
        categories = CategoryHelper.getCategoryArrayList(context);
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
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_category_horizontal_view,
                parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final  MyViewHolder holder, int position) {
        final Category category = categories.get(position);

        holder.tvName.setText(category.getName());
        holder.imgIcon.setImageDrawable(category.getIcDrawable());


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
                    holder.imgTick.setVisibility(View.VISIBLE);
                }else{
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
