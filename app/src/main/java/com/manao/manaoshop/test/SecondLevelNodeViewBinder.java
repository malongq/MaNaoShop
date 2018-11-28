package com.manao.manaoshop.test;

import android.view.View;
import android.widget.TextView;

import com.manao.manaoshop.R;

import me.texy.treeview.TreeNode;
import me.texy.treeview.base.BaseNodeViewBinder;

/**
 * Created by Malong
 * on 18/11/15.
 */
class SecondLevelNodeViewBinder extends BaseNodeViewBinder {
    private final TextView test_tv;

    public SecondLevelNodeViewBinder(View itemView) {
        super(itemView);
        test_tv = itemView.findViewById(R.id.test_tv);
    }

    @Override
    public int getLayoutId() {
        return R.layout.test_item;
    }

    @Override
    public void bindView(TreeNode treeNode) {
        test_tv.setText(treeNode.getValue().toString());
    }
}
