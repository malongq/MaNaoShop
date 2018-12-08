package com.manao.manaoshop.http;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;

import com.manao.manaoshop.MaNaoAppaplication;
import com.manao.manaoshop.R;
import com.manao.manaoshop.activity.LoginActivity;
import com.manao.manaoshop.utils.ToastUtils;

import dmax.dialog.SpotsDialog;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Malong
 * on 18/11/13.
 * 请求时的dialog
 */
public abstract class SpotsCallBack<T> extends BaseCallBack<T> {

    private AlertDialog dialog;
    private Context mContext;

    public SpotsCallBack(Context context) {
        mContext = context;
        initSpotsDialog();
    }

    private void initSpotsDialog(){
        dialog = new SpotsDialog.Builder().setContext(mContext).build();
        dialog.setMessage("努力加载中...");
    }

    public void dismissDialog() {
        if (dialog != null) {
            dialog.dismiss();
        }
    }

    public void showDialog() {
        dialog.show();
    }

    public void setMessage(String message) {
        dialog.setMessage(message);
    }

    @Override
    public void onRequestBefore(Request request) {
        showDialog();
    }

    @Override
    public void onFailure(Request request, Exception e) {
        dismissDialog();
    }

    @Override
    public void onResponse(Response response) {
        dismissDialog();
    }

    @Override
    public void onErrorToken(Response response, int code) {
        //token出错，跳转进登录页面，并清空原理保存到本地的用户数据
        ToastUtils.show(mContext, R.string.token_error);
        Intent intent = new Intent(mContext, LoginActivity.class);
        mContext.startActivity(intent);
        MaNaoAppaplication.getInstance().clearUser();

    }
}
