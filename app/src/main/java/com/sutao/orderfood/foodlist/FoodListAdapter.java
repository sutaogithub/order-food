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
public class FoodListAdapter extends RecyclerView.Adapter<FoodListAdapter.FoodViewHolder> implements View.OnClickListener{

    private List<Food> mData;
    private Activity mActivity;
    private LayoutInflater mInflater;

    public FoodListAdapter(Activity context) {
        this.mActivity = context;
        mInflater = LayoutInflater.from(context);
    }

    public void setData(List<Food> data) {
        this.mData.clear();
        this.mData = data;
    }

    public void addData(List<Food> data) {
        this.mData.addAll(data);
    }

    @Override
    public FoodViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_list_food, parent, false);
        return new FoodViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final FoodViewHolder holder, int position) {
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityOptionsCompat activityOptions = ActivityOptionsCompat.makeSceneTransitionAnimation(mActivity,
                        ((FoodViewHolder) holder).mImg,
                        DetailActivity.VIEW_NAME_FOOD_IMAGE);
                Intent intent = new Intent(mActivity,DetailActivity.class);
                ActivityCompat.startActivity(mActivity, intent, activityOptions.toBundle());
            }
        });
    }


    @Override
    public int getItemCount() {
        return mData.size() + 2;
    }

    @Override
    public void onClick(View v) {
        int id =v.getId();
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

        }
    }


    public class SlideViewHolder extends RecyclerView.ViewHolder {
        public SlideShowView showView;

        public SlideViewHolder(View itemView) {
            super(itemView);
            showView = (SlideShowView) itemView.findViewById(R.id.view_slide);
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
            mSecond = (RelativeLayout) itemView.findViewById(R.id.view_second);
            mThird = (RelativeLayout) itemView.findViewById(R.id.view_third);
            mFourth = (RelativeLayout) itemView.findViewById(R.id.view_fourth);
        }
    }

    public class FoodViewHolder extends RecyclerView.ViewHolder {
        public ImageView mImg;
        public TextView mName;
        public TextView mDesc;
        public TextView mSelling;
        public TextView mPrice;

        public FoodViewHolder(View itemView) {
            super(itemView);
            mImg = (ImageView) itemView.findViewById(R.id.img_food);
            mName = (TextView) itemView.findViewById(R.id.txt_name);
            mDesc = (TextView) itemView.findViewById(R.id.txt_desc);
            mSelling = (TextView) itemView.findViewById(R.id.txt_selling);
            mPrice = (TextView) itemView.findViewById(R.id.txt_price);
        }
    }

}
