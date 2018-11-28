package com.manao.manaoshop.test;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.manao.manaoshop.R;

import me.texy.treeview.TreeNode;
import me.texy.treeview.TreeView;

/**
 * Created by Malong
 * on 18/11/5.
 * 测试toolbar页面
 */
public class TestActivity extends AppCompatActivity {

    private RelativeLayout tree_view;
    private TreeNode root;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        tree_view = findViewById(R.id.tree_view);
        initData();
    }

    private void initData() {
        root = TreeNode.root();
        buildTree();
        TreeView treeView = new TreeView(root, this, new MyNodeViewFactory());
        treeView.expandAll();
        View view = treeView.getView();
        view.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        tree_view.addView(view);
    }

    private void buildTree() {
        for (int i = 0; i < 5; i++) {
            String s = new String("马龙车水马龙  " + i);
            TreeNode treeNode = new TreeNode(s);
            treeNode.setLevel(0);
            for (int j = 0; j < 2; j++) {
                TreeNode treeNode1 = new TreeNode(new String("珍珠玛瑙 " + j));
                treeNode1.setLevel(1);
                treeNode.addChild(treeNode1);
            }
            root.addChild(treeNode);
        }
    }

}
