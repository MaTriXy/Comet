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
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;

import java.io.IOException;
import java.util.ArrayList;

public class WallpapersAdapter extends RecyclerView.Adapter<WallpapersAdapter.ViewHolder> {

    private ArrayList<Integer> mThumbs;
    private ArrayList<String> mTitles;
    private Context context;
    private Activity activity;
    private int clicked;
    private ArrayList<Bitmap> bitmaps;


public static class ViewHolder extends RecyclerView.ViewHolder {
    public View v, vh, title;
    public ViewHolder(View v, View vh, View title) {
        super(v);
        this.v = v;
        this.vh = vh;
        this.title = title;
    }
}

    public WallpapersAdapter(ArrayList<Integer> mThumbs, ArrayList<String> mTitles, Activity activity) {
        this.mThumbs = mThumbs;
        this.mTitles = mTitles;
        this.activity = activity;
        this.context = activity.getApplicationContext();
        bitmaps = new ArrayList<>();

        new Thread() {
            @Override
            public void run() {
                for (int i = 0; i < WallpapersAdapter.this.mThumbs.size(); i++) {
                    Drawable d = context.getResources().getDrawable(WallpapersAdapter.this.mThumbs.get(i));
                    Bitmap b = ((BitmapDrawable)d).getBitmap();
                    b = Bitmap.createBitmap(b, b.getWidth()/2 - b.getHeight()/2, 0, b.getHeight(), b.getHeight());
                    b = Bitmap.createScaledBitmap(b, b.getWidth() / 5, b.getHeight() / 5, true);
                    WallpapersAdapter.this.bitmaps.add(i, b);
                }
                WallpapersAdapter.this.activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //i did thing
                    }
                });
            }
        }.start();
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
        new Thread() {
            @Override
            public void run() {
                while(true) {
                    if (bitmaps.size() > position) break;
                    try {
                        sleep(500);
                    } catch(InterruptedException e) {
                        //do nothing
                    }
                }
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Bitmap b = bitmaps.get(position);
                        SquareImageView wallView = (SquareImageView) holder.vh;

                        TransitionDrawable wall = new TransitionDrawable(new Drawable[]{wallView.getDrawable(), new BitmapDrawable(context.getResources(), b)});
                        wallView.setImageDrawable(wall);
                        wall.startTransition(250);
                        holder.v.setTag(b);
                    }
                });
            }
        }.start();
        holder.v.setId(position);
        holder.v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                if (v.getTag() != null) {
                    clicked = v.getId();
                    View wall = LayoutInflater.from(v.getContext()).inflate(R.layout.wall_item, null).findViewById(R.id.wall);
                    ((SquareImageView)wall).setImageBitmap((Bitmap) v.getTag());

                    new MaterialDialog.Builder(activity)
                            .callback(new MaterialDialog.ButtonCallback() {
                                @Override
                                public void onPositive(MaterialDialog dialog) {
                                    super.onPositive(dialog);
                                    boolean set = true;
                                    WallpaperManager myWallpaperManager = WallpaperManager.getInstance(context.getApplicationContext());
                                    try {
                                        myWallpaperManager.setResource(mThumbs.get(clicked));
                                    } catch (IOException e) {
                                        set = false;
                                        Toast.makeText(dialog.getContext(), "Wallpaper not set :(", Toast.LENGTH_SHORT).show();
                                    }
                                    if (set) Toast.makeText(dialog.getContext(), "Wallpaper set!", Toast.LENGTH_SHORT).show();
                                }
                            })
                            .title("Set wallpaper?")
                            .customView(wall, false)
                            .positiveText("Apply")
                            .negativeText("Cancel")
                            .show();
                }
            }
        });



        ((TextView)holder.title).setText(mTitles.get(position));
    }

    @Override
    public int getItemCount() {
        return mThumbs.size();
    }
}