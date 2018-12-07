package com.manao.manaoshop.adapter;

import android.content.Context;

import com.manao.manaoshop.R;
import com.manao.manaoshop.base.baseadapter.BaseViewHolder;
import com.manao.manaoshop.base.baseadapter.SimpleAdapter;
import com.manao.manaoshop.bean.Category;

import java.util.List;

/**
 * Created by Malong
 * on 18/11/26.
 * 分类adapter
 */
public class CategroyAdapter extends SimpleAdapter<Category> {

    public CategroyAdapter(Context context, List<Category> datas) {
        super(context, R.layout.template_single_text, datas);
    }

    @Override
    protected void convert(BaseViewHolder holder, Category item) {
        holder.getTextView(R.id.textView).setText(item.getName());
    }
}
