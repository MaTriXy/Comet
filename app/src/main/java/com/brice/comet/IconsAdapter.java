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

        new Thread() {
            @Override
            public void run() {
                for (ArrayList<IconItem> section_icons : icons) {
                    for (IconItem section_icon : section_icons) {
                        section_icon.drawable = ContextCompat.getDrawable(IconsAdapter.this.activity, section_icon.icon);
                    }
                }
            }
        }.start();
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
    public void onBindViewHolder(final ViewHolder holder, final int section, final int relative, final int absolute) {
        final SquareImageView image = (SquareImageView) holder.vh;
        final SquareImageView image2 = (SquareImageView) holder.vh2;

        new Thread() {
            @Override
            public void run() {
                while(true) {
                    if (icons.get(section).get(relative).drawable != null) break;
                    try {
                        sleep(500);
                    } catch(InterruptedException e) {
                        return;
                    }
                }
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        TransitionDrawable icon = new TransitionDrawable(new Drawable[]{new ColorDrawable(Color.TRANSPARENT), icons.get(section).get(relative).drawable});
                        image.setImageDrawable(icon);
                        icon.startTransition(250);
                    }
                });
            }
        }.start();

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    image2.setImageResource(icons.get(section).get(relative).icon);
                } catch (Exception e) {
                    return;
                }

                image2.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
                new MaterialDialog.Builder(activity)
                        .customView(image2, false)
                        .title(icons.get(section).get(relative).name)
                        .positiveText("Close")
                        .show();
            }
        });

        ((TextView)holder.title).setText(icons.get(section).get(relative).name);
    }

    @Override
    public void onBindHeaderViewHolder(ViewHolder holder, int section) {
        holder.vh.setVisibility(View.GONE);
        TextView tv = ((TextView)holder.title);
        tv.setTextSize(20);
        tv.setPadding(32, 32, 32, 0);
        String title = titles.get(section);
        tv.setText(title);
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
