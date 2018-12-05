package com.manao.manaoshop.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.manao.manaoshop.Constants;
import com.manao.manaoshop.R;
import com.manao.manaoshop.bean.Wares;
import com.manao.manaoshop.http.ApiService;
import com.manao.manaoshop.utils.ShopCarProvider;
import com.manao.manaoshop.utils.ToastUtils;
import com.manao.manaoshop.weiget.MaNaoToolbar;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.Serializable;

import dmax.dialog.SpotsDialog;


/**
 * Created by Malong
 * on 18/12/5.
 * 商品详情页面 H5
 */
public class WaresActivity extends AppCompatActivity{

    @ViewInject(R.id.manao_toobar)
    private MaNaoToolbar maNaoToolbar;

    @ViewInject(R.id.web)
    private WebView webView;
    private AppInterface appInterface;

    private Wares wares;
    private ShopCarProvider provider;
//    private SpotsDialog mDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.waresactivity);

        x.view().inject(this);

        Intent intent = getIntent();
        Serializable data = intent.getSerializableExtra(Constants.WARE);
        if (data == null){
            this.finish();
        }
        wares = (Wares) data;

//        mDialog = new SpotsDialog(this,"loading....");
//        mDialog.show();

        provider = new ShopCarProvider(this);

        //加载ToolBar
        initToolBar(this);

        //加载WebView
        initWebView();
    }

    //加载ToolBar
    private void initToolBar(final Context context) {
        maNaoToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WaresActivity.this.finish();
            }
        });
        maNaoToolbar.setRightButtonText("分享");
        maNaoToolbar.setRightButtonOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtils.show(context,"分享");
//                showShare();
            }
        });
    }

    //加载WebView
    private void initWebView() {
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);//必须的
        settings.setBlockNetworkImage(false);//设置为fasle，可以加载图片，否则图片显示不了
        settings.setAppCacheEnabled(true);//设置上缓存

        webView.loadUrl(ApiService.API.WARES_DETAIL);//加载地址

        appInterface = new AppInterface(this);

        webView.addJavascriptInterface(appInterface,"appInterface");//与H5通信接口桥梁
        webView.setWebViewClient(new WebAppClient());//WebViewClient主要帮助WebView处理各种通知、请求事件
    }

    //与H5通信接口桥梁
    class AppInterface {

        private Context mContext;
        public AppInterface(Context context) {
            this.mContext = context;
        }

        //调用H5方法
        @JavascriptInterface
        public void showDetail(){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    webView.loadUrl("javascript:showDetail("+wares.getId()+")");
                }
            });
        }

        //调用Android方法
        @JavascriptInterface
        public void buy(long id){
            provider.put(wares);
            ToastUtils.show(mContext,"已添加到购物车");
        }

        //调用Android方法
        @JavascriptInterface
        public void addFavorites(long id){
            ToastUtils.show(mContext,"收藏");
        }

    }

    //WebViewClient主要帮助WebView处理各种通知、请求事件
    class  WebAppClient extends WebViewClient{

        //网页加载完成回调
        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);

            //if (mDialog != null && mDialog.isShowing()){
            //mDialog.dismiss();
            //}

            //调用h5方法--展示H5页面元素
            appInterface.showDetail();
        }
    }

}
