package com.manao.manaoshop.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.manao.manaoshop.R;
import com.manao.manaoshop.baseadapter.BaseViewHolder;
import com.manao.manaoshop.baseadapter.SimpleAdapter;
import com.manao.manaoshop.bean.ShoppingCart;
import com.manao.manaoshop.bean.Wares;
import com.manao.manaoshop.utils.ShopCarProvider;
import com.manao.manaoshop.utils.ToastUtils;

import java.util.List;

/**
 * Created by Malong
 * on 18/11/16.
 * 热卖adapter  优化后的
 */
public class HotAdapterNew extends SimpleAdapter<Wares> {

    private ShopCarProvider provider;
    private Context context;

    public HotAdapterNew(Context context, List<Wares> datas) {
        super(context, R.layout.template_hot_wares, datas);
        this.context = context;
        //创建provider
        provider = new ShopCarProvider(context);
    }

    @Override
    protected void convert(BaseViewHolder holder, final Wares wares) {

        SimpleDraweeView draweeView = (SimpleDraweeView) holder.getView(R.id.drawee_view);
        draweeView.setImageURI(Uri.parse(wares.getImgUrl()));

        holder.getTextView(R.id.text_title).setText(wares.getName());

        holder.getTextView(R.id.text_price).setText("￥" + wares.getPrice());

        Button button = holder.getButton(R.id.btn_into);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                provider.put(convertData(wares));
                ToastUtils.show(context, "已经加入购物车");
            }
        });
    }

    //将数据添加进provider
    public ShoppingCart convertData(Wares item) {
        ShoppingCart cart = new ShoppingCart();
        cart.setId(item.getId());
        cart.setDescription(item.getDescription());
        cart.setImgUrl(item.getImgUrl());
        cart.setName(item.getName());
        cart.setPrice(item.getPrice());
        return cart;
    }

    public void resetLayout(int layoutId) {
        this.layoutResId = layoutId;
        notifyItemRangeChanged(0, getDatas().size());
    }

}
