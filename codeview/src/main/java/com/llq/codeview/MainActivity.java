package com.llq.codeview;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.llq.adapter.FragmentAdapter;
import com.llq.fragments.BaseFragment;
import com.llq.fragments.ExploreFragment;
import com.llq.fragments.LatestFragment;
import com.llq.fragments.ProjectFragment;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    // 当前显示的Fragment的引用
    private BaseFragment currentFragment;

    // 记录按下返回键的时间
    private long exitTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();

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

        List<Fragment> fragments = new ArrayList<>();
        fragments.add(new LatestFragment());
        fragments.add(new ExploreFragment());
        fragments.add(new ProjectFragment());
        FragmentAdapter adapter = new FragmentAdapter(getSupportFragmentManager(),
                fragments, titles);
        viewPager.setAdapter(adapter);

        tabLayout.setTabsFromPagerAdapter(adapter);
        tabLayout.setupWithViewPager(viewPager);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                String fragTag = "android:switcher:" + R.id.view_pager + ":" + position;
                currentFragment = (BaseFragment) getSupportFragmentManager().findFragmentByTag(fragTag);
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
            if (currentTime - exitTime > 2 *1000) {
                // 两次连按时间差大于2s，可能是误按，提示
                Snackbar.make(viewPager, "再按一次退出", Snackbar.LENGTH_LONG).show();
                exitTime = System.currentTimeMillis();
            } else {
                // 两次连按，退出程序
                super.onBackPressed();
            }
        }
    }
}
