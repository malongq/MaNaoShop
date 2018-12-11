package com.manao.manaoshop.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.Html;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.manao.manaoshop.Constants;
import com.manao.manaoshop.MaNaoAppaplication;
import com.manao.manaoshop.MainActivity;
import com.manao.manaoshop.R;
import com.manao.manaoshop.base.baseactivity.BaseActivity;
import com.manao.manaoshop.bean.LoginRespMsg;
import com.manao.manaoshop.bean.User;
import com.manao.manaoshop.http.ApiService;
import com.manao.manaoshop.http.OkHttpHelper;
import com.manao.manaoshop.http.SpotsCallBack;
import com.manao.manaoshop.utils.CountTimerView;
import com.manao.manaoshop.utils.DESUtil;
import com.manao.manaoshop.utils.ToastUtils;
import com.manao.manaoshop.weiget.LoginEditLayout;
import com.manao.manaoshop.weiget.MaNaoToolbar;

import org.json.JSONObject;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.HashMap;
import java.util.Map;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import cn.smssdk.utils.SMSLog;
import dmax.dialog.SpotsDialog;
import okhttp3.Response;

/**
 * Created by Malong
 * on 18/12/10.
 * 用户注册2页面
 */
public class RegisterTwoActivity extends BaseActivity {

    @ViewInject(R.id.manao_toobar)
    private MaNaoToolbar mMaNaoToolbar;

    @ViewInject(R.id.txtTip)
    private TextView mTxtTip;

    @ViewInject(R.id.edittxt_code)
    private LoginEditLayout mEtCode;

    @ViewInject(R.id.btn_reSend)
    private TextView mBtnResend;

    private String phone;
    private String pwd;
    private String countryCode;
    private CountTimerView countTimerView;
    private OkHttpHelper okHttpHelper = OkHttpHelper.getInstance();
    private AlertDialog dialog;

    private SMSEvenHanlder evenHanlder;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_reg_two);

        //引入xUtils
        x.view().inject(this);

        //加载toolbar
        initToolbar();

        phone = getIntent().getStringExtra("phone");
        pwd = getIntent().getStringExtra("pwd");
        countryCode = getIntent().getStringExtra("countryCode");

        String formatedPhone = "+" + countryCode + " " + splitPhoneNum(phone);///** 分割电话号码 */
        String text = getString(R.string.smssdk_resend_identify_code) + formatedPhone;
        mTxtTip.setText(Html.fromHtml(text));

        CountTimerView timerView = new CountTimerView(mBtnResend);
        timerView.start();

        //信息发送回调函数
        evenHanlder = new SMSEvenHanlder();
        SMSSDK.registerEventHandler(evenHanlder);

        dialog = new SpotsDialog.Builder().setContext(this).build();
        dialog.setMessage("正在校验验证码...");

        //完成的点击方法
        mBtnResend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SMSSDK.getVerificationCode("+"+countryCode, phone);
                countTimerView = new CountTimerView(mBtnResend,R.string.smssdk_resend_identify_code2);
                countTimerView.start();

                dialog.setMessage("正在重新获取验证码");
                dialog.show();
            }
        });
    }

    /**
     * 分割电话号码
     */
    private String splitPhoneNum(String phone) {
        StringBuilder builder = new StringBuilder(phone);
        builder.reverse();
        for (int i = 4, len = builder.length(); i < len; i += 5) {
            builder.insert(i, ' ');
        }
        builder.reverse();
        return builder.toString();
    }

    //加载toolbar
    private void initToolbar() {
        mMaNaoToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RegisterTwoActivity.this.finish();
            }
        });
        mMaNaoToolbar.hideSearchView();
        mMaNaoToolbar.setRightButtonText("完成");
        mMaNaoToolbar.setRightButtonOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //注册完成函数
                registerTwo();
            }
        });
    }

    //注册完成函数
    private void registerTwo() {
        String vCode = mEtCode.getText().toString().trim();
        if (TextUtils.isEmpty(vCode)) {
            ToastUtils.show(this, R.string.smssdk_resend_identify_code);
            return;
        }
        SMSSDK.submitVerificationCode(countryCode,phone,vCode);
        dialog.show();
    }

    //信息发送回调函数
    class SMSEvenHanlder extends EventHandler {

        @Override
        public void afterEvent(final int event, final int result, final Object data) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (dialog != null && dialog.isShowing()) {
                        dialog.dismiss();
                    }

                    if (result == SMSSDK.RESULT_COMPLETE) {
                        if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
                            //提交注册
                            doReg();
                            dialog.setMessage("正在提交注册信息");
                            dialog.show();
                        }
                    } else {
                        // 根据服务器返回的网络错误，给toast提示
                        try {
                            ((Throwable) data).printStackTrace();
                            Throwable throwable = (Throwable) data;
                            JSONObject object = new JSONObject(throwable.getMessage());
                            String des = object.optString("detail");
                            if (!TextUtils.isEmpty(des)) {
                                ToastUtils.show(RegisterTwoActivity.this, des);
                                return;
                            }
                        } catch (Exception e) {
                            SMSLog.getInstance().w(e);
                        }
                    }
                }
            });
        }
    }

    //提交注册
    private void doReg() {
        Map<String, String> params = new HashMap<>(2);
        params.put("phone", phone);
        params.put("password", DESUtil.encode(Constants.DES_KEY, pwd));
        okHttpHelper.post(ApiService.API.REGISTER, params, new SpotsCallBack<LoginRespMsg<User>>(this) {

            @Override
            public void onSuccess(Response response, LoginRespMsg<User> userLoginRespMsg) {
                if (dialog != null && dialog.isShowing()) {
                    dialog.dismiss();
                }
                if (userLoginRespMsg.getStatus() == LoginRespMsg.STATUS_ERROR) {
                    ToastUtils.show(RegisterTwoActivity.this, "注册失败:" + userLoginRespMsg.getMessage());
                    return;
                }
                MaNaoAppaplication application = MaNaoAppaplication.getInstance();
                application.putUser(userLoginRespMsg.getData(), userLoginRespMsg.getToken());
                startActivity(new Intent(RegisterTwoActivity.this, MainActivity.class));
                finish();
            }

            @Override
            public void onError(Response response, int code, Exception e) {}

            @Override
            public void onErrorToken(Response response, int code) {
                super.onErrorToken(response, code);
                ToastUtils.show(RegisterTwoActivity.this, "注册失败:" + code);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SMSSDK.unregisterEventHandler(evenHanlder);
    }

}
