package com.sutao.orderfood.foodlist;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.bumptech.glide.Glide;
import com.sutao.base.ActivityController;
import com.sutao.customview.slideshow.SlideShowView;
import com.sutao.orderfood.R;
import com.sutao.orderfood.bean.Classify;
import com.sutao.orderfood.bean.Food;
import com.sutao.orderfood.detail.DetailActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/3/12.
 */
public class FoodListAdapter extends RecyclerView.Adapter<FoodListAdapter.FoodViewHolder> implements View.OnClickListener {

    private List<Food> mData;
    private List<AVObject> mObjectData;
    private Activity mActivity;
    private LayoutInflater mInflater;

    private DeleteClickListener mDeleteListener;

    @Override
    public void onClick(View v) {
        int position = (int) v.getTag();
        if (mDeleteListener != null) {
            mDeleteListener.onClick(position,mObjectData.get(position));
        }
    }

    public interface DeleteClickListener {
        void onClick(int posistion,AVObject obj);
    }

    public FoodListAdapter(Activity context) {
        this.mActivity = context;
        mInflater = LayoutInflater.from(context);
        mData = new ArrayList<>();
        mObjectData = new ArrayList<>();
    }

    public void setData(List<AVObject> data) {
        this.mData.clear();
        this.mObjectData.clear();
        mObjectData.addAll(data);
        setupData(data);
    }



    private void setupData(List<AVObject> data) {
        for (AVObject object : data) {
            Food food = new Food();
            food.setName(object.getString("name"));
            food.setDesc(object.getString("desc"));
            food.setShortDesc(object.getString("shortDesc"));
            food.setType(object.getInt("type"));
            food.setPrice(object.getInt("price"));
            food.setImgUrl(object.getAVFile("img").getUrl());
            mData.add(food);
        }
    }
    public void setDeleteClickListener(DeleteClickListener listener) {
        mDeleteListener = listener;
    }

    public void addData(List<AVObject> data) {
        mObjectData.addAll(data);
        setupData(data);
    }

    public void delete(int position) {
        mData.remove(position);
        mObjectData.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public FoodViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_list_food, parent, false);
        return new FoodViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final FoodViewHolder holder, final int position) {
        Food food = mData.get(position);
        Glide.with(mActivity).load(food.getImgUrl()).into(holder.mImg);
        holder.mName.setText(food.getName());
        holder.mDesc.setText(food.getDesc());
        holder.mPrice.setText(food.getPrice() + "");
        holder.mSelling.setText(mActivity.getString(R.string.food_selling, food.getSelling() + ""));
        holder.mDelete.setVisibility(View.VISIBLE);
        holder.mDelete.setTag(position);
        holder.mDelete.setOnClickListener(this);
    }


    @Override
    public int getItemCount() {
        return mData.size();
    }


    public class FoodViewHolder extends RecyclerView.ViewHolder {
        public ImageView mImg;
        public TextView mName;
        public TextView mDesc;
        public TextView mSelling;
        public TextView mPrice;
        public ImageView mDelete;

        public FoodViewHolder(View itemView) {
            super(itemView);
            mImg = (ImageView) itemView.findViewById(R.id.img_food);
            mName = (TextView) itemView.findViewById(R.id.txt_name);
            mDesc = (TextView) itemView.findViewById(R.id.txt_desc);
            mSelling = (TextView) itemView.findViewById(R.id.txt_selling);
            mPrice = (TextView) itemView.findViewById(R.id.txt_price);
            mDelete = (ImageView) itemView.findViewById(R.id.img_delete);
        }
    }

}
