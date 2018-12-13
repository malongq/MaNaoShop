package com.manao.manaoshop.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.manao.manaoshop.Constants;
import com.manao.manaoshop.MaNaoAppaplication;
import com.manao.manaoshop.R;
import com.manao.manaoshop.base.baseactivity.BaseActivity;
import com.manao.manaoshop.bean.Favorites;
import com.manao.manaoshop.bean.User;
import com.manao.manaoshop.bean.Wares;
import com.manao.manaoshop.http.ApiService;
import com.manao.manaoshop.http.OkHttpHelper;
import com.manao.manaoshop.http.SpotsCallBack;
import com.manao.manaoshop.utils.ShopCarProvider;
import com.manao.manaoshop.utils.ToastUtils;
import com.manao.manaoshop.weiget.MaNaoToolbar;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.onekeyshare.ShareContentCustomizeCallback;
import dmax.dialog.SpotsDialog;
import okhttp3.Response;

/**
 * Created by Malong
 * on 18/12/5.
 * 商品详情页面 H5
 */
public class WaresActivity extends BaseActivity {

    @ViewInject(R.id.manao_toobar)
    private MaNaoToolbar maNaoToolbar;

    @ViewInject(R.id.web)
    private WebView webView;
    private AppInterface appInterface;

    private Wares wares;
    private ShopCarProvider provider;
    private AlertDialog mDialog;
    private OkHttpHelper okHttpHelper = OkHttpHelper.getInstance();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.waresactivity);

        //引入xUtils
        x.view().inject(this);

        Intent intent = getIntent();
        Serializable data = intent.getSerializableExtra(Constants.WARE);
        if (data == null) {
            this.finish();
        }
        wares = (Wares) data;

        mDialog = new SpotsDialog.Builder().setContext(this).build();
        mDialog.setMessage("加载中...");
        mDialog.show();

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
                //分享函数
                showShare();
            }
        });
    }

    //分享函数
    private void showShare() {
        OnekeyShare oks = new OnekeyShare();
        //关闭sso授权
        oks.disableSSOWhenAuthorize();
        oks.setCallback(new PlatformActionListener() {
            @Override
            public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
                Log.d("ShareLogin", "onComplete ---->  分享成功");
                platform.getName();
            }

            @Override
            public void onError(Platform platform, int i, Throwable throwable) {
                Log.d("ShareLogin", "onError ---->  失败" + throwable.getStackTrace());
                Log.d("ShareLogin", "onError ---->  失败" + throwable.getMessage());
                throwable.printStackTrace();
            }

            @Override
            public void onCancel(Platform platform, int i) {
                Log.d("ShareLogin", "onCancel ---->  分享取消");
            }
        });

        oks.setShareContentCustomizeCallback(new ShareContentCustomizeCallback() {
            @Override
            public void onShare(Platform platform, cn.sharesdk.framework.Platform.ShareParams paramsToShare) {
                if ("Dingding".equals(platform.getName())) {
                    paramsToShare.setText("分享到 钉钉 的内容");
                    paramsToShare.setImageUrl(wares.getImgUrl());
                }
                if ("Wechat".equals(platform.getName())) {
                    paramsToShare.setTitle("玛瑙商城");
                    paramsToShare.setText("分享到 微信 的内容");
                    paramsToShare.setImageUrl(wares.getImgUrl());
                    paramsToShare.setUrl("https://github.com/malongq");
                    paramsToShare.setShareType(Platform.SHARE_WEBPAGE);
                    Log.d("ShareSDK", paramsToShare.toMap().toString());
                    Toast.makeText(WaresActivity.this, "点击微信分享啦", Toast.LENGTH_SHORT).show();
                }
                if ("WechatMoments".equals(platform.getName())) {
                    paramsToShare.setTitle("玛瑙商城");
                    paramsToShare.setText("分享到 微信朋友圈 的内容");
                    paramsToShare.setImageUrl(wares.getImgUrl());
                    paramsToShare.setUrl("https://github.com/malongq");
                    paramsToShare.setShareType(Platform.SHARE_WEBPAGE);
                }
                if ("QQ".equals(platform.getName())) {
                    paramsToShare.setTitle("玛瑙商城");
                    paramsToShare.setTitleUrl("https://github.com/malongq");
                    paramsToShare.setText("分享到 QQ 的内容");
//                    paramsToShare.setImageUrl(wares.getImgUrl());
                    String url = "https://hmls.hfbank.com.cn/hfapp-api/9.png";
                    paramsToShare.setImageUrl(url);
                }
            }
        });
        // 启动分享GUI
        oks.show(this);
    }

    //加载WebView
    private void initWebView() {
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);//必须的
        settings.setBlockNetworkImage(false);//设置为fasle，可以加载图片，否则图片显示不了
        settings.setAppCacheEnabled(true);//设置上缓存

        webView.loadUrl(ApiService.API.WARES_DETAIL);//加载地址

        appInterface = new AppInterface(this);

        webView.addJavascriptInterface(appInterface, "appInterface");//与H5通信接口桥梁
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
        public void showDetail() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    webView.loadUrl("javascript:showDetail(" + wares.getId() + ")");
                }
            });
        }

        //调用Android方法
        @JavascriptInterface
        public void buy(long id) {
            provider.put(wares);
            ToastUtils.show(mContext, "已添加到购物车");
        }

        //调用Android方法
        @JavascriptInterface
        public void addFavorites(long id) {
            addToFavorite();
        }

    }

    //WebViewClient主要帮助WebView处理各种通知、请求事件
    class WebAppClient extends WebViewClient {

        //网页加载完成回调
        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);

            if (mDialog != null && mDialog.isShowing()) {
                mDialog.dismiss();
            }

            //调用h5方法--展示H5页面元素
            appInterface.showDetail();
        }
    }

    //收藏方法
    private void addToFavorite(){

        User user = MaNaoAppaplication.getInstance().getUser();
        if(user==null){
            startActivity(new Intent(this,LoginActivity.class),true);
        }
        Long userId = MaNaoAppaplication.getInstance().getUser().getId();
        Map<String, Object> params = new HashMap<>();
        params.put("user_id",userId);
        params.put("ware_id",wares.getId());
        okHttpHelper.post(ApiService.API.FAVORITE_CREATE, params, new SpotsCallBack<List<Favorites>>(this) {
            @Override
            public void onSuccess(Response response, List<Favorites> favorites) {
                ToastUtils.show(WaresActivity.this,"已添加到收藏夹");
            }

            @Override
            public void onError(Response response, int code, Exception e) {
                Log.i("收藏","code:"+code);
            }
        });

    }


}
