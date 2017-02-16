package com.tx.listview.base.listener;

import android.support.annotation.IntDef;

/**
 * 滚动监听
 * <p>
 * Created by Cheng on 17/2/16.
 */
public interface TXOnScrollListener {

    // is not currently scrolling.
    int SCROLL_STATE_IDLE = 0;

    // is currently being dragged by outside input such as user touch input.
    int SCROLL_STATE_DRAGGING = 1;

    // is currently animating to a final position while not under outside control.
    int SCROLL_STATE_SETTLING = 2;

    @IntDef({SCROLL_STATE_IDLE, SCROLL_STATE_DRAGGING, SCROLL_STATE_SETTLING})
    @interface STATUS {
    }

    /**
     * 滚动回调
     *
     * @param dy 垂直方向滚动距离
     */
    void onScrolled(int dy);

    /**
     * 滚动状态改变
     *
     * @param newStatus 状态
     */
    void onScrollStateChanged(@STATUS int newStatus);
}
