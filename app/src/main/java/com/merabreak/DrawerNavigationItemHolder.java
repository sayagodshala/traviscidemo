package com.merabreak;

import android.content.Context;
import android.view.View;

import com.unnamed.b.atv.model.TreeNode;

public class DrawerNavigationItemHolder extends TreeNode.BaseNodeViewHolder<NavigationItem> {
    public DrawerNavigationItemHolder(Context context) {
        super(context);
    }

    @Override
    public View createNodeView(TreeNode treeNode, NavigationItem navigationItem) {
        return null;
    }
}
