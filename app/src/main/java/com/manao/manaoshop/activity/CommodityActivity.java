package com.manao.manaoshop.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.google.gson.reflect.TypeToken;
import com.manao.manaoshop.Constants;
import com.manao.manaoshop.R;
import com.manao.manaoshop.adapter.DividerItemDecortionMl;
import com.manao.manaoshop.adapter.HotAdapterNew;
import com.manao.manaoshop.bean.Page;
import com.manao.manaoshop.bean.Wares;
import com.manao.manaoshop.http.ApiService;
import com.manao.manaoshop.utils.PageUtIls;
import com.manao.manaoshop.weiget.MaNaoToolbar;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.List;

/**
 * Created by Malong
 * on 18/12/3.
 * 首页商品列表点击进入的--页面
 */
public class CommodityActivity extends AppCompatActivity implements PageUtIls.OnPageListener, TabLayout.OnTabSelectedListener {

    @ViewInject(R.id.manao_toobar)
    private MaNaoToolbar mMaNaoToolbar;

    @ViewInject(R.id.tab_layout)
    private TabLayout mTabLayout;

    @ViewInject(R.id.tv_sum)
    private TextView mTv_sum;

    @ViewInject(R.id.smartrefresh)
    private SmartRefreshLayout mSmartRefreshLayout;

    @ViewInject(R.id.recycler_view)
    private RecyclerView mRecyclerView;
    private long campaign_id = 0;
    private int orderBy = 0;

    private static final int FIRST_TAG = 0;
    private static final int TWO_TAG = 1;
    private static final int THREE_TAG = 2;
    private HotAdapterNew mWaresAdapter;
    private PageUtIls pageUtIls;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.commodity_activity);

        x.view().inject(this);

        campaign_id = getIntent().getLongExtra(Constants.CAMPAIGN_ID, 0);

        //加载toolbar
        initToolbar();

        //加载tab
        initTab();

        //请求数据
        requestData();

    }

    //加载toolbar
    private void initToolbar() {
        mMaNaoToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommodityActivity.this.finish();
            }
        });
    }

    //加载tab
    private void initTab() {

        TabLayout.Tab tab1 = mTabLayout.newTab();
        tab1.setText("默认");
        tab1.setTag(FIRST_TAG);
        mTabLayout.addTab(tab1);

        TabLayout.Tab tab2 = mTabLayout.newTab();
        tab2.setText("价格");
        tab2.setTag(TWO_TAG);
        mTabLayout.addTab(tab2);

        TabLayout.Tab tab3 = mTabLayout.newTab();
        tab3.setText("销量");
        tab3.setTag(THREE_TAG);
        mTabLayout.addTab(tab3);

        mTabLayout.addOnTabSelectedListener(this);

    }

    //请求数据
    private void requestData() {
        pageUtIls = PageUtIls.newBuilder()
                .setUrl(ApiService.API.WARES_CAMPAIN_LIST)
                .putParam("campaignId", campaign_id)
                .putParam("orderBy", orderBy)
                .setRefreshLayout(mSmartRefreshLayout)
                .setOnPageListener(this)
                .build(this, new TypeToken<Page<Wares>>() {
                }.getType());
        pageUtIls.request();
    }

    //请求
    @Override
    public void load(List datas, int currentPage, int totalPage, int totalCount) {
        mTv_sum.setText("共有"+totalCount+"件商品");
        if (mWaresAdapter == null) {
            mWaresAdapter = new HotAdapterNew(this, datas);
            mRecyclerView.setAdapter(mWaresAdapter);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
            mRecyclerView.addItemDecoration(new DividerItemDecortionMl(this,DividerItemDecortionMl.VERTICAL_LIST));
            mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        } else {
            mWaresAdapter.refreshData(datas);
        }
    }

    //下拉刷新
    @Override
    public void refresh(List datas, int currentPage, int totalPage, int totalCount) {
        mWaresAdapter.refreshData(datas);
        mRecyclerView.scrollToPosition(0);
    }

    //上拉加载
    @Override
    public void loadMore(List datas, int currentPage, int totalPage, int totalCount) {
        mWaresAdapter.loadMoreData(datas);
    }

    //tab选中
    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        orderBy = (int) tab.getTag();
        pageUtIls.putParam("orderBy",orderBy);
        pageUtIls.request();
    }

    //tab未选中--不用该方法
    @Override
    public void onTabUnselected(TabLayout.Tab tab) {}

    //tab再次选中--不用该方法
    @Override
    public void onTabReselected(TabLayout.Tab tab) {}
}
