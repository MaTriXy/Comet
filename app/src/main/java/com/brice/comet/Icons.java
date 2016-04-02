package com.brice.comet;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class Icons extends AppCompatActivity {

    ArrayList<IconItem> icons;
    ArrayList<String> section_names;
    ArrayList<ArrayList<IconItem>> section_icons;

    RecyclerView rv;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler);
        setTitle("Icons");

        new com.brice.comet.Drawer().make(Icons.this, savedInstanceState).build();
        rv = (RecyclerView) findViewById(R.id.rv);

        icons = new ArrayList<>();
        section_names = new ArrayList<>();
        section_icons = new ArrayList<>();

        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
        rv.setLayoutManager(layoutManager);

        new Thread() {
            @Override
            public void run() {
                Field[] drawables = R.drawable.class.getFields();
                String[] iconses = getResources().getStringArray(R.array.icon_pack);
                for (String icon : iconses) {
                    for (Field drawable : drawables) {
                        if (drawable.getName().matches(icon)) {
                            String name = icon.replace("_", " ");
                            if (name.startsWith("default ")) name = name.substring(8);
                            if (name.endsWith(" icon")) name = name.substring(0, name.length() - 5);

                            icons.add(new IconItem(Character.toString(name.charAt(0)).toUpperCase() + name.substring(1, name.length()), getResources().getIdentifier(drawable.getName(), "drawable", R.drawable.class.getPackage().getName())));
                            break;
                        }
                    }
                }

                for (int section = 0; section < icons.size(); section++) {
                    //create a list of possible names for the section
                    ArrayList<String> possiblenames = new ArrayList<>();
                    for (int icon = 0; icon < icons.size(); icon++) {
                        if (similar(icons.get(section).name, icons.get(icon).name)) possiblenames.add(overlap(icons.get(section).name, icons.get(icon).name));
                    }

                    String name = possiblenames.get(0);
                    for (String possible : possiblenames) {
                        if ((possible.length() < name.length() || name.length() == 0) && possible.length() > 0) name = possible;
                    }
                    if (name.length() == 0) continue;

                    //create a list of all the icons that would be in the section
                    ArrayList<IconItem> possible_icons = new ArrayList<>();
                    for (int icon = 0; icon < icons.size(); icon++) {
                        if (icons.get(icon).name.contains(name)) {
                            possible_icons.add(icons.get(icon));
                        }
                    }

                    name = Character.toString(name.charAt(0)).toUpperCase() + name.substring(1, name.length());

                    //create the section
                    if (!section_names.contains(name)) {
                        section_names.add(name);
                        section_icons.add(possible_icons);
                    }
                }

                //check each section doesn't already have icons in another section
                for (int i = 0; i < section_icons.size(); i++) {
                    int contains = 0;
                    for (int i2 = 0; i2 < section_icons.get(i).size(); i2++) {
                        for (ArrayList<IconItem> icon_names1 : section_icons) {
                            if (icon_names1.contains(section_icons.get(i).get(i2))) contains++;
                        }
                    }

                    if (contains > ((double) section_icons.get(i).size() / 1.5)) {
                        section_names.remove(i);
                        section_icons.remove(i);
                    }
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        IconsAdapter adapter = new IconsAdapter(section_names, section_icons, Icons.this);
                        rv.setAdapter(adapter);
                    }
                });
            }
        }.start();
    }

    private boolean similar(String first, String second) {
        int similar = 0;
        for (int i = 0; i < first.length() && i < second.length(); i++) {
            if (first.charAt(i) == second.charAt(i)) similar++;
        }
        return ((double) (similar * 2) / (first.length() + second.length())) > 0.5;
    }

    private String overlap(String first, String second) {
        String lap = "";

        for (String word : first.split(" ")) {
            if (second.contains(word)) lap += word + " ";
        }
        if (lap.endsWith(" ")) lap = lap.substring(0, lap.length() - 1);

        return lap;
    }
}
