package com.example.administrator.liliqing;

import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

public class SubActivity extends AppCompatActivity {

    private Toolbar toolbar;

    private CollapsingToolbarLayout collapsingToolbarLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sub);

        toolbar = (Toolbar) findViewById(R.id.sub_tool_bar);
        toolbar.setLogo(R.drawable.ic_launcher);
        toolbar.setTitle("Title");
        toolbar.setBackgroundColor(getResources().getColor(R.color.color_0));
        setSupportActionBar(toolbar);

        collapsingToolbarLayout = (CollapsingToolbarLayout)
                findViewById(R.id.sub_collapsing_toolbar);
        collapsingToolbarLayout.setTitle("详情界面");
    }


}
