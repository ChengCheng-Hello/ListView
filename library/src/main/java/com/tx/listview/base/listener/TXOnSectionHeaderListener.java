package com.tx.listview.base.listener;

import android.widget.TextView;

/**
 * 设置列表分组悬停回调
 * <p>
 * Created by Cheng on 17/2/17.
 */
public interface TXOnSectionHeaderListener<T> {

    /**
     * 分组悬停的view，目前只能是TextView
     *
     * @return
     */
    TextView getSectionTextView();

    /**
     * 分组悬停Cell的类型
     *
     * @return
     */
    int getSectionCellViewType();

    /**
     * 悬停区域显示的内容
     *
     * @param t
     * @return
     */
    String getSectionContent(T t);
}
