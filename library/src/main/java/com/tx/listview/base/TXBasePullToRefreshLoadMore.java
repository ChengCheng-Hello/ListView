package com.tx.listview.base;

import android.content.Context;

/**
 * 下拉刷新，加载更多
 * <p>
 * Created by Cheng on 17/2/16.
 */
public interface TXBasePullToRefreshLoadMore {

    /**
     * 设置下拉刷新
     *
     * @param pullToRefreshEnable 是否可以下拉刷新
     */
    void setPullToRefreshEnabled(boolean pullToRefreshEnable);

    /**
     * 设置加载更多
     *
     * @param loadMoreEnable 是否可以加载更多
     */
    void setLoadMoreEnabled(boolean loadMoreEnable);

    /**
     * 显示刷新错误信息
     *
     * @param context
     * @param code    错误code
     * @param message 错误信息
     */
    void showRefreshError(Context context, long code, String message);

    /**
     * 显示加载更多错误信息
     *
     * @param context
     * @param code    错误code
     * @param message 错误信息
     */
    void showLoadMoreError(Context context, long code, String message);

    /**
     * 刷新列表
     */
    void refresh();

    // 设置正在刷新
//    void setRefreshing(boolean refreshing);

    // 显示下拉刷新view
//    void showPullToRefreshView();

    // 隐藏下拉刷新view
//    void hidePullToRefreshView();

    // 清除数据并刷新列表
//    void clearDataAndNotify();
}
