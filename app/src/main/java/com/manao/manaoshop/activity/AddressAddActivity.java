package com.manao.manaoshop.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectChangeListener;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.google.gson.Gson;
import com.manao.manaoshop.MaNaoAppaplication;
import com.manao.manaoshop.R;
import com.manao.manaoshop.base.baseactivity.BaseActivity;
import com.manao.manaoshop.bean.AddressBean;
import com.manao.manaoshop.bean.BaseRespMsg;
import com.manao.manaoshop.city.XmlParserHandler;
import com.manao.manaoshop.city.model.CityModel;
import com.manao.manaoshop.city.model.DistrictModel;
import com.manao.manaoshop.city.model.ProvinceModel;
import com.manao.manaoshop.http.ApiService;
import com.manao.manaoshop.http.OkHttpHelper;
import com.manao.manaoshop.http.SpotsCallBack;
import com.manao.manaoshop.utils.GetJsonDataUtil;
import com.manao.manaoshop.utils.ToastUtils;
import com.manao.manaoshop.weiget.LoginEditLayout;
import com.manao.manaoshop.weiget.MaNaoToolbar;

import org.json.JSONArray;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import okhttp3.Response;

/**
 * Created by Malong
 * on 18/12/12.
 * 添加收货地址页面
 */
public class AddressAddActivity extends BaseActivity {

    @ViewInject(R.id.txt_address)
    private TextView mTxtAddress;

    @ViewInject(R.id.edittxt_consignee)
    private LoginEditLayout mEditConsignee;

    @ViewInject(R.id.edittxt_phone)
    private LoginEditLayout mEditPhone;

    @ViewInject(R.id.edittxt_add)
    private LoginEditLayout mEditAddr;

    @ViewInject(R.id.toolbar)
    private MaNaoToolbar mToolBar;

    private OptionsPickerView mCityPikerView;

    private ArrayList<AddressBean> options1Items = new ArrayList<>();
    private ArrayList<ArrayList<String>> options2Items = new ArrayList<>();
    private ArrayList<ArrayList<ArrayList<String>>> options3Items = new ArrayList<>();

    private OkHttpHelper mHttpHelper = OkHttpHelper.getInstance();
    private Thread thread;
    private static final int MSG_LOAD_DATA = 0x0001;
    private static final int MSG_LOAD_SUCCESS = 0x0002;
    private static final int MSG_LOAD_FAILED = 0x0003;

    private boolean isLoaded = false;
    private String name;
    private String phone;
    private String addr;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_address_add);

        x.view().inject(this);

        //加载toolbar
        initToolbar();

        //一进来就开始handler
        mHandler.sendEmptyMessage(MSG_LOAD_DATA);

        Intent intent = getIntent();
        name = intent.getStringExtra("name");
        phone = intent.getStringExtra("phone");
        addr = intent.getStringExtra("addr");
        if (!TextUtils.isEmpty(name)){
            mEditConsignee.setText(name);
            mEditPhone.setText(phone);
            mTxtAddress.setText(addr);
        }

        //加载UI数据
        init();
    }

    //加载UI数据
    private void init() {
        mTxtAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hintKeyBoard();//关闭软键盘
                if (isLoaded) {
                    showPickerView();
                } else {
                    Toast.makeText(AddressAddActivity.this, "正在解析省市县三级列表，请稍后", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    //关闭软键盘
    public void hintKeyBoard() {
        //拿到InputMethodManager
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        //如果window上view获取焦点 && view不为空
        if (imm.isActive() && getCurrentFocus() != null) {
            //拿到view的token 不为空
            if (getCurrentFocus().getWindowToken() != null) {
                //表示软键盘窗口总是隐藏，除非开始时以SHOW_FORCED显示。
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }

    //Handler
    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_LOAD_DATA:
                    if (thread == null) {//如果已创建就不再重新创建子线程了
                        Toast.makeText(AddressAddActivity.this, "Begin Parse Data", Toast.LENGTH_SHORT).show();
                        thread = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                // 子线程中解析省市区数据
                                initJsonData();
                            }
                        });
                        thread.start();
                    }
                    break;
                case MSG_LOAD_SUCCESS:
                    Toast.makeText(AddressAddActivity.this, "Parse Succeed", Toast.LENGTH_SHORT).show();
                    isLoaded = true;
                    break;
                case MSG_LOAD_FAILED:
                    Toast.makeText(AddressAddActivity.this, "Parse Failed", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    // 子线程中解析省市区数据
    private void initJsonData() {
        /**
         * 注意：assets 目录下的Json文件仅供参考，实际使用可自行替换文件
         * 关键逻辑在于循环体
         * */
        String JsonData = new GetJsonDataUtil().getJson(this, "province.json");//获取assets目录下的json文件数据
        ArrayList<AddressBean> jsonBean = parseData(JsonData);//用Gson 转成实体
        /**
         * 添加省份数据
         * 注意：如果是添加的JavaBean实体，则实体类需要实现 IPickerViewData 接口，
         * PickerView会通过getPickerViewText方法获取字符串显示出来。
         */
        options1Items = jsonBean;
        for (int i = 0; i < jsonBean.size(); i++) {//遍历省份
            ArrayList<String> CityList = new ArrayList<>();//该省的城市列表（第二级）
            ArrayList<ArrayList<String>> Province_AreaList = new ArrayList<>();//该省的所有地区列表（第三极）
            for (int c = 0; c < jsonBean.get(i).getCityList().size(); c++) {//遍历该省份的所有城市
                String CityName = jsonBean.get(i).getCityList().get(c).getName();
                CityList.add(CityName);//添加城市
                ArrayList<String> City_AreaList = new ArrayList<>();//该城市的所有地区列表
                //如果无地区数据，建议添加空字符串，防止数据为null 导致三个选项长度不匹配造成崩溃
                if (jsonBean.get(i).getCityList().get(c).getArea() == null
                        || jsonBean.get(i).getCityList().get(c).getArea().size() == 0) {
                    City_AreaList.add("");
                } else {
                    City_AreaList.addAll(jsonBean.get(i).getCityList().get(c).getArea());
                }
                Province_AreaList.add(City_AreaList);//添加该省所有地区数据
            }
            /**
             * 添加城市数据
             */
            options2Items.add(CityList);
            /**
             * 添加地区数据
             */
            options3Items.add(Province_AreaList);
        }
        mHandler.sendEmptyMessage(MSG_LOAD_SUCCESS);
    }

    //用Gson 转成实体
    public ArrayList<AddressBean> parseData(String result) {//Gson 解析
        ArrayList<AddressBean> detail = new ArrayList<>();
        try {
            JSONArray data = new JSONArray(result);
            Gson gson = new Gson();
            for (int i = 0; i < data.length(); i++) {
                AddressBean entity = gson.fromJson(data.optJSONObject(i).toString(), AddressBean.class);
                detail.add(entity);
            }
        } catch (Exception e) {
            e.printStackTrace();
            mHandler.sendEmptyMessage(MSG_LOAD_FAILED);
        }
        return detail;
    }

    //加载toolbar
    private void initToolbar() {
        mToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mToolBar.setRightButtonText("保存");
        mToolBar.setRightButtonOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createAddress();
            }
        });
    }

    //创建新地址
    public void createAddress() {
        String consignee = mEditConsignee.getText().toString();
        String phone = mEditPhone.getText().toString();
        String address = mTxtAddress.getText().toString() + mEditAddr.getText().toString();

        Map<String, Object> params = new HashMap<>(1);
        params.put("user_id", MaNaoAppaplication.getInstance().getUser().getId());
        params.put("consignee", consignee);
        params.put("phone", phone);
        params.put("addr", address);
        params.put("zip_code", "000000");

        mHttpHelper.post(ApiService.API.ADDRESS_CREATE, params, new SpotsCallBack<BaseRespMsg>(this) {

            @Override
            public void onSuccess(Response response, BaseRespMsg baseRespMsg) {
                if (baseRespMsg.getStatus() == BaseRespMsg.STATUS_SUCCESS) {
                    setResult(RESULT_OK);
                    finish();
                }
            }

            @Override
            public void onError(Response response, int code, Exception e) {

            }

            @Override
            public void onErrorToken(Response response, int code) {
                super.onErrorToken(response, code);
            }
        });

    }

    // 弹出选择器
    private void showPickerView() {
        OptionsPickerView pvOptions = new OptionsPickerBuilder(this, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                //返回的分别是三个级别的选中位置
                String tx = options1Items.get(options1).getPickerViewText() +
                        options2Items.get(options1).get(options2) +
                        options3Items.get(options1).get(options2).get(options3);
                mTxtAddress.setText(tx);
            }
        }).setTitleText("城市选择")
                .setDividerColor(Color.BLACK)
                .setTextColorCenter(Color.BLACK) //设置选中项文字颜色
                .setContentTextSize(20)
                .build();

        /*pvOptions.setPicker(options1Items);//一级选择器
        pvOptions.setPicker(options1Items, options2Items);//二级选择器*/
        pvOptions.setPicker(options1Items, options2Items, options3Items);//三级选择器
        pvOptions.show();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
        }
    }

}
