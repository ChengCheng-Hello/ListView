package com.tx.listview.base;

import android.view.View;
import android.view.ViewGroup;

import com.tx.listview.base.listener.TXOnLoadMoreListener;
import com.tx.listview.base.listener.TXOnLoadingListener;

/**
 * Created by Cheng on 17/2/16.
 */
public interface TXBaseListViewAdapter<T> {

    int TYPE_LOAD_MORE = 1001;
    int TYPE_LOAD_MORE_COMPLETE = 1002;
    int TYPE_LOADING = 1003;
    int TYPE_EMPTY = 1004;
    int TYPE_ERROR = 1005;
    int TYPE_HEADER = 1006;

    // 设置是否可以加载更多
    void setLoadMoreEnabled(boolean loadMoreEnabled);

    // 设置加载更多事件
    void setLoadMoreListener(TXOnLoadMoreListener<T> loadMoreListener);

    // 设置正在加载事件
    void setLoadingListener(TXOnLoadingListener listener);

    // 设置加载错误信息
    void loadError(long errorCode, String message);

    // 重新加载
    void onReload();

    // 清空数据并刷新
    void clearDataAndNotify();

    // 加载更多View
    View getLoadMoreView(ViewGroup parent);

    // 加载更多完成View
    View getLoadMoreCompleteView(ViewGroup parent);

    // 空View
    View getEmptyView(ViewGroup parent);

    // 正在加载View
    View getLoadingView(ViewGroup parent);

    // 出错View
    View getErrorView(ViewGroup parent, long errorCode, String message);

    // 头布局
    View getHeaderView(ViewGroup parent);
}
