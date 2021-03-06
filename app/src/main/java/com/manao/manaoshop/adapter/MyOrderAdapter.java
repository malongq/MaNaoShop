package com.manao.manaoshop.adapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.manao.manaoshop.R;
import com.manao.manaoshop.base.baseadapter.BaseAdapter;
import com.manao.manaoshop.base.baseadapter.BaseViewHolder;
import com.manao.manaoshop.base.baseadapter.SimpleAdapter;
import com.manao.manaoshop.bean.Order;
import com.manao.manaoshop.bean.OrderItem;
import com.squareup.picasso.Picasso;
import com.w4lle.library.NineGridAdapter;
import com.w4lle.library.NineGridlayout;

import java.util.List;

/**
 * Created by Malong
 * on 18/12/13.
 * 我的订单adapter
 */
public class MyOrderAdapter extends SimpleAdapter<Order> {

    private Context mContext;

    public MyOrderAdapter(Context context, List<Order> datas) {
        super(context, R.layout.template_my_orders, datas);
        this.mContext = context;
    }

    @Override
    protected void convert(BaseViewHolder viewHoder, Order orders) {

        viewHoder.getTextView(R.id.txt_order_num).setText("订单号：" + orders.getOrderNum());
        viewHoder.getTextView(R.id.txt_order_money).setText("实付金额： ￥：" + orders.getAmount());

        TextView txtStatus = viewHoder.getTextView(R.id.txt_status);
        switch (orders.getStatus()) {
            case Order.STATUS_SUCCESS:
                txtStatus.setText("成功");
                txtStatus.setTextColor(Color.parseColor("#ff4CAF50"));
                break;
            case Order.STATUS_PAY_FAIL:
                txtStatus.setText("支付失败");
                txtStatus.setTextColor(Color.parseColor("#ffF44336"));
                break;
            case Order.STATUS_PAY_WAIT:
                txtStatus.setText("等待支付");
                txtStatus.setTextColor(Color.parseColor("#ffFFEB3B"));
                break;
        }

        NineGridlayout nineGridlayout = (NineGridlayout) viewHoder.getView(R.id.iv_ngrid_layout);
        nineGridlayout.setGap(5);
        nineGridlayout.setDefaultWidth(50);
        nineGridlayout.setDefaultHeight(50);
        nineGridlayout.setAdapter(new OrderItemAdapter(mContext, orders.getItems()));

        //动态布局添加UI
//        LinearLayout ll = (LinearLayout) viewHoder.getView(R.id.iv_ngrid_layout1);
//        ll.removeAllViews();
//        LinearLayout inflate = (LinearLayout) View.inflate(mContext,R.layout.template_order, null);
//        LinearLayout.LayoutParams relLayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
//        inflate.setLayoutParams(relLayoutParams);
//        SimpleDraweeView simpleDraweeView = inflate.findViewById(R.id.drawee_view);
//        simpleDraweeView.setImageURI(orders.getItems().get(0).getWares().getImgUrl());
//        ll.addView(inflate);

    }

    //9宫格实现的adapter
    class OrderItemAdapter extends NineGridAdapter {

        private List<OrderItem> items;

        public OrderItemAdapter(Context context, List<OrderItem> items) {
            super(context, items);
            this.items = items;
        }

        @Override
        public int getCount() {
            return (items == null) ? 0 : items.size();
        }

        @Override
        public String getUrl(int position) {
            OrderItem item = getItem(position);
            return item.getWares().getImgUrl();

        }

        @Override
        public OrderItem getItem(int position) {
            return (items == null) ? null : items.get(position);
        }

        @Override
        public long getItemId(int position) {
            OrderItem item = getItem(position);
            return item == null ? 0 : item.getId();
        }

        @Override
        public View getView(int i, View view) {
            ImageView iv = new ImageView(context);
            iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
            iv.setBackgroundColor(Color.parseColor("#f5f5f5"));
            Picasso.with(context).load(getUrl(i)).placeholder(new ColorDrawable(Color.parseColor("#f5f5f5"))).into(iv);
            return iv;
        }

    }

}
