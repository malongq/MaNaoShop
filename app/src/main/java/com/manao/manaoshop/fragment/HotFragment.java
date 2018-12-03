package com.manao.manaoshop.fragment;

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

import com.manao.manaoshop.R;
import com.manao.manaoshop.adapter.DividerItemDecortionMl;
//import com.manao.manaoshop.adapter.HotAdapter;
import com.manao.manaoshop.adapter.HotAdapterNew;
import com.manao.manaoshop.baseadapter.BaseAdapter;
import com.manao.manaoshop.baseadapter.SimpleAdapter;
import com.manao.manaoshop.bean.Page;
import com.manao.manaoshop.bean.Wares;
import com.manao.manaoshop.http.ApiService;
import com.manao.manaoshop.http.OkHttpHelper;
import com.manao.manaoshop.http.SpotsCallBack;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.List;

import okhttp3.Response;

/**
 * Created by Malong
 * on 18/11/5.
 * 热卖
 */
@Deprecated
public class HotFragment extends Fragment {

    private OkHttpHelper okHttpHelper = OkHttpHelper.getInstance();
    private List<Wares> mDatas;

    @ViewInject(R.id.swp_refresh_layout)
    SmartRefreshLayout swp_refresh_layout;
    @ViewInject(R.id.recycleview_hot)
    RecyclerView recycleview_hot;

//    private HotAdapter hotAdapter;
    private int currentPage = 1;//当前页
    private int totalPage = 1;//总页数
    private int pageSize = 10;//每页请求时参数，固定10条数据每页
    private static final int NORMALL = 0;//正常
    private static final int REFRESH = 1;//下拉刷新
    private static final int LOADMORE = 2;//上拉加载

    private int state = NORMALL;//默认是正常进入
    private SimpleAdapter<Wares> adapter;
    private HotAdapterNew adapterNew;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_hot, container, false);
        //注入 Xutils
        x.view().inject(this, view);
        //下拉刷新、上拉加载
        initRefreshAndLoadMore();
        //网络请求加载数据
        initData();
        return view;
    }

    /**
     * 网络请求加载数据
     */
    private void initData() {
        String url = ApiService.API.WARES_HOT + "?curPage=" + currentPage + "&pageSize=" + pageSize;
        okHttpHelper.get(url, new SpotsCallBack<Page<Wares>>(getContext()) {

            @Override
            public void onSuccess(Response response, Page<Wares> waresPage) {
                mDatas = waresPage.getList();
                currentPage = waresPage.getCurrentPage();//当前页
                totalPage = waresPage.getTotalPage();//总页数
                showData();//展示网络请求到的数据
            }

            @Override
            public void onError(Response response, int code, Exception e) {
                Toast.makeText(getContext(), "加载出错了", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 展示网络请求到的数据
     */
    private void showData() {
        switch (state) {//默认是正常进入
            case NORMALL://正常
                /**----------为封装BaseAdapter之前写法-----------**/
//                hotAdapter = new HotAdapter(mDatas);
//                recycleview_hot.setAdapter(hotAdapter);
//                recycleview_hot.setLayoutManager(new LinearLayoutManager(getActivity()));
//                recycleview_hot.setItemAnimator(new DefaultItemAnimator());
//                recycleview_hot.addItemDecoration(new DividerItemDecortion(getContext(), DividerItemDecortion.VERTICAL_LIST));
                /**----------封装BaseAdapter之后写法-----------**/
//                recycleview_hot.setAdapter(new SimpleAdapter<Wares>(getContext(),R.layout.template_hot_wares,mDatas) {
//
//                    @Override
//                    protected void convert(BaseViewHolder holder, Wares wares) {
//                        SimpleDraweeView draweeView = (SimpleDraweeView) holder.getView(R.id.drawee_view);
//                        draweeView.setImageURI(Uri.parse(wares.getImgUrl()));
//                        holder.getTextView(R.id.text_title).setText(wares.getName());
//                        holder.getTextView(R.id.text_price).setText("￥" + wares.getPrice());
//                    }
//                });
//                recycleview_hot.setLayoutManager(new LinearLayoutManager(getActivity()));
//                recycleview_hot.setItemAnimator(new DefaultItemAnimator());
//                recycleview_hot.addItemDecoration(new DividerItemDecortion(getContext(), DividerItemDecortion.VERTICAL_LIST));
                /**----------继续优化封装BaseAdapter之后写法-----------**/
                adapterNew = new HotAdapterNew(getContext(),mDatas);
                recycleview_hot.setAdapter(adapterNew);
                recycleview_hot.setLayoutManager(new LinearLayoutManager(getActivity()));
                recycleview_hot.setItemAnimator(new DefaultItemAnimator());
                recycleview_hot.addItemDecoration(new DividerItemDecortionMl(getContext(), DividerItemDecortionMl.VERTICAL_LIST));
                adapterNew.setOnItemClickListener(new BaseAdapter.OnItemClickListener() {
                    @Override
                    public void onClick(View view, int position) {
                        Toast.makeText(getContext(), mDatas.get(position).getName(), Toast.LENGTH_SHORT).show();
                    }
                });
                break;
            case REFRESH://下拉刷新
                adapterNew.clear();
                adapterNew.addData(mDatas);
                recycleview_hot.scrollToPosition(0);
                swp_refresh_layout.finishRefresh();
                break;
            case LOADMORE://上拉加载
                adapterNew.addData(adapterNew.getDatas().size(), mDatas);
                recycleview_hot.scrollToPosition(adapterNew.getDatas().size());
                swp_refresh_layout.finishLoadMore();
                break;
        }
    }

    /**
     * 下拉刷新、上拉加载
     */
    private void initRefreshAndLoadMore() {
        //下拉刷新
        swp_refresh_layout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshLayout) {
                //请求数据
                refreshData();
            }
        });

        //上拉加载
        swp_refresh_layout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshLayout) {
                if (currentPage < totalPage) {
                    //请求数据
                    loadMoreData();
                } else {
                    swp_refresh_layout.finishLoadMore();
                    Toast.makeText(getContext(), "已经到底了...", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /**
     * 下拉刷新 : 请求数据
     */
    private void refreshData() {
        currentPage = 1;
        state = REFRESH;
        initData();
    }

    /**
     * 上拉加载 : 请求数据
     */
    private void loadMoreData() {
        currentPage = ++currentPage;
        state = LOADMORE;
        initData();
    }

}
