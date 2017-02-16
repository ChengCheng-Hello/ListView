package com.tx.listview.base.cell;

/**
 * 滑动的cell
 * <p>
 * Created by Cheng on 17/2/16.
 */
public interface TXBaseSwipeListCell<T> extends TXBaseListCell<T> {

    /**
     * SwipeLayout的总布局的id
     *
     * @return
     */
    int getSwipeLayoutId();

    /**
     * 内容区域的id
     *
     * @return
     */
    int getContentLayoutId();
}
