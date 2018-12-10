package com.manao.manaoshop.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.manao.manaoshop.R;
import com.manao.manaoshop.base.baseactivity.BaseActivity;
import com.manao.manaoshop.utils.ToastUtils;
import com.manao.manaoshop.weiget.LoginEditLayout;
import com.manao.manaoshop.weiget.MaNaoToolbar;

import org.json.JSONObject;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;
import cn.smssdk.utils.SMSLog;

/**
 * Created by Malong
 * on 18/12/10.
 * 用户注册1页面
 */
public class RegisterOneActivity extends BaseActivity {

    @ViewInject(R.id.manao_toobar)
    private MaNaoToolbar mMaNaoToolbar;

    @ViewInject(R.id.txtCountry)
    private TextView mTxtCountry;

    @ViewInject(R.id.txtCountryCode)
    private TextView mTxtCountryCode;

    @ViewInject(R.id.edittxt_phone)
    private LoginEditLayout mEtxtPhone;

    @ViewInject(R.id.edittxt_pwd)
    private LoginEditLayout mEtxtPwd;

    // 默认使用中国区号
    private static final String DEFAULT_COUNTRY_ID = "42";
    private EventHandler eh;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_reg_one);

        //引入xUtils
        x.view().inject(this);

        //加载toolbar
        initToolbar();

        //信息发送回调函数
        eh = new SMSEvenHanlder();
        SMSSDK.registerEventHandler(eh); //注册短信回调

        //获取国家和国家码
        String[] country = SMSSDK.getCountry(DEFAULT_COUNTRY_ID);
        if (country != null) {
            mTxtCountryCode.setText("+" + country[1]);//国家码
            mTxtCountry.setText(country[0]);//国家
        }

    }

    //信息发送回调函数
    class SMSEvenHanlder extends EventHandler {

        //只实现这一个方法就可以了
        @Override
        public void afterEvent(final int event, final int result, final Object data) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (result == SMSSDK.RESULT_COMPLETE) {
                        if (event == SMSSDK.EVENT_GET_SUPPORTED_COUNTRIES) {
                            //获取支持的国家
                            onCountryListGot((ArrayList<HashMap<String, Object>>) data);
                        } else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
                            /** 请求验证码后，跳转到验证码填写页面 */
                            afterVerificationCodeRequested((Boolean) data);
                        } else if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {

                        }
                    } else {
                        // 根据服务器返回的网络错误，给toast提示
                        try {
                            ((Throwable) data).printStackTrace();
                            Throwable throwable = (Throwable) data;
                            JSONObject object = new JSONObject(throwable.getMessage());
                            String des = object.optString("detail");
                            if (!TextUtils.isEmpty(des)) {
                                ToastUtils.show(RegisterOneActivity.this, des+"马龙 des");
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

    //获取支持的国家
    private void onCountryListGot(ArrayList<HashMap<String, Object>> countries) {
        // 解析国家列表
        for (HashMap<String, Object> country : countries) {
            String code = (String) country.get("zone");
            String rule = (String) country.get("rule");
            if (TextUtils.isEmpty(code) || TextUtils.isEmpty(rule)) {
                continue;
            }
            Log.d("RegisterOneActivity", "code=" + code + "rule=" + rule);
        }
    }

    /**
     * 请求验证码后，跳转到验证码填写页面
     */
    private void afterVerificationCodeRequested(boolean smart) {
        String phone = mEtxtPhone.getText().toString().trim().replaceAll("\\s*", "");
        String code = mTxtCountryCode.getText().toString().trim();
        String pwd = mEtxtPwd.getText().toString().trim();
        if (code.startsWith("+")) {
            code = code.substring(1);
        }
        Intent intent = new Intent(this, RegisterTwoActivity.class);
        intent.putExtra("phone", phone);
        intent.putExtra("pwd", pwd);
        intent.putExtra("countryCode", code);
        startActivity(intent);
    }

    //加载布局
//    @Override
//    public int initLayout() {
//        return R.layout.activity_reg_one;
//    }

    //加载toolbar
    private void initToolbar() {
        mMaNaoToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RegisterOneActivity.this.finish();
            }
        });
        mMaNaoToolbar.hideSearchView();
        mMaNaoToolbar.setRightButtonText("下一步");
        mMaNaoToolbar.setRightButtonOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //注册获取验证码函数
                registerOne();
            }
        });
    }

    //注册获取验证码函数
    private void registerOne() {
        String phone = mEtxtPhone.getText().toString().trim().replaceAll("\\s*", "");
        String code = mTxtCountryCode.getText().toString().trim();
        String pwd = mEtxtPwd.getText().toString().trim();
        checkPhoneNum(phone,code);//校验手机号
        //not 86   +86
        SMSSDK.getVerificationCode(code,phone);
    }

    //校验手机号
    private void checkPhoneNum(String phone, String code) {
        if (code.startsWith("+")) {
            code = code.substring(1);
        }
        if (TextUtils.isEmpty(phone)) {
            ToastUtils.show(this, "请输入手机号码");
            return;
        }
        if (code == "86") {
            if(phone.length() != 11) {
                ToastUtils.show(this,"手机号码长度不对");
                return;
            }
        }
        String rule = "^1(2|3|5|6|7|8|9|4)\\d{9}";
        Pattern p = Pattern.compile(rule);
        Matcher m = p.matcher(phone);
        if (!m.matches()) {
            ToastUtils.show(this,"您输入的手机号码格式不正确");
            return;
        }
    }

    /*****************-------下面两个方法没有用到-------*******************/
    private String[] getCurrentCountry() {
        String mcc = getMCC();
        String[] country = null;
        if (!TextUtils.isEmpty(mcc)) {
            country = SMSSDK.getCountryByMCC(mcc);
        }

        if (country == null) {
            Log.w("SMSSDK", "no country found by MCC: " + mcc);
            country = SMSSDK.getCountry(DEFAULT_COUNTRY_ID);
        }
        return country;
    }

    private String getMCC() {
        TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        // 返回当前手机注册的网络运营商所在国家的MCC+MNC. 如果没注册到网络就为空.
        String networkOperator = tm.getNetworkOperator();
        if (!TextUtils.isEmpty(networkOperator)) {
            return networkOperator;
        }

        // 返回SIM卡运营商所在国家的MCC+MNC. 5位或6位. 如果没有SIM卡返回空
        return tm.getSimOperator();
    }
    /*****************-------上面两个方法没有用到-------*******************/

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SMSSDK.unregisterEventHandler(eh);
    }

}
