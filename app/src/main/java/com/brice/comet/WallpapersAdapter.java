package com.brice.comet;

import android.app.Activity;
import android.app.WallpaperManager;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;

import java.io.IOException;
import java.util.ArrayList;

public class WallpapersAdapter extends RecyclerView.Adapter<WallpapersAdapter.ViewHolder> {

    private ArrayList<String> mThumbs;
    private ArrayList<String> mTitles;
    private Activity activity;

public static class ViewHolder extends RecyclerView.ViewHolder {
    public View v, vh, title;
    public ViewHolder(View v, View vh, View title) {
        super(v);
        this.v = v;
        this.vh = vh;
        this.title = title;
    }
}

    public WallpapersAdapter(ArrayList<String> mThumbs, ArrayList<String> mTitles, Activity activity) {
        this.mThumbs = mThumbs;
        this.mTitles = mTitles;
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
                View wall = LayoutInflater.from(v.getContext()).inflate(R.layout.wall_item, null).findViewById(R.id.wall);
                ((SquareImageView)wall).setImageBitmap((Bitmap) v.getTag());

                new MaterialDialog.Builder(activity)
                        .callback(new MaterialDialog.ButtonCallback() {
                            @Override
                            public void onPositive(MaterialDialog dialog) {
                                super.onPositive(dialog);
                                    /*boolean set = true;
                                    WallpaperManager myWallpaperManager = WallpaperManager.getInstance(activity);
                                    try {
                                        myWallpaperManager.setResource(mThumbs.get(clicked));
                                    } catch (IOException e) {
                                        set = false;
                                        Toast.makeText(dialog.getContext(), "Wallpaper not set :(", Toast.LENGTH_SHORT).show();
                                    }
                                    if (set) Toast.makeText(dialog.getContext(), "Wallpaper set!", Toast.LENGTH_SHORT).show();*/
                            }
                        })
                        .title("Set wallpaper?")
                        .customView(wall, false)
                        .positiveText("Apply")
                        .negativeText("Cancel")
                        .show();
            }
        });



        ((TextView)holder.title).setText(mTitles.get(position));
        Glide.with(activity).load(mThumbs.get(position)).into((ImageView) holder.v.findViewById(R.id.wall));
    }

    @Override
    public int getItemCount() {
        return mThumbs.size();
    }
}