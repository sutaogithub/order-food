package com.sutao.orderfood.bean;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Parcel;
import android.os.Parcelable;

import com.sutao.orderfood.utils.BitmapUtils;
import com.sutao.orderfood.utils.IOUtils;

import java.io.ByteArrayOutputStream;

/**
 * Created by Administrator on 2017/3/22.
 */
public class Food implements Parcelable{

    private Bitmap img;
    private String name;
    private String desc;
    private String shortDesc;

    private int selling;
    private int price;
    private int size;
    private int type;
    private int num;

    private String imgUrl;



    public Food() {

    }


    protected Food(Parcel in) {
        img = in.readParcelable(Bitmap.class.getClassLoader());
        name = in.readString();
        desc = in.readString();
        shortDesc = in.readString();
        selling = in.readInt();
        price = in.readInt();
        size = in.readInt();
        type = in.readInt();
        num = in.readInt();
        imgUrl = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(img, flags);
        dest.writeString(name);
        dest.writeString(desc);
        dest.writeString(shortDesc);
        dest.writeInt(selling);
        dest.writeInt(price);
        dest.writeInt(size);
        dest.writeInt(type);
        dest.writeInt(num);
        dest.writeString(imgUrl);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Food> CREATOR = new Creator<Food>() {
        @Override
        public Food createFromParcel(Parcel in) {
            return new Food(in);
        }

        @Override
        public Food[] newArray(int size) {
            return new Food[size];
        }
    };



    public int getSize() {
        return size;
    }


    public void setSize(int size) {
        this.size = size;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }




    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }



    public Bitmap getImg() {
        return img;
    }

    public void setImg(Bitmap img) {
        this.img = img;
    }

    public String getShortDesc() {
        return shortDesc;
    }

    public void setShortDesc(String shortDesc) {
        this.shortDesc = shortDesc;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getSelling() {
        return selling;
    }

    public void setSelling(int selling) {
        this.selling = selling;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public byte[] getBitmapByte() {
        if (img == null) {
            return null;
        }
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        img.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        IOUtils.closeQuitely(stream);
        return byteArray;
    }


}
