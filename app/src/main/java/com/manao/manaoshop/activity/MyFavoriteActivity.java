package com.manao.manaoshop.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.manao.manaoshop.MaNaoAppaplication;
import com.manao.manaoshop.R;
import com.manao.manaoshop.adapter.DividerItemDecortionMl;
import com.manao.manaoshop.adapter.MyFavoriteAdapter;
import com.manao.manaoshop.base.baseactivity.BaseActivity;
import com.manao.manaoshop.bean.Favorites;
import com.manao.manaoshop.http.ApiService;
import com.manao.manaoshop.http.OkHttpHelper;
import com.manao.manaoshop.http.SpotsCallBack;
import com.manao.manaoshop.utils.ToastUtils;
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
 * 我的收藏页面
 */
public class MyFavoriteActivity extends BaseActivity {

    @ViewInject(R.id.toolbar)
    private MaNaoToolbar mToolbar;

    @ViewInject(R.id.recycler_view)
    private RecyclerView recyclerView;

    private MyFavoriteAdapter mAdapter;
    private OkHttpHelper okHttpHelper = OkHttpHelper.getInstance();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_my_favorite);

        x.view().inject(this);

        initToolBar();//加载ToolBar

        getFavorites();;//加载数据UI
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

    private void getFavorites(){

        Long userId = MaNaoAppaplication.getInstance().getUser().getId();
        Map<String, Object> params = new HashMap<>(1);
        params.put("user_id",userId);
        okHttpHelper.get(ApiService.API.FAVORITE_LIST, params, new SpotsCallBack<List<Favorites>>(this) {
            @Override
            public void onSuccess(Response response, List<Favorites> favorites) {
                showFavorites(favorites);//加载数据UI
            }

            @Override
            public void onError(Response response, int code, Exception e) {
                Log.i("MyFavoriteActivity", "code:" + code);
            }
        });
    }

    //加载数据UI
    private void showFavorites(final List<Favorites> favorites) {
        if (mAdapter == null){
            mAdapter = new MyFavoriteAdapter(this,favorites, new MyFavoriteAdapter.OnAdapterClickListener() {
                @Override
                public void clickRemove(View view, int position) {
                    favorites.remove(position);
                    mAdapter.notifyDataSetChanged();
                }

                @Override
                public void clickLike(View view, int position) {
                    ToastUtils.show(MyFavoriteActivity.this,"找相似ing . . .");
                }
            });
            recyclerView.addItemDecoration(new DividerItemDecortionMl(this,DividerItemDecortionMl.VERTICAL_LIST));
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.setAdapter(mAdapter);
        }else {
            mAdapter.refreshData(favorites);
            recyclerView.setAdapter(mAdapter);
        }
    }

}
