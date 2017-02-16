package com.tx.listview.base.listener;

/**
 * getItemViewType回调
 * <p/>
 * Created by Cheng on 17/2/16.
 */
public interface TXOnGetCellViewTypeListener<T> {

    int getCellViewType(T data);
}
