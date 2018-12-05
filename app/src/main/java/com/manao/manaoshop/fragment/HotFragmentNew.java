package com.manao.manaoshop.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.gson.reflect.TypeToken;
import com.manao.manaoshop.Constants;
import com.manao.manaoshop.R;
import com.manao.manaoshop.activity.WaresActivity;
import com.manao.manaoshop.adapter.DividerItemDecortionMl;
import com.manao.manaoshop.adapter.HotAdapterNew;
import com.manao.manaoshop.baseadapter.BaseAdapter;
import com.manao.manaoshop.bean.Page;
import com.manao.manaoshop.bean.Wares;
import com.manao.manaoshop.http.ApiService;
import com.manao.manaoshop.utils.PageUtIls;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Malong
 * on 18/11/5.
 * 热卖--用封装好的分页工具类，简洁
 */
public class HotFragmentNew extends Fragment implements PageUtIls.OnPageListener<Wares>{


    @ViewInject(R.id.swp_refresh_layout)
    SmartRefreshLayout swp_refresh_layout;
    @ViewInject(R.id.recycleview_hot)
    RecyclerView recycleview_hot;

    private HotAdapterNew adapterNew;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_hot, container, false);
        //注入 Xutils
        x.view().inject(this, view);
        //网络请求加载数据
        initData();
        return view;
    }

    /**
     * 网络请求加载数据
     */
    private void initData() {
        PageUtIls pageUtIls = PageUtIls.newBuilder()
                .setUrl(ApiService.API.WARES_HOT)
                .setOnPageListener(this)
                .setPage(10)
                .setRefreshLayout(swp_refresh_layout)
                .build(getContext(), new TypeToken<Page<Wares>>() {
                }.getType());
        pageUtIls.request();
    }

    /**
     * 实现分页监听方法后，实现的函数 ：正常  ／  上拉加载  ／  下拉刷新
     * @param datas
     * @param currentPage
     * @param totalPage
     */
    @Override
    public void load(final List<Wares> datas, int currentPage, int totalPage, int totalCount) {
        adapterNew = new HotAdapterNew(getContext(),datas);
        recycleview_hot.setAdapter(adapterNew);
        recycleview_hot.setLayoutManager(new LinearLayoutManager(getActivity()));
        recycleview_hot.setItemAnimator(new DefaultItemAnimator());
        recycleview_hot.addItemDecoration(new DividerItemDecortionMl(getContext(), DividerItemDecortionMl.VERTICAL_LIST));
        adapterNew.setOnItemClickListener(new BaseAdapter.OnItemClickListener() {
            @Override
            public void onClick(View view, int position) {

                Wares wares = adapterNew.getItem(position);
                Intent intent = new Intent(getActivity(), WaresActivity.class);
                intent.putExtra(Constants.WARE, wares);
                startActivity(intent);
            }
        });
    }

    @Override
    public void refresh(List<Wares> datas, int currentPage, int totalPage, int totalCount) {
        adapterNew.clear();
        adapterNew.addData(datas);
        recycleview_hot.scrollToPosition(0);
    }

    @Override
    public void loadMore(List<Wares> datas, int currentPage, int totalPage, int totalCount) {
        adapterNew.addData(adapterNew.getDatas().size(), datas);
        recycleview_hot.scrollToPosition(adapterNew.getDatas().size());
    }

}
