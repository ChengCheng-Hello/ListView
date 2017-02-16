package com.tx.listview.base.listener;

import com.tx.listview.base.cell.TXBaseListCell;

/**
 * 创建cell
 * <p>
 * Created by Cheng on 17/2/16.
 */
public interface TXOnCreateCellListener<T> {

    /**
     * 创建cell回调
     *
     * @param viewType 类型
     * @return viewType对应的cell
     */
    TXBaseListCell<T> onCreateCell(int viewType);
}
