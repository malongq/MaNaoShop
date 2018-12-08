package com.manao.manaoshop.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.manao.manaoshop.R;
import com.manao.manaoshop.activity.NewOrderActivity;
import com.manao.manaoshop.adapter.DividerItemDecortionMl;
import com.manao.manaoshop.adapter.ShopCarAdapter;
import com.manao.manaoshop.base.basefragment.BaseFragment;
import com.manao.manaoshop.bean.ShoppingCart;
import com.manao.manaoshop.bean.User;
import com.manao.manaoshop.http.ApiService;
import com.manao.manaoshop.http.OkHttpHelper;
import com.manao.manaoshop.http.SpotsCallBack;
import com.manao.manaoshop.utils.ShopCarProvider;
import com.manao.manaoshop.weiget.MaNaoToolbar;

import org.xutils.view.annotation.ViewInject;

import java.util.List;

import okhttp3.Response;


/**
 * Created by Malong
 * on 18/11/5.
 * 购物车
 */
public class CartFragment extends BaseFragment implements View.OnClickListener {

    @ViewInject(R.id.manao_toobar)
    private MaNaoToolbar mToolbar;

    @ViewInject(R.id.recycler_view)
    private RecyclerView mRecyclerView;

    @ViewInject(R.id.checkbox_all)
    private CheckBox mCheckBox;

    @ViewInject(R.id.txt_total)
    private TextView mTextTotal;

    @ViewInject(R.id.btn_order)
    private Button mBtnOrder;

    @ViewInject(R.id.btn_del)
    private Button mBtnDel;

    private ShopCarProvider provider;
    private ShopCarAdapter mAdapter;

    public static final int ACTION_EDIT = 1;//编辑
    public static final int ACTION_CAMPLATE = 2;//完成

    @Override
    protected View CreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_cart, container, false);
    }

    /**
     * 加载Toolbar
     */
    @Override
    public void initToolbar() {
        mToolbar.hideSearchView();
        mToolbar.showTitleView();
        mToolbar.setTitle(R.string.cart);
        mToolbar.hideRightButton();
        mToolbar.setRightButtonText("编辑");
        mToolbar.getRightButton().setOnClickListener(this);
        mToolbar.getRightButton().setTag(ACTION_EDIT);
    }

    @Override
    protected void init() {
        provider = new ShopCarProvider(getContext());//创建provider

        //展示购物车数据
        showData();

        mBtnDel.setOnClickListener(this);//删除
        mBtnOrder.setOnClickListener(this);//去结算
    }

    /**
     * 展示购物车数据
     */
    private void showData() {
        List<ShoppingCart> carts = provider.getAll();
        mAdapter = new ShopCarAdapter(getContext(), carts, mCheckBox, mTextTotal);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.addItemDecoration(new DividerItemDecortionMl(getContext(), DividerItemDecortionMl.VERTICAL_LIST));
    }

    /**
     * 刷新数据--提供给MainActivity刷新该页面调用
     */
    public void refData() {
        mAdapter.clear();
        List<ShoppingCart> carts = provider.getAll();
        mAdapter.addData(carts);
        mAdapter.showTotalPrice();
    }

    /**
     * 点击事件，如果是编辑就调 清除选中方法 否则调 全选方法
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.toolbar_rightButton://右上角 编辑--完成
                int action = (int) v.getTag();
                if (ACTION_EDIT == action) {
                    showDelControl();
                } else if (ACTION_CAMPLATE == action) {
                    hideDelControl();
                }
                break;
            case R.id.btn_del://删除
                mAdapter.delCart();
                break;
            case R.id.btn_order://去结算
                /**-----------------测试token没有了后，返回401，402，403，则直接调起登录页面------------------**/
//                testToken();
                Intent intent = new Intent(getActivity(), NewOrderActivity.class);
                startActivity(intent,true);
                break;

        }
    }

    /**-----------------测试token没有了后，返回401，402，403，则直接调起登录页面------------------**/
    private OkHttpHelper ok = OkHttpHelper.getInstance();
    private void testToken() {
        ok.get(ApiService.API.USER_DETAILS, new SpotsCallBack<User>(getActivity()) {

            @Override
            public void onSuccess(Response response, User user) {
                Log.i("去结算", "onSuccess: ");
            }

            @Override
            public void onError(Response response, int code, Exception e) {
                Log.i("去结算", "onError: ");
            }
        });
    }
    /**-----------------测试token没有了后，返回401，402，403，则直接调起登录页面------------------**/

    /**
     * 点击右上角编辑   清除选中方法
     */
    private void showDelControl() {
        mToolbar.getRightButton().setText("完成");
        mTextTotal.setVisibility(View.GONE);
        mBtnOrder.setVisibility(View.GONE);
        mBtnDel.setVisibility(View.VISIBLE);
        mToolbar.getRightButton().setTag(ACTION_CAMPLATE);
        mAdapter.checkAll_None(false);//把商品全部设置未选中
        mCheckBox.setChecked(false);//把全选按钮也设置未选中
    }

    /**
     * 点击右上角完成   全部选中方法
     */
    private void hideDelControl() {
        mTextTotal.setVisibility(View.VISIBLE);
        mBtnOrder.setVisibility(View.VISIBLE);
        mBtnDel.setVisibility(View.GONE);
        mToolbar.setRightButtonText("编辑");
        mToolbar.getRightButton().setTag(ACTION_EDIT);
        mAdapter.checkAll_None(true);
        mAdapter.showTotalPrice();
        mCheckBox.setChecked(true);
    }

}

