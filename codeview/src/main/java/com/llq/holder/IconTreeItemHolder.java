package com.llq.holder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.github.johnkil.print.PrintView;
import com.llq.codeview.R;
import com.unnamed.b.atv.model.TreeNode;

import java.io.File;

public class IconTreeItemHolder extends TreeNode.BaseNodeViewHolder<IconTreeItemHolder.IconFileTreeItem> {

    private PrintView pvArrowView;
    private PrintView pvNodeIcon;
    private TextView tvNodeValue;

    public IconTreeItemHolder(Context context) {
        super(context);
    }

    @Override
    public View createNodeView(TreeNode node, IconFileTreeItem value) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_tree_node_layout, null, false);

        pvArrowView = (PrintView) view.findViewById(R.id.node_arrow_icon);
        pvNodeIcon = (PrintView) view.findViewById(R.id.node_item_icon);
        tvNodeValue = (TextView) view.findViewById(R.id.node_item_value);

        tvNodeValue.setText(value.file.getName());
        pvNodeIcon.setIconText(context.getResources().getString(value.icon));
        // 如果是文件，就不显示前面的箭头
        if (value.file.isFile()) {
            pvArrowView.setVisibility(View.INVISIBLE);
        }
        return view;
    }

    @Override
    public void toggle(boolean active) {
        int icon = active ? R.string.ic_keyboard_arrow_down : R.string.ic_keyboard_arrow_right;
        pvArrowView.setIconText(context.getResources().getString(icon));
    }

    public static class IconFileTreeItem {
        int icon;
        File file;

        public IconFileTreeItem(int icon, File file) {
            this.icon = icon;
            this.file = file;
        }

        public File getFile() {
            return file;
        }
    }
}
