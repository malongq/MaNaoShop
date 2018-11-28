package com.manao.manaoshop.adapter;

import android.content.Context;
import android.net.Uri;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.manao.manaoshop.R;
import com.manao.manaoshop.baseadapter.BaseViewHolder;
import com.manao.manaoshop.baseadapter.SimpleAdapter;
import com.manao.manaoshop.bean.Wares;

import java.util.List;

/**
 * Created by Malong
 * on 18/11/16.
 * 热卖adapter  优化后的
 */
public class HotAdapterNew extends SimpleAdapter<Wares> {


    public HotAdapterNew(Context context, List<Wares> datas) {
        super(context, R.layout.template_hot_wares, datas);
    }

    @Override
    protected void convert(BaseViewHolder holder, Wares wares) {
        SimpleDraweeView draweeView = (SimpleDraweeView) holder.getView(R.id.drawee_view);
        draweeView.setImageURI(Uri.parse(wares.getImgUrl()));
        holder.getTextView(R.id.text_title).setText(wares.getName());
        holder.getTextView(R.id.text_price).setText("￥" + wares.getPrice());
    }
}
