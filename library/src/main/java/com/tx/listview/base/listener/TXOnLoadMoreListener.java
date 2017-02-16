package com.tx.listview.base.listener;

/**
 * 加载更多
 * <p/>
 * Created by Cheng on 17/2/16.
 */
public interface TXOnLoadMoreListener<T> {

    /**
     * 加载更多回调
     *
     * @param lastData 最后一条数据
     */
    void onLoadMore(T lastData);
}
