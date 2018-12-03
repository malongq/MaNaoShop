package com.manao.manaoshop.baseadapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Malong
 * on 18/11/16.
 * 一层列表  使用基类  BaseAdapter
 */
public abstract class BaseAdapter<T, H extends BaseViewHolder> extends RecyclerView.Adapter<BaseViewHolder> {

    protected static final String TAG = BaseAdapter.class.getSimpleName();

    //protected 可以让子类使用
    protected List<T> mDatas;
    protected final Context mContext;
    protected int layoutResId;

    public BaseAdapter(Context context, int layoutResId) {
        this(context, layoutResId, null);
    }

    //构造方法，将上下文，泛型数据，要加载的布局id传过来
    public BaseAdapter(Context context, int layoutResId, List<T> datas) {
        this.mDatas = datas == null ? new ArrayList<T>() : datas;
        this.mContext = context;
        this.layoutResId = layoutResId;
    }

    //创建ViewHolder
    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(layoutResId, parent, false);
        BaseViewHolder holder = new BaseViewHolder(view, onItemClickListener);
        return holder;
    }

    //绑定View和数据
    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        T t = getItem(position);
        convert((H) holder, t);
    }

    //获取单个条目的数据
    public T getItem(int position) {
        if (position >= mDatas.size()) return null;
        return mDatas.get(position);
    }

    //多少条
    @Override
    public int getItemCount() {
        if (mDatas != null && mDatas.size() > 0) {
            return mDatas.size();
        }
        return 0;
    }

    //抽象方法，给不同子类实现，具体的holder和数据
    protected abstract void convert(H holder, T t);

    //条目点击事件
    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void onClick(View view, int position);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
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
    public void addData(List<T> datas) {
        addData(0, datas);
    }

    //加载数据
    public void addData(int i, List<T> datas) {
        if (datas != null && datas.size() > 0) {
            mDatas.addAll(datas);
            notifyItemRangeChanged(i, mDatas.size());
        }
    }

    //给HotFragment调用,获取原来页面的数据个数
    public List<T> getDatas() {
        return mDatas;
    }

    //刷新数据
    public void refreshData(List<T> list) {
        if (list != null && list.size() > 0) {
            clear();
            int size = list.size();
            for (int i = 0; i < size; i++) {
                mDatas.add(i, list.get(i));
                notifyItemInserted(i);
            }
        }
    }

    //加载更多数据
    public void loadMoreData(List<T> list) {
        if (list != null && list.size() > 0) {
            int size = list.size();
            int begin = mDatas.size();
            for (int i = 0; i < size; i++) {
                mDatas.add(list.get(i));
                notifyItemInserted(i + begin);
            }
        }
    }

}
