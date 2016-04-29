package com.brice.comet;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.sectionedrecyclerview.SectionedRecyclerViewAdapter;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class IconsAdapter extends SectionedRecyclerViewAdapter<IconsAdapter.ViewHolder> {

    private ArrayList<String> titles;
    private ArrayList<ArrayList<IconItem>> icons;

    private Activity activity;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public View v, vh, v2, vh2, title;
        public ViewHolder(View v, View vh, View v2, View vh2, View title) {
            super(v);
            this.v = v;
            this.vh = vh;
            this.v2 = v2;
            this.vh2 = vh2;
            this.title = title;
        }
    }

    public IconsAdapter(ArrayList<String> section_titles, ArrayList<ArrayList<IconItem>> section_icons, Activity activity) {
        this.activity = activity;
        titles = section_titles;
        icons = section_icons;
    }

    @Override
    public IconsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.icon_item, parent, false);
        View vh = v.findViewById(R.id.image);
        View v2 = LayoutInflater.from(parent.getContext()).inflate(R.layout.icon_item, parent, false);
        View vh2 = v2.findViewById(R.id.image);
        View title = v.findViewById(R.id.title);
        return new ViewHolder(v, vh, v2, vh2, title);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int section, final int relative, int absolute) {
        Glide.with(activity).load(icons.get(section).get(relative).icon).into((SquareImageView) holder.vh);

        holder.v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SquareImageView image = (SquareImageView) holder.vh2;
                Glide.with(activity).load(icons.get(section).get(relative).icon).into(image);

                image.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                new MaterialDialog.Builder(activity)
                        .customView(image, false)
                        .title(icons.get(section).get(relative).name)
                        .positiveText("Close")
                        .show();
            }
        });

        ((TextView) holder.title).setText(icons.get(section).get(relative).name);
    }

    @Override
    public void onBindHeaderViewHolder(ViewHolder holder, int section) {
        holder.vh.setVisibility(View.GONE);
        TextView tv = ((TextView)holder.title);
        tv.setTextSize(20);
        tv.setPadding(32, 32, 32, 0);
        String title = titles.get(section);
        tv.setText(title);
        holder.v.setClickable(false);
    }

    @Override
    public int getSectionCount() {
        return icons.size();
    }

    @Override
    public int getItemCount(int section) {
        return icons.get(section).size();
    }
}
