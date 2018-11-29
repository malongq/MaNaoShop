package com.manao.manaoshop.adapter;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.manao.manaoshop.R;
import com.manao.manaoshop.bean.Wares;

import java.util.List;

/**
 * Created by Malong
 * on 18/11/14.
 * 热卖adapter
 * 已经有  HotAdapterNew  类
 */
@Deprecated
public class HotAdapter extends RecyclerView.Adapter<HotAdapter.ViewHolder> {

    private List<Wares> mDatas;
    private LayoutInflater inflater;

    public HotAdapter(List<Wares> wares) {
        this.mDatas = wares;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        inflater = LayoutInflater.from(parent.getContext());//拿上下文Context
        View view = inflater.inflate(R.layout.template_hot_wares, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Wares wares = mDatas.get(position);
        holder.draweeView.setImageURI(Uri.parse(wares.getImgUrl()));
        holder.textTitle.setText(wares.getName());
        holder.textPrice.setText("￥" + wares.getPrice());
    }

    @Override
    public int getItemCount() {
        if (mDatas != null && mDatas.size() > 0) {
            return mDatas.size();
        }
        return 0;
    }

    //下拉刷新清除一下原有数据
    public void clear() {
        mDatas.clear();//原有数据clear
        //如果一个列表删除后请调用
        notifyDataSetChanged();
        //而不要调用这个，会有动画，出现闪屏效果 notifyItemRangeRemoved();
        //notifyItemRangeRemoved(0, mDatas.size());
    }

    //加载数据
    public void addData(List<Wares> datas) {
        addData(0, datas);
    }

    //加载数据
    public void addData(int i, List<Wares> datas) {
        if (datas != null && datas.size() > 0) {
            mDatas.addAll(datas);
            notifyItemRangeChanged(i, mDatas.size());
        }
    }

    //给HotFragment调用,获取原来页面的数据个数
    public List<Wares> getDatas() {
        return mDatas;
    }

    //ViewHolder类
    public class ViewHolder extends RecyclerView.ViewHolder {
        SimpleDraweeView draweeView;
        TextView textTitle;
        TextView textPrice;
        public ViewHolder(View itemView) {
            super(itemView);
            draweeView = itemView.findViewById(R.id.drawee_view);
            textTitle = itemView.findViewById(R.id.text_title);
            textPrice = itemView.findViewById(R.id.text_price);
        }
    }

}
