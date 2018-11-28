package com.manao.manaoshop.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.manao.manaoshop.R;
import com.manao.manaoshop.bean.Campaign;
import com.manao.manaoshop.bean.HomeCampaign;
import com.manao.manaoshop.bean.HomeCategory;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Malong
 * on 18/11/12.
 */
public class HomeCategroyAdapter extends RecyclerView.Adapter<HomeCategroyAdapter.ViewHolder> {

    private static final int CARD_VIEW_L = 0;
    private static final int CARD_VIEW_R = 1;
    private LayoutInflater mlayoutInfater;
    private List<HomeCampaign> mDatas;
    private Context mContext;

    public HomeCategroyAdapter(List<HomeCampaign> datas, Context context){
        mDatas = datas;
        this.mContext = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mlayoutInfater = LayoutInflater.from(parent.getContext());
        if (viewType == CARD_VIEW_L) {//如果等于0，加载第一类型：左边大图个布局，否则加载第二类型
            return new ViewHolder(mlayoutInfater.inflate(R.layout.home_card_left, parent, false));
        }
        return new ViewHolder(mlayoutInfater.inflate(R.layout.home_card_right, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        HomeCampaign campaign = mDatas.get(position);
        holder.textTitle.setText(campaign.getTitle());
        Picasso.with(mContext).load(campaign.getCpOne().getImgUrl()).into(holder.imageViewBig);
        Picasso.with(mContext).load(campaign.getCpTwo().getImgUrl()).into(holder.imageViewSmallTop);
        Picasso.with(mContext).load(campaign.getCpThree().getImgUrl()).into(holder.imageViewSmallBottom);
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (position % 2 == 0) {//取模如果是0  加载第一类型：左边大图个布局，否则加载第二类型
            return CARD_VIEW_R;
        }
        return CARD_VIEW_L;
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView textTitle;
        ImageView imageViewBig;
        ImageView imageViewSmallTop;
        ImageView imageViewSmallBottom;

        public ViewHolder(View itemView) {
            super(itemView);
            textTitle = (TextView) itemView.findViewById(R.id.text_title);
            imageViewBig = (ImageView) itemView.findViewById(R.id.imgview_big);
            imageViewSmallTop = (ImageView) itemView.findViewById(R.id.imgview_small_top);
            imageViewSmallBottom = (ImageView) itemView.findViewById(R.id.imgview_small_bottom);

            imageViewBig.setOnClickListener(this);
            imageViewSmallTop.setOnClickListener(this);
            imageViewSmallBottom.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            HomeCampaign campaign = mDatas.get(getLayoutPosition());
            if (mOnCampaignClickListener != null){
                switch (v.getId()){
                    case R.id.imgview_big:
                        mOnCampaignClickListener.onClick(v,campaign.getCpOne());
                        break;
                    case R.id.imgview_small_top:
                        mOnCampaignClickListener.onClick(v,campaign.getCpTwo());
                        break;
                    case R.id.imgview_small_bottom:
                        mOnCampaignClickListener.onClick(v,campaign.getCpThree());
                        break;
                }
            }
        }
    }

    private OnCampaignClickListener mOnCampaignClickListener;
    public void setmOnCampaignClickListener(OnCampaignClickListener listener){
        this.mOnCampaignClickListener = listener;
    }
    public interface OnCampaignClickListener{
        void onClick(View view, Campaign campaign);
    }
}
