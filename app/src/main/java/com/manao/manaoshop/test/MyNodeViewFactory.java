package com.manao.manaoshop.test;

import android.view.View;

import me.texy.treeview.base.BaseNodeViewBinder;
import me.texy.treeview.base.BaseNodeViewFactory;

/**
 * Created by Malong
 * on 18/11/15.
 */
public class MyNodeViewFactory extends BaseNodeViewFactory {
    @Override
    public BaseNodeViewBinder getNodeViewBinder(View view, int level) {

        switch (level) {
            case 0:
                return new FirstLevelNodeViewBinder(view);
            case 1:
                return new SecondLevelNodeViewBinder(view);
            default:
                return null;
        }
    }
}
