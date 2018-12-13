package com.manao.manaoshop.adapter;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.manao.manaoshop.R;
import com.manao.manaoshop.base.baseadapter.BaseAdapter;
import com.manao.manaoshop.base.baseadapter.BaseViewHolder;
import com.manao.manaoshop.base.baseadapter.SimpleAdapter;
import com.manao.manaoshop.bean.Favorites;
import com.manao.manaoshop.bean.Order;
import com.manao.manaoshop.bean.Wares;

import java.util.List;

/**
 * Created by Malong
 * on 18/12/13.
 * 我的收藏adapter
 */
public class MyFavoriteAdapter extends SimpleAdapter<Favorites> implements BaseAdapter.OnItemClickListener {

    private Context mContext;

    public MyFavoriteAdapter(Context context, List<Favorites> datas, OnAdapterClickListener onAdapterClickListener) {
        super(context, R.layout.template_favorite, datas);
        this.mContext = context;
        this.onAdapterClickListener = onAdapterClickListener;
    }

    @Override
    protected void convert(BaseViewHolder viewHolder, final Favorites favorites) {

        Wares wares = favorites.getWares();
        SimpleDraweeView draweeView = (SimpleDraweeView) viewHolder.getView(R.id.drawee_view);
        draweeView.setImageURI(Uri.parse(wares.getImgUrl()));

        viewHolder.getTextView(R.id.text_title).setText(wares.getName());
        viewHolder.getTextView(R.id.text_price).setText("￥ " + wares.getPrice());

        Button buttonRemove = viewHolder.getButton(R.id.btn_remove);
        Button buttonLike = viewHolder.getButton(R.id.btn_like);

        //删除
        buttonRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //1. 这是在adapter里面直接调用删除
//                mDatas.remove(position);
//                notifyDataSetChanged();

                //2. 这是通过写回调方法在Activity里面调用删除
                if (onAdapterClickListener != null){
                    onAdapterClickListener.clickRemove(v, position);
                }

            }
        });

        //找相似
        buttonLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //通过写回调方法在Activity里面调用删除
                if (onAdapterClickListener != null){
                    onAdapterClickListener.clickLike(v, position);
                }

            }
        });

    }

    private int position;

    @Override
    public void onClick(View view, int position) {
        this.position = position;
    }

    public void setOnAdapterClickListener(OnAdapterClickListener onAdapterClickListener) {
        this.onAdapterClickListener = onAdapterClickListener;
    }

    OnAdapterClickListener onAdapterClickListener;

    public interface OnAdapterClickListener {
        void clickRemove(View view, int position);

        void clickLike(View view, int position);
    }


}
