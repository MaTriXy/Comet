package com.brice.comet;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

public class Home extends AppCompatActivity {

    int h, w;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("Home");

        ((TextView) findViewById(R.id.version)).setText("Version " + BuildConfig.VERSION_NAME);

        h = findViewById(R.id.icon).getHeight();
        w = findViewById(R.id.icon).getWidth();

        new com.brice.comet.Drawer().make(Home.this, savedInstanceState).build();

        findViewById(R.id.icons).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Home.this, Icons.class));
                finish();
            }
        });

        findViewById(R.id.walls).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Home.this, Wallpapers.class));
                finish();
            }
        });
    }
}
