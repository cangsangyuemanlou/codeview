package com.llq.fragments;

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
import com.llq.adapter.FileAdapter;
import com.llq.codeview.R;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ExploreFragment extends BaseFragment {

    private RecyclerView recyclerView;

    private List<File> fileList; //文件列表
    private File nowFileDir; //当前所在文件夹

    private Context context;
    private FileAdapter adapter;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_explore, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        context = getActivity();
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initData();

        adapter = new FileAdapter(context, fileList);
        adapter.setOnItemClickListener(new FileAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                // 单击事件
                File clickFile = fileList.get(position);
                if (clickFile.isDirectory()) {
                    fileList = loadFiles(clickFile);
                    adapter.setFiles(fileList);
                } else {
                    Snackbar.make(view, fileList.get(position).getName(), Snackbar.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        });

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));

    }


    // 加载文件夹dir下的所有文件
    private List<File> loadFiles(File dir) {
        nowFileDir = dir;
        return Arrays.asList(dir.listFiles());
    }


    // 初始化数据，显示存储根目录
    private void initData() {
        Object[] info = FileUtil.getStoragePath(context);
        List<Boolean> basicBool = (List<Boolean>) info[0]; //internal or external
        List<String> basicPaths = (List<String>) info[1]; //基本路径
        fileList = new ArrayList<>();
        for (int i = 0; i < basicBool.size(); i++) {
            if (!FileUtil.isEmptyFileDir(basicPaths.get(i))){
                if (!basicBool.get(i)){
                    fileList.add(new File(basicPaths.get(i)));
                } else {
                    fileList.add(new File(basicPaths.get(i)));
                }
            }
        }
    }


    private void log(String log) {
        Log.i("hello", log);
    }

    @Override
    public boolean onBackPressed() {
        Snackbar.make(getView(), "hello ExploreFragment", Snackbar.LENGTH_LONG).show();
        return true;
    }
}
