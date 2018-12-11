package com.manao.manaoshop.activity;

import android.os.Bundle;
import android.view.View;

import com.manao.manaoshop.R;
import com.manao.manaoshop.base.baseactivity.BaseActivity;
import com.manao.manaoshop.weiget.MaNaoToolbar;

import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

/**
 * Created by Malong
 * on 18/12/8.
 * 支付结果页
 */
public class PayResultActivity extends BaseActivity {

    @ViewInject(R.id.toolbar)
    private MaNaoToolbar mMaNaoToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_result);
        //引入xUtils
        x.view().inject(this);
        //加载toolbar
        initToolbar();
    }

    //返回键
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    //加载toolbar
    private void initToolbar() {
        mMaNaoToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PayResultActivity.this.finish();
            }
        });
        mMaNaoToolbar.hideSearchView();
        mMaNaoToolbar.hideRightButton();
    }

}
