package com.brice.comet;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

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

        Glide.with(this).load(getResources().getString(R.string.main_cover)).centerCrop().into(main);
        Glide.with(this).load(getResources().getString(R.string.icon_cover)).centerCrop().into(secondary);

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
