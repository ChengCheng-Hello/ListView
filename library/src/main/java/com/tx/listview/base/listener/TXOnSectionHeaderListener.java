package com.tx.listview.base.listener;

import android.view.View;

/**
 * 设置列表分组悬停回调
 * <p>
 * Created by Cheng on 17/2/17.
 */
public interface TXOnSectionHeaderListener<T> {

    /**
     * 悬停的布局文件 id
     *
     * @return
     */
    int getSectionLayoutId();

    /**
     * 初始化悬停的各个子 view。
     *
     * @param view 根view
     */
    void initSectionViews(View view);

    /**
     * 分组悬停Cell的类型
     *
     * @return
     */
    int getSectionViewType();

    /**
     * 悬停区域显示的内容
     *
     * @param t
     */
    void setSectionData(T t);
}
