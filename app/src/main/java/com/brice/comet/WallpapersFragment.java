package com.brice.comet;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

public class WallpapersFragment extends Fragment {

    ArrayList<WallpaperItem> walls;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_recycler, container, false);

        walls = new ArrayList<>();

        String[] titles = getResources().getStringArray(R.array.wallpaper_names);
        String[] urls = getResources().getStringArray(R.array.wallpapers);

        for (int i = 0; i < titles.length; i++) {
            walls.add(new WallpaperItem(titles[i], urls[i]));
        }

        RecyclerView rv = (RecyclerView) rootView.findViewById(R.id.rv);
        rv.setLayoutManager(new GridLayoutManager(getContext(), 2));
        rv.setAdapter(new WallpapersAdapter(walls, (AppCompatActivity) getActivity()));

        return rootView;
    }
}
