package com.manao.manaoshop.adapter;

import android.content.Context;
import android.net.Uri;

import com.facebook.drawee.view.SimpleDraweeView;
import com.manao.manaoshop.R;
import com.manao.manaoshop.base.baseadapter.BaseViewHolder;
import com.manao.manaoshop.base.baseadapter.SimpleAdapter;
import com.manao.manaoshop.bean.ShoppingCart;

import java.util.List;

/**
 * Created by Malong
 * on 18/12/8.
 * 提交订单我的商品列表图片页面
 */
public class WareOrderAdapter extends SimpleAdapter<ShoppingCart> {

    private List<ShoppingCart> datas;

    public WareOrderAdapter(Context context, List<ShoppingCart> datas) {
        super(context, R.layout.template_order_wares, datas);
        this.datas = datas;
    }

    @Override
    protected void convert(BaseViewHolder viewHoder, final ShoppingCart item) {
        SimpleDraweeView draweeView = (SimpleDraweeView) viewHoder.getView(R.id.drawee_view);
        draweeView.setImageURI(Uri.parse(item.getImgUrl()));
    }

    //获取总价
    public float getTotalPrice() {
        float sum = 0;
        if (!isNull()){
            return sum;
        }
        for (ShoppingCart cart : datas) {
            sum += cart.getCount() * cart.getPrice();
        }
        return sum;
    }

    //是否为空
    private boolean isNull() {
        return (datas != null && datas.size() > 0);
    }

}
