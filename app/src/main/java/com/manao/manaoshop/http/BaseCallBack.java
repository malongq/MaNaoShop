package com.manao.manaoshop.http;

import com.google.gson.internal.$Gson$Types;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Malong
 * on 18/11/13.
 * 请求返回 CallBack 处理类
 */
public abstract class BaseCallBack<T> {

    public Type type;

    //目的是把 <T> 转换成 type
    static Type getSuperclassTypeParameter(Class<?> subclass){
        Type superclass = subclass.getGenericSuperclass();
        if (superclass instanceof Class){
            throw new RuntimeException("Missing type parameter...");
        }
        ParameterizedType parameterized = (ParameterizedType) superclass;
        return $Gson$Types.canonicalize(parameterized.getActualTypeArguments()[0]);
    }

    public BaseCallBack() {
        type = getSuperclassTypeParameter(getClass());
    }

    public abstract void onRequestBefore(Request request);//加载前放loading

    public abstract void onFailure(Request request,Exception e);//请求直接错误，请求网络时出现不可恢复的错误时调用该方法

    public abstract void onSuccess(Response response, T t);//请求网络成功时调用该方法  Http 状态码大于200并且小于300表走该方法，正常拿到数据，其它错误码走else

    public abstract void onError(Response response, int code, Exception e);//其它错误码走onError  状态码400，404，403，500等时调用此方法

    public abstract void onResponse(Response response);//请求成功时调用此方法

}
