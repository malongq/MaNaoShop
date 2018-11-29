package com.manao.manaoshop.weiget;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.TintTypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.manao.manaoshop.R;

/**
 * Created by Malong
 * on 18/11/28.
 * 自定义布局购物车加减框
 */
public class AddOrSubLayout extends LinearLayout implements View.OnClickListener {

    private Button btn_add;
    private Button btn_sub;
    private TextView tv_num;
    private LayoutInflater inflater;

    public int getValue() {
        String val = tv_num.getText().toString();
        if (!TextUtils.isEmpty(val)) {
            this.value = Integer.parseInt(val);
        }
        return value;
    }

    public void setValue(int value) {
        tv_num.setText(value + "");
        this.value = value;
    }

    public int getMinValue() {
        return minValue;
    }

    public void setMinValue(int minValue) {
        this.minValue = minValue;
    }

    public int getMaxValue() {
        return maxValue;
    }

    public void setMaxValue(int maxValue) {
        this.maxValue = maxValue;
    }

    private int value;
    private int minValue;
    private int maxValue;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public AddOrSubLayout(Context context) {
        this(context, null);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public AddOrSubLayout(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @SuppressLint("RestrictedApi")
    public AddOrSubLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        inflater = LayoutInflater.from(context);//布局填充者
        //加载UI
        initView();

        if (attrs != null) {
            TintTypedArray a = TintTypedArray.obtainStyledAttributes(context, attrs, R.styleable.AddOrSub, defStyleAttr, 0);

            int val = a.getInt(R.styleable.AddOrSub_tv_num, 0);
            setValue(val);

            int minVal = a.getInt(R.styleable.AddOrSub_tv_min_num, 0);
            setMinValue(minVal);

            int maxVal = a.getInt(R.styleable.AddOrSub_tv_max_num, 0);
            setMaxValue(maxVal);

            Drawable color_add = a.getDrawable(R.styleable.AddOrSub_btn_add_color);
            setButtonAddBackgroud(color_add);

            Drawable color_tv = a.getDrawable(R.styleable.AddOrSub_tv_color);
            setTexViewtBackground(color_tv);

            Drawable color_sub = a.getDrawable(R.styleable.AddOrSub_btn_sub_color);
            setButtonSubBackgroud(color_sub);

            a.recycle();
        }

    }

    //加载布局UI
    public void initView() {
        View view = inflater.inflate(R.layout.add_or_sub_layout, this, true);
        btn_add = view.findViewById(R.id.btn_add);
        btn_sub = view.findViewById(R.id.btn_sub);
        tv_num = view.findViewById(R.id.tv_num);
        btn_add.setOnClickListener(this);
        btn_sub.setOnClickListener(this);
    }

    //点击事件
    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.btn_add:
                //点击一次+1
                numAdd();
                if (mOnClickAddOrSubListener != null) {
                    mOnClickAddOrSubListener.onAddClick(v, value);
                }
                break;
            case R.id.btn_sub:
                //点击一次-1
                numSub();
                if (mOnClickAddOrSubListener != null) {
                    mOnClickAddOrSubListener.onSubClick(v, value);
                }
                break;
        }
    }

    //点击一次-1
    private void numSub() {
        if (minValue < value) {
            value = value - 1;
        }else {
            Toast.makeText(getContext(),"已经是最小了！",Toast.LENGTH_SHORT).show();
        }
        tv_num.setText(value + "");
    }

    //点击一次+1
    private void numAdd() {
        if (value < maxValue) {
            value = value + 1;
        }else {
            Toast.makeText(getContext(),"已经是最大了！",Toast.LENGTH_SHORT).show();
        }
        tv_num.setText(value + "");
    }

    public void setOnClickAddOrSubListener(OnClickAddOrSubListener onClickAddOrSubListener) {
        this.mOnClickAddOrSubListener = onClickAddOrSubListener;
    }

    OnClickAddOrSubListener mOnClickAddOrSubListener;

    public interface OnClickAddOrSubListener {
        void onAddClick(View v, int value);

        void onSubClick(View v, int value);
    }

    /**---------下面是设置自定义控件背景及大小的方法----------**/
    public void setTexViewtBackground(Drawable drawable) {
        tv_num.setBackgroundDrawable(drawable);
    }

    public void setTextViewBackground(int drawableId) {

        setTexViewtBackground(getResources().getDrawable(drawableId));

    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void setButtonAddBackgroud(Drawable backgroud) {
        btn_add.setBackground(backgroud);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public void setButtonSubBackgroud(Drawable backgroud) {
        btn_sub.setBackground(backgroud);
    }


}
