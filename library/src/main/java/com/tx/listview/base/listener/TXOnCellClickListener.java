package com.tx.listview.base.listener;

import android.view.View;

/**
 * 点击事件
 * <p>
 * Created by Cheng on 17/2/16.
 */
public interface TXOnCellClickListener<T> {

    /**
     * 点击事件回调
     *
     * @param data 数据
     * @param view view
     */
    void onCellClick(T data, View view);
}
