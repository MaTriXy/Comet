package com.brice.comet;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.bumptech.glide.Glide;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;

public class Drawer {

    private AccountHeader getAccountHeader(AppCompatActivity activity, Bundle savedState) {
        AccountHeader header = new AccountHeaderBuilder()
                .withActivity(activity)
                .withCompactStyle(false)
                .withProfileImagesClickable(false)
                .withSelectionListEnabledForSingleProfile(false)
                .withSavedInstance(savedState)
                .addProfiles(
                        new ProfileDrawerItem().withName(activity.getResources().getString(R.string.app_name)).withEmail("Version " + BuildConfig.VERSION_NAME).withIcon(new ColorDrawable(ContextCompat.getColor(activity, R.color.colorPrimary)))
                )
                .build();
        Glide.with(activity).load(activity.getResources().getString(R.string.drawer_cover)).into(header.getHeaderBackgroundView());
        return header;
    }

    public DrawerBuilder make(final AppCompatActivity activity, Bundle savedState) {
        int selected = -1;
        switch(activity.getTitle().toString()){
            case "Home":
                selected = 1;
                break;
            case "Icons":
                selected = 2;
                break;
            case "Wallpapers":
                selected = 3;
                break;
            case "Apply":
                selected = 4;
                break;
            case "About":
                selected = 5;
                break;
        }

        Toolbar toolbar = (Toolbar) activity.findViewById(R.id.toolbar);
        toolbar.setTitle(activity.getTitle().toString());

        return new DrawerBuilder()
                .withActivity(activity)
                .withTranslucentStatusBar(true)
                .withActionBarDrawerToggle(true)
                .withAccountHeader(getAccountHeader(activity, savedState))
                .withToolbar(toolbar)
                        .withSelectedItem(selected)
                        .addDrawerItems(
                                new SecondaryDrawerItem().withName("Home").withIdentifier(1).withIcon(GoogleMaterial.Icon.gmd_home).withIconColor(Color.parseColor("#C62828")).withTextColor(Color.parseColor("#212121")),
                                new SecondaryDrawerItem().withName("Icons").withIdentifier(2).withIcon(GoogleMaterial.Icon.gmd_insert_emoticon).withIconColor(Color.parseColor("#EF6C00")).withTextColor(Color.parseColor("#212121")),
                                new SecondaryDrawerItem().withName("Wallpapers").withIdentifier(3).withIcon(GoogleMaterial.Icon.gmd_image).withIconColor(Color.parseColor("#1565C0")).withTextColor(Color.parseColor("#212121")),
                                new SecondaryDrawerItem().withName("Apply").withIdentifier(4).withIcon(GoogleMaterial.Icon.gmd_check).withIconColor(Color.parseColor("#2E7D32")).withTextColor(Color.parseColor("#212121")),
                                new DividerDrawerItem(),
                                new SecondaryDrawerItem().withName("About " + activity.getResources().getString(R.string.app_name)).withIdentifier(5).withTextColor(Color.parseColor("#212121"))

                        )
                        .withOnDrawerItemClickListener(new com.mikepenz.materialdrawer.Drawer.OnDrawerItemClickListener() {
                            @Override
                            public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                                Intent i = null;
                                if (drawerItem.getIdentifier() == 1) {
                                    i = new Intent(activity, Home.class);
                                } else if (drawerItem.getIdentifier() == 2) {
                                    i = new Intent(activity, Icons.class);
                                } else if (drawerItem.getIdentifier() == 3) {
                                    i = new Intent(activity, Wallpapers.class);
                                } else if (drawerItem.getIdentifier() == 4) {
                                    i = new Intent(activity, Launchers.class);
                                } else if (drawerItem.getIdentifier() == 5) {
                                    i = new Intent(activity, About.class);
                                }
                                if (i != null) {
                                    activity.startActivity(i);
                                    activity.finish();
                                }
                                return false;
                            }
                        });
    }
}
