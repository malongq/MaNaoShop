package com.manao.manaoshop.bean;

/**
 * Created by Malong
 * on 18/11/5.
 * APP 底部菜单栏bean类
 */
public class Tab {

    private int tab_title;
    private int tab_icon;
    private Class tab_fragment;

    public Tab(int tab_title, int tab_icon, Class tab_fragment) {
        this.tab_title = tab_title;
        this.tab_icon = tab_icon;
        this.tab_fragment = tab_fragment;
    }

    public int getTab_title() {
        return tab_title;
    }

    public void setTab_title(int tab_title) {
        this.tab_title = tab_title;
    }

    public int getTab_icon() {
        return tab_icon;
    }

    public void setTab_icon(int tab_icon) {
        this.tab_icon = tab_icon;
    }

    public Class getTab_fragment() {
        return tab_fragment;
    }

    public void setTab_fragment(Class tab_fragment) {
        this.tab_fragment = tab_fragment;
    }

}
