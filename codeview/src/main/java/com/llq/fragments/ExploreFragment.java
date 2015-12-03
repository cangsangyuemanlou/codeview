package com.llq.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.llq.Util.FileUtil;
import com.llq.adapter.BaseFileAdapter;
import com.llq.adapter.ExploreFileAdapter;
import com.llq.codeview.R;
import com.llq.global.GlobalConfig;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ExploreFragment extends BaseFragment {

    private RecyclerView recyclerView;

    private List<File> fileList; //文件列表
    private File nowFileDir; //当前所在文件夹

    private Context context;
    private ExploreFileAdapter exploreFileAdapter;

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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.i("hello", "ExploreFragment onCreateView " + this);
        View view = inflater.inflate(R.layout.fragment_explore, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        context = getActivity();
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        // 初始化数据，显示存储根目录
        if (GlobalConfig.rootFiles == null || GlobalConfig.rootFiles.size() == 0) {
            initData();
        } else {
            fileList = new ArrayList<>(Arrays.asList(new File[GlobalConfig.rootFiles.size()]));
            Collections.copy(fileList, GlobalConfig.rootFiles);
            nowFileDir = GlobalConfig.rootFiles.get(0);
        }

        exploreFileAdapter = new ExploreFileAdapter(context, fileList);
        exploreFileAdapter.setOnItemClickListener(new BaseFileAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                // 单击事件
                File clickFile = fileList.get(position);
                if (clickFile.isDirectory()) {
                    nowFileDir = clickFile;
                    fileList = FileUtil.loadFiles(clickFile);
                    exploreFileAdapter.setFiles(fileList);
                } else {
                    Snackbar.make(view, fileList.get(position).getName(),
                            Snackbar.LENGTH_SHORT).show();
                    onFileClickListener.onFileClick(clickFile);
                }
            }

            @Override
            public void onItemLongClick(View view, int position) {
                onFileClickListener.onFileLongClick(fileList.get(position));
            }
        });

        recyclerView.setAdapter(exploreFileAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("hello", "ExploreFragment onDestroy");
    }

    // 初始化数据，显示存储根目录
    private void initData() {
        fileList = new ArrayList<>();
        Object[] info = FileUtil.getStoragePath(context);
        List<Boolean> basicBool = (List<Boolean>) info[0]; //internal or external
        List<String> basicPaths = (List<String>) info[1]; //基本路径

        // 只有一个存储设备
        if (basicPaths.size() == 1) {
            File file = new File(basicPaths.get(0));
            if (!FileUtil.isEmptyFileDir(file)) {
                fileList.add(file);
                GlobalConfig.rootFiles.add(file);
                // 初始化根目录为rootFiles的上一级目录
                nowFileDir = file.getParentFile();
            }
            return;
        }
        // 多于一个
        // 将内部存储置于fileList第一个
        for (int i = 0; i < basicBool.size(); i++) {
            if (!basicBool.get(i)) { // internal
                File file = new File(basicPaths.get(i));
                if (!FileUtil.isEmptyFileDir(file)) {
                    fileList.add(file);
                    GlobalConfig.rootFiles.add(file);
                    // 初始化根目录为rootFiles的上一级目录
                    nowFileDir = file.getParentFile();
                }
            }
        }
        for (int i = 0; i < basicBool.size(); i++) {
            if (basicBool.get(i)) { // external
                File file = new File(basicPaths.get(i));
                if (!FileUtil.isEmptyFileDir(file)) {
                    fileList.add(file);
                    GlobalConfig.rootFiles.add(file);
                }
            }
        }
    }

    @Override
    public boolean onBackPressed() {
        File parent = nowFileDir.getParentFile();

        // nowFile已经到达根目录
        if (GlobalConfig.rootFiles.indexOf(nowFileDir) != -1) {
            fileList = GlobalConfig.rootFiles;
            exploreFileAdapter.setFiles(fileList);
            nowFileDir = parent;
            return true;
        }

        File rootParent = GlobalConfig.rootFiles.get(0).getParentFile();
        if (nowFileDir.getAbsolutePath().equals(rootParent.getAbsolutePath())) {
            return false;
        } else {
            nowFileDir = parent;
            fileList = FileUtil.loadFiles(parent);
            exploreFileAdapter.setFiles(fileList);
            return true;
        }
    }
}
