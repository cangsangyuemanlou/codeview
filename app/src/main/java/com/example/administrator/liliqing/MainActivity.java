package com.example.administrator.liliqing;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.llq.fragments.AgendaFragment;
import com.llq.fragments.InfoDetailsFragment;
import com.llq.fragments.ShareFragment;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = "llq";

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
    }

    private void initView() {
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.navigation_view);
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        viewPager = (ViewPager) findViewById(R.id.view_pager);

        //对NavigationView添加item点击事件
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {

                switch (menuItem.getItemId()) {
                    case R.id.menu_info_details:
                        viewPager.setCurrentItem(0);
                        break;
                    case R.id.menu_share:
                        viewPager.setCurrentItem(1);
                        break;
                    case R.id.menu_agenda:
                        viewPager.setCurrentItem(2);
                        break;
                    case R.id.views:
                        startActivity(new Intent(MainActivity.this, ViewsActivity.class));
                        break;
                }
                drawerLayout.closeDrawer(navigationView);
                return false;
            }
        });
        // 开启应用是默认打开DrawerLayout
        drawerLayout.openDrawer(navigationView);

        //初始化Toolbar
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setHomeAsUpIndicator(android.R.drawable.ic_dialog_dialer);
        actionBar.setDisplayHomeAsUpEnabled(true);
//        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
//            @Override
//            public boolean onMenuItemClick(MenuItem item) {
//
//                Log.i(TAG, "item: " + item.getItemId());
//
//                switch (item.getItemId()) {
//                    case R.id.menu_info_details2:
//                        Log.i(TAG, "0");
////                        viewPager.setCurrentItem(0);
//                        break;
//                    case R.id.menu_share2:
//                        Log.i(TAG, "1");
////                        viewPager.setCurrentItem(1);
//                        break;
//                    case R.id.menu_agenda2:
//                        Log.i(TAG, "2");
////                        viewPager.setCurrentItem(2);
//                        break;
//                    case android.R.id.home:
//                        drawerLayout.openDrawer(GravityCompat.START);
//                        break;
//                }
//                return true;
//            }
//        });

        //添加TabLayout
        List<String> titles = new ArrayList<>();
        titles.add("details");
        titles.add("share");
        titles.add("details");
        tabLayout.addTab(tabLayout.newTab().setText(titles.get(0)));
        tabLayout.addTab(tabLayout.newTab().setText(titles.get(1)));
        tabLayout.addTab(tabLayout.newTab().setText(titles.get(2)));

        //初始化ViewPager的数据集
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(new InfoDetailsFragment());
        fragments.add(new ShareFragment());
        fragments.add(new AgendaFragment());
        //创建ViewPager的adapter
        FragmentAdapter adapter = new FragmentAdapter(getSupportFragmentManager(),
                fragments, titles);
        viewPager.setAdapter(adapter);

        //关联TabLayout和ViewPager
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setTabsFromPagerAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menu_info_details2:
                viewPager.setCurrentItem(0);
                break;
            case R.id.menu_share2:
                viewPager.setCurrentItem(1);
                break;
            case R.id.menu_agenda2:
                viewPager.setCurrentItem(2);
                break;
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                break;
        }
        return super.onOptionsItemSelected(item);
    }


}
