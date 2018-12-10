package com.manao.manaoshop.fragment;

import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;
import com.manao.manaoshop.R;
import com.manao.manaoshop.adapter.CategroyAdapter;
import com.manao.manaoshop.adapter.CategroyRightAdapter;
import com.manao.manaoshop.adapter.DividerGridItemDecoration;
import com.manao.manaoshop.adapter.DividerItemDecortionMl;
import com.manao.manaoshop.base.baseadapter.BaseAdapter;
import com.manao.manaoshop.bean.Banner;
import com.manao.manaoshop.bean.Category;
import com.manao.manaoshop.bean.CategroyRightListBean;
import com.manao.manaoshop.bean.Page;
import com.manao.manaoshop.http.BaseCallBack;
import com.manao.manaoshop.http.ApiService;
import com.manao.manaoshop.http.OkHttpHelper;
import com.manao.manaoshop.http.SpotsCallBack;
import com.manao.manaoshop.utils.ToastUtils;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.List;

import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Malong
 * on 18/11/5.
 * 分类
 */
public class CategoryFragment extends Fragment {

    @ViewInject(R.id.recycler_left)
    private RecyclerView mRecycler_view_left;
    @ViewInject(R.id.slider_category)
    private SliderLayout mSlider_category;
    @ViewInject(R.id.smartrefresh)
    private SmartRefreshLayout mSmartrefreshlayout;
    @ViewInject(R.id.recycler_right)
    private RecyclerView mRecycler_view_right;

    private OkHttpHelper mOkhttpHelper = OkHttpHelper.getInstance();
    private CategroyAdapter mCategroyAdapterLeft;
    private CategroyRightAdapter mCategroyRightAdapter;

    private static final int NORMAL = 0;
    private static final int REFRESH = 1;
    private static final int LOAD_MORE = 2;
    private int status = NORMAL;
    private int category_id = 0;
    private int curPage = 1;
    private int totalPage = 1;
    private int pageSize = 10;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_category, container, false);
        x.view().inject(this, view);

        //请求左侧数据
        requestCategroyLeftData();

        //请求右侧Banner数据
        requestCategroyRightBannerData();

        //下拉刷新、上拉加载
        initRefreshAndLoadMore();

        return view;
    }

    /**
     * 请求左侧数据
     */
    private void requestCategroyLeftData() {
        mOkhttpHelper.get(ApiService.API.CATEGROY_LEFT, new SpotsCallBack<List<Category>>(getContext()) {

            @Override
            public void onSuccess(Response response, List<Category> categories) {
                //展示左侧数据
                showCategroyLeftData(categories);
                if (categories != null && categories.size() > 0) {
                    category_id = (int) categories.get(0).getId();
                    //请求右侧列表数据 -- 刚进入页面请求左侧第一条位置数据
                    requestCategroyRightData(category_id);
                }
            }

            @Override
            public void onError(Response response, int code, Exception e) {
                Toast.makeText(getContext(), "分类页面左侧数据请求Json解析出错", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 展示左侧数据
     */
    private void showCategroyLeftData(final List<Category> categories) {
        mCategroyAdapterLeft = new CategroyAdapter(getContext(), categories);
        mRecycler_view_left.setAdapter(mCategroyAdapterLeft);
        mRecycler_view_left.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecycler_view_left.setItemAnimator(new DefaultItemAnimator());
        mRecycler_view_left.addItemDecoration(new DividerItemDecortionMl(getContext(), DividerItemDecortionMl.VERTICAL_LIST));
        mCategroyAdapterLeft.setOnItemClickListener(new BaseAdapter.OnItemClickListener() {
            @Override
            public void onClick(View view, int position) {
                Category category = mCategroyAdapterLeft.getItem(position);
                category_id = (int) category.getId();
                curPage = 1;
                status = NORMAL;
                //请求右侧列表数据 -- 根据左侧点击的条目位置请求数据
                requestCategroyRightData(category_id);
            }
        });
    }

    /**
     * 请求右侧Banner数据
     */
    private void requestCategroyRightBannerData() {

        String url = ApiService.API.BANNER + "?type=1";

        mOkhttpHelper.get(url, new SpotsCallBack<List<Banner>>(getContext()) {
            @Override
            public void onSuccess(Response response, List<Banner> banners) {
                showCategroyRightBannerData(banners);
            }

            @Override
            public void onError(Response response, int code, Exception e) {
                Toast.makeText(getContext(), "分类页面右侧Banner数据请求Json解析出错", Toast.LENGTH_SHORT).show();
            }
        });

    }

    /**
     * 展示右侧轮播图数据
     */
    private void showCategroyRightBannerData(List<Banner> banners) {
        if (banners != null) {
            for (final Banner banner : banners) {
                DefaultSliderView sliderView = new DefaultSliderView(getContext());//DefaultSliderView 不展示描述
                sliderView.image(banner.getImgUrl());//Banner图片地址
                sliderView.setScaleType(BaseSliderView.ScaleType.Fit);//Banner图片填充方式
                mSlider_category.addSlider(sliderView);
                //轮播图点击事件
                sliderView.setOnSliderClickListener(new BaseSliderView.OnSliderClickListener() {
                    @Override
                    public void onSliderClick(BaseSliderView slider) {
                        Toast.makeText(getActivity(), "点击了页面" + banner.getName(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
        mSlider_category.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);//默认：设置指示器在轮播图底部
        mSlider_category.setCustomAnimation(new DescriptionAnimation());//动画
        mSlider_category.setPresetTransformer(SliderLayout.Transformer.Default);//转场动画
        mSlider_category.setDuration(3000);//轮播时长
    }

    /**
     * 下拉刷新、上拉加载
     */
    private void initRefreshAndLoadMore() {
        //下拉刷新
        mSmartrefreshlayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshLayout) {
                //请求数据
                refreshData();
            }
        });

        //上拉加载
        mSmartrefreshlayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshLayout) {
                if (curPage < totalPage) {
                    //请求数据
                    loadMoreData();
                } else {
                    mSmartrefreshlayout.finishLoadMore();
                    Toast.makeText(getContext(), "已经到底了...", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /**
     * 下拉刷新 : 请求数据
     */
    private void refreshData() {
        curPage = 1;
        status = REFRESH;
        requestCategroyRightData(category_id);
    }

    /**
     * 上拉加载 : 请求数据
     */
    private void loadMoreData() {
        curPage = ++curPage;
        status = LOAD_MORE;
        requestCategroyRightData(category_id);
    }

    /**
     * 请求右侧列表数据
     */
    private void requestCategroyRightData(int categroyId) {
        String url = ApiService.API.WARES_LIST + "?categoryId=" + categroyId + "&curPage=" + curPage + "&pageSize=" + pageSize;
        mOkhttpHelper.get(url, new BaseCallBack<Page<CategroyRightListBean>>() {
            @Override
            public void onRequestBefore(Request request) {
            }

            @Override
            public void onFailure(Request request, Exception e) {
            }

            @Override
            public void onSuccess(Response response, Page<CategroyRightListBean> categroyRightListBeanPage) {
                curPage = categroyRightListBeanPage.getCurrentPage();
                totalPage = categroyRightListBeanPage.getTotalPage();
                //展示右侧列表数据
                showCategroyRightData(categroyRightListBeanPage.getList());
            }

            @Override
            public void onError(Response response, int code, Exception e) {
            }

            @Override
            public void onResponse(Response response) {
            }

            @Override
            public void onErrorToken(Response response, int code) {
            }
        });
    }

    /**
     * 展示右侧列表数据
     */
    private void showCategroyRightData(List<CategroyRightListBean> list) {
        switch (status) {
            case NORMAL://正常请求
                if (mCategroyRightAdapter == null){
                    mCategroyRightAdapter = new CategroyRightAdapter(getContext(), list);
                    mRecycler_view_right.setAdapter(mCategroyRightAdapter);
                    mRecycler_view_right.setLayoutManager(new GridLayoutManager(getContext(), 2));
                    mRecycler_view_right.setItemAnimator(new DefaultItemAnimator());
                    mRecycler_view_right.addItemDecoration(new DividerGridItemDecoration(getContext()));
                }else {
                    mCategroyRightAdapter.clear();
                    mCategroyRightAdapter.addData(list);
                }
                break;
            case REFRESH://下拉刷新
                mCategroyRightAdapter.clear();
                mCategroyRightAdapter.addData(list);
                mRecycler_view_right.scrollToPosition(0);
                mSmartrefreshlayout.finishRefresh();
                break;
            case LOAD_MORE://上拉加载
                mCategroyRightAdapter.addData(mCategroyRightAdapter.getDatas().size(), list);
                mRecycler_view_right.scrollToPosition(mCategroyRightAdapter.getDatas().size());
                mSmartrefreshlayout.finishLoadMore();
                break;
        }
    }

}



