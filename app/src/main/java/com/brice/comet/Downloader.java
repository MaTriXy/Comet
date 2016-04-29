package com.brice.comet;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;

import com.bumptech.glide.Glide;

import java.util.concurrent.ExecutionException;

public class Downloader {

    public static Bitmap downloadImage(Context context, String src) {
        try {
            return Glide.with(context).load(src).asBitmap().error(R.mipmap.wall_loading).into(-1, -1).get();
        } catch (InterruptedException | ExecutionException i) {
            i.printStackTrace();
        }

        Bitmap bmp = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bmp);
        canvas.drawColor(Color.TRANSPARENT);
        return bmp;
    }

}
