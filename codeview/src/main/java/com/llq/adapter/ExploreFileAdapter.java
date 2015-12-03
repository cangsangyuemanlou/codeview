package com.llq.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.llq.codeview.R;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.List;

public class ExploreFileAdapter extends BaseFileAdapter<ExploreFileAdapter.MyViewHolder> {

    private Context context;
    private List<File> files;

    private LayoutInflater inflater;

    public ExploreFileAdapter(Context context, List<File> files) {
        this.context = context;
        this.files = files;
        inflater = LayoutInflater.from(context);
    }

    public void setFiles(List<File> files) {
        this.files = files;
        notifyDataSetChanged();
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(R.layout.item_file_layout, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        File file = files.get(position);
        String fileName = file.getName();
        if (file.isDirectory()) {
            holder.ivFileImage.setImageResource(R.mipmap.folder);
        } else if (file.isFile()) {
            String type = fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
            int resId = context.getResources().getIdentifier(type, "mipmap",
                    context.getPackageName());
            if (resId != 0) {
                holder.ivFileImage.setImageResource(resId);
            } else {
                holder.ivFileImage.setImageResource(R.mipmap.default_fileicon);
            }
        }
        holder.tvFileName.setText(file.getName());
        holder.tvFileTime.setText(new SimpleDateFormat("yyyy-MM-dd HH:mm")
                .format(file.lastModified()));
        // 如果定义了点击事件，则点击时启动相应的事件
        if (onItemClickListener != null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = holder.getLayoutPosition();
                    onItemClickListener.onItemClick(holder.itemView, pos);
                }
            });
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    int pos = holder.getLayoutPosition();
                    onItemClickListener.onItemLongClick(holder.itemView, pos);
                    return false;
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return files.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        View itemView; // item的整体布局视图
        ImageView ivFileImage;
        TextView tvFileName;
        TextView tvFileTime;

        public MyViewHolder(View itemView) {
            super(itemView);
            this.itemView = itemView;
            ivFileImage = (ImageView) itemView.findViewById(R.id.item_image_view);
            tvFileName = (TextView) itemView.findViewById(R.id.item_file_name);
            tvFileTime = (TextView) itemView.findViewById(R.id.item_file_time);
        }
    }
}
