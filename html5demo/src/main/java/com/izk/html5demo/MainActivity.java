package com.izk.html5demo;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private Button btn_ml;
    private WebView web_ml;
    private WebAppInterface appInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_ml = findViewById(R.id.btn_ml);
        btn_ml.setOnClickListener(this);

        web_ml = findViewById(R.id.web_ml);
        web_ml.loadUrl("file:///android_asset/index.html");
        web_ml.getSettings().setJavaScriptEnabled(true);
        appInterface = new WebAppInterface(this);
        web_ml.addJavascriptInterface(appInterface,"app");
    }

    @Override
    public void onClick(View v) {
        //安卓调Web
        appInterface.showName(btn_ml.getText().toString());
    }

    //希望一个内部类用于存放 Android 与 Web 互调的函数
    class WebAppInterface{

        private Context mCxt;
        public WebAppInterface(Context cxt) {
            this.mCxt = cxt;
        }

        //Web调安卓
        @JavascriptInterface
        public void sayHello(String str){
            Toast.makeText(mCxt,"str="+str,Toast.LENGTH_SHORT).show();
        }

        //安卓调Web
        public void showName(final String str){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    web_ml.loadUrl("Javascript:androidToWeb('"+str+"')");
                }
            });
        }

    }


}
