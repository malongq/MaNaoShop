package com.manao.manaoshop.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.Indicators.PagerIndicator;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.manao.manaoshop.R;
import com.manao.manaoshop.adapter.DividerItemDecortionMl;
import com.manao.manaoshop.adapter.HomeCategroyAdapter;
import com.manao.manaoshop.bean.Banner;
import com.manao.manaoshop.bean.Campaign;
import com.manao.manaoshop.bean.HomeCampaign;
import com.manao.manaoshop.http.BaseCallBack;
import com.manao.manaoshop.http.HttpApi;
import com.manao.manaoshop.http.OkHttpHelper;
import com.manao.manaoshop.http.SpotsCallBack;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Malong
 * on 18/11/5.
 * 主页
 */
public class HomeFragment extends Fragment {

    private static final String TAG = HomeFragment.class.getSimpleName();

    private SliderLayout slider;
    private PagerIndicator indicator;
    private RecyclerView mRecycle_view;
    private HomeCategroyAdapter mAdapter;
    private List<Banner> mBanner;
    private OkHttpHelper httpHelper = OkHttpHelper.getInstance();

    /**
     * 1. Fragment中onCreate类似于Activity.onCreate，在其中可初始化除了view之外的一切；
     * 2. onCreateView是创建该fragment对应的视图，其中需要创建自己的视图并返回给调用者
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        //查找Id
        initView(view);

        //首页轮播图下面的广告数据
        initRecyclerView();

        //为封装前请求Banner数据
        //requestBanner();

        //封装后请求Banner数据
        requestBannerNew();

        return view;
    }

    /**
     * 查找Id
     *
     * @param view
     */
    private void initView(View view) {
        //上面的轮播图
        slider = view.findViewById(R.id.slider);
        //轮播图的指示器
        indicator = view.findViewById(R.id.indicator);
        //首页轮播图下面的Recycle_view
        mRecycle_view = view.findViewById(R.id.recycleview);
    }

    /**
     * 加载首页轮播图下面的广告数据
     */
    private void initRecyclerView() {

        httpHelper.get(HttpApi.API.HOME_RECRCLER_URL, new BaseCallBack<List<HomeCampaign>>() {
            @Override
            public void onRequestBefore(Request request) {
                Log.i(TAG, "onRequestBefore: 马龙");
            }

            @Override
            public void onFailure(Request request, Exception e) {
                Toast.makeText(getContext(), "onRequestBefore :  加载出错了", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess(Response response, List<HomeCampaign> homeCampaigns) {
                initRecyclerData(homeCampaigns);
            }

            @Override
            public void onError(Response response, int code, Exception e) {
                Toast.makeText(getContext(), "onRequestBefore :  加载出错了", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(Response response) {
                Log.i(TAG, "onResponse: 马龙");
            }
        });

        //造数据
//        List<HomeCategory> datas = new ArrayList<>(15);
//        HomeCategory category = new HomeCategory("热门活动", R.drawable.img_big_1, R.drawable.img_1_small1, R.drawable.img_1_small2);
//        datas.add(category);
//        category = new HomeCategory("有利可图", R.drawable.img_big_4, R.drawable.img_4_small1, R.drawable.img_4_small2);
//        datas.add(category);
//        category = new HomeCategory("品牌街", R.drawable.img_big_2, R.drawable.img_2_small1, R.drawable.img_2_small2);
//        datas.add(category);
//        category = new HomeCategory("金融街 包赚翻", R.drawable.img_big_1, R.drawable.img_3_small1, R.drawable.imag_3_small2);
//        datas.add(category);
//        category = new HomeCategory("超值购", R.drawable.img_big_0, R.drawable.img_0_small1, R.drawable.img_0_small2);
//        datas.add(category);

    }

    /**
     * 加载首页轮播图下面的广告数据
     */
    private void initRecyclerData(List<HomeCampaign> datas) {
        //下面的广告图片
        mAdapter = new HomeCategroyAdapter(datas,getContext());
        mRecycle_view.addItemDecoration(new DividerItemDecortionMl(getContext(), DividerItemDecortionMl.VERTICAL_LIST));//线
        mRecycle_view.setLayoutManager(new LinearLayoutManager(this.getActivity()));//排列方式
        mRecycle_view.setAdapter(mAdapter);
        mAdapter.setmOnCampaignClickListener(new HomeCategroyAdapter.OnCampaignClickListener() {
            @Override
            public void onClick(View view, Campaign campaign) {
                Toast.makeText(getContext(), campaign.getTitle()+"", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 封装后请求Banner数据
     */
    private void requestBannerNew() {
        String url_banner = "http://112.124.22.238:8081/course_api/banner/query?type=1";
        httpHelper.get(url_banner, new SpotsCallBack<List<Banner>>(getContext()) {
            @Override
            public void onSuccess(Response response, List<Banner> banners) {
                mBanner = banners;
                initSlide();
            }

            @Override
            public void onError(Response response, int code, Exception e) {
                Toast.makeText(getContext(), "加载出错了", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 加载首页轮播图数据
     */
    private void initSlide() {
        if (mBanner != null) {
            for (final Banner banner : mBanner) {
                TextSliderView textSliderView = new TextSliderView(this.getActivity());
                textSliderView.image(banner.getImgUrl());//Banner图片地址
                textSliderView.description(banner.getName());//Banner描述
                textSliderView.setScaleType(BaseSliderView.ScaleType.Fit);//Banner图片填充方式
                slider.addSlider(textSliderView);
                //轮播图点击事件
                textSliderView.setOnSliderClickListener(new BaseSliderView.OnSliderClickListener() {
                    @Override
                    public void onSliderClick(BaseSliderView slider) {
                        Toast.makeText(getActivity(), "点击了页面" + banner.getName(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
        //slider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);//默认：设置指示器在轮播图底部
        slider.setCustomIndicator(indicator);//自定义指示器
        slider.setCustomAnimation(new DescriptionAnimation());//动画
        slider.setPresetTransformer(SliderLayout.Transformer.Default);//转场动画
        slider.setDuration(3000);//轮播时长
        //轮播图加载监听方法
        slider.addOnPageChangeListener(new ViewPagerEx.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {//Log.e("HomeFragment", "onPageScrolled: ");
            }

            @Override
            public void onPageSelected(int position) {//Log.e("HomeFragment", "onPageSelected: ");
            }

            @Override
            public void onPageScrollStateChanged(int state) {//Log.e("HomeFragment", "onPageScrollStateChanged: ");
            }
        });
    }

    /**
     * 页面销毁onDestroy
     */
    @Override
    public void onDestroy() {
        slider.stopAutoCycle();//页面销毁关闭轮播
        super.onDestroy();
    }

    /**---------------------OkHttpHelper类  就是依据这个流程封装的，可以参考这个去看-------------------**/
    /**
     * 请求Banner数据
     */
    private Gson mGson = new Gson();

    private void requestBanner() {
        String url_banner = "http://112.124.22.238:8081/course_api/banner/query";
        OkHttpClient client = new OkHttpClient();//创建OkHttpClient连接
        final FormBody body = new FormBody.Builder().add("type", "1").build();//添加参数
        //创建Request
        Request build = new Request.Builder()
                .url(url_banner)
                .post(body)//post请求
                .build();
        //开始异步调用网络请求
        client.newCall(build).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {//请求网络时出现不可恢复的错误时调用该方法
                Log.d(TAG, "Banner数据请求出错了...");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {//请求网络成功时调用该方法

                //Http 状态码大于200并且小于300表走该方法，正常拿到数据，其它错误码走else
                if (response.isSuccessful()) {
                    String reponse_body = response.body().string();//接收数据
                    Log.d(TAG, "requestBanner: " + reponse_body);
                    Type token = new TypeToken<List<Banner>>() {
                    }.getType();//该方法可以创建各种类型接收Type
                    mBanner = mGson.fromJson(reponse_body, token);//将接收到数据转成json
                    //加载首页轮播图数据
                    initSlide();
                }

            }
        });
    }

}
