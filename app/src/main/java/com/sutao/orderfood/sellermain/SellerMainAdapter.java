package com.sutao.orderfood.sellermain;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sutao.base.ActivityController;
import com.sutao.orderfood.R;
import com.sutao.orderfood.bean.Classify;
import com.sutao.orderfood.bean.FoodOrder;
import com.sutao.orderfood.bean.UserOrder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/3/12.
 */
public class SellerMainAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener {

    private static final int TYPE_CLASSIFY = 1;
    private static final int TYPE_FOOD_ORDER = 2;
    private List<UserOrder> mData;
    private Activity mActivity;
    private LayoutInflater mInflater;


    private OnItemClickListener mListener;

    public interface OnItemClickListener {
        void onDetailClick(int position,UserOrder order);
        void onDeleteClick(int position,UserOrder order);
    }

    public OnItemClickListener getListener() {
        return mListener;
    }

    public void setmListener(OnItemClickListener listener) {
        this.mListener = listener;
    }

    public SellerMainAdapter(Activity context) {
        this.mActivity = context;
        mInflater = LayoutInflater.from(context);
        mData = new ArrayList<>();
    }

    public void setData(List<UserOrder> data) {
        this.mData.clear();
        this.mData = data;
    }

    public void addData(List<UserOrder> data) {
        this.mData.addAll(data);
    }

    public void addData(UserOrder data) {
        this.mData.add(0,data);
        notifyItemInserted(0);
    }

    public void delete(int position) {
        this.mData.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE_FOOD_ORDER: {
                View view = mInflater.inflate(R.layout.item_order_list, parent, false);
                return new FoodViewHolder(view);
            }

            case TYPE_CLASSIFY: {
                View view = mInflater.inflate(R.layout.item_classify_bar, parent, false);
                return new ClasifyViewHolder(view);
            }
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case TYPE_FOOD_ORDER:
                bindFoodHolder((FoodViewHolder) holder, position);
                break;
            case TYPE_CLASSIFY:
                bindClasifyHolder(holder);
                break;
        }
    }

    private void bindClasifyHolder(RecyclerView.ViewHolder holder) {
        ((ClasifyViewHolder) holder).mFirst.setOnClickListener(this);
        ((ClasifyViewHolder) holder).mSecond.setOnClickListener(this);
        ((ClasifyViewHolder) holder).mThird.setOnClickListener(this);
        ((ClasifyViewHolder) holder).mFourth.setOnClickListener(this);
    }

    private void bindFoodHolder(final FoodViewHolder holder, final int position) {
        UserOrder order = mData.get(position-1);
        holder.mName.setText(mActivity.getString(R.string.item_order_name, order.getName()));
        List<FoodOrder> foods = order.getFoodOrders();
        if (foods.size() >= 1) {
            holder.mOrder1.setText(mActivity.getString(R.string.item_order,
                    foods.get(0).getName(),foods.get(0).getNum()));
        }

        if (foods.size() >= 2) {
            holder.mOrder2.setText(mActivity.getString(R.string.item_order,
                    foods.get(1).getName(), foods.get(1).getNum()));
        }

        if (foods.size() >= 3) {
            holder.mOrder3.setVisibility(View.VISIBLE);
        } else {
            holder.mOrder3.setVisibility(View.GONE);
        }
        holder.mDelete.setOnClickListener(this);
        holder.mDetail.setOnClickListener(this);
        holder.mDelete.setTag(position);
        holder.mDetail.setTag(position);
    }

    @Override
    public int getItemCount() {
        return mData != null ? mData.size() + 1 : 1;
    }

    @Override
    public int getItemViewType(int position) {
        switch (position) {
            case 0:
                return TYPE_CLASSIFY;
            default:
                return TYPE_FOOD_ORDER;
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.view_first:
                ActivityController.showFoodListActivity(mActivity, Classify.MAIN_FOOD);
                break;
            case R.id.view_second:
                ActivityController.showFoodListActivity(mActivity, Classify.SOUP);
                break;
            case R.id.view_third:
                ActivityController.showFoodListActivity(mActivity, Classify.DRINDK);
                break;
            case R.id.view_fourth:
                ActivityController.showFoodListActivity(mActivity, Classify.SNACK);
                break;
            case R.id.btn_detail:
                if (mListener!=null) {
                    int position = (Integer) v.getTag()-1;
                    UserOrder order = mData.get(position);
                    mListener.onDetailClick(position,order);
                }
                break;
            case R.id.img_delete:
                if (mListener!=null) {
                    int position = (Integer) v.getTag()-1;
                    UserOrder order = mData.get(position);
                    mListener.onDeleteClick(position,order);
                }
                break;
        }
    }


    public class ClasifyViewHolder extends RecyclerView.ViewHolder {
        public RelativeLayout mFirst;
        public RelativeLayout mSecond;
        public RelativeLayout mThird;
        public RelativeLayout mFourth;

        public ClasifyViewHolder(View itemView) {
            super(itemView);
            mFirst = (RelativeLayout) itemView.findViewById(R.id.view_first);
            ((TextView) mFirst.findViewById(R.id.txt_tab_classify)).setText("主食");
            mSecond = (RelativeLayout) itemView.findViewById(R.id.view_second);
            ((TextView) mSecond.findViewById(R.id.txt_tab_classify)).setText("汤");
            mThird = (RelativeLayout) itemView.findViewById(R.id.view_third);
            ((TextView) mThird.findViewById(R.id.txt_tab_classify)).setText("饮料");
            mFourth = (RelativeLayout) itemView.findViewById(R.id.view_fourth);
            ((TextView) mFourth.findViewById(R.id.txt_tab_classify)).setText("小吃");
        }
    }

    public class FoodViewHolder extends RecyclerView.ViewHolder {
        public TextView mName;
        public TextView mOrder1;
        public TextView mOrder2;
        public TextView mOrder3;
        public Button mDetail;
        public ImageView mDelete;

        public FoodViewHolder(View itemView) {
            super(itemView);
            mName = (TextView) itemView.findViewById(R.id.txt_order_name);
            mOrder1 = (TextView) itemView.findViewById(R.id.txt_order1);
            mOrder2 = (TextView) itemView.findViewById(R.id.txt_order2);
            mOrder3 = (TextView) itemView.findViewById(R.id.txt_order3);
            mDetail = (Button) itemView.findViewById(R.id.btn_detail);
            mDelete = (ImageView) itemView.findViewById(R.id.img_delete);
        }
    }

}
