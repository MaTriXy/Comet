package com.brice.comet;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;

public class Wallpapers extends AppCompatActivity {

    ArrayList<String> names;
    ArrayList<String> icons;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler);
        setTitle("Wallpapers");

        new com.brice.comet.Drawer().make(Wallpapers.this, savedInstanceState).build();

        names = new ArrayList<>();
        icons = new ArrayList<>();

        String[] titles = getResources().getStringArray(R.array.wallpaper_names);
        String[] urls = getResources().getStringArray(R.array.wallpapers);

        Collections.addAll(names, titles);
        Collections.addAll(icons, urls);

        RecyclerView rv = (RecyclerView) findViewById(R.id.rv);
        rv.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        rv.setAdapter(new WallpapersAdapter(icons, names, Wallpapers.this));
    }
}
