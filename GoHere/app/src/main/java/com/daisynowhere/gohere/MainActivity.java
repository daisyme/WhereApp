package com.daisynowhere.gohere;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends ActionBarActivity {

    private Toolbar mToolbar;

    private NavigationView mNavigationView;

    private DrawerLayout mDrawerLayout;

    private TabLayout mTabLayout;

    private ViewPager mViewPager;

    private int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_main);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        id = bundle.getInt("id");

        initView();
    }

    private void initView() {

        mToolbar = (Toolbar) this.findViewById(R.id.tool_bar);
        mDrawerLayout = (DrawerLayout) this.findViewById(R.id.drawer_layout);
        mNavigationView = (NavigationView) this.findViewById(R.id.navigation_view);
        mTabLayout = (TabLayout) this.findViewById(R.id.tab_layout);
        mViewPager = (ViewPager) this.findViewById(R.id.view_pager);


        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(android.R.drawable.ic_menu_info_details);
        actionBar.setDisplayHomeAsUpEnabled(true);


        mNavigationView.setNavigationItemSelectedListener(naviListener);

        mDrawerLayout.openDrawer(mNavigationView);


        List<String> titles = new ArrayList<>();
        titles.add("List");
        titles.add("Map");

        mTabLayout.addTab(mTabLayout.newTab().setText(titles.get(0)));
        mTabLayout.addTab(mTabLayout.newTab().setText(titles.get(1)));

        List<Fragment> fragments = new ArrayList<>();
        fragments.add(new ShowListFragment());
        fragments.add(new GPSFragment());

        FragmentAdapter adapter = new FragmentAdapter(getSupportFragmentManager(), fragments, titles);
        mViewPager.setAdapter(adapter);

        mTabLayout.setupWithViewPager(mViewPager);
        mTabLayout.setTabsFromPagerAdapter(adapter);
    }

    private NavigationView.OnNavigationItemSelectedListener naviListener = new NavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(MenuItem menuItem) {

            Intent intent;
            switch (menuItem.getItemId()) {
                case R.id.basic:
                    intent = new Intent();
                    intent.setClass(MainActivity.this,UserInfo.class);
                    startActivity(intent);
                    break;
                case R.id.history:
                    intent = new Intent();
                    intent.setClass(MainActivity.this,History.class);
                    startActivity(intent);
                    break;
                case R.id.togo:
                    intent = new Intent();
                    intent.setClass(MainActivity.this,ToGo.class);
                    startActivity(intent);
                    break;
                case R.id.like:
                    intent = new Intent();
                    intent.setClass(MainActivity.this,Like.class);
                    startActivity(intent);
                    break;
                case R.id.settings:
                    intent = new Intent();
                    intent.setClass(MainActivity.this,UserInfoEdit.class);
                    startActivity(intent);
                    break;
                case R.id.logout:
                    back();
                    break;
            }

            mDrawerLayout.closeDrawer(mNavigationView);
            return false;
        }
    };

    private void back(){
        this.finish();
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {

//        getMenuInflater().inflate(R.menu.menu_main, menu);
//        return true;
//    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
//            case R.id.menu_info_details:
//                mViewPager.setCurrentItem(0);
//                break;
//            case R.id.menu_share:
//                mViewPager.setCurrentItem(1);
//                break;
//            case R.id.menu_agenda:
//                mViewPager.setCurrentItem(2);
//                break;
            case android.R.id.home:

                mDrawerLayout.openDrawer(GravityCompat.START);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
