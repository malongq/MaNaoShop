package com.manao.manaoshop.activity;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.manao.manaoshop.BuildConfig;
import com.manao.manaoshop.Constants;
import com.manao.manaoshop.MaNaoAppaplication;
import com.manao.manaoshop.R;
import com.manao.manaoshop.adapter.WareOrderAdapter;
import com.manao.manaoshop.adapter.layoutmanager.FullyLinearLayoutManager;
import com.manao.manaoshop.base.baseactivity.BaseActivity;
import com.manao.manaoshop.bean.BaseRespMsg;
import com.manao.manaoshop.bean.Charge;
import com.manao.manaoshop.bean.CreateOrderRespMsg;
import com.manao.manaoshop.bean.ShoppingCart;
import com.manao.manaoshop.http.ApiService;
import com.manao.manaoshop.http.OkHttpHelper;
import com.manao.manaoshop.http.SpotsCallBack;
import com.manao.manaoshop.utils.JSONUtils;
import com.manao.manaoshop.utils.ShopCarProvider;
import com.manao.manaoshop.utils.ToastUtils;
import com.manao.manaoshop.weiget.MaNaoToolbar;
import com.pingplusplus.android.PaymentActivity;
import com.pingplusplus.android.Pingpp;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import okhttp3.Response;

/**
 * Created by Malong
 * on 18/12/8.
 * 提交订单页面
 */
public class CreateOrderActivity extends BaseActivity implements View.OnClickListener {

    /**
     * 银联支付渠道
     */
    private static final String CHANNEL_UPACP = "upacp";
    /**
     * 微信支付渠道
     */
    private static final String CHANNEL_WECHAT = "wx";
    /**
     * 支付支付渠道
     */
    private static final String CHANNEL_ALIPAY = "alipay";

    @ViewInject(R.id.toolbar)
    private MaNaoToolbar mMaNaoToolbar;

    @ViewInject(R.id.txt_order)
    private TextView txtOrder;//订单列表文字

    @ViewInject(R.id.recycler_view)
    private RecyclerView mRecyclerView;//订单列表图片

    @ViewInject(R.id.rl_alipay)
    private RelativeLayout mLayoutAlipay;//支付宝支付

    @ViewInject(R.id.rl_wechat)
    private RelativeLayout mLayoutWechat;//微信支付

    @ViewInject(R.id.rl_bd)
    private RelativeLayout mLayoutBd;//银联支付

    @ViewInject(R.id.rb_alipay)
    private RadioButton mRbAlipay;//支付宝选中按钮

    @ViewInject(R.id.rb_webchat)
    private RadioButton mRbWechat;//微信选中按钮

    @ViewInject(R.id.rb_bd)
    private RadioButton mRbBd;//银联选中按钮

    @ViewInject(R.id.btn_createOrder)
    private Button mBtnCreateOrder;//银提交订单按钮

    @ViewInject(R.id.txt_total)
    private TextView mTxtTotal;//总价钱

    OkHttpHelper okHttpHelper = OkHttpHelper.getInstance();//创建网络请求

    private ShopCarProvider provider;//数据存储类

    private WareOrderAdapter mAdapter;//列表商品图片

    private String orderNum;//订单id

    private String payChannel = CHANNEL_ALIPAY;//默认选择支付宝支付

    private float amount;//总价钱

    private HashMap<String, RadioButton> channels = new HashMap<>(3);//三个支付渠道选择要互斥，所以创建一个map保存

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_create_order);

        //引入xUtils
        x.view().inject(this);

        //展示商品数据
        showData();

        //初始化
        init();

        //加载toolbar
        initToolbar();

    }

    //展示商品数据
    private void showData() {

        provider = new ShopCarProvider(this);

        mAdapter = new WareOrderAdapter(this, provider.getAll());

        FullyLinearLayoutManager layoutManager = new FullyLinearLayoutManager(this);//解决ScrollView嵌套recyclerView宽高问题
        layoutManager.setOrientation(GridLayoutManager.HORIZONTAL);
        mRecyclerView.setLayoutManager(layoutManager);

        mRecyclerView.setAdapter(mAdapter);

        amount = mAdapter.getTotalPrice();
        mTxtTotal.setText("应付款： ￥" + amount);

    }

    //初始化
    private void init() {

        channels.put(CHANNEL_ALIPAY, mRbAlipay);
        channels.put(CHANNEL_WECHAT, mRbWechat);
        channels.put(CHANNEL_UPACP, mRbBd);

        mLayoutAlipay.setOnClickListener(this);
        mLayoutWechat.setOnClickListener(this);
        mLayoutBd.setOnClickListener(this);
        mBtnCreateOrder.setOnClickListener(this);

    }

    //加载toolbar
    private void initToolbar() {
        mMaNaoToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateOrderActivity.this.finish();
            }
        });
        mMaNaoToolbar.hideSearchView();
        mMaNaoToolbar.hideRightButton();
    }

    //点击支付渠道，互斥，只能选中一个
    @Override
    public void onClick(View v) {

        int id = v.getId();
        switch (id) {
            case R.id.rl_alipay:
            case R.id.rl_wechat:
            case R.id.rl_bd:
                String tag = (String) v.getTag();//设置一个tag，在布局里设置的，要跟最上面的内容一致才可以
                for (Map.Entry<String, RadioButton> entry : channels.entrySet()) {
                    payChannel = tag;
                    RadioButton button = entry.getValue();
                    if (entry.getKey().equals(tag)) {
                        boolean checked = button.isChecked();
                        button.setChecked(!checked);
                    } else {
                        button.setChecked(false);
                    }
                }
                break;
            case R.id.btn_createOrder:
                postNewOrder();//发起支付
                break;
        }

    }

    //发起支付
    private void postNewOrder() {
        final List<ShoppingCart> datas = mAdapter.getDatas();
        List<WareItem> items = new ArrayList<>(datas.size());//发起支付时需要有这一种格式
        //遍历元素并装入 WareItem
        for (ShoppingCart c : datas) {
            WareItem item = new WareItem(c.getId(), c.getPrice().intValue());
            items.add(item);
        }

        String json = JSONUtils.toJSON(items);//将其转换为string

        HashMap<String, String> params = new HashMap<>(5);//初始化参数
        params.put("user_id", MaNaoAppaplication.getInstance().getUser().getId() + "");
        params.put("item_json", json);
        params.put("pay_channel", payChannel);
        params.put("amount", (int) amount + "");
        params.put("addr_id", 1 + "");

        mBtnCreateOrder.setEnabled(false);//在发起支付的时候不可以再点击提交订单按钮

        //请求网络
        okHttpHelper.post(ApiService.API.ORDER_CREATE, params, new SpotsCallBack<CreateOrderRespMsg>(this) {

            @Override
            public void onSuccess(Response response, CreateOrderRespMsg respMsg) {

//                provider.delete((ShoppingCart) provider.getAll());//清空购物车信息

                mBtnCreateOrder.setEnabled(true);//发起支付请求成功后可以点击
                orderNum = respMsg.getData().getOrderNum();
                Charge charge = respMsg.getData().getCharge();
                //请求后台API成功后，获取支付Charge,去调起支付SDK API发起支付
                openPaymentActivity(JSONUtils.toJSON(charge));
            }

            @Override
            public void onError(Response response, int code, Exception e) {
                mBtnCreateOrder.setEnabled(true);//发起支付请求失败后可以点击
            }

            @Override
            public void onErrorToken(Response response, int code) {
                super.onErrorToken(response, code);
            }
        });

    }

    //请求后台API成功后，获取支付Charge,去调起支付SDK API发起支付
    private void openPaymentActivity(String s) {
        Pingpp.createPayment(this,s);
        if (BuildConfig.DEBUG){
            Pingpp.DEBUG = true;//打开Ping++ log日志
        }
    }

    //intent回调 然后直接进入支付结果页面
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Constants.REQUEST_CODE_PAYMENT && resultCode == Activity.RESULT_OK) {
            String pay_result = data.getExtras().getString("pay_result");
            /**
             *  处理返回值
             * "success" - payment succeed
             * "fail"    - payment failed
             * "cancel"  - user canceld
             * "invalid" - payment plugin not installed 不合法
             *
             * 如果是银联渠道返回 invalid，调用 UPPayAssistEx.installUPPayPlugin(this); 安装银联安全支付控件。
             */
            if (pay_result.equals("success"))
                changeOrderStatus(1);//处理不同返回值得函数
            else if (pay_result.equals("fail"))
                changeOrderStatus(-1);//处理不同返回值得函数
            else if (pay_result.equals("cancel"))
                changeOrderStatus(-2);//处理不同返回值得函数
            else
                changeOrderStatus(0);//处理不同返回值得函数

        }
    }

    //处理不同返回值得函数
    private void changeOrderStatus(final int status) {

        Map<String, String> params = new HashMap<>(5);
        params.put("order_num", orderNum);
        params.put("status", status + "");

        okHttpHelper.post(ApiService.API.ORDER_COMPLEPE, params, new SpotsCallBack<BaseRespMsg>(this) {
            @Override
            public void onSuccess(Response response, BaseRespMsg o) {
                toPayResultActivity(status);//进入支付完成的结果页
            }

            @Override
            public void onError(Response response, int code, Exception e) {
                toPayResultActivity(-1);//进入支付完成的结果页
            }
        });

    }

    //进入支付完成的结果页
    private void toPayResultActivity(int status) {
        Intent intent = new Intent(this, PayResultActivity.class);
        intent.putExtra("status", status);
        startActivity(intent);
        this.finish();
    }

    //发起支付时需要有这一种格式
    class WareItem {
        private Long ware_id;
        private int amount;

        public WareItem(Long ware_id, int amount) {
            this.ware_id = ware_id;
            this.amount = amount;
        }

        public Long getWare_id() {
            return ware_id;
        }

        public void setWare_id(Long ware_id) {
            this.ware_id = ware_id;
        }

        public int getAmount() {
            return amount;
        }

        public void setAmount(int amount) {
            this.amount = amount;
        }
    }


}
