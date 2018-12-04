package com.manao.manaoshop.weiget;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.TintTypedArray;
import android.support.v7.widget.Toolbar;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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
    private Button mRightButton;
    private ImageView mImageView;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @SuppressLint("RestrictedApi")
    public MaNaoToolbar(Context context) {
        this(context, null);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @SuppressLint("RestrictedApi")
    public MaNaoToolbar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @SuppressLint("RestrictedApi")
    public MaNaoToolbar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initView();
//        setContentInsetsRelative(10, 10);//设置左右间距
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
                hideRightButton();
            }

            CharSequence rightButtonText = a.getText(R.styleable.MaNaoTooBar_rightButtonText);
            if (rightButtonText != null) {
                setRightButtonText(rightButtonText);
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
            mRightButton = mView.findViewById(R.id.toolbar_rightButton);

            LayoutParams lp = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.CENTER_HORIZONTAL);
            addView(mView, lp);
        }

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

    //展示右侧图标button
    public void showRightButton() {
        if (mRightButton != null)
            mRightButton.setVisibility(VISIBLE);
    }

    //隐藏右侧图标button
    public void hideRightButton() {
        if (mRightButton != null)
            mRightButton.setVisibility(GONE);
    }

    //右侧图标文字button
    public void setRightButtonText(int id) {
        if (mRightButton != null)
            mRightButton.setText(getResources().getString(id));
    }

    //右侧图标文字button
    public void setRightButtonText(CharSequence text) {
        mRightButton.setText(text);
        mRightButton.setVisibility(VISIBLE);
    }

    //获取右侧图标控件button
    public Button getRightButton() {
        return this.mRightButton;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void setRightButtonIcon(Drawable icon) {
        if (mRightButton != null) {
            mRightButton.setBackground(icon);
            mRightButton.setVisibility(VISIBLE);
        }

    }

    public void setRightButtonIcon(int icon) {
        setRightButtonIcon(getResources().getDrawable(icon));
    }

    public void setRightButtonOnClickListener(OnClickListener li) {
        mRightButton.setOnClickListener(li);
    }


}
