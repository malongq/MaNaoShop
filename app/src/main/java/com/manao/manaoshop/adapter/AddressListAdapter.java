package com.manao.manaoshop.adapter;

import android.content.Context;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import com.manao.manaoshop.R;
import com.manao.manaoshop.base.baseadapter.BaseAdapter;
import com.manao.manaoshop.base.baseadapter.BaseViewHolder;
import com.manao.manaoshop.base.baseadapter.SimpleAdapter;
import com.manao.manaoshop.bean.Address;

import java.util.List;

/**
 * Created by Malong
 * on 18/12/12.
 * 收货地址adapter
 */
public class AddressListAdapter extends SimpleAdapter<Address> implements BaseAdapter.OnItemClickListener  {

    private AddAdapterListener listener;
    private int position;

    @Override
    public void onClick(View view, int position) {
        this.position = position;
    }

    public interface AddAdapterListener {
        void setDefault(Address address);
        void editAddress(Address address);
        void delAddress(Address address,int position);
    }

    public AddressListAdapter(Context context, List<Address> datas,AddAdapterListener listener) {
        super(context, R.layout.template_address, datas);
        this.listener = listener;
    }

    @Override
    protected void convert(BaseViewHolder holder, final Address item) {
        holder.getTextView(R.id.txt_name).setText(item.getConsignee());

        String phone = item.getPhone();
        String p = phone.substring(0, phone.length() - (phone.substring(3)).length()) + "****" + phone.substring(7);//截取中间位变成*
        holder.getTextView(R.id.txt_phone).setText(p);

        holder.getTextView(R.id.txt_address).setText(item.getAddr());

        CheckBox box = holder.getCheckBox(R.id.cb_is_defualt);
        Boolean isDefault = item.getIsDefault();
        box.setChecked(isDefault);
        //布局里该控件默认Clickable为false,如果是选中就显示为选中，且不可在点击，否则可以点击
        if (isDefault) {
            box.setText("默认地址");
        } else {
            box.setClickable(true);
            box.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {//buttonView代表被点击控件的本身，isChecked代表状态
                    if (isChecked && listener != null) {
                        item.setIsDefault(true);
                        listener.setDefault(item);
                    }
                }
            });
        }

        //编辑
        TextView edit = holder.getTextView(R.id.txt_edit);
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.editAddress(item);
            }
        });

        //删除
        TextView del = holder.getTextView(R.id.txt_del);
        del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.delAddress(item,position);
            }
        });
    }

}
