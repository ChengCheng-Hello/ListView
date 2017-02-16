package com.tx.listview.base.listener;

/**
 * 正在加载
 * <p/>
 * Created by Cheng on 17/2/16.
 */
public interface TXOnLoadingListener {

    /**
     * 刷新数据回调
     *
     * @param canPtr 是否下拉刷新
     */
    void onLoading(boolean canPtr);
}
