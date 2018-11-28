package com.izk.frescodemo;

import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.controller.AbstractDraweeController;
import com.facebook.drawee.view.SimpleDraweeView;

import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

//Xutils加载布局
@ContentView(R.layout.activity_main)
public class MainActivity extends AppCompatActivity {

    //Xutils注解id控件
    @ViewInject(R.id.simple_drawee_view)
    SimpleDraweeView simpleDraweeView;
    //Xutils注解id控件
    @ViewInject(R.id.simple_drawee_view2)
    SimpleDraweeView simpleDraweeView2;
    //Xutils注解id控件
    @ViewInject(R.id.simple_drawee_view3)
    SimpleDraweeView simpleDraweeView3;
    //Xutils注解id控件
    @ViewInject(R.id.simple_drawee_view4)
    SimpleDraweeView simpleDraweeView4;
    //Xutils注解id控件
    @ViewInject(R.id.simple_drawee_view5)
    SimpleDraweeView simpleDraweeView5;

    private String img_url1 = "http://heilongjiang.sinaimg.cn/2015/0326/U10061P1274DT20150326104659.jpg";
    private String img_url2 = "http://img4q.duitang.com/uploads/item/201411/20/20141120132318_3eAuc.thumb.700_0.jpeg";
    private String img_url3 = "http://hiphotos.baidu.com/%CC%EC%C9%BD%B6%FE%CF%C0%B5%C4%D0%A1%CE%DD/pic/item/70c553e736d12f2e5b0614d64fc2d5628535682a.jpg";
    private String img_url4 = "http://heilongjiang.sinaimg.cn/2015/0326/U10061P1274DT20150326104659.jpg";
    private String img_url5 = "http://hiphotos.baidu.com/%CC%EC%C9%BD%B6%FE%CF%C0%B5%C4%D0%A1%CE%DD/pic/item/70c553e736d12f2e5b0614d64fc2d5628535682a.jpg";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        //加载View
        initView();

    }

    //加载View
    private void initView() {

        //创建将要下载的图片的URI
        Uri uri = Uri.parse(img_url5);
        //开始下载
        simpleDraweeView.setImageURI(uri);

        //创建将要下载的图片的URI
        Uri uri1 = Uri.parse(img_url1);
        //开始下载
        simpleDraweeView2.setImageURI(uri1);

        //创建将要下载的图片的URI
        Uri uri2 = Uri.parse(img_url2);
        //开始下载
        simpleDraweeView3.setImageURI(uri2);

        //创建将要下载的图片的URI
        Uri uri3 = Uri.parse(img_url3);
        //开始下载
        simpleDraweeView4.setImageURI(uri3);

        //创建将要下载的图片的URI
        Uri uri4 = Uri.parse(img_url4);
        //开始下载
        simpleDraweeView5.setImageURI(uri4);

//        AbstractDraweeController controller = Fresco.newDraweeControllerBuilder().setUri(uri1).setAutoPlayAnimations(true).build();
//        simpleDraweeView2.setController(controller);
    }
}
