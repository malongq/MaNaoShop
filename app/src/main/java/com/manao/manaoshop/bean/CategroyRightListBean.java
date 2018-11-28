package com.manao.manaoshop.bean;

/**
 * Created by Malong
 * on 18/11/27.
 * 分类右侧列表Bean
 */
public class CategroyRightListBean {

    private long id;
    private String name;
    private String imgUrl;
    private String price;
    private String description;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImgurl() {
        return imgUrl;
    }

    public void setImgurl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
