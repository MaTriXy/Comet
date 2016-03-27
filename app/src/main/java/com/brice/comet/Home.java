package com.brice.comet;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.TransitionDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class Home extends AppCompatActivity {

    TextView version;
    ImageView main, secondary;
    View icons, walls;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Home");

        version = (TextView) findViewById(R.id.version);
        main = (ImageView) findViewById(R.id.main_image);
        secondary = (ImageView) findViewById(R.id.secondary_image);
        icons = findViewById(R.id.icons);
        walls = findViewById(R.id.walls);

        version.setText("Version " + BuildConfig.VERSION_NAME);

        new com.brice.comet.Drawer().make(Home.this, savedInstanceState).build();

        new Thread() {
            @Override
            public void run() {
                final Bitmap main_cover = Downloader.downloadImage(Home.this, getResources().getString(R.string.main_cover));
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        TransitionDrawable td = new TransitionDrawable(new Drawable[]{new ColorDrawable(Color.TRANSPARENT), new BitmapDrawable(getResources(), main_cover)});
                        main.setImageDrawable(td);
                        td.startTransition(250);
                    }
                });

                final Bitmap icon_cover = Downloader.downloadImage(Home.this, getResources().getString(R.string.icon_cover));
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        TransitionDrawable td = new TransitionDrawable(new Drawable[]{new ColorDrawable(Color.TRANSPARENT), new BitmapDrawable(getResources(), icon_cover)});
                        secondary.setImageDrawable(td);
                        td.startTransition(250);
                    }
                });
            }
        }.start();

        icons.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Home.this, Icons.class));
                finish();
            }
        });

        walls.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Home.this, Wallpapers.class));
                finish();
            }
        });
    }
}
