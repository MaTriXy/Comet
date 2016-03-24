package com.brice.comet;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;

import java.lang.reflect.Constructor;

public class LaunchersAdapter extends RecyclerView.Adapter<LaunchersAdapter.ViewHolder> {

    Context context;
    Activity activity;
    String[] titles, names;
    TypedArray ids;

public static class ViewHolder extends RecyclerView.ViewHolder {
    public View v, icon, title;
    public ViewHolder(View v, View icon, View title) {
        super(v);
        this.v = v;
        this.icon = icon;
        this.title = title;
    }
}

    public LaunchersAdapter(Activity activity) {
        this.activity = activity;
        this.context = activity.getApplicationContext();

        titles = context.getResources().getStringArray(R.array.launchers);
        names = context.getResources().getStringArray(R.array.access);
        ids = context.getResources().obtainTypedArray(R.array.ids);
    }

    @Override
    public LaunchersAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.launcher_icon, parent, false);
        View icon = v.findViewById(R.id.icon);
        View title = v.findViewById(R.id.title);
        return new ViewHolder(v, icon, title);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        ((ImageView)holder.icon).setImageResource(ids.getResourceId(position, -1));
        ((TextView)holder.title).setText(titles[position]);
        holder.v.setTag(position);
        holder.v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int pos = (int) v.getTag();
                if (launcherIsInstalled(names[pos])) {
                    openLauncher(titles[pos]);
                } else {
                    new MaterialDialog.Builder(activity)
                            .title(titles[pos])
                            .content(titles[pos] + " isn't installed. Would you like to install it?")
                            .positiveText("Install")
                            .negativeText("Dismiss")
                            .callback(new MaterialDialog.ButtonCallback() {
                                @Override
                                public void onPositive(MaterialDialog dialog) {
                                    Intent intent = new Intent(Intent.ACTION_VIEW);
                                    intent.setData(Uri.parse("https://play.google.com/store/apps/details?id=" + names[pos]));
                                    context.startActivity(intent);
                                }
                            }).show();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return titles.length;
    }

    public static int getDominantColor(Bitmap bitmap) {
        if (null == bitmap) return Color.TRANSPARENT;

        int redBucket = 0;
        int greenBucket = 0;
        int blueBucket = 0;
        int alphaBucket = 0;

        boolean hasAlpha = bitmap.hasAlpha();
        int pixelCount = bitmap.getWidth() * bitmap.getHeight();
        int[] pixels = new int[pixelCount];
        bitmap.getPixels(pixels, 0, bitmap.getWidth(), 0, 0, bitmap.getWidth(), bitmap.getHeight());

        for (int y = 0, h = bitmap.getHeight(); y < h; y++) {
            for (int x = 0, w = bitmap.getWidth(); x < w; x++) {
                int color = pixels[x + y * w];
                redBucket += (color >> 16) & 0xFF;
                greenBucket += (color >> 8) & 0xFF;
                blueBucket += (color & 0xFF);
                if (hasAlpha) alphaBucket += (color >>> 24);
            }
        }

        return Color.argb(
                (hasAlpha) ? (alphaBucket / pixelCount) : 255,
                redBucket / pixelCount,
                greenBucket / pixelCount,
                blueBucket / pixelCount);
    }

    public int darkColor(int color){
        float[] hsv = new float[3];
        Color.colorToHSV(color, hsv);
        hsv[2] *= 0.8f;
        return Color.HSVToColor(hsv);
    }

    private void openLauncher(String name) {

        final String className = "com.brice.comet" + ".launchers." + Character.toUpperCase(name.charAt(0)) + name.substring(1).toLowerCase().replace(" ", "").replace("launcher", "") + "Launcher";

        Class<?> cl = null;
        try {
            cl = Class.forName(className);
        } catch (ClassNotFoundException e) {
            Log.e("LAUNCHER CLASS MISSING", "Launcher class for: '" + name + "' missing!");
        }
        if (cl != null) {
            Constructor<?> constructor = null;
            try {
                constructor = cl.getConstructor(Context.class);
            } catch (NoSuchMethodException e) {
                Log.e("LAUNCHER CLASS CONS", "Launcher class for: '" + name + "' is missing a constructor!");
            }
            try {
                if (constructor != null) constructor.newInstance(activity);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private boolean launcherIsInstalled(String packageName) {
        final PackageManager pm = activity.getPackageManager();
        boolean installed;
        try {
            pm.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES);
            installed = true;
        } catch (PackageManager.NameNotFoundException e) {
            installed = false;
        }
        return installed;
    }
}
