package com.manao.manaoshop.http;

import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.manao.manaoshop.MaNaoAppaplication;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * 网络请求管理类
 */
public class OkHttpHelper {

    private static final String TAG = "OkHttpHelper";
    private static final int TOKEN_ERROE_ONE = 401;//TOKEN 出错
    private static final int TOKEN_ERROE_TWO = 402;//TOKEN 过期
    private static final int TOKEN_ERROE_THREE = 403;//TOKEN 丢失
    private static OkHttpHelper mInstance;
    private OkHttpClient mHttpClient;
    private Gson mGson;
    private Handler mHandler;

    /**
     * 构造函数 创建 OkHttpClient  Gson  Handler
     */
    private OkHttpHelper() {
        mHttpClient = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .build();
        mGson = new Gson();
        mHandler = new Handler(Looper.getMainLooper());
    }

    /**
     * 饿汉式（静态代码块）
     * 单例模式，将该类构建单例，节约开支
     * 优点：这种写法比较简单，就是在类装载的时候就完成实例化。避免了线程同步问题。
     * 缺点：在类装载的时候就完成实例化，没有达到Lazy Loading的效果。如果从始至终从未使用过这个实例，则会造成内存的浪费。
     */
    //    static {
//        mInstance = new OkHttpHelper();
//    }
//
//    public static OkHttpHelper getInstance() {
//        return mInstance;
//    }

    /**
     * 静态内部类
     * 这种方式跟饿汉式方式采用的机制类似，但又有不同。两者都是采用了类装载的机制来保证初始化实例时只有一个线程
     * 不同的地方在饿汉式方式是只要Singleton类被装载就会实例化，没有Lazy-Loading的作用，
     * 而静态内部类方式在Singleton类被装载时并不会立即实例化，而是在需要实例化时，
     * 调用getInstance方法，才会装载SingletonInstance类，从而完成Singleton的实例化。
     * 类的静态属性只会在第一次加载类的时候初始化，所以在这里，
     * JVM帮助我们保证了线程的安全性，在类进行初始化时，别的线程是无法进入的。
     * 优点：避免了线程不安全，延迟加载，效率高。
     */
    private static class SingletonInstance {
        private static final OkHttpHelper INSTANCE = new OkHttpHelper();
    }

    public static OkHttpHelper getInstance() {
        return SingletonInstance.INSTANCE;
    }

    /**
     * GET 请求  请求添加token后改装
     *
     * @param url
     * @param callback
     */
    public void get(String url, Map<String, String> param, BaseCallBack callback) {
        Log.i(TAG, "get: " + url);
        Request request = buildGetRequest(url, param);
        request(request, callback);
    }

    /**
     * GET 请求  请求添加token后改装  重载上面方法
     *
     * @param url
     * @param callback
     */
    public void get(String url, BaseCallBack callback) {
        get(url, null, callback);
    }

    /**
     * GET  请求添加token后改装
     *
     * @param url
     * @return
     */
    private Request buildGetRequest(String url, Map<String, String> param) {
        return buildRequest(url, HttpMethodType.GET, param);
    }

    //拼装url参数
    private String buildUrlParams(String url, Map<String, String> param) {
        if (param == null) {
            param = new HashMap<>(1);
        }
        String token = MaNaoAppaplication.getInstance().getToken();
        if (!TextUtils.isEmpty(token)) {
            param.put("token", token);
        }
        StringBuffer sb = new StringBuffer();
        for (Map.Entry<String, String> entry : param.entrySet()) {
            sb.append(entry.getKey() + "=" + entry.getValue());
            sb.append("&");
        }
        String s = sb.toString();
        if (s.endsWith("&")) {
            s = s.substring(0, s.length() - 1);
        }
        if (url.indexOf("?") > 0) {
            url = url + "&" + s;
        } else {
            url = url + "?" + s;
        }
        return url;
    }

    /**
     * POST 请求
     *
     * @param url
     * @param param
     * @param callback
     */
    public void post(String url, Map<String, String> param, BaseCallBack callback) {
        Request request = buildPostRequest(url, param);
        request(request, callback);
    }

    /**
     * POST
     *
     * @param url
     * @param params
     * @return
     */
    private Request buildPostRequest(String url, Map<String, String> params) {
        return buildRequest(url, HttpMethodType.POST, params);
    }

    /**
     * 判断是 GET / POST
     *
     * @param url
     * @param methodType
     * @param params
     * @return
     */
    private Request buildRequest(String url, HttpMethodType methodType, Map<String, String> params) {
        Request.Builder builder = new Request.Builder().url(url);
        if (methodType == HttpMethodType.POST) {
            RequestBody body = builderFormData(params);
            builder.post(body);
        } else if (methodType == HttpMethodType.GET) {
            String s = buildUrlParams(url, params);
            builder.url(s);
            builder.get();
        }
        return builder.build();
    }

    /**
     * 如果是POST请求则需要添加body进去
     *
     * @param params
     * @return
     */
    private RequestBody builderFormData(Map<String, String> params) {
        FormBody.Builder builder = new FormBody.Builder();
        if (params != null) {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                builder.add(entry.getKey(), entry.getValue());
            }

            //添加token
            String token = MaNaoAppaplication.getInstance().getToken();
            if (!TextUtils.isEmpty(token)) {
                builder.add("token", token);
            }
        }
        return builder.build();
    }

    /**
     * 枚举 区分是  GET / POST
     */
    enum HttpMethodType {
        GET,
        POST,
    }

    /**
     * 请求所用的参数，开始请求
     *
     * @param request
     * @param callback
     */
    public void request(final Request request, final BaseCallBack callback) {
        //加载前开启dialog
        callback.onRequestBefore(request);
        //开始请求
        mHttpClient.newCall(request).enqueue(new Callback() {
            //请求出错
            @Override
            public void onFailure(Call call, IOException e) {
                callback.onFailure(request, e);
            }

            //请求完成，但是分情况，有可能返回错误码
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //加载完毕关闭dialog
                callback.onResponse(response);
                //真正的请求成功
                if (response.isSuccessful()) {
                    String resultStr = response.body().string();
                    Log.d(TAG, "result=" + resultStr);
                    //判断是否包含json
                    if (callback.type == String.class) {
                        callbackSuccess(callback, response, resultStr);
                    } else {
                        try {
                            Object obj = mGson.fromJson(resultStr, callback.type);
                            callbackSuccess(callback, response, obj);
                        } catch (com.google.gson.JsonParseException e) { // Json解析的错误
//                            callback.onError(response, response.code(), e);
                            callbackError(callback, response, e);
                        }
                    }
                } else if (response.code() == TOKEN_ERROE_ONE || response.code() == TOKEN_ERROE_TWO || response.code() == TOKEN_ERROE_THREE) {
                    callbackTokenFailure(callback, response);
                } else {
                    callbackError(callback, response, null);
                }
            }
        });
    }

    /**
     * token请求失败--在主线程更新UI
     *
     * @param callback
     * @param response
     */
    private void callbackTokenFailure(final BaseCallBack callback, final Response response) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                callback.onErrorToken(response, response.code());
                Log.i(TAG, "response: " + response.toString());
            }
        });
    }

    /**
     * 请求成功--在主线程更新UI
     *
     * @param callback
     * @param response
     * @param obj
     */
    private void callbackSuccess(final BaseCallBack callback, final Response response, final Object obj) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                callback.onSuccess(response, obj);
                Log.i(TAG, "response: " + response.toString());
            }
        });
    }

    /**
     * 请求失败--在主线程更新UI
     *
     * @param callback
     * @param response
     */
    private void callbackError(final BaseCallBack callback, final Response response, final Exception e) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                callback.onError(response, response.code(), e);
            }
        });
    }

}
