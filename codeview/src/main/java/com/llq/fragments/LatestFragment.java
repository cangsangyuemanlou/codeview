package com.llq.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.llq.adapter.BaseFileAdapter;
import com.llq.adapter.LatestFileAdapter;
import com.llq.codeview.R;
import com.llq.global.GlobalConfig;

import java.io.File;
import java.util.List;


public class LatestFragment extends BaseFragment {

    private Context context;
    private RecyclerView recyclerView;
    private LatestFileAdapter latestFileAdapter;
    private List<File> fileList;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof OnFileClickListener) {
            onFileClickListener = (OnFileClickListener) activity;
        } else {
            throw new ClassCastException(activity.toString() + "必须实现" +
                    OnFileClickListener.class);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.i("hello", "LatestFragment onCreateView " + this);
        View view = inflater.inflate(R.layout.fragment_latest, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_latest);
        context = getActivity();
        initData();
        latestFileAdapter = new LatestFileAdapter(context, fileList);
        latestFileAdapter.setOnItemClickListener(new BaseFileAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                File clickFile = fileList.get(position);
                onFileClickListener.onFileClick(clickFile);
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        });
        recyclerView.setAdapter(latestFileAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("hello", "LatestFragment onDestroy");
    }

    public void update(){
        fileList = GlobalConfig.latestQueue.toList();
        // 判断空
        if (this.latestFileAdapter != null) {
            this.latestFileAdapter.setFiles(fileList);
        }
    }


    private void initData() {
        fileList = GlobalConfig.latestQueue.toList();
    }

}
