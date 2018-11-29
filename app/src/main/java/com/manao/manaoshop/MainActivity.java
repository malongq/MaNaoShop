package com.manao.manaoshop;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TextView;
import com.manao.manaoshop.bean.Tab;
import com.manao.manaoshop.fragment.CartFragment;
import com.manao.manaoshop.fragment.CategoryFragment;
import com.manao.manaoshop.fragment.HomeFragment;
import com.manao.manaoshop.fragment.HotFragment;
import com.manao.manaoshop.fragment.MineFragment;
import com.manao.manaoshop.weiget.FragmentTabHost;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private FragmentTabHost mTabHost;
    private LayoutInflater mInflater;
    private ArrayList<Tab> mTabs = new ArrayList<>(5);//主页有5个tab

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //加载底部菜单栏
        initTab();
    }

    //加载底部菜单栏
    private void initTab() {
        Tab tab_home = new Tab(R.string.home, R.drawable.selector_icon_home, HomeFragment.class);//首页
        Tab tab_hot = new Tab(R.string.hot, R.drawable.selector_icon_hot, HotFragment.class);//热卖
        Tab tab_catagory = new Tab(R.string.catagory, R.drawable.selector_icon_category, CategoryFragment.class);//分类
        Tab tab_cart = new Tab(R.string.cart, R.drawable.selector_icon_cart, CartFragment.class);//购物车
        Tab tab_mine = new Tab(R.string.mine, R.drawable.selector_icon_mine, MineFragment.class);//我的
        //依次添加
        mTabs.add(tab_home);
        mTabs.add(tab_hot);
        mTabs.add(tab_catagory);
        mTabs.add(tab_cart);
        mTabs.add(tab_mine);
        //固定模式
        mInflater = LayoutInflater.from(this);
        mTabHost = findViewById(android.R.id.tabhost);
        mTabHost.setup(this,getSupportFragmentManager(),R.id.realtabcontent);
        //添加显示
        for (Tab tab : mTabs) {
            TabHost.TabSpec tabSpec = mTabHost.newTabSpec(getString(tab.getTab_title()));
            tabSpec.setIndicator(buildIndicator(tab));
            mTabHost.addTab(tabSpec, tab.getTab_fragment(), null);
        }
        //固定刷新某一个fragment
        mTabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                //如果等于购物车Fragment
                if (tabId == getString(R.string.cart)){
                    //刷新购物车页面数据
                    refCartFragmentData();
                }
            }
        });
        mTabHost.getTabWidget().setShowDividers(LinearLayout.SHOW_DIVIDER_NONE);//去除各个子view中间竖线
        mTabHost.setCurrentTab(0);//默认选中第一个tab
    }

    //刷新购物车页面数据
    private CartFragment cartFragment;
    private void refCartFragmentData() {
        if (cartFragment == null){
            Fragment fragment = getSupportFragmentManager().findFragmentByTag(getString(R.string.cart));
            if (fragment != null){
                cartFragment = (CartFragment) fragment;
                cartFragment.refData();
            }
        }else {
            cartFragment.refData();
        }
    }

    //加载底部菜单栏中的个体view
    private View buildIndicator(Tab tab) {
        View view = mInflater.inflate(R.layout.tab_indicator, null);
        ImageView img = view.findViewById(R.id.icon_tab);
        TextView text = view.findViewById(R.id.txt_indicator);
        img.setBackgroundResource(tab.getTab_icon());
        text.setText(tab.getTab_title());
        return view;
    }

}
