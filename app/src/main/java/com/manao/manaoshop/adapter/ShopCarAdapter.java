package com.manao.manaoshop.adapter;

import android.content.Context;
import android.net.Uri;
import android.text.Html;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.manao.manaoshop.R;
import com.manao.manaoshop.baseadapter.BaseAdapter;
import com.manao.manaoshop.baseadapter.BaseViewHolder;
import com.manao.manaoshop.baseadapter.SimpleAdapter;
import com.manao.manaoshop.bean.ShoppingCart;
import com.manao.manaoshop.bean.Wares;
import com.manao.manaoshop.utils.ShopCarProvider;
import com.manao.manaoshop.weiget.AddOrSubLayout;

import java.util.Iterator;
import java.util.List;

/**
 * Created by Malong
 * on 18/11/29.
 * 购物车adapter
 */
public class ShopCarAdapter extends SimpleAdapter<ShoppingCart> implements BaseAdapter.OnItemClickListener {

    private final ShopCarProvider provider;
    private CheckBox checkBox;
    private TextView textView;
    private List<ShoppingCart> datas;

    public ShopCarAdapter(Context context, List<ShoppingCart> datas, final CheckBox checkBox, TextView tv) {
        super(context, R.layout.template_cart, datas);

        this.datas = datas;

        setCheckBox(checkBox);

        setTextView(tv);

        provider = new ShopCarProvider(context);

        setOnItemClickListener(this);

        showTotalPrice();
    }

    @Override
    protected void convert(BaseViewHolder holder, final ShoppingCart item) {

        holder.getTextView(R.id.text_title).setText(item.getName());

        holder.getTextView(R.id.text_price).setText("￥" + item.getPrice());

        SimpleDraweeView draweeView = (SimpleDraweeView) holder.getView(R.id.drawee_view);
        draweeView.setImageURI(Uri.parse(item.getImgUrl()));

        CheckBox checkBox = (CheckBox) holder.getView(R.id.checkbox);
        checkBox.setChecked(item.isChecked());//购物车内物品默认选中

        AddOrSubLayout numberAddSubView = (AddOrSubLayout) holder.getView(R.id.num_control);
        numberAddSubView.setValue(item.getCount());
        numberAddSubView.setOnClickAddOrSubListener(new AddOrSubLayout.OnClickAddOrSubListener() {
            @Override
            public void onAddClick(View v, int value) {
                item.setCount(value);
                provider.update(item);
                showTotalPrice();

            }

            @Override
            public void onSubClick(View v, int value) {
                item.setCount(value);
                provider.update(item);
                showTotalPrice();
            }
        });
    }

    /**
     * 获取总价
     *
     * @return
     */
    private float getTotalPrice() {
        float sum = 0;
        if (!isNull()) {
            return sum;
        }
        for (ShoppingCart cart : datas) {
            if (cart.isChecked())
                sum += cart.getCount() * cart.getPrice();
        }
        return sum;
    }

    /**
     * 展示总价
     */
    public void showTotalPrice() {
        float total = getTotalPrice();
        textView.setText(Html.fromHtml("合计 ￥<span style='color:#eb4f38'>" + total + "</span>"), TextView.BufferType.SPANNABLE);
    }

    /**
     * 判断数据是否为null
     *
     * @return
     */
    private boolean isNull() {
        return (datas != null && datas.size() > 0);
    }

    /**
     * 条目点击事件
     *
     * @param view
     * @param position
     */
    @Override
    public void onClick(View view, int position) {
        ShoppingCart cart = getItem(position);
        cart.setIsChecked(!cart.isChecked());
        notifyItemChanged(position);
        checkListen();
        showTotalPrice();
    }

    /**
     * 点击条目选中某条数据后  判断是否要勾选下方全选
     */
    private void checkListen() {
        int count = 0;
        int checkNum = 0;
        if (datas != null) {
            count = datas.size();
            for (ShoppingCart cart : datas) {
                if (!cart.isChecked()) {
                    checkBox.setChecked(false);
                    break;
                } else {
                    checkNum = checkNum + 1;
                }
            }
            if (count == checkNum) {
                checkBox.setChecked(true);
            }
        }
    }

    /**
     * 全部选中
     *
     * @param isChecked
     */
    public void checkAll_None(boolean isChecked) {
        if (!isNull()) {
            return;
        }
        int i = 0;
        for (ShoppingCart cart : datas) {
            cart.setIsChecked(isChecked);
            notifyItemChanged(i);
            i++;
        }
    }

    /**
     * 删除选中数据
     */
    public void delCart() {
        if (!isNull()) {
            return;
        }
        for (Iterator iterator = datas.iterator(); iterator.hasNext();) {
            ShoppingCart cart = (ShoppingCart) iterator.next();
            if (cart.isChecked()) {
                int position = datas.indexOf(cart);
                provider.delete(cart);
                iterator.remove();
                notifyItemRemoved(position);
            }
        }
    }

    /**
     * 获取合计总价控件
     *
     * @param textview
     */
    public void setTextView(TextView textview) {
        this.textView = textview;
    }

    /**
     * 点击全选
     *
     * @param ck
     */
    public void setCheckBox(CheckBox ck) {
        this.checkBox = ck;
        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAll_None(checkBox.isChecked());
                showTotalPrice();
            }
        });
    }


}
