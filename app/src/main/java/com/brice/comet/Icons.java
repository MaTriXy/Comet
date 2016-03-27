package com.brice.comet;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class Icons extends AppCompatActivity {

    ArrayList<String> names, section_names;
    ArrayList<Integer> icons;
    ArrayList<ArrayList<String>> section_icon_names;
    ArrayList<ArrayList<Integer>> section_icon_icons;

    RecyclerView rv;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler);
        setTitle("Icons");

        new com.brice.comet.Drawer().make(Icons.this, savedInstanceState).build();
        rv = (RecyclerView) findViewById(R.id.rv);

        names = new ArrayList<>();
        icons = new ArrayList<>();

        section_names = new ArrayList<>();
        section_icon_names = new ArrayList<>();
        section_icon_icons = new ArrayList<>();

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

                            names.add(name);
                            icons.add(getResources().getIdentifier(drawable.getName(), "drawable", R.drawable.class.getPackage().getName()));
                            break;
                        }
                    }
                }

                for (int section = 0; section < names.size(); section++) {
                    //create a list of possible names for the section
                    ArrayList<String> possiblenames = new ArrayList<>();
                    for (int icon = 0; icon < names.size(); icon++) {
                        if (similar(names.get(section), names.get(icon))) possiblenames.add(overlap(names.get(section), names.get(icon)));
                    }

                    String name = possiblenames.get(0);
                    for (String possible : possiblenames) {
                        if (possible.length() < name.length()) name = possible;
                    }

                    //create a list of all the icons that would be in the section
                    ArrayList<String> icon_names = new ArrayList<>();
                    ArrayList<Integer> icon_icons = new ArrayList<>();
                    for (int icon = 0; icon < names.size(); icon++) {
                        if (names.get(icon).contains(name)) {
                            icon_names.add(names.get(icon));
                            icon_icons.add(icons.get(icon));
                        }
                    }

                    //create the section
                    if (!section_names.contains(name)) {
                        section_names.add(name);
                        section_icon_names.add(icon_names);
                        section_icon_icons.add(icon_icons);
                    }
                }

                //check each section doesn't already have icons in another section
                for (int i = 0; i < section_icon_names.size(); i++) {
                    int contains = 0;
                    for (int i2 = 0; i2 < section_icon_names.get(i).size(); i2++) {
                        for (ArrayList<String> icon_names1 : section_icon_names) {
                            if (icon_names1.contains(section_icon_names.get(i).get(i2))) contains++;
                        }
                    }

                    if (contains > ((double) section_icon_names.get(i).size() / 2)) {
                        section_names.remove(i);
                        section_icon_names.remove(i);
                        section_icon_icons.remove(i);
                    }
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        IconsAdapter adapter = new IconsAdapter(section_names, section_icon_names, section_icon_icons, Icons.this);
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
