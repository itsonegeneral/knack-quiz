package com.rstudio.knackquiz.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityOptionsCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.github.twocoffeesoneteam.glidetovectoryou.GlideToVectorYou;
import com.google.android.material.textview.MaterialTextView;
import com.google.gson.internal.$Gson$Preconditions;
import com.rstudio.knackquiz.HomeActivity;
import com.rstudio.knackquiz.IntroFavouriteActivity;
import com.rstudio.knackquiz.R;
import com.rstudio.knackquiz.SelectCategoryActivity;
import com.rstudio.knackquiz.gameplay.QuestionActivity;
import com.rstudio.knackquiz.gameplay.QuizOptionsActivity;
import com.rstudio.knackquiz.helpers.CategoryHelper;
import com.rstudio.knackquiz.models.Category;
import com.rstudio.knackquiz.models.Question;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;
import com.thekhaeng.pushdownanim.PushDownAnim;

import java.util.ArrayList;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.MyViewHolder> {
    private Context context;
    private ArrayList<Category> categories;
    private static final String TAG = "CategoryAdapter";
    private OnItemClickListener onItemClickListener;

    public CategoryAdapter(Context context, ArrayList<Category> categories) {
        this.context = context;
        this.categories = categories;
    }

    public interface OnItemClickListener {
        public void OnItemClick(Category category);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        onItemClickListener = listener;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        TextView tvName;
        RelativeLayout relativeLayout;
        ImageView imgIcon, imgTick;

        public MyViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);

            tvName = itemView.findViewById(R.id.tv_categoryNameList);
            imgIcon = itemView.findViewById(R.id.img_iconCategoryList);
            imgTick = itemView.findViewById(R.id.img_tickCategoryList);
            relativeLayout = itemView.findViewById(R.id.rl_categoryListHolder);

            relativeLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        Category category = categories.get(getAdapterPosition());
                        if(position != RecyclerView.NO_POSITION){
                            listener.OnItemClick(category);
                        }
                    }
                }
            });

        }
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (context == null) {
            context = parent.getContext();
        }
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_category_horizontal_view,
                parent, false);
        return new MyViewHolder(view,onItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {
        final Category category = categories.get(position);


        holder.tvName.setText(category.getCategory());


        try {
            GlideToVectorYou.justLoadImage((Activity) context, Uri.parse(category.getIconLink()), holder.imgIcon);
        }catch (ClassCastException e){
        }
        PushDownAnim.setPushDownAnimTo(holder.relativeLayout).setScale(.93f);
        if (context instanceof HomeActivity) {
            holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Bitmap bitmap = getBitmapFromView(holder.imgIcon);

                    HomeActivity activity = (HomeActivity) context;
                    ActivityOptionsCompat activityOptionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(activity, holder.imgIcon, context.getString(R.string.toolbar_transition));
                    Intent intent = new Intent(context, QuizOptionsActivity.class);
                    intent.putExtra("cat", category);
                    intent.putExtra("bitmap", bitmap);
                    context.startActivity(intent, activityOptionsCompat.toBundle());
                }
            });
        } else if (context instanceof SelectCategoryActivity) {

        }

    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    public Bitmap getBitmapFromView(View view) {
        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);
        return bitmap;
    }


}
