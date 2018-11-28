package com.manao.manaoshop.weiget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.TintTypedArray;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.manao.manaoshop.R;

/**
 * Created by Malong
 * on 18/11/5.
 * 自定义ToolBar
 */
public class MaNaoToolbar extends Toolbar {

    private LayoutInflater mInflater;
    private View mView;
    private TextView mTextTitle;
    private EditText mSearchView;
    private ImageButton mRightImageButton;

    public MaNaoToolbar(Context context) {
        this(context, null);
    }

    public MaNaoToolbar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    @SuppressLint("RestrictedApi")
    public MaNaoToolbar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initView();
        setContentInsetsRelative(10, 10);//设置左右间距
        if (attrs != null) {
            TintTypedArray a = TintTypedArray.obtainStyledAttributes(getContext(), attrs,
                    R.styleable.MaNaoTooBar, defStyleAttr, 0);

            Drawable rightIcon = a.getDrawable(R.styleable.MaNaoTooBar_rightButtonIcon);
            if (rightIcon != null) {
                setRightButtonIcon(rightIcon);
            }

            boolean isShowSearchView = a.getBoolean(R.styleable.MaNaoTooBar_isShowSearchView, false);
            if (isShowSearchView) {
                showSearchView();
                hideTitleView();
                hideRightImageButton();
            }

            a.recycle();
        }
    }

    //加载自定义ToolBar
    private void initView() {
        if (mView == null) {
            mInflater = LayoutInflater.from(getContext());
            mView = mInflater.inflate(R.layout.toolbar, null);
            mTextTitle = mView.findViewById(R.id.toolbar_title);
            mSearchView = mView.findViewById(R.id.toolbar_searchview);
            mRightImageButton = mView.findViewById(R.id.toolbar_rightButton);
            LayoutParams lp = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.CENTER_HORIZONTAL);
            addView(mView, lp);
        }
    }

    //设置右侧
    public void setRightButtonIcon(Drawable icon) {
        if (mRightImageButton != null) {
            mRightImageButton.setImageDrawable(icon);
            mRightImageButton.setVisibility(VISIBLE);
        }
    }

    //右侧点击事件
    public void setRightButtonOnClickListener(OnClickListener li) {
        mRightImageButton.setOnClickListener(li);
    }

    //重写ToolBar settitle方法
    @Override
    public void setTitle(int resId) {
        setTitle(getContext().getText(resId));
    }

    //重写ToolBar settitle方法
    @Override
    public void setTitle(CharSequence title) {
        initView();
        if (mTextTitle != null) {
            mTextTitle.setText(title);
            showTitleView();
        }
    }

    //展示搜索
    public void showSearchView() {
        if (mSearchView != null)
            mSearchView.setVisibility(VISIBLE);
    }

    //隐藏搜索
    public void hideSearchView() {
        if (mSearchView != null)
            mSearchView.setVisibility(GONE);
    }

    //展示title
    public void showTitleView() {
        if (mTextTitle != null)
            mTextTitle.setVisibility(VISIBLE);
    }

    //隐藏title
    public void hideTitleView() {
        if (mTextTitle != null)
            mTextTitle.setVisibility(GONE);
    }

    //展示右侧图标
    public void showRightImageButton() {
        if (mRightImageButton != null)
            mRightImageButton.setVisibility(VISIBLE);
    }

    //隐藏右侧图标
    public void hideRightImageButton() {
        if (mRightImageButton != null)
            mRightImageButton.setVisibility(GONE);
    }

}
