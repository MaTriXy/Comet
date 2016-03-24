package com.brice.comet;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class Wallpapers extends AppCompatActivity {

    ArrayList<String> names;
    ArrayList<Integer> icons;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler);
        setTitle("Wallpapers");

        new com.brice.comet.Drawer().make(Wallpapers.this, savedInstanceState).build();

        names = new ArrayList<>();
        icons = new ArrayList<>();

        Field[] drawables = R.drawable.class.getFields();
        String[] iconses = getResources().getStringArray(R.array.wallpapers);
        for (String icon : iconses) {
            for (Field drawable : drawables) {
                if (drawable.getName().contains(icon)) {
                    String name = icon.replace("wallpaper_", "").replace("_", " ");
                    name = Character.toString(name.charAt(0)).toUpperCase() + name.substring(1, name.length());
                    names.add(name);
                    icons.add(getResources().getIdentifier(drawable.getName(), "drawable", R.drawable.class.getPackage().getName()));
                    break;
                }
            }
        }

        RecyclerView rv = (RecyclerView) findViewById(R.id.rv);
        rv.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        rv.setAdapter(new WallpapersAdapter(icons, names, Wallpapers.this));
    }
}
