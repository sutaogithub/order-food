package com.sutao.orderfood;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.sutao.customview.slideshow.SlideShow;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private SlideShow mSlideShow;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mSlideShow = (SlideShow) findViewById(R.id.view_slide);

        Bitmap first = BitmapFactory.decodeResource(getResources(), R.drawable.first);
        Bitmap second = BitmapFactory.decodeResource(getResources(), R.drawable.second);
        Bitmap third = BitmapFactory.decodeResource(getResources(), R.drawable.third);

        List<Bitmap> list = new ArrayList<>();
        list.add(first);
        list.add(second);
        list.add(third);

        mSlideShow.setBitmaps(list);

        mSlideShow.setOnItemClickListener(new SlideShow.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Toast.makeText(MainActivity.this, position+"", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void start(View  view){
        mSlideShow.startSlide();
    }

    public void stop(View view){
        mSlideShow.stopSlide();
    }
}
