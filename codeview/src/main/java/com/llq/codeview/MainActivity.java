package com.llq.codeview;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.llq.adapter.FragmentAdapter;
import com.llq.fragments.BaseFragment;
import com.llq.fragments.ExploreFragment;
import com.llq.fragments.LatestFragment;
import com.llq.fragments.ProjectFragment;
import com.llq.global.GlobalConfig;
import com.llq.global.SharedPreferencesHelper;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements BaseFragment.OnFileClickListener {

    public static final int FRAGMENT_LATEST = 0;
    public static final int FRAGMENT_EXPLORE = 1;
    public static final int FRAGMENT_PROJECT = 2;

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    // 管理的Fragment
    private List<Fragment> fragments;
    // Fragment适配器
    private FragmentAdapter fragmentAdapter;

    // 当前显示的Fragment的引用
    private BaseFragment currentFragment;

    // 记录按下返回键的时间
    private long exitTime = 0;

    public SharedPreferencesHelper spHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i("hello", "MainActivity onCreate");
        setContentView(R.layout.activity_main);
        fragments = new ArrayList<>();
        spHelper = new SharedPreferencesHelper(MainActivity.this);

        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        spHelper.restoreGlobalConfig();
    }

    @Override
    protected void onPause() {
        super.onPause();
        spHelper.saveGlobalConfig();
    }

    private void initView() {
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        viewPager = (ViewPager) findViewById(R.id.view_pager);

        setSupportActionBar(toolbar);

        List<String> titles = new ArrayList<>();
        titles.add(getString(R.string.latest));
        titles.add(getString(R.string.explore));
        titles.add(getString(R.string.project));

        fragments.add(new LatestFragment());
        fragments.add(new ExploreFragment());
        fragments.add(new ProjectFragment());
        fragmentAdapter = new FragmentAdapter(getSupportFragmentManager(),
                fragments, titles);
        viewPager.setAdapter(fragmentAdapter);

        tabLayout.setTabsFromPagerAdapter(fragmentAdapter);
        tabLayout.setupWithViewPager(viewPager);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset,
                                       int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                String fragTag = "android:switcher:" + R.id.view_pager + ":" + position;
                currentFragment = (BaseFragment) getSupportFragmentManager()
                        .findFragmentByTag(fragTag);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (currentFragment == null || !currentFragment.onBackPressed()) {
            // 如果当前没有Fragment或者当前Fragment已经处理了事件
            long currentTime = System.currentTimeMillis();
            if (currentTime - exitTime > 2 * 1000) {
                // 两次连按时间差大于2s，可能是误按，提示
                Snackbar.make(viewPager, "再按一次退出", Snackbar.LENGTH_LONG).show();
                exitTime = System.currentTimeMillis();
            } else {
                // 两次连按，退出程序
                super.onBackPressed();
            }
        }
    }

    @Override
    public void onFileClick(File file) {
        // 更新最近文件列表
        GlobalConfig.latestQueue.addItem(file);
        ((LatestFragment) getFragmentByIdx(MainActivity.FRAGMENT_LATEST)).update();

        // TODO: 2015/11/15 跳转到代码阅读activity
        Intent intent = new Intent(MainActivity.this, CodeViewActivity.class);
        intent.putExtra(CodeViewActivity.SOURCE_FILE_PATH, file.getAbsolutePath());
        startActivity(intent);
    }

    @Override
    public void onFileLongClick(File file) {
        ProjectFragment projectFragment = (ProjectFragment)
                getFragmentByIdx(MainActivity.FRAGMENT_PROJECT);
        projectFragment.setProjectRootDir(file);
        GlobalConfig.projectRootDir = file;
        fragmentAdapter.notifyDataSetChanged();
    }

    private Fragment getFragmentByIdx(int idx) {
        String fragTag = "android:switcher:" + R.id.view_pager + ":" + idx;
        return getSupportFragmentManager().findFragmentByTag(fragTag);
    }

}
