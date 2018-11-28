package com.manao.manaoshop.http;

import android.app.AlertDialog;
import android.content.Context;
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

}
