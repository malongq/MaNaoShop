package com.izk.frescodemo;

import android.app.Application;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.imagepipeline.core.ImagePipelineConfig;
import com.facebook.imagepipeline.decoder.SimpleProgressiveJpegConfig;

import org.xutils.x;

/**
 * Created by Malong
 * on 18/11/14.
 */
public class FrescoApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        //配置代码不写也可以，根据需求配置
        ImagePipelineConfig config = ImagePipelineConfig.newBuilder(this)
                .setProgressiveJpegConfig(new SimpleProgressiveJpegConfig())
                .build();
        Fresco.initialize(this,config);
        x.Ext.init(this);
        x.Ext.setDebug(BuildConfig.DEBUG);// 是否输出debug日志, 开启debug会影响性能
    }
}
