package com.manao.manaoshop.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.manao.manaoshop.R;
import com.manao.manaoshop.adapter.DividerItemDecortionMl;
import com.manao.manaoshop.adapter.ShopCarAdapter;
import com.manao.manaoshop.bean.ShoppingCart;
import com.manao.manaoshop.utils.ShopCarProvider;
import com.manao.manaoshop.weiget.MaNaoToolbar;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.List;


/**
 * Created by Malong
 * on 18/11/5.
 * 购物车
 */
public class CartFragment extends Fragment implements View.OnClickListener {

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

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_cart, container, false);

        x.view().inject(this, view);//xUtils引入

        initToolbar();//加载Toolbar

        provider = new ShopCarProvider(getContext());//创建provider

        //展示购物车数据
        showData();

        mBtnDel.setOnClickListener(this);
        mBtnOrder.setOnClickListener(this);

        return view;
    }

    /**
     * 加载Toolbar
     */
    private void initToolbar() {
        mToolbar.hideSearchView();
        mToolbar.showTitleView();
        mToolbar.setTitle(R.string.cart);
        mToolbar.hideRightButton();
        mToolbar.setRightButtonText("编辑");
        mToolbar.getRightButton().setOnClickListener(this);
        mToolbar.getRightButton().setTag(ACTION_EDIT);
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
            case R.id.toolbar_rightButton:
                int action = (int) v.getTag();
                if (ACTION_EDIT == action) {
                    showDelControl();
                } else if (ACTION_CAMPLATE == action) {
                    hideDelControl();
                }
                break;
            case R.id.btn_del:
                mAdapter.delCart();
                break;
        }
    }

    /**
     * 点击右上角编辑   清除选中方法
     */
    private void showDelControl() {
        mToolbar.getRightButton().setText("完成");
        mTextTotal.setVisibility(View.GONE);
        mBtnOrder.setVisibility(View.GONE);
        mBtnDel.setVisibility(View.VISIBLE);
        mToolbar.getRightButton().setTag(ACTION_CAMPLATE);
        mAdapter.checkAll_None(false);
        mCheckBox.setChecked(false);
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

