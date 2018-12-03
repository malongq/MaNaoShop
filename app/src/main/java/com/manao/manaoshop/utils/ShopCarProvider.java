package com.manao.manaoshop.utils;

import android.content.Context;
import android.util.SparseArray;

import com.google.gson.reflect.TypeToken;
import com.manao.manaoshop.bean.ShoppingCart;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Malong
 * on 18/11/29.
 * 购物车提供者
 */
public class ShopCarProvider {

    public static final String SHOP_CAR_JSON = "shop_car_json";

    private static SparseArray<ShoppingCart> datas = null;//需用static修饰，否则会不好使

    private Context mContext;

    public ShopCarProvider(Context mContext) {
        this.mContext = mContext;
        datas = new SparseArray<>(100);
        listToSparse();
    }

    //将数据添加
    private void listToSparse() {
        List<ShoppingCart> carts = getDataFromLocal();
        if (carts != null && carts.size() > 0) {
            for (ShoppingCart cart : carts) {
                datas.put(cart.getId().intValue(), cart);
            }
        }
    }

    //取出sp数据并转换成数组
    public List<ShoppingCart> getDataFromLocal() {
        String json = SPreferencesUtils.getString(mContext, SHOP_CAR_JSON);
        List<ShoppingCart> carts = null;
        if (json != null) {
            carts = JSONUtils.fromJson(json, new TypeToken<List<ShoppingCart>>() {}.getType());
        }
        return carts;
    }

    //获取所有数据
    public List<ShoppingCart> getAll() {
        return getDataFromLocal();
    }

    //添加数据
    public void put(ShoppingCart cart) {
        ShoppingCart temp = datas.get(cart.getId().intValue());
        if (temp != null) {
            temp.setCount(temp.getCount() + 1);
        } else {
            temp = cart;
            temp.setCount(1);
        }
        datas.put(cart.getId().intValue(), temp);
        commit();
    }

    //更新数据
    public void update(ShoppingCart cart) {
        datas.put(cart.getId().intValue(), cart);
        commit();
    }

    //删除数据
    public void delete(ShoppingCart cart) {
        datas.delete(cart.getId().intValue());
        commit();
    }

    //提交数据到sp
    private void commit() {
        int size = datas.size();
        List<ShoppingCart> list = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            list.add(datas.valueAt(i));
        }
        SPreferencesUtils.putString(mContext, SHOP_CAR_JSON, JSONUtils.toJSON(list));
    }

}
