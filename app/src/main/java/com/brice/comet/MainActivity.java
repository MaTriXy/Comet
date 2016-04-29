package com.brice.comet;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

public class MainActivity extends AppCompatActivity {

    private AccountHeader header;
    private TextView title;
    private Fragment f;
    private Drawer drawer;
    private FragmentManager.OnBackStackChangedListener listener;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        title = (TextView) findViewById(R.id.title);

        header = new AccountHeaderBuilder()
                .withActivity(this)
                .withCompactStyle(false)
                .withProfileImagesClickable(false)
                .withSelectionListEnabledForSingleProfile(false)
                .addProfiles(
                        new ProfileDrawerItem().withName(getResources().getString(R.string.app_name)).withEmail("Version " + BuildConfig.VERSION_NAME).withIcon(new ColorDrawable(ContextCompat.getColor(this, R.color.colorPrimary)))
                )
                .build();

        drawer = new DrawerBuilder()
                .withActivity(this)
                .withToolbar(toolbar)
                .withAccountHeader(header)
                .withSelectedItem(0)
                .addDrawerItems(
                        new SecondaryDrawerItem().withName("Home").withIdentifier(1).withIcon(GoogleMaterial.Icon.gmd_home),
                        new SecondaryDrawerItem().withName("Icons").withIdentifier(2).withIcon(GoogleMaterial.Icon.gmd_insert_emoticon),
                        new SecondaryDrawerItem().withName("Wallpapers").withIdentifier(3).withIcon(GoogleMaterial.Icon.gmd_image),
                        new SecondaryDrawerItem().withName("Apply").withIdentifier(4).withIcon(GoogleMaterial.Icon.gmd_check),
                        new DividerDrawerItem(),
                        new SecondaryDrawerItem().withName("About " + getResources().getString(R.string.app_name)).withIdentifier(5)
                ).withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        if (drawerItem instanceof SecondaryDrawerItem) title.setText(((SecondaryDrawerItem) drawerItem).getName().toString());

                        if (drawerItem.getIdentifier() == 1) f = new HomeFragment();
                        else if (drawerItem.getIdentifier() == 2) f = new IconsFragment();
                        else if (drawerItem.getIdentifier() == 3) f = new WallpapersFragment();
                        else if (drawerItem.getIdentifier() == 4) f = new ApplyFragment();
                        else if (drawerItem.getIdentifier() == 5) f = new AboutFragment();
                        else return false;

                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment, f).commit();
                        drawer.closeDrawer();
                        return false;
                    }
                }).build();

        Glide.with(this).load(getResources().getString(R.string.drawer_cover)).into(header.getHeaderBackgroundView());

        if (savedInstanceState != null) {
            f = getSupportFragmentManager().findFragmentById(R.id.fragment);
        }

        f = new HomeFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.fragment, f).commit();

        listener = new FragmentManager.OnBackStackChangedListener() {
            @Override
            public void onBackStackChanged() {
                f = getSupportFragmentManager().findFragmentById(R.id.fragment);
                if (drawer.isDrawerOpen()) drawer.closeDrawer();

                getSupportFragmentManager().removeOnBackStackChangedListener(this);
            }
        };

        getSupportFragmentManager().addOnBackStackChangedListener(listener);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        getSupportFragmentManager().addOnBackStackChangedListener(listener);
    }
}
