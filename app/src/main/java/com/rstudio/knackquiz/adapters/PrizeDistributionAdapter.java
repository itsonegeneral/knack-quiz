package com.rstudio.knackquiz.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.rstudio.knackquiz.R;

import java.util.ArrayList;
import java.util.List;

public class PrizeDistributionAdapter extends ArrayAdapter<Integer> {

    List<Integer> prizes;

    private int resourceLayout;
    private Context mContext;

    public PrizeDistributionAdapter(@NonNull Context context, int resource, @NonNull List<Integer> objects) {
        super(context, resource, objects);
        this.resourceLayout = resource;
        this.mContext = context;
        this.prizes = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View v = convertView;

        if (v == null) {
            LayoutInflater vi;
            vi = LayoutInflater.from(mContext);
            v = vi.inflate(resourceLayout, null);
        }


        TextView tvPosition,tvPrize;
        tvPosition  = v.findViewById(R.id.tv_positionPrizeList);
        tvPrize = v.findViewById(R.id.tv_prizePrizeList);

        tvPosition.setText(prizes.get(position));

        return v;
    }
}
