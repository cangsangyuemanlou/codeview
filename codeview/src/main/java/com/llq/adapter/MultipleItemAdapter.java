package com.llq.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.llq.codeview.R;
import com.llq.global.GlobalConfig;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MultipleItemAdapter extends BaseFileAdapter<RecyclerView.ViewHolder> {

    public enum ITEM_TYPE {
        HEADER, FILE
    }

    private Context context;
    private Map<File, List<File>> dirFileMap;
    private LayoutInflater inflater;

    //按顺序记录dirFileMap中所有的文件夹和文件
    private List<File> fileList;

    public MultipleItemAdapter(Context context, Map<File, List<File>> dirFileMap) {
        this.context = context;
        this.dirFileMap = dirFileMap;
        inflater = LayoutInflater.from(context);
        initFileList();
    }

    //将dirFileMap中的内容记录到fileList中
    private void initFileList() {
        fileList = new ArrayList<>();
        for (File parentFile : dirFileMap.keySet()) {
            fileList.add(parentFile);
            for (File file : dirFileMap.get(parentFile)) {
                fileList.add(file);
            }
        }
    }

    public void setDirFileMap(Map<File, List<File>> dirFileMap) {
        this.dirFileMap = dirFileMap;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == ITEM_TYPE.HEADER.ordinal()) {
            View itemView = inflater.inflate(R.layout.item_header_layout, parent, false);
            return new HeaderViewHolder(itemView);
        } else if (viewType == ITEM_TYPE.FILE.ordinal()) {
            View itemView = inflater.inflate(R.layout.item_file_layout, parent, false);
            return new FileViewHolder(itemView);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof HeaderViewHolder) {
            String headerInfo = fileList.get(position).getAbsolutePath();
            // 截取字符串从projectName开始
            String projectName = GlobalConfig.projectRootDir.getName();
            int idx = headerInfo.indexOf(projectName);
            String newHeaderInfo = headerInfo.substring(idx - 1);
            ((HeaderViewHolder) holder).tvHeader.setText(newHeaderInfo);
        } else if (holder instanceof FileViewHolder) {
            File file = fileList.get(position);
            final FileViewHolder fileViewHolder = (FileViewHolder) holder;
            String fileName = file.getName();
            String type = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
            int resId = context.getResources().getIdentifier(type, "mipmap",
                    context.getPackageName());
            if (resId != 0) {
                fileViewHolder.ivFileImage.setImageResource(resId);
            } else {
                fileViewHolder.ivFileImage.setImageResource(R.mipmap.default_fileicon);
            }
            fileViewHolder.tvFileName.setText(file.getName());
            if (onItemClickListener != null) {
                fileViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int pos = fileViewHolder.getLayoutPosition();
                        onItemClickListener.onItemClick(fileViewHolder.itemView, pos);
                    }
                });
                fileViewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        int pos = fileViewHolder.getLayoutPosition();
                        onItemClickListener.onItemLongClick(fileViewHolder.itemView, pos);
                        return false;
                    }
                });
            }
        }
    }

    @Override
    public int getItemCount() {
        return fileList.size();
    }

    @Override
    public int getItemViewType(int position) {
        // 判断第position个元素是什么type
        File tempFile = fileList.get(position);
        if (tempFile.isDirectory()) {
            return ITEM_TYPE.HEADER.ordinal();
        } else if (tempFile.isFile()) {
            return ITEM_TYPE.FILE.ordinal();
        }
        return ITEM_TYPE.FILE.ordinal();
    }

    public class HeaderViewHolder extends RecyclerView.ViewHolder {

        TextView tvHeader;

        public HeaderViewHolder(View itemView) {
            super(itemView);
            tvHeader = (TextView) itemView.findViewById(R.id.item_header_info);
        }
    }

    public class FileViewHolder extends RecyclerView.ViewHolder {
        View itemView; // item的整体布局视图
        ImageView ivFileImage;
        TextView tvFileName;

        public FileViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            ivFileImage = (ImageView) itemView.findViewById(R.id.item_image_view);
            tvFileName = (TextView) itemView.findViewById(R.id.item_file_name);
        }
    }
}
