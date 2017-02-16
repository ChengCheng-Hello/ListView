package com.tx.listview.base.listener;


import android.view.View;

/**
 * 创建错误布局回调
 * <p/>
 * Created by Cheng on 17/2/16.
 */
public interface TXOnCreateErrorViewListener {

    /**
     * 创建错误布局回调
     *
     * @param view      错误布局
     * @param errorCode 错误code
     * @param errorMsg  错误信息
     */
    void onCreateErrorView(View view, long errorCode, String errorMsg);
}
