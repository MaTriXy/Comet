package com.brice.comet;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class IconsFragment extends Fragment {

    ArrayList<IconItem> icons;
    ArrayList<String> section_names;
    ArrayList<ArrayList<IconItem>> section_icons;

    RecyclerView rv;

    Thread t;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_recycler, container, false);

        rv = (RecyclerView) rootView.findViewById(R.id.rv);

        icons = new ArrayList<>();
        section_icons = new ArrayList<>();

        StaggeredGridLayoutManager layoutManager = new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
        rv.setLayoutManager(layoutManager);

        t = new Thread() {
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

                section_names = new ArrayList<>(Arrays.asList("A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"));
                for (int i = 0; i < section_names.size(); i++) {
                    section_icons.add(new ArrayList<IconItem>());
                }

                for (IconItem icon : icons) {
                    for (String name : section_names) {
                        if (icon.name.startsWith(name)) section_icons.get(section_names.indexOf(name)).add(icon);
                    }
                }

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        IconsAdapter adapter = new IconsAdapter(section_names, section_icons, getActivity());
                        rv.setAdapter(adapter);
                    }
                });
            }
        };
        t.start();

        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (t != null && t.isAlive()) t.interrupt();
    }
}
