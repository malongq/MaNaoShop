package com.manao.manaoshop.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.manao.manaoshop.MaNaoAppaplication;
import com.manao.manaoshop.R;
import com.manao.manaoshop.adapter.CardViewtemDecortion;
import com.manao.manaoshop.adapter.DividerItemDecortionMl;
import com.manao.manaoshop.adapter.MyOrderAdapter;
import com.manao.manaoshop.base.baseactivity.BaseActivity;
import com.manao.manaoshop.base.baseadapter.BaseAdapter;
import com.manao.manaoshop.bean.Order;
import com.manao.manaoshop.http.ApiService;
import com.manao.manaoshop.http.OkHttpHelper;
import com.manao.manaoshop.http.SpotsCallBack;
import com.manao.manaoshop.weiget.MaNaoToolbar;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Response;

/**
 * Created by Malong
 * on 18/12/13.
 * 我的订单页面
 */
public class MyOrderActivity extends BaseActivity implements TabLayout.OnTabSelectedListener {

    @ViewInject(R.id.toolbar)
    private MaNaoToolbar mToolbar;

    @ViewInject(R.id.tab_layout)
    private TabLayout tabLayout;

    @ViewInject(R.id.recycler_view)
    private RecyclerView recyclerView;

    public static final int STATUS_ALL = 1000;
    public static final int STATUS_SUCCESS = 1; //支付成功的订单
    public static final int STATUS_PAY_FAIL = -2; //支付失败的订单
    public static final int STATUS_PAY_WAIT = 0; //：待支付的订单
    private int status = STATUS_ALL;

    OkHttpHelper okHttpHelper = OkHttpHelper.getInstance();

    private MyOrderAdapter mAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_my_order);

        x.view().inject(this);

        initToolBar();//加载ToolBar

        init();//加载数据UI

        getOrders();//获取当前tab下的数据

    }

    //加载ToolBar
    private void initToolBar() {
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mToolbar.hideRightButton();
        mToolbar.hideSearchView();
    }

    //加载数据UI
    private void init() {

        TabLayout.Tab tab = tabLayout.newTab();
        tab.setText("全部");
        tab.setTag(STATUS_ALL);
        tabLayout.addTab(tab);

        tab = tabLayout.newTab();
        tab.setText("支付成功");
        tab.setTag(STATUS_SUCCESS);
        tabLayout.addTab(tab);

        tab = tabLayout.newTab();
        tab.setText("待支付");
        tab.setTag(STATUS_PAY_WAIT);
        tabLayout.addTab(tab);

        tab = tabLayout.newTab();
        tab.setText("支付失败");
        tab.setTag(STATUS_PAY_FAIL);
        tabLayout.addTab(tab);

        tabLayout.addOnTabSelectedListener(this);
    }

    //获取当前tab下的数据
    private void getOrders() {

        Long userId = MaNaoAppaplication.getInstance().getUser().getId();
        Map<String, Object> params = new HashMap<>(2);
        params.put("user_id", userId);
        params.put("status", status);

        okHttpHelper.get(ApiService.API.ORDER_LIST, params, new SpotsCallBack<List<Order>>(this) {
            @Override
            public void onSuccess(Response response, List<Order> orders) {
                showOrders(orders);
            }

            @Override
            public void onError(Response response, int code, Exception e) {
                Log.i("MyOrderActivity", "code:" + code);
            }

            @Override
            public void onErrorToken(Response response, int code) {
                super.onErrorToken(response, code);
            }
        });
    }

    //展示数据
    private void showOrders(List<Order> orders) {
        if (mAdapter == null) {
            mAdapter = new MyOrderAdapter(this,orders);
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
//            recyclerView.addItemDecoration(new DividerItemDecortionMl(this, DividerItemDecortionMl.VERTICAL_LIST));
            recyclerView.setAdapter(mAdapter);
            recyclerView.addItemDecoration(new CardViewtemDecortion());
            mAdapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener() {
                @Override
                public void onClick(View view, int position) {
                    toDetailActivity(position);
                }
            });
        } else {
            mAdapter.refreshData(orders);
            recyclerView.setAdapter(mAdapter);
        }
    }

    //跳转至订单详情页
    private void toDetailActivity(int position){
        Intent intent = new Intent(this,OrderDetailActivity.class);
        Order order = mAdapter.getItem(position);
        intent.putExtra("order",order);
        startActivity(intent,true);
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        status = (int) tab.getTag();//获取当前tab的tag
        getOrders();//获取当前tab下的数据
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {
    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {
    }
}
