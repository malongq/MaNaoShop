package com.manao.manaoshop.baseadapter;

import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Malong
 * on 18/11/16.
 * 一层列表  使用基类  BaseViewHolder
 */
public class BaseViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    private SparseArray<View> views;//这里传过来的View类型太多，所以用泛型
    private BaseAdapter.OnItemClickListener onItemClickListener;

    //构造函数
    public BaseViewHolder(View itemView, BaseAdapter.OnItemClickListener onItemClickListener) {
        super(itemView);
        this.views = new SparseArray<View>();
        this.onItemClickListener = onItemClickListener;
        itemView.setOnClickListener(this);//条目点击事件
    }

    //这里传过来的View类型太多，所以用泛型
    protected  <T extends View> T retrieveView(int id) {
        View view = views.get(id);
        if (view == null) {
            view = itemView.findViewById(id);
            views.put(id, view);
        }
        return (T) view;
    }

    //获取不同类型View--View
    public View getView(int id) {
        return retrieveView(id);
    }

    //获取不同类型View--TextView
    public TextView getTextView(int id) {
        return retrieveView(id);
    }

    //获取不同类型View--ImageView
    public ImageView getImageView(int id) {
        return retrieveView(id);
    }

    //获取不同类型View--Button
    public Button getButton(int id) {
        return retrieveView(id);
    }

    //条目点击事件
    @Override
    public void onClick(View v) {
        if (onItemClickListener != null){
            onItemClickListener.onClick(v,getLayoutPosition());
        }
    }
}
