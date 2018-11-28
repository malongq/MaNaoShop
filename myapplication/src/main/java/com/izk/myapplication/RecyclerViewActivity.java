package com.izk.myapplication;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Malong
 * on 18/11/9.
 */
public class RecyclerViewActivity extends AppCompatActivity implements View.OnClickListener {

    private MyAdapter myAdapter;
    private List<String> mdata = new ArrayList<String>();
    private RecyclerView rv_main;
    private TextView tv_add, tv_remove, tv_change;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_recycler_view);
        tv_add = findViewById(R.id.tv_add);
        tv_remove = findViewById(R.id.tv_remove);
        tv_change = findViewById(R.id.tv_change);
        tv_add.setOnClickListener(this);
        tv_remove.setOnClickListener(this);
        tv_change.setOnClickListener(this);


        //添加数据
        initData();
        rv_main = findViewById(R.id.rv_main);
        myAdapter = new MyAdapter(mdata, this);
        rv_main.setAdapter(myAdapter);
        rv_main.setLayoutManager(new LinearLayoutManager(this));//纵向布局
//        rv_main.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false));//横向布局
//        rv_main.setLayoutManager(new StaggeredGridLayoutManager(4,StaggeredGridLayoutManager.VERTICAL));//瀑布布局  第一个参数表示每行展示4个 第二个表示横向、纵向
//        rv_main.setLayoutManager(new GridLayoutManager(this,3));//3列
        rv_main.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));//添加条目分割线---系统默认的
        rv_main.setItemAnimator(new DefaultItemAnimator());//加载动画
        //条目点击事件
        myAdapter.setOnItemClickListener(new MyAdapter.OnItemClickListener() {
            @Override
            public void onClick(View view, int position) {
                Toast.makeText(RecyclerViewActivity.this, "第" + position + "条", Toast.LENGTH_SHORT).show();
            }
        });

    }

    //添加数据
    private void initData() {
        mdata.add("000");
        mdata.add("111");
        mdata.add("222");
        mdata.add("333");
        mdata.add("444");
        mdata.add("555");
        mdata.add("666");
        mdata.add("777");
        mdata.add("888");
        mdata.add("999");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_add:
                myAdapter.addPosition(3, "马龙");//角标为3那里添加，也就是在第3条数据后面添加
                break;
            case R.id.tv_remove:
                myAdapter.removePosition(6);//角标为6那里删除，也就是删除第7条数据
                break;
            case R.id.tv_change:
                rv_main.setLayoutManager(new GridLayoutManager(this, 3));//3列
                break;
        }
    }
}
