package com.manao.manaoshop.utils;

import android.content.Context;
import android.widget.Toast;

import com.manao.manaoshop.bean.Page;
import com.manao.manaoshop.http.OkHttpHelper;
import com.manao.manaoshop.http.SpotsCallBack;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Response;

/**
 * Created by Malong
 * on 18/12/3.
 * 创建一个分页工具类
 */
public class PageUtIls {

    private static Builder builder;

    private OkHttpHelper httpHelper;

    private static final int NORMALL = 0;//正常
    private static final int REFRESH = 1;//下拉刷新
    private static final int LOADMORE = 2;//上拉加载
    private int state = NORMALL;//默认是正常进入

    /**
     * 第一步：构造函数。私有化 private ，防止所有在其它类里通过构造方法实例化该类
     */
    private PageUtIls() {
        //初始化请求OkHttpHelper
        httpHelper = OkHttpHelper.getInstance();
        //初始化刷新函数
        initRefreshLayout();
    }

    /**
     * 第三步：创建静态方法，返回值为内部类Builder的方法，并在内部创建 Builder ，这样就可以链式调用了，提供一个公开静态的方法去实例化
     */
    public static Builder newBuilder() {
        builder = new Builder();
        return builder;
    }

    /**
     * 第二步：创建静态内部类Builder
     */
    public static class Builder {

        private Context mContext;
        private Type mType;
        private String mUrl;//url地址
        private HashMap<String, Object> params = new HashMap<>(8);//url参数，一般参数最多几个，所以直接限定初始化的时候8个，防止占用内存
        private int mPage;//url地址参数
        private SmartRefreshLayout mRefreshLayout;//刷新传入的参数UI控件类型
        private OnPageListener onPageListener;
        private int totalPage = 1;
        private int pageIndex = 1;
        private int pageSize = 10;

        /**
         * 第四步：创建返回值为外部类PageUtIls的方法，最后调用完链式方法后，构建该外部类
         */
        public PageUtIls build(Context context, Type type) {
            this.mContext = context;
            this.mType = type;
            valid();//一些判空操作
            return new PageUtIls();
        }

        //一些判空操作
        private void valid() {
            if (this.mContext == null)
                throw new RuntimeException("context can't be null");
            if (this.mUrl == null || "".equals(this.mUrl))
                throw new RuntimeException("url can't be  null");
            if (this.mRefreshLayout == null)
                throw new RuntimeException("MaterialRefreshLayout can't be  null");
        }

        /************************  该类内部链式调用，所有方法返回都是Builder  *************************/
        //url地址
        public Builder setUrl(String url) {
            builder.mUrl = url;
            return builder;
        }

        //页面请求数量
        public Builder setPage(int page) {
            builder.mPage = page;
            return builder;
        }

        //url参数
        public Builder putParam(String key, Object value) {
            params.put(key, value);
            return builder;
        }

        //刷新传入的参数UI控件类型
        public Builder setRefreshLayout(SmartRefreshLayout refreshLayout) {
            this.mRefreshLayout = refreshLayout;
            return builder;
        }

        //加载监听
        public Builder setOnPageListener(OnPageListener onPageListener) {
            this.onPageListener = onPageListener;
            return builder;
        }


    }

    //把请求加入
    class RequestCallBack<T> extends SpotsCallBack<Page<T>> {

        public RequestCallBack(Context context) {
            super(context);
            super.type = builder.mType;
        }

        @Override
        public void onSuccess(Response response, Page<T> tPage) {
            builder.pageIndex = tPage.getCurrentPage();//当前页
            builder.totalPage = tPage.getTotalPage();//总页数
            showData(tPage.getList(), tPage.getCurrentPage(), tPage.getTotalPage(), tPage.getTotalCount());//展示网络请求到的数据
        }

        @Override
        public void onError(Response response, int code, Exception e) {
            Toast.makeText(builder.mContext, "加载出错了", Toast.LENGTH_SHORT).show();
        }

    }

    //初始化刷新函数
    private void initRefreshLayout() {
        //下拉刷新
        builder.mRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshLayout) {
                //请求数据
                refreshData();
            }
        });

        //上拉加载
        builder.mRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshLayout) {
                if (builder.pageIndex < builder.totalPage) {
                    //请求数据
                    loadMoreData();
                } else {
                    builder.mRefreshLayout.finishLoadMore();
                    Toast.makeText(builder.mContext, "已经到底了...", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    //让外界调用请求数据函数
    public void request() {
        requestData();
    }

    //让外界调用传入参数
    public void putParam(String key, Object value) {
        builder.params.put(key, value);
    }

    /**
     * 显示数据
     */
    private <T> void showData(List<T> datas, int currentPage, int totalPage, int totalCount) {

        if (datas == null || datas.size() <= 0) {
            Toast.makeText(builder.mContext, "加载不到数据", Toast.LENGTH_LONG).show();
            return;
        }

        if (NORMALL == state) {
            if (builder.onPageListener != null) {
                builder.onPageListener.load(datas, currentPage, totalPage, totalCount);
            }
        } else if (REFRESH == state) {
            builder.mRefreshLayout.finishRefresh();
            if (builder.onPageListener != null) {
                builder.onPageListener.refresh(datas, currentPage, totalPage, totalCount);
            }

        } else if (LOADMORE == state) {
            builder.mRefreshLayout.finishLoadMore();
            if (builder.onPageListener != null) {
                builder.onPageListener.loadMore(datas, currentPage, totalPage, totalCount);
            }

        }
    }

    //下拉刷新 : 请求数据
    private void refreshData() {
        builder.pageIndex = 1;
        state = REFRESH;
        requestData();
    }

    //上拉加载 : 请求数据
    private void loadMoreData() {
        builder.pageIndex = ++builder.pageIndex;
        state = LOADMORE;
        requestData();
    }

    //请求数据
    private void requestData() {
        String url = buildUrl();
        httpHelper.get(url, new RequestCallBack(builder.mContext));
    }

    //构建URL
    private String buildUrl() {
        return builder.mUrl + "?" + buildUrlParams();
    }

    //拼装url参数
    private String buildUrlParams() {
        HashMap<String, Object> map = builder.params;
        map.put("curPage", builder.pageIndex);
        map.put("pageSize", builder.pageSize);
        StringBuffer sb = new StringBuffer();
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            sb.append(entry.getKey() + "=" + entry.getValue());
            sb.append("&");
        }
        String s = sb.toString();
        if (s.endsWith("&")) {
            s = s.substring(0, s.length() - 1);
        }
        return s;
    }

    //请求类型监听
    public interface OnPageListener<T> {

        void load(List<T> datas, int currentPage, int totalPage, int totalCount);

        void refresh(List<T> datas, int currentPage, int totalPage, int totalCount);

        void loadMore(List<T> datas, int currentPage, int totalPage, int totalCount);

    }


}
