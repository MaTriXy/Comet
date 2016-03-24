package com.brice.comet;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class About extends AppCompatActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        setTitle("About");

        link(R.id.jplus, "http://theandroidmaster.github.io/");
        link(R.id.jweb, "http://www.jahirfiquitiva.net/");
        link(R.id.rplus, "https://plus.google.com/100576513238377229890/about");
        link(R.id.rmail, "mailto:briceseibert@gmail.com");
        link(R.id.fplus, "https://plus.google.com/+JamesFennJAFFA2157/about");
        link(R.id.fweb, "http://theandroidmaster.github.io/");

        link(R.id.dialog, "https://github.com/afollestad/material-dialogs");
        link(R.id.drawer, "https://github.com/mikepenz/MaterialDrawer");

        new com.brice.comet.Drawer().make(About.this, savedInstanceState).build();
    }

    private void link(int id, final String url) {
        findViewById(id).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
            }
        });
    }
}
