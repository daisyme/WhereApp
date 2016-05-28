package com.daisynowhere.gohere;

import android.content.Intent;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends ActionBarActivity {
    //��ToolBar��TabLayout��Ϸ���AppBarLayout
    private Toolbar mToolbar;
    //DrawerLayout�е����˵��ؼ�
    private NavigationView mNavigationView;
    //DrawerLayout�ؼ�
    private DrawerLayout mDrawerLayout;
    //Tab�˵��������������tab�л��˵�
    private TabLayout mTabLayout;
    //v4�е�ViewPager�ؼ�
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_main);
        //��ʼ���ؼ�������
        initView();
    }

    private void initView() {
        //MainActivity�Ĳ����ļ��е���Ҫ�ؼ���ʼ��
        mToolbar = (Toolbar) this.findViewById(R.id.tool_bar);
        mDrawerLayout = (DrawerLayout) this.findViewById(R.id.drawer_layout);
        mNavigationView = (NavigationView) this.findViewById(R.id.navigation_view);
        mTabLayout = (TabLayout) this.findViewById(R.id.tab_layout);
        mViewPager = (ViewPager) this.findViewById(R.id.view_pager);

        //��ʼ��ToolBar
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(android.R.drawable.ic_menu_info_details);
        actionBar.setDisplayHomeAsUpEnabled(true);

        //��NavigationView���item�ļ����¼�
        mNavigationView.setNavigationItemSelectedListener(naviListener);
        //����Ӧ��Ĭ�ϴ�DrawerLayout
        mDrawerLayout.openDrawer(mNavigationView);

        //��ʼ��TabLayout��title���ݼ�
        List<String> titles = new ArrayList<>();
        titles.add("List");
        titles.add("Map");
        //��ʼ��TabLayout��title
        mTabLayout.addTab(mTabLayout.newTab().setText(titles.get(0)));
        mTabLayout.addTab(mTabLayout.newTab().setText(titles.get(1)));
        //��ʼ��ViewPager�����ݼ�
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(new ShowListFragment());
        fragments.add(new GPSFragment());
        //����ViewPager��adapter
        FragmentAdapter adapter = new FragmentAdapter(getSupportFragmentManager(), fragments, titles);
        mViewPager.setAdapter(adapter);
        //ǧ������ˣ�����TabLayout��ViewPager
        //ͬʱҲҪ��дPagerAdapter��getPageTitle����������Tabû��title
        mTabLayout.setupWithViewPager(mViewPager);
        mTabLayout.setTabsFromPagerAdapter(adapter);
    }

    private NavigationView.OnNavigationItemSelectedListener naviListener = new NavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(MenuItem menuItem) {
            //���NavigationView�ж����menu itemʱ������Ӧ
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
            //�ر�DrawerLayout�ص�������ѡ�е�tab��fragmentҳ
            mDrawerLayout.closeDrawer(mNavigationView);
            return false;
        }
    };

    private void back(){
        this.finish();
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        //���������Ͻǵ�menu�˵�
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
                //���������Ͻǵ�icon�����Ӧ
                mDrawerLayout.openDrawer(GravityCompat.START);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
