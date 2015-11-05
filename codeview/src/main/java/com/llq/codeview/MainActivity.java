package com.llq.codeview;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.llq.adapter.FragmentAdapter;
import com.llq.fragments.ExploreFragment;
import com.llq.fragments.LatestFragment;
import com.llq.fragments.ProjectFragment;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

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

    }
}
