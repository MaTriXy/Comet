package com.brice.comet;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.sectionedrecyclerview.SectionedRecyclerViewAdapter;

import java.util.ArrayList;

public class IconsAdapter extends SectionedRecyclerViewAdapter<IconsAdapter.ViewHolder> {

    private ArrayList<String> titles;
    private ArrayList<ArrayList<String>> icon_names;
    private ArrayList<ArrayList<Integer>> icon_icons;

    private ArrayList<ArrayList<Drawable>> section_icons;
    private Context context;
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

    public IconsAdapter(ArrayList<String> section_titles, ArrayList<ArrayList<String>> section_icon_names, ArrayList<ArrayList<Integer>> section_icon_icons, Activity activity) {
        this.activity = activity;
        this.context = activity.getApplicationContext();
        titles = section_titles;
        icon_names = section_icon_names;
        icon_icons = section_icon_icons;
        section_icons = new ArrayList<>();
        new Thread() {
            @Override
            public void run() {
                for (int i1 = 0; i1 < icon_icons.size(); i1++) {
                    ArrayList<Drawable> icons = new ArrayList<>();
                    for (int i2 = 0; i2 < icon_icons.get(i1).size(); i2++) {
                        icons.add(i2, context.getResources().getDrawable(icon_icons.get(i1).get(i2)));
                    }
                    section_icons.add(i1, icons);
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
                    if (section_icons.size() > section && section_icons.get(section).size() > relative) break;
                    try {
                        sleep(500);
                    } catch(InterruptedException e) {
                        //do nothing
                    }
                }
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        TransitionDrawable icon = new TransitionDrawable(new Drawable[]{new ColorDrawable(Color.TRANSPARENT), section_icons.get(section).get(relative)});
                        image.setImageDrawable(icon);
                        icon.startTransition(250);
                    }
                });
            }
        }.start();

        Bundle bundle = new Bundle();
        bundle.putInt("section", section);
        bundle.putInt("relative", relative);
        image.setTag(bundle);
        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = (Bundle) v.getTag();
                int section = bundle.getInt("section"), relative = bundle.getInt("relative");
                try {
                    image2.setImageResource(icon_icons.get(section).get(relative));
                } catch (Exception e) {
                    return;
                }
                image2.setPadding(64, 64, 64, 64);
                new MaterialDialog.Builder(activity)
                        .customView(image2, false)
                        .title(icon_names.get(section).get(relative))
                        .positiveText("Close")
                        .show();
            }
        });

        String name = icon_names.get(section).get(relative);
        ((TextView)holder.title).setText(Character.toString(name.charAt(0)).toUpperCase() + name.substring(1, name.length()));
    }

    @Override
    public void onBindHeaderViewHolder(ViewHolder holder, int section) {
        holder.vh.setVisibility(View.GONE);
        TextView tv = ((TextView)holder.title);
        tv.setTextSize(20);
        tv.setPadding(32, 32, 32, 0);
        String title = titles.get(section);
        tv.setText(Character.toString(title.charAt(0)).toUpperCase() + title.substring(1, title.length()));
    }

    @Override
    public int getSectionCount() {
        return titles.size();
    }

    @Override
    public int getItemCount(int section) {
        return icon_names.get(section).size();
    }
}
