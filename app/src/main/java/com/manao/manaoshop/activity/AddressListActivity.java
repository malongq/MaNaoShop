package com.manao.manaoshop.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.manao.manaoshop.Constants;
import com.manao.manaoshop.MaNaoAppaplication;
import com.manao.manaoshop.R;
import com.manao.manaoshop.adapter.AddressListAdapter;
import com.manao.manaoshop.adapter.DividerItemDecortionMl;
import com.manao.manaoshop.base.baseactivity.BaseActivity;
import com.manao.manaoshop.bean.Address;
import com.manao.manaoshop.bean.BaseRespMsg;
import com.manao.manaoshop.http.ApiService;
import com.manao.manaoshop.http.OkHttpHelper;
import com.manao.manaoshop.http.SpotsCallBack;
import com.manao.manaoshop.utils.JSONUtils;
import com.manao.manaoshop.utils.ToastUtils;
import com.manao.manaoshop.weiget.MaNaoToolbar;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Response;

/**
 * Created by Malong
 * on 18/12/12.
 * 收货地址页面
 */
@ContentView(R.layout.activity_address_list)
public class AddressListActivity extends BaseActivity {

    @ViewInject(R.id.toolbar)
    private MaNaoToolbar toolbar;

    @ViewInject(R.id.recycler_view)
    private RecyclerView recyclerView;

    private AddressListAdapter adapter;
    private OkHttpHelper okHttpHelper = OkHttpHelper.getInstance();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        x.view().inject(this);

        //加载toolbar
        initToolbar();

        //加载UI数据
        init();
    }

    //加载toolbar
    private void initToolbar() {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddressListActivity.this.finish();
            }
        });
        toolbar.hideSearchView();
        toolbar.setRightButtonText("新增地址");
        toolbar.setRightButtonOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddressListActivity.this, AddressAddActivity.class);
                startActivityForResult(intent, Constants.REQUEST_CODE);//要返回的时候更新地址页面
            }
        });
    }

    //回调
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //如果符合则更新页面
        if (requestCode == Constants.REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            init();
        }
    }

    //加载UI数据
    private void init() {
        Map<String, Object> params = new HashMap<>(1);
        params.put("user_id", MaNaoAppaplication.getInstance().getUser().getId());
        okHttpHelper.get(ApiService.API.ADDRESS_LIST, params, new SpotsCallBack<List<Address>>(this) {
            @Override
            public void onSuccess(Response response, List<Address> address) {
                if (response.isSuccessful()) {
                    showAddress(address);
                }
            }

            @Override
            public void onError(Response response, int code, Exception e) {

            }

            @Override
            public void onErrorToken(Response response, int code) {
                super.onErrorToken(response, code);
            }
        });
    }

    //展示数据
    private void showAddress(final List<Address> addresses) {
        Collections.sort(addresses);
        if (adapter == null) {
            adapter = new AddressListAdapter(this, addresses, new AddressListAdapter.AddAdapterListener() {
                //点击设为默认
                @Override
                public void setDefault(Address address) {
                    //设置为默认地址--更新地址页面
                    updateAddress(address);
                }

                //点击编辑
                @Override
                public void editAddress(Address address) {
                    //要返回的时候更新地址页面
                    Intent intent = new Intent(AddressListActivity.this, AddressAddActivity.class);
                    intent.putExtra("name",MaNaoAppaplication.getInstance().getUser().getUsername());
                    intent.putExtra("phone", address.getPhone());//电话
                    intent.putExtra("addr", address.getAddr());//地址
                    startActivityForResult(intent, Constants.REQUEST_CODE);
                }

                //点击删除
                @Override
                public void delAddress(Address address,int position) {
                    ToastUtils.show(AddressListActivity.this,"删除数据接口报错了");
                    deleteAddress();//设置为默认地址--删除地址页面
                }
            });
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            recyclerView.addItemDecoration(new DividerItemDecortionMl(this, LinearLayoutManager.HORIZONTAL));
            recyclerView.setItemAnimator(new DefaultItemAnimator());
            recyclerView.setAdapter(adapter);
        } else {
            adapter.refreshData(addresses);
            recyclerView.setAdapter(adapter);
        }
    }

    //设置为默认地址--更新地址页面
    private void updateAddress(Address address) {
        Map<String, Object> params = new HashMap<>(6);
        params.put("id", address.getId().toString());
        params.put("consignee", address.getConsignee());
        params.put("phone", address.getPhone());
        params.put("addr", address.getAddr());
        params.put("zip_code", address.getZipCode());
        params.put("is_default", address.getIsDefault());
        okHttpHelper.post(ApiService.API.ADDRESS_UPDATE, params, new SpotsCallBack<BaseRespMsg>(this) {

            @Override
            public void onSuccess(Response response, BaseRespMsg baseRespMsg) {
                if (baseRespMsg.getStatus() == BaseRespMsg.STATUS_SUCCESS) {
                    init();
                }
            }

            @Override
            public void onError(Response response, int code, Exception e) {

            }

            @Override
            public void onErrorToken(Response response, int code) {
                super.onErrorToken(response, code);
            }
        });
    }

    //设置为默认地址--删除地址页面
    private void deleteAddress() {
        Map<String, Object> params = new HashMap<>(1);
        params.put("user_id", MaNaoAppaplication.getInstance().getUser().getId());
        okHttpHelper.post(ApiService.API.ADDRESS_DELETE, params, new SpotsCallBack<BaseRespMsg>(this) {

            @Override
            public void onSuccess(Response response, BaseRespMsg baseRespMsg) {
                if (baseRespMsg.getStatus() == BaseRespMsg.STATUS_SUCCESS) {
                    init();
                }
            }

            @Override
            public void onError(Response response, int code, Exception e) {
                Log.i("", "onError: ");
            }

            @Override
            public void onErrorToken(Response response, int code) {
                super.onErrorToken(response, code);
            }
        });
    }

}
