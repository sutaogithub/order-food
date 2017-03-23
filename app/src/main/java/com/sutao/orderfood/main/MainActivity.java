package com.sutao.orderfood.main;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import com.sutao.base.BaseActivity;
import com.sutao.customview.pullrefresh.PullToRefreshBase;
import com.sutao.customview.pullrefresh.PullToRefreshRecycleView;
import com.sutao.orderfood.R;
import com.sutao.orderfood.bean.Food;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity {

    private PullToRefreshRecycleView mRefreshList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        showTopBar(false);
        setContentView(R.layout.activity_main);
        mRefreshList = (PullToRefreshRecycleView) findViewById(R.id.list_content);
        mRefreshList.setPullLoadEnabled(true);
        RecyclerView listView = mRefreshList.getRefreshableView();

        final List<Food> list = new ArrayList<>();
        for (int i = 1; i < 50; i++) {
            list.add(new Food());
        }

        final MainAdapter adapter = new MainAdapter(this);
        adapter.setData(list);

        listView.setAdapter(adapter);

        mRefreshList.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<RecyclerView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<RecyclerView> refreshView) {
                adapter.notifyDataSetChanged();
                mRefreshList.onPullDownRefreshComplete();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<RecyclerView> refreshView) {
                int size = list.size();
                for (int i = size; i < size + 10; i++) {
                    list.add(new Food());
                }
                mRefreshList.onPullUpRefreshComplete();
                adapter.notifyDataSetChanged();

            }
        });





    }

}
