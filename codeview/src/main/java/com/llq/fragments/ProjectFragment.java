package com.llq.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.llq.Util.FileUtil;
import com.llq.adapter.BaseFileAdapter;
import com.llq.adapter.MultipleItemAdapter;
import com.llq.codeview.R;
import com.llq.global.GlobalConfig;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.regex.Pattern;


public class ProjectFragment extends BaseFragment {

    private File projectRootDir;

    private Map<File, List<File>> dirFileMap;

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
        Log.i("hello", "ProjectFragment onCreateView " + this);
        View view = inflater.inflate(R.layout.fragment_project, container, false);

        dirFileMap = new TreeMap<>();
        projectRootDir = GlobalConfig.projectRootDir;
        if (!FileUtil.isEmptyFileDir(projectRootDir)) {
            loadDirFileMap(projectRootDir);
        }

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_project);
        MultipleItemAdapter multipleItemAdapter = new MultipleItemAdapter(getActivity(), dirFileMap);
        multipleItemAdapter.setOnItemClickListener(new BaseFileAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                int count = -1;
                File clickFile = null;
                for (File parentFile : dirFileMap.keySet()) {
                    count++;
                    for (File file : dirFileMap.get(parentFile)) {
                        count++;
                        if (count == position) {
                            clickFile = file;
                            break;
                        }
                    }
                }
                if (onFileClickListener != null) {
                    onFileClickListener.onFileClick(clickFile);
                }
            }

            @Override
            public void onItemLongClick(View view, int position) {

            }
        });
        recyclerView.setAdapter(multipleItemAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("hello", "ProjectFragment onDestroy");
    }

    // 递归
    private void loadDirFileMap(File dir) {
        for (File file : FileUtil.loadFiles(dir)) {
            if (file.isDirectory()) {
                loadDirFileMap(file); // 进入递归
            } else if (file.isFile() && isAllowedFile(file)) {
                File parentFile = file.getParentFile();
                if (dirFileMap.get(parentFile) == null) {
                    List<File> list = new ArrayList<>();
                    list.add(file);
                    dirFileMap.put(parentFile, list);
                } else {
                    dirFileMap.get(parentFile).add(file);
                }
            }
        }
    }

    private boolean isAllowedFile(File file) {
        Pattern pattern = Pattern.compile(GlobalConfig.allowedFileRegex,
                Pattern.CASE_INSENSITIVE);
        return pattern.matcher(file.getAbsolutePath()).matches();
    }

    public void setProjectRootDir(File projectRootDir) {
        this.projectRootDir = projectRootDir;
        GlobalConfig.projectRootDir = projectRootDir;
    }
}
