package com.sutao.orderfood.buyermain;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.avos.avoscloud.AVObject;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.sutao.base.ActivityController;
import com.sutao.customview.slideshow.SlideShowView;
import com.sutao.orderfood.R;
import com.sutao.orderfood.bean.Classify;
import com.sutao.orderfood.bean.Food;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/3/12.
 */
public class BuyerMainAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements View.OnClickListener {

    private static final int TYPE_SLIDE_SHOW = 1;
    private static final int TYPE_CLASSIFY = 2;
    private static final int TYPE_FOOD = 3;


    private List<Food> mTopSellFood;
    private List<Food> mData;
    private List<Object> mObjectData;
    private List<AVObject> mTopSellObj;

    private Activity mActivity;
    private LayoutInflater mInflater;

    public BuyerMainAdapter(Activity context) {
        this.mActivity = context;
        mInflater = LayoutInflater.from(context);
        mData = new ArrayList<>();
        mObjectData = new ArrayList<>();
        mTopSellFood = new ArrayList<>();
        mTopSellObj = new ArrayList<>();
    }

    public void setData(List<AVObject> data) {
        mData.clear();
        mObjectData.clear();
        mObjectData.addAll(data);
        setupData(data,mData);
        notifyDataSetChanged();
    }

    public void addData(List<AVObject> data) {
        mObjectData.addAll(data);
        setupData(data,mData);
        notifyDataSetChanged();
    }

    public void setTopSellData(List<AVObject> data) {
        mTopSellFood.clear();
        mTopSellObj.clear();
        mTopSellObj.addAll(data);
        setupData(data,mTopSellFood);
        notifyDataSetChanged();
    }

    private void setupData(List<AVObject> data,List<Food> container) {
        for (AVObject object : data) {
            Food food = new Food();
            food.setName(object.getString("name"));
            food.setDesc(object.getString("desc"));
            food.setShortDesc(object.getString("shortDesc"));
            food.setType(object.getInt("type"));
            food.setPrice(object.getInt("price"));
            food.setSelling(object.getInt("selling"));
            food.setImgUrl(object.getAVFile("img").getUrl());
            container.add(food);
        }
    }



    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE_FOOD: {
                View view = mInflater.inflate(R.layout.item_list_food, parent, false);
                return new FoodViewHolder(view);
            }

            case TYPE_CLASSIFY: {
                View view = mInflater.inflate(R.layout.item_classify_bar, parent, false);
                return new ClasifyViewHolder(view);
            }
            case TYPE_SLIDE_SHOW: {
                View view = mInflater.inflate(R.layout.item_slide_show, parent, false);
                return new SlideViewHolder(view);
            }
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case TYPE_FOOD:
                bindFoodHolder((FoodViewHolder) holder,position);
                break;
            case TYPE_CLASSIFY:
                bindClasifyHolder(holder);
                break;
            case TYPE_SLIDE_SHOW:
                bindSlideHolder(holder);
                break;
        }
    }

    private void bindClasifyHolder(RecyclerView.ViewHolder holder) {
        ((ClasifyViewHolder) holder).mFirst.setOnClickListener(this);
        ((ClasifyViewHolder) holder).mSecond.setOnClickListener(this);
        ((ClasifyViewHolder) holder).mThird.setOnClickListener(this);
        ((ClasifyViewHolder) holder).mFourth.setOnClickListener(this);
    }

    private void bindFoodHolder(final FoodViewHolder holder,int position) {
        final Food food = mData.get(position-2);
        Glide.with(mActivity).load(food.getImgUrl()).into(holder.mImg);
        holder.mName.setText(food.getName());
        holder.mDesc.setText(food.getDesc());
        holder.mPrice.setText(food.getPrice() + "");
        holder.mSelling.setText(mActivity.getString(R.string.food_selling, food.getSelling() + ""));
        holder.mDelete.setVisibility(View.GONE);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActivityController.showDetailActivity(mActivity, holder.mImg,food);
            }
        });
    }

    private void bindSlideHolder(final RecyclerView.ViewHolder holder) {
        final List<Bitmap> bitmaps = new ArrayList<>();
        if (mTopSellFood.size() == 0) {
            holder.itemView.setVisibility(View.GONE);
            return;
        } else {
            holder.itemView.setVisibility(View.VISIBLE);
        }
        ((SlideViewHolder)holder).showView.setOnItemClickListener(new SlideShowView.OnItemClickListener() {
            @Override
            public void onItemClick(int position,View view) {
                Food food = mTopSellFood.get(position);

                ActivityController.showDetailActivity(mActivity, view,food);
            }
        });
        for (Food food : mTopSellFood) {
            Glide.with(mActivity).load(food.getImgUrl()).asBitmap().into(new SimpleTarget<Bitmap>() {

                @Override
                public void onResourceReady(Bitmap resource, GlideAnimation glideAnimation) {
                    // Do something with bitmap here.
                    bitmaps.add(resource);
                    if (bitmaps.size() == mTopSellFood.size()) {
                        ((SlideViewHolder) holder).showView.setBitmaps(bitmaps);
                        ((SlideViewHolder) holder).showView.setmSlideInteval(2500);
                        ((SlideViewHolder) holder).showView.startSlide();

                    }
                }

            });
        }

    }

    @Override
    public int getItemCount() {
        return mData != null?mData.size() + 2 : 2;
    }

    @Override
    public int getItemViewType(int position) {
        switch (position) {
            case 0:
                return TYPE_SLIDE_SHOW;
            case 1:
                return TYPE_CLASSIFY;
            default:
                return TYPE_FOOD;
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
            ((TextView) mFirst.findViewById(R.id.txt_tab_classify)).setText("主食");
            ((TextView) mSecond.findViewById(R.id.txt_tab_classify)).setText("汤");
            ((TextView) mThird.findViewById(R.id.txt_tab_classify)).setText("饮料");
            ((TextView) mFourth.findViewById(R.id.txt_tab_classify)).setText("小吃");
        }
    }

    public class FoodViewHolder extends RecyclerView.ViewHolder {
        public ImageView mImg;
        public TextView mName;
        public TextView mDesc;
        public TextView mSelling;
        public TextView mPrice;
        public TextView mDelete;

        public FoodViewHolder(View itemView) {
            super(itemView);
            mImg = (ImageView) itemView.findViewById(R.id.img_food);
            mName = (TextView) itemView.findViewById(R.id.txt_name);
            mDesc = (TextView) itemView.findViewById(R.id.txt_desc);
            mSelling = (TextView) itemView.findViewById(R.id.txt_selling);
            mPrice = (TextView) itemView.findViewById(R.id.txt_price);
            mDelete = (TextView) itemView.findViewById(R.id.txt_delete);
        }
    }

}
