package com.brice.comet;

import android.app.ProgressDialog;
import android.app.WallpaperManager;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.Request;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.SizeReadyCallback;
import com.bumptech.glide.request.target.Target;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class WallpaperFragment extends Fragment {

    WallpaperItem item;
    View rootView;
    ProgressDialog downloadDialog;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_wallpaper, container, false);

        item = getArguments().getParcelable("wall");

        Glide.with(getContext()).load(item.url).into((ImageView) rootView.findViewById(R.id.wall));

        rootView.findViewById(R.id.download).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Glide.with(getContext()).load(item.url).asBitmap().into(new Target<Bitmap>() {
                    @Override
                    public void onLoadStarted(Drawable placeholder) {
                        downloadDialog = new ProgressDialog(getContext());
                        downloadDialog.setTitle("Downloading...");
                        downloadDialog.setIndeterminate(true);
                        downloadDialog.setProgressStyle(R.style.Widget_MaterialProgressBar_ProgressBar_Horizontal);
                        downloadDialog.show();
                    }

                    @Override
                    public void onLoadFailed(Exception e, Drawable errorDrawable) {
                        if (downloadDialog != null) downloadDialog.dismiss();
                        Snackbar.make(rootView, "Error: " + e.getMessage(), Snackbar.LENGTH_LONG).show();
                    }

                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        downloadDialog.dismiss();
                        FileOutputStream out = null;
                        try {
                            out = new FileOutputStream(item.title + System.currentTimeMillis() + ".png");
                            resource.compress(Bitmap.CompressFormat.PNG, 100, out);
                        } catch (Exception e) {
                            e.printStackTrace();
                        } finally {
                            try {
                                if (out != null) {
                                    out.close();
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onLoadCleared(Drawable placeholder) {
                    }

                    @Override
                    public void getSize(SizeReadyCallback cb) {
                    }

                    @Override
                    public void setRequest(Request request) {

                    }

                    @Override
                    public Request getRequest() {
                        return null;
                    }

                    @Override
                    public void onStart() {
                    }

                    @Override
                    public void onStop() {
                    }

                    @Override
                    public void onDestroy() {
                    }
                });
            }
        });

        rootView.findViewById(R.id.share).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Glide.with(getContext()).load(item.url).asBitmap().into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        Uri uri = null;
                        try {
                            File file =  new File(getContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES), item.title + System.currentTimeMillis() + ".png");
                            FileOutputStream out = new FileOutputStream(file);
                            resource.compress(Bitmap.CompressFormat.PNG, 90, out);
                            out.close();
                            uri = Uri.fromFile(file);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        Intent i = new Intent(Intent.ACTION_SEND);
                        i.setType("image/*");
                        i.putExtra(Intent.EXTRA_STREAM, uri);
                        startActivity(Intent.createChooser(i, "Share " + item.title));
                    }
                });
            }
        });

        rootView.findViewById(R.id.apply).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Glide.with(getContext()).load(item.url).asBitmap().into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation glideAnimation) {
                        WallpaperManager myWallpaperManager = WallpaperManager.getInstance(getContext());

                        try {
                            myWallpaperManager.setBitmap(resource);
                        } catch (Exception e) {
                            e.printStackTrace();
                            Snackbar.make(rootView, "Wallpaper not set :(", Snackbar.LENGTH_SHORT).setAction("Retry", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    rootView.findViewById(R.id.apply).callOnClick();
                                }
                            }).show();
                            return;
                        }

                        Snackbar.make(rootView, "Wallpaper set!", Snackbar.LENGTH_SHORT).show();
                    }
                });
            }
        });

        return rootView;
    }
}
