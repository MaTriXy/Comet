package com.brice.comet;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class WallpapersAdapter extends RecyclerView.Adapter<WallpapersAdapter.ViewHolder> {

    private ArrayList<WallpaperItem> list;
    private AppCompatActivity activity;

public static class ViewHolder extends RecyclerView.ViewHolder {
    public View v, vh, title;
    public ViewHolder(View v, View vh, View title) {
        super(v);
        this.v = v;
        this.vh = vh;
        this.title = title;
    }
}

    public WallpapersAdapter(ArrayList<WallpaperItem> list, AppCompatActivity activity) {
        this.list = list;
        this.activity = activity;
    }

    @Override
    public WallpapersAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.wall_item, parent, false);
        View vh = v.findViewById(R.id.wall);
        View title = v.findViewById(R.id.textView);
        return new ViewHolder(v, vh, title);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                Bundle args = new Bundle();
                args.putParcelable("wall", list.get(holder.getAdapterPosition()));

                Fragment f = new WallpaperFragment();
                f.setArguments(args);

                activity.getSupportFragmentManager().beginTransaction().add(R.id.fragment, f).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).addToBackStack(null).commit();
            }
        });

        ((TextView)holder.title).setText(list.get(position).title);

        Glide.with(activity).load(list.get(position).url).into((ImageView) holder.v.findViewById(R.id.wall));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}