package com.shrikanthravi.customnavigationdrawer;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import com.shrikanthravi.customnavigationdrawer2.data.MenuItem;
import com.shrikanthravi.customnavigationdrawer2.widget.SNavigationDrawer;

import java.util.ArrayList;
import java.util.List;

public class Main2Activity extends AppCompatActivity {

    SNavigationDrawer sNavigationDrawer;
    int color1 = 0;
    Class<? extends Fragment> fragmentClass;
    public static Fragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        //Set up SNavigationDrawer
        initNavigation();

    }


    private void initNavigation() {
        sNavigationDrawer = findViewById(R.id.navigationDrawer);
        List<MenuItem> menuItems = new ArrayList<>();
        menuItems.add(new MenuItem("News", R.drawable.news_bg));
        menuItems.add(new MenuItem("Feed", R.drawable.feed_bg));
        menuItems.add(new MenuItem("Messages", R.drawable.message_bg));
        menuItems.add(new MenuItem("Music", R.drawable.music_bg));

        //Set which item should be selected
        //This method must be called before  setMenuItemList(menuItems)
        sNavigationDrawer.setCurrentPos(2);

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
                sNavigationDrawer.setDrawerListener(new SNavigationDrawer.DrawerListener() {

                    @Override
                    public void onDrawerOpened() {

                    }

                    @Override
                    public void onDrawerOpening() {

                    }

                    @Override
                    public void onDrawerClosing() {

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
        });
    }

    public void onGoButtonClick(View view) {
        startActivity(new Intent(Main2Activity.this, CustomToolBarActivity.class));
        finish();
    }
}
