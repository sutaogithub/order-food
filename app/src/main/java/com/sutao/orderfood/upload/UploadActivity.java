package com.sutao.orderfood.upload;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.SaveCallback;
import com.bumptech.glide.load.resource.bitmap.BitmapDecoder;
import com.sutao.base.BaseActivity;
import com.sutao.orderfood.R;
import com.sutao.orderfood.bean.Classify;
import com.sutao.orderfood.bean.Food;
import com.sutao.orderfood.leanclound.LeanCloundHelper;
import com.sutao.orderfood.utils.BitmapUtils;
import com.sutao.orderfood.utils.ToastUtils;

import java.io.File;

/**
 * Created by Administrator on 2017/3/22.
 */
public class UploadActivity extends BaseActivity implements View.OnClickListener {

    private static final int PHOTO_REQUEST_GALLERY = 1;
    private static final int PHOTO_REQUEST_CUT = 2;

    private Button mGallery;
    private Button mUpload;
    private ImageView mFoodImg;
    private EditText mName;
    private EditText mDesc;
    private EditText mShortDesc;
    private EditText mPrice;
    private Bitmap mBitmap;
    private RadioGroup mRadioGroup;

    private int mType;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);
        initView();
        initEvent();
    }


    private void initView() {
        mUpload = findView(R.id.btn_upload);
        mGallery = findView(R.id.btn_gallery);
        mFoodImg = findView(R.id.img_food);
        mName = findView(R.id.edit_name);
        mDesc = findView(R.id.edit_desc);
        mPrice = findView(R.id.eidt_price);
        mShortDesc = findView(R.id.edit_short_desc);
        mRadioGroup = findView(R.id.radio_type);

        ((RadioButton)findView(R.id.btn_main_food)).setChecked(true);
        mType = Classify.MAIN_FOOD;
    }

    private void initEvent() {
        mUpload.setOnClickListener(this);
        mGallery.setOnClickListener(this);

        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.btn_main_food:
                        mType = Classify.MAIN_FOOD;
                        break;
                    case R.id.btn_soup:
                        mType = Classify.SOUP;
                        break;
                    case R.id.btn_drink:
                        mType = Classify.DRINDK;
                        break;
                    case R.id.btn_snack:
                        mType = Classify.SNACK;
                        break;
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.btn_gallery:
                openGallery();
                break;
            case R.id.btn_upload:
                save();
                break;
        }
    }

    private void save() {
        String name = mName.getText().toString();
        if (TextUtils.isEmpty(name)) {
            ToastUtils.makeShortToast(this, "请输入名字");
            return;
        }
        String desc = mDesc.getText().toString();
        if (TextUtils.isEmpty(name)) {
            ToastUtils.makeShortToast(this, "请输入描述");
            return;
        }
        String shortDesc = mShortDesc.getText().toString();
        if (TextUtils.isEmpty(name)) {
            ToastUtils.makeShortToast(this, "请输入短描述");
            return;
        }
        String price = mPrice.getText().toString();
        if (TextUtils.isEmpty(name)) {
            ToastUtils.makeShortToast(this, "请输入价格");
            return;
        }
        Food food = new Food();
        food.setName(name);
        food.setDesc(desc);
        food.setImg(mBitmap);
        food.setPrice(Integer.parseInt(price));
        food.setShortDesc(shortDesc);
        food.setType(mType);
        showLoadingDialog();
        LeanCloundHelper.saveFood(food, new SaveCallback() {
            @Override
            public void done(AVException e) {
                dismissDialog();
                if (e == null) {
                    ToastUtils.makeShortToast(UploadActivity.this, "存储成功");
                } else {
                    ToastUtils.makeShortToast(UploadActivity.this, e.toString());
                }
            }
        });
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");
        startActivityForResult(intent, PHOTO_REQUEST_GALLERY);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            return;
        }
        if (requestCode == PHOTO_REQUEST_GALLERY) {
            if (data != null) {
                Uri uri = data.getData();
                File file =BitmapUtils.getFileFromMediaUri(this,uri);
                mBitmap = BitmapUtils.getBitmapFormUri(this,uri);
                int degree = BitmapUtils.getBitmapDegree(file.getAbsolutePath());
                mBitmap = BitmapUtils.rotateBitmapByDegree(mBitmap,degree);
                mFoodImg.setImageBitmap(mBitmap);
            }

        }
        super.onActivityResult(requestCode, resultCode, data);
    }

}
