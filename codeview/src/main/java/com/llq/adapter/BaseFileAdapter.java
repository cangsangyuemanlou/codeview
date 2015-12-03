package com.llq.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;

public abstract class BaseFileAdapter<VH extends RecyclerView.ViewHolder>
        extends RecyclerView.Adapter<VH>{

    //设置点击事件监听接口
    public interface OnItemClickListener {
        void onItemClick(View view, int position);

        void onItemLongClick(View view, int position);
    }

    // 点击事件监听对象
    protected OnItemClickListener onItemClickListener;

    // 设置事件监听器
    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

}
