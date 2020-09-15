package com.shrikanthravi.customnavigationdrawer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;

import com.google.android.material.appbar.MaterialToolbar;
import com.shrikanthravi.customnavigationdrawer2.data.MenuItem;
import com.shrikanthravi.customnavigationdrawer2.widget.SNavigationDrawer;

import java.util.ArrayList;
import java.util.List;

public class CustomToolBarActivity extends AppCompatActivity {
    private MaterialToolbar toolbar;
    private ImageView navImageView;

    SNavigationDrawer sNavigationDrawer;
    int color1 = 0;
    Class<? extends Fragment> fragmentClass;
    public static Fragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_tool_bar);

        // Set up the toolbar
        initToolbar();

        //Set up SNavigationDrawer
        initNavigation();


    }

    private void initToolbar() {
        navImageView = findViewById(R.id.nav_image_view);

        toolbar = findViewById(R.id.main_app_bar);
        this.setSupportActionBar(toolbar);
    }

    private void initNavigation() {
        sNavigationDrawer = findViewById(R.id.navigationDrawer);
        final List<MenuItem> menuItems = new ArrayList<>();
        menuItems.add(new MenuItem("News", R.drawable.news_bg));
        menuItems.add(new MenuItem("Feed", R.drawable.feed_bg));
        menuItems.add(new MenuItem("Messages", R.drawable.message_bg));
        menuItems.add(new MenuItem("Music", R.drawable.music_bg));

        //Setting menu list.
        //Exception throws when list is empty
        try {
            sNavigationDrawer.setMenuItemList(menuItems);
        } catch (Exception e) {
            e.printStackTrace();
        }

        fragmentClass = NewsFragment.class;

        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out).replace(R.id.frameLayout, fragment).commit();
            toolbar.setTitle(menuItems.get(0).getTitle());
        }


        sNavigationDrawer.setOnMenuItemClickListener(new SNavigationDrawer.OnMenuItemClickListener() {
            @Override
            public void onMenuItemClicked(int position) {
                System.out.println("Position " + position);

                switch (position) {
                    case 0: {
                        color1 = R.color.red;
                        fragmentClass = NewsFragment.class;
                        break;
                    }
                    case 1: {
                        color1 = R.color.orange;
                        fragmentClass = FeedFragment.class;
                        break;
                    }
                    case 2: {
                        color1 = R.color.green;
                        fragmentClass = MessagesFragment.class;
                        break;
                    }
                    case 3: {
                        color1 = R.color.blue;
                        fragmentClass = MusicFragment.class;
                        break;
                    }

                }

                toolbar.setTitle(menuItems.get(position).getTitle());

            }
        });

        sNavigationDrawer.setDrawerListener(new SNavigationDrawer.DrawerListener() {

            @Override
            public void onDrawerOpened() {

            }

            @Override
            public void onDrawerOpening() {
                Log.d("tag", "onDrawerOpening: ");
                final int[] stateSet = {android.R.attr.state_checked * (sNavigationDrawer.isDrawerOpen() ? -1 : 1)};
                navImageView.setImageState(stateSet, true);
            }

            @Override
            public void onDrawerClosing() {

                final int[] stateSet = {android.R.attr.state_checked * (sNavigationDrawer.isDrawerOpen() ? -1 : 1)};
                navImageView.setImageState(stateSet, true);


                try {
                    fragment = (Fragment) fragmentClass.newInstance();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (fragment != null) {
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    fragmentManager.beginTransaction()
                            .setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out)
                            .replace(R.id.frameLayout, fragment)
                            .commit();


                }
            }

            @Override
            public void onDrawerClosed() {

            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });
    }

    public void onGoButtonClick(View view) {
        startActivity(new Intent(CustomToolBarActivity.this, Main2Activity.class));
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.custom_toolbar_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }
}