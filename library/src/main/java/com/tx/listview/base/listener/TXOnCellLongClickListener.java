package com.tx.listview.base.listener;

import android.view.View;

/**
 * 长按事件
 * <p>
 * Created by Cheng on 17/2/16.
 */
public interface TXOnCellLongClickListener<T> {

    /**
     * 长按事件回调
     *
     * @param data 数据
     * @param view view
     */
    boolean onCellLongClick(T data, View view);
}
