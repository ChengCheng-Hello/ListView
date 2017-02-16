package com.tx.listview.base.cell;

import android.view.View;

/**
 * Created by Cheng on 17/2/16.
 */
public interface TXBaseListCell<T> {

    /**
     * 数据回调
     *
     * @param model 数据
     */
    void setData(T model);

    /**
     * 布局ID
     *
     * @return 当前 cell 的布局文件 id
     */
    int getCellLayoutId();

    /**
     * 初始化 cell 的各个子 view。
     *
     * @param view 根view
     */
    void initCellViews(View view);
}
