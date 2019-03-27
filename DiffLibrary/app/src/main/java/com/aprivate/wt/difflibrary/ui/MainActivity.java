package com.aprivate.wt.difflibrary.ui;

import android.app.Activity;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.aprivate.wt.difflibrary.R;
import com.aprivate.wt.difflibrary.adapter.TestAdapter;
import com.aprivate.wt.difflibrary.bean.TestBean;
import com.aprivate.wt.difflibrary.diff.TestDiff;
import com.aprivate.wt.difflibrary.iface.ItemClickCallBack;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener, ItemClickCallBack {


    private String[] iconUrl = new String[]{"https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1538111258772&di=5b02096573add0cd3d81e4739458d970&imgtype=0&src=http%3A%2F%2Fimg5.duitang.com%2Fuploads%2Fitem%2F201410%2F17%2F20141017235209_MEsRe.thumb.700_0.jpeg"
            , "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1538112782674&di=015235f6a29cc1e751898646ae45697e&imgtype=0&src=http%3A%2F%2Fimg4.duitang.com%2Fuploads%2Fitem%2F201410%2F03%2F20141003160129_nUfjf.thumb.700_0.jpeg"
            , "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1538112806780&di=7f81c66d4fa059013acecb61e9be56f8&imgtype=0&src=http%3A%2F%2Fg.hiphotos.baidu.com%2Fzhidao%2Fwh%253D450%252C600%2Fsign%3D4de3531eb54543a9f54ef2c82b27a6b4%2F960a304e251f95ca2f321f5ccb177f3e66095211.jpg"};
    private String fixedUrl = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1538112842810&di=3f640d2add5fe33594291ad83b93f702&imgtype=0&src=http%3A%2F%2Fwenwen.soso.com%2Fp%2F20140219%2F20140219115643-1609899437.jpg";

    private List<TestBean> mList = new ArrayList<>();
    private TestAdapter adapter;
    private ImageView ivAdd;
    private ImageView ivDelete;
    private SwipeRefreshLayout refreshLayout;
    private RecyclerView recyclerView;
    private View footView;
    private TextView tvFoot;

    private int pageNum = 1;
    private int pageCount = 4;
    private int addCount = 1;

    private boolean isLoading;
    private boolean hadMoreDATA;


    private List<TestBean> oldData;
    private DiffUtil.DiffResult diffResult;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bindViews();
        addListener();

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new TestAdapter(this, mList, this);
        adapter.setFootView(footView);
        recyclerView.setAdapter(adapter);
        DefaultItemAnimator defaultItemAnimator = new DefaultItemAnimator();
        defaultItemAnimator.setAddDuration(1000);
        defaultItemAnimator.setRemoveDuration(1000);
        recyclerView.setItemAnimator(defaultItemAnimator);

        refreshLayout.setRefreshing(true);
        loadData(true);
    }

    private void initData() {

        for (int i = 0; i < 10; i++) {
            TestBean bean = new TestBean();
            bean.setId(pageNum + "" + (i + 1));
            bean.setTitle("标题" + pageNum + "-" + (i + 1));
            bean.setContext("内容" + pageNum + "-" + (i + 1));
            bean.setiConUrl(iconUrl[i % 3]);
            mList.add(bean);
        }
    }

    private void changItem(int positon) {
        oldData = adapter.getData();
        mList.get(positon).setiConUrl(fixedUrl);
        mList.get(positon).setTitle("标题" + pageNum);
        mList.get(positon).setContext("内容" + pageNum);
        diffResult = DiffUtil.calculateDiff(new TestDiff(oldData, mList), true);
        diffResult.dispatchUpdatesTo(adapter);
    }

    private void addListener() {
        ivAdd.setOnClickListener(this);
        ivDelete.setOnClickListener(this);
        refreshLayout.setOnRefreshListener(this);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (!recyclerView.canScrollVertically(1)) {
                    if (hadMoreDATA && !isLoading) {
                        pageNum++;
                        loadData(false);
                    }
                }
            }
        });
    }

    private void bindViews() {
        ivAdd = findViewById(R.id.iv_add);
        ivDelete = findViewById(R.id.iv_delete);
        recyclerView = findViewById(R.id.recyclerView);
        refreshLayout = findViewById(R.id.refresh);
        footView = LayoutInflater.from(this).inflate(R.layout.item_foot, null, false);
        tvFoot = footView.findViewById(R.id.tv_foot);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_add:
                oldData = adapter.getData();
                TestBean bean = new TestBean();
                bean.setTitle("标题" + pageNum + "新增");
                bean.setContext("内容" + pageNum + "新增");
                bean.setId(addCount + "");
                bean.setiConUrl(iconUrl[1]);
                addCount ++;
                mList.add(1,bean);
                diffResult = DiffUtil.calculateDiff(new TestDiff(oldData, mList), true);
                diffResult.dispatchUpdatesTo(adapter);
                break;
            case R.id.iv_delete:
                oldData = adapter.getData();
                if (mList.size() > 1) {
                    mList.remove(0);
                    diffResult = DiffUtil.calculateDiff(new TestDiff(oldData, mList), true);
                    diffResult.dispatchUpdatesTo(adapter);
                } else {
                    Toast.makeText(this, "没有数据了", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;

        }
    }

    @Override
    public void onRefresh() {
        loadData(true);
    }

    public void loadData(boolean clean) {
        isLoading = true;
        refreshLayout.setRefreshing(false);
        oldData = adapter.getData();
        if (clean) {
            mList.clear();
            pageNum = 1;
            addCount = 1;
        }

        if (pageNum > pageCount) {
            hadMoreDATA = false;
            tvFoot.setText("已全部加载完毕!");
        } else {
            tvFoot.setText(mList.size() <= 6 ? "" : "加载中...");
            hadMoreDATA = true;
        }
        initData();

        if (clean) {
            adapter.notifyDataSetChanged();
        } else {
            diffResult = DiffUtil.calculateDiff(new TestDiff(oldData, mList), true);
            diffResult.dispatchUpdatesTo(adapter);
        }
        isLoading = false;
    }

    @Override
    public void onItemClick(int position) {
        changItem(position);
    }
}
