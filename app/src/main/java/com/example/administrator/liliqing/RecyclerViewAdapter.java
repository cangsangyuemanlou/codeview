package com.example.administrator.liliqing;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Administrator on 2015/8/4.
 */
public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private int[] colors = {R.color.color_0, R.color.color_1, R.color.color_2,
            R.color.color_3,R.color.color_4, R.color.color_5, R.color.color_6,
            R.color.color_7,R.color.color_8, R.color.color_9};

    private Context context;

    public RecyclerViewAdapter(Context context) {
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int i) {

        View itemView;
        itemView = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.list_item, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, final int position) {

        ViewHolder holder = (ViewHolder) viewHolder;
        holder.textView.setBackgroundColor(context.getResources().getColor(
                colors[position%(colors.length)]
        ));
        holder.textView.setText("Position " + position);
        holder.textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, SubActivity.class));
            }
        });

    }

    @Override
    public int getItemCount() {
        return colors.length * 2;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView imageView;
        public TextView textView;

        public ViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.list_item_image_view);
            textView = (TextView) itemView.findViewById(R.id.list_item_text_view);
        }
    }
}
