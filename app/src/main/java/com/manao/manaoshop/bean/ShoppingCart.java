package com.manao.manaoshop.bean;

import java.io.Serializable;

/**
 * Created by Malong
 * on 18/11/29.
 * 购物车Bean
 */
public class ShoppingCart extends Wares implements Serializable {

    private int count;
    private boolean isChecked = true;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setIsChecked(boolean isChecked) {
        this.isChecked = isChecked;
    }

}
