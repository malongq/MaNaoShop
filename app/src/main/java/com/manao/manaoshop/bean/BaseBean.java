package com.manao.manaoshop.bean;

import java.io.Serializable;

/**
 * Created by Malong
 * on 18/11/12.
 */
public class BaseBean implements Serializable {

    protected long id;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
