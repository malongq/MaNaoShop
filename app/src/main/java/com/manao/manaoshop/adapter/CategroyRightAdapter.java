package com.manao.manaoshop.adapter;

import android.content.Context;
import android.net.Uri;

import com.facebook.drawee.view.SimpleDraweeView;
import com.manao.manaoshop.R;
import com.manao.manaoshop.base.baseadapter.BaseViewHolder;
import com.manao.manaoshop.base.baseadapter.SimpleAdapter;
import com.manao.manaoshop.bean.CategroyRightListBean;

import java.util.List;

/**
 * Created by Malong
 * on 18/11/27.
 * 分类右侧adapter
 */
public class CategroyRightAdapter extends SimpleAdapter<CategroyRightListBean> {

    public CategroyRightAdapter(Context context, List<CategroyRightListBean> datas) {
        super(context, R.layout.template_categroy_right, datas);
    }

    @Override
    protected void convert(BaseViewHolder holder, CategroyRightListBean item) {
        SimpleDraweeView draweeView = (SimpleDraweeView) holder.getView(R.id.drawee_view);
        draweeView.setImageURI(Uri.parse(item.getImgurl()));
        holder.getTextView(R.id.text_title).setText(item.getName());
        holder.getTextView(R.id.text_price).setText("￥" + item.getPrice());
    }
}
