package com.manao.manaoshop.base.baseadapter;

import android.content.Context;
import java.util.List;

/**
 * Created by Malong
 * on 18/11/16.
 */
public abstract class SimpleAdapter<T> extends BaseAdapter<T, BaseViewHolder> {

    public SimpleAdapter(Context context, int layoutResId) {
        super(context, layoutResId);
    }

    public SimpleAdapter(Context context, int layoutResId, List<T> datas) {
        super(context, layoutResId, datas);
    }
}
