package com.izk.myapplication;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

/**
 * Created by Malong
 * on 18/11/9.
 */
public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHodler> implements View.OnClickListener {

    private LayoutInflater inflater;
    private ViewHodler hodler;
    private List<String> mdata;
    private Context context;

    /**
     * 传递过来的数据
     *
     * @param mdata
     * @param context
     */
    public MyAdapter(List<String> mdata, Context context) {
        this.mdata = mdata;
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    /**
     * 创建Holder
     *
     * @param parent
     * @param viewType
     * @return
     */
    @NonNull
    @Override
    public ViewHodler onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.rv_item, parent, false);
        view.setOnClickListener(this);
        return new ViewHodler(view);
    }

    /**
     * 绑定Holder与数据并显示到View上
     *
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(@NonNull ViewHodler holder, int position) {
        holder.itemView.setTag(position);
        holder.tv_rv.setText(mdata.get(position));
    }

    /**
     * 拿到数据长度
     *
     * @return
     */
    @Override
    public int getItemCount() {
        return mdata.size();
    }

    /**
     * Holder内部类，查找Item_Id
     */
    class ViewHodler extends RecyclerView.ViewHolder {
        private TextView tv_rv;
        public ViewHodler(View itemView) {
            super(itemView);
            tv_rv = itemView.findViewById(R.id.tv_rv);
            //条目内的子View点击事件
            tv_rv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(context, tv_rv.getText().toString().trim(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    /**
     * 1.创建接口条目点击
     */
    private OnItemClickListener listener;

    interface OnItemClickListener {
        void onClick(View view, int position);
    }

    /**
     * 2.创建一个对外的public点击方法
     *
     * @param listener
     */
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    /**
     * 3.让类实现View.OnClickListener 并重写方法条目点击事件
     */
    @Override
    public void onClick(View v) {
        if (listener != null) {
            int position = (int) v.getTag();
            listener.onClick(v, position);
        }
    }

    /**
     * 指定条目添加数据
     *
     * @param position 角标位置
     * @param txt
     */
    public void addPosition(int position, String txt) {
        mdata.add(position, txt);
        notifyItemInserted(position);
    }

    /**
     * 指定条目删除数据
     *
     * @param position 角标位置
     */
    public void removePosition(int position) {
        mdata.remove(position);
        notifyItemRemoved(position);
    }

}
