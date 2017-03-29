package com.sutao.orderfood.confirm;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sutao.orderfood.R;
import com.sutao.orderfood.bean.FoodOrder;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/3/27.
 */
public class ConfirmAdapter extends RecyclerView.Adapter<ConfirmAdapter.ViewHolder>{

    private static final String TAG = "ConfirmAdapter";

    private Context mContext;
    private LayoutInflater mInflater;
    private List<FoodOrder> mDatas;

    public void setDatas(List<FoodOrder> datas) {
        this.mDatas.addAll(datas);
        notifyDataSetChanged();
    }

    public ConfirmAdapter(Context context) {
        mContext = context;
        mInflater = LayoutInflater.from(context);
        mDatas = new ArrayList<>();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_confirm,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        FoodOrder order = mDatas.get(position);
        Log.d(TAG, "onBindViewHolder: "+position+"   "+mDatas.size());
        holder.mOrder.setText(mContext.getString(R.string.item_order,order.getName(),order.getNum()));
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView mOrder;

        public ViewHolder(View itemView) {
            super(itemView);
            mOrder = (TextView) itemView.findViewById(R.id.txt_order);
        }
    }

}
