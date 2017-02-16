package com.tx.listview.base;

import android.os.Bundle;
import android.support.annotation.IntRange;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentActivity;
import android.view.View;

import com.tx.listview.R;
import com.tx.listview.TXListView;
import com.tx.listview.base.cell.TXBaseListCell;
import com.tx.listview.base.listener.TXOnCellClickListener;
import com.tx.listview.base.listener.TXOnCellLongClickListener;
import com.tx.listview.base.listener.TXOnCreateCellListener;
import com.tx.listview.base.listener.TXOnCreateEmptyViewListener;
import com.tx.listview.base.listener.TXOnCreateErrorViewListener;
import com.tx.listview.base.listener.TXOnCreateHeaderViewListener;
import com.tx.listview.base.listener.TXOnGetCellViewTypeListener;
import com.tx.listview.base.listener.TXOnLoadMoreListener;
import com.tx.listview.base.listener.TXOnRefreshListener;

/**
 * Created by Cheng on 17/2/16.
 */

public abstract class TXBaseListActivity<T> extends FragmentActivity implements TXOnRefreshListener, TXOnLoadMoreListener<T>, TXOnCreateCellListener<T>, TXOnGetCellViewTypeListener<T>, TXOnCellClickListener<T>, TXOnCellLongClickListener<T>, TXOnCreateEmptyViewListener, TXOnCreateErrorViewListener, TXOnCreateHeaderViewListener {

    public TXListView<T> mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        bindContentView();

        initData();

        mListView = (TXListView) findViewById(getListViewId());

        if (mListView == null) {
            return;
        }

        mListView.setOnRefreshListener(this);
        mListView.setOnLoadMoreListener(this);
        mListView.setOnCreateCellListener(this);
        mListView.setOnGetCellViewTypeListener(this);
        mListView.setOnCellClickListener(this);
        mListView.setOnCellLongClickListener(this);
        mListView.setOnCreateEmptyViewListener(this);
        mListView.setOnCreateErrorViewListener(this);
        mListView.setOnCreateHeaderViewListener(this);

        onRefresh();
    }

    protected boolean bindContentView() {
        setContentView(R.layout.tx_activity_base_listview);
        return true;
    }

    /**
     * 如果重写了layout文件则要重载这个方法，返回layout里的AbsListView id
     */
    protected int getListViewId() {
        return R.id.listView;
    }

    /**
     * 子类需要在初始化list相关类之前初始其他数据的可以重载这个方法
     */
    protected void initData() {
    }

    /**
     * 用于主动刷新,如筛选刷新
     */
    public void refresh() {
        mListView.refresh();
    }

    /**
     * 加载更多回调
     *
     * @param lastData 最后一条数据
     */
    @Override
    public abstract void onLoadMore(T lastData);

    /**
     * 下拉刷新回调
     */
    @Override
    public abstract void onRefresh();

    @Override
    public abstract TXBaseListCell<T> onCreateCell(int viewType);

    @Override
    @IntRange(from = 0, to = 1000)
    public int getCellViewType(@Nullable T data) {
        return 0;
    }

    @Override
    public void onCellClick(T data, View view) {
    }

    @Override
    public boolean onCellLongClick(T data, View view) {
        return false;
    }

    @Override
    public void onCreateEmptyView(View view) {
    }

    @Override
    public void onCreateErrorView(View view, long errorCode, String errorMsg) {
    }

    @Override
    public void onCreateHeaderView(View view) {
    }
}
