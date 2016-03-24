package com.brice.comet;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;

public class Launchers extends AppCompatActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_items_recycler);
        setTitle("Apply");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        new com.brice.comet.Drawer().make(Launchers.this, savedInstanceState).build();

        RecyclerView rv = (RecyclerView) findViewById(R.id.rv);
        rv.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL));
        rv.setAdapter(new LaunchersAdapter(Launchers.this));
        rv.addItemDecoration(new DividerItemDecoration(Launchers.this));
    }
}
