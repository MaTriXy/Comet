package com.brice.comet;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

public class HomeFragment extends Fragment {

    TextView version;
    ImageView main, secondary;
    View icons, walls;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        version = (TextView) rootView.findViewById(R.id.version);
        main = (ImageView) rootView.findViewById(R.id.main_image);
        secondary = (ImageView) rootView.findViewById(R.id.secondary_image);
        icons = rootView.findViewById(R.id.icons);
        walls = rootView.findViewById(R.id.walls);

        version.setText("Version " + BuildConfig.VERSION_NAME);

        Glide.with(getContext()).load(getResources().getString(R.string.main_cover)).into(main);
        Glide.with(getContext()).load(getResources().getString(R.string.icon_cover)).into(secondary);

        icons.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment, new IconsFragment()).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).addToBackStack(null).commit();
            }
        });

        walls.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.fragment, new WallpaperFragment()).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).addToBackStack(null).commit();
            }
        });

        return rootView;
    }
}
