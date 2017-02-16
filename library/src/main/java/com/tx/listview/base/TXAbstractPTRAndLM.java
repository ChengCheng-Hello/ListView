package com.tx.listview.base;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.ColorInt;
import android.support.annotation.IntDef;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import com.cc.listview.R;
import com.tx.listview.base.listener.TXOnCellClickListener;
import com.tx.listview.base.listener.TXOnCellLongClickListener;
import com.tx.listview.base.listener.TXOnCreateCellListener;
import com.tx.listview.base.listener.TXOnCreateEmptyViewListener;
import com.tx.listview.base.listener.TXOnCreateErrorViewListener;
import com.tx.listview.base.listener.TXOnCreateHeaderViewListener;
import com.tx.listview.base.listener.TXOnGetCellViewTypeListener;
import com.tx.listview.base.listener.TXOnLoadMoreListener;
import com.tx.listview.base.listener.TXOnRefreshListener;
import com.tx.listview.base.listener.TXOnScrollListener;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 支持下拉刷新和加载更多的基类
 * <p>
 * Created by Cheng on 17/2/16.
 */
public abstract class TXAbstractPTRAndLM<T> extends FrameLayout implements TXBasePullToRefreshLoadMore, TXBaseListViewDataProcess<T> {

    // 下拉刷新事件
    protected TXOnRefreshListener mRefreshListener;
    // 加载更多事件
    protected TXOnLoadMoreListener<T> mLoadMoreListener;
    // CellViewType
    protected TXOnGetCellViewTypeListener<T> mCellViewTypeListener;
    // createCell
    protected TXOnCreateCellListener<T> mOnCreateCellListener;
    // Cell点击事件
    protected TXOnCellClickListener<T> mOnCellClickListener;
    // Cell长按事件
    protected TXOnCellLongClickListener<T> mOnCellLongClickListener;
    // 创建空布局事件
    protected TXOnCreateEmptyViewListener mOnCreateEmptyViewListener;
    // 创建错误布局事件
    protected TXOnCreateErrorViewListener mOnCreateErrorViewListener;
    // 创建头布局事件
    protected TXOnCreateHeaderViewListener mOnCreateHeaderViewListener;
    // 滚动监听
    protected TXOnScrollListener mOnScrollListener;

    private int mLoadingLayoutId;
    private int mEmptyLayoutId;
    private int mErrorLayoutId;
    private int mErrorNoNetworkLayoutId;
    private boolean mEnabledLoadMore;
    private boolean mEnabledPullToRefresh;
    private int mLoadingMoreLayoutId;
    private int mLoadMoreCompleteLayoutId;
    private int mHeaderLayoutId;

    private String mEmptyMsg;
    private String mErrorMSg;

    private int mLayoutType;
    private int mGridSpanCount;

    private boolean mEnabledSwipe;

    @ColorInt
    private int mPtrLoadingColor;
    private boolean mClipToPadding;
    private int mPadding;
    private int mPaddingTop;
    private int mPaddingBottom;
    private int mPaddingLeft;
    private int mPaddingRight;
    private int mOverScroll;

    public static final int LAYOUT_TYPE_LINEAR = 0;
    public static final int LAYOUT_TYPE_GRID = 1;

    @IntDef({LAYOUT_TYPE_LINEAR, LAYOUT_TYPE_GRID})
    @Retention(RetentionPolicy.SOURCE)
    public @interface LAYOUT_TYPE {
    }

    public static final int OVER_SCROLL_ALWAYS = 0;
    public static final int OVER_SCROLL_IF_CONTENT_SCROLLS = 1;
    public static final int OVER_SCROLL_NEVER = 2;

    @IntDef({OVER_SCROLL_ALWAYS, OVER_SCROLL_IF_CONTENT_SCROLLS, OVER_SCROLL_NEVER})
    @Retention(RetentionPolicy.SOURCE)
    public @interface OVER_SCROLL {
    }

    public TXAbstractPTRAndLM(Context context) {
        this(context, null);
    }

    public TXAbstractPTRAndLM(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TXAbstractPTRAndLM(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.TXPTRAndLMBase);
        if (a != null) {
            mLoadingLayoutId = a.getResourceId(R.styleable.TXPTRAndLMBase_txLayoutLoading, R.layout.tx_layout_listview_loading);
            mEmptyLayoutId = a.getResourceId(R.styleable.TXPTRAndLMBase_txLayoutEmpty, R.layout.tx_layout_listview_empty);
            mErrorLayoutId = a.getResourceId(R.styleable.TXPTRAndLMBase_txLayoutError, R.layout.tx_layout_listview_error);
            mErrorNoNetworkLayoutId = a.getResourceId(R.styleable.TXPTRAndLMBase_txLayoutErrorNoNetwork, R.layout.tx_layout_listview_error_no_network);
            mLoadingMoreLayoutId = a.getResourceId(R.styleable.TXPTRAndLMBase_txLayoutLoadMore, R.layout.tx_layout_listview_load_more);
            mLoadMoreCompleteLayoutId = a.getResourceId(R.styleable.TXPTRAndLMBase_txLayoutLoadMoreComplete, R.layout.tx_layout_listview_load_more_complete);
            mHeaderLayoutId = a.getResourceId(R.styleable.TXPTRAndLMBase_txLayoutHeader, 0);

            mEnabledLoadMore = a.getBoolean(R.styleable.TXPTRAndLMBase_txEnabledLoadMore, true);
            mEnabledPullToRefresh = a.getBoolean(R.styleable.TXPTRAndLMBase_txEnabledPullToRefresh, true);

            mEmptyMsg = a.getString(R.styleable.TXPTRAndLMBase_txEmptyMsg);
            mErrorMSg = a.getString(R.styleable.TXPTRAndLMBase_txErrorMsg);

            mLayoutType = a.getInt(R.styleable.TXPTRAndLMBase_txLayoutType, LAYOUT_TYPE_LINEAR);
            mGridSpanCount = a.getInt(R.styleable.TXPTRAndLMBase_txGridSpanCount, 1);

            mEnabledSwipe = a.getBoolean(R.styleable.TXPTRAndLMBase_txEnabledSwipe, false);

            mPtrLoadingColor = a.getColor(R.styleable.TXPTRAndLMBase_txPtrLoadingColor, ContextCompat.getColor(context, R.color.colorPrimary));
            mClipToPadding = a.getBoolean(R.styleable.TXPTRAndLMBase_txClipToPadding, true);
            mPadding = a.getDimensionPixelOffset(R.styleable.TXPTRAndLMBase_txPadding, -1);
            mPaddingTop = a.getDimensionPixelOffset(R.styleable.TXPTRAndLMBase_txPaddingTop, 0);
            mPaddingBottom = a.getDimensionPixelOffset(R.styleable.TXPTRAndLMBase_txPaddingBottom, 0);
            mPaddingRight = a.getDimensionPixelOffset(R.styleable.TXPTRAndLMBase_txPaddingRight, 0);
            mPaddingLeft = a.getDimensionPixelOffset(R.styleable.TXPTRAndLMBase_txPaddingLeft, 0);
            mOverScroll = a.getInt(R.styleable.TXPTRAndLMBase_txOverScrollMode, OVER_SCROLL_ALWAYS);

            a.recycle();
        }

        initView(context);
    }


    protected abstract void initView(Context context);

    @OVER_SCROLL
    public int getOverScroll() {
        return mOverScroll;
    }

    @LAYOUT_TYPE
    public int getLayoutType() {
        return mLayoutType;
    }

    public int getGridSpanCount() {
        return mGridSpanCount;
    }

    public void setOnRefreshListener(TXOnRefreshListener listener) {
        mRefreshListener = listener;
    }

    public void setOnLoadMoreListener(TXOnLoadMoreListener<T> listener) {
        mLoadMoreListener = listener;
    }

    public void setOnGetCellViewTypeListener(TXOnGetCellViewTypeListener<T> listener) {
        mCellViewTypeListener = listener;
    }

    public void setOnCreateCellListener(TXOnCreateCellListener<T> listener) {
        mOnCreateCellListener = listener;
    }

    public void setOnCellClickListener(TXOnCellClickListener<T> listener) {
        mOnCellClickListener = listener;
    }

    public void setOnCellLongClickListener(TXOnCellLongClickListener<T> listener) {
        mOnCellLongClickListener = listener;
    }

    public void setPullToRefreshEnabled(boolean pullToRefreshEnabled) {
        this.mEnabledPullToRefresh = pullToRefreshEnabled;
    }

    public void setOnCreateEmptyViewListener(TXOnCreateEmptyViewListener listener) {
        this.mOnCreateEmptyViewListener = listener;
    }

    public void setOnCreateErrorViewListener(TXOnCreateErrorViewListener listener) {
        this.mOnCreateErrorViewListener = listener;
    }

    public void setOnCreateHeaderViewListener(TXOnCreateHeaderViewListener listener) {
        this.mOnCreateHeaderViewListener = listener;
    }

    public void setOnScrollListener(TXOnScrollListener listener) {
        mOnScrollListener = listener;
    }

    public void setLoadMoreEnabled(boolean loadMoreEnabled) {
        this.mEnabledLoadMore = loadMoreEnabled;
    }

    protected int getLoadingLayoutId() {
        return mLoadingLayoutId;
    }

    protected int getEmptyLayoutId() {
        return mEmptyLayoutId;
    }

    protected int getErrorLayoutId() {
        return mErrorLayoutId;
    }

    protected int getErrorNoNetworkLayoutId() {
        return mErrorNoNetworkLayoutId;
    }

    public boolean isEnabledLoadMore() {
        return mEnabledLoadMore;
    }

    public boolean isEnabledPullToRefresh() {
        return mEnabledPullToRefresh;
    }

    protected int getLoadingMoreLayoutId() {
        return mLoadingMoreLayoutId;
    }

    protected int getLoadMoreCompleteLayoutId() {
        return mLoadMoreCompleteLayoutId;
    }

    protected int getHeaderLayoutId() {
        return mHeaderLayoutId;
    }

    protected String getEmptyMsg() {
        return mEmptyMsg;
    }

    protected String getErrorMsg() {
        return mErrorMSg;
    }

    public boolean isEnabledSwipe() {
        return mEnabledSwipe;
    }

    @ColorInt
    public int getLoadingColor() {
        return mPtrLoadingColor;
    }

    public boolean getClipToPadding() {
        return mClipToPadding;
    }

    public int getPadding() {
        return mPadding;
    }

    public int getPaddingTop() {
        return mPaddingTop;
    }

    public int getPaddingBottom() {
        return mPaddingBottom;
    }

    public int getPaddingLeft() {
        return mPaddingLeft;
    }

    public int getPaddingRight() {
        return mPaddingRight;
    }

    public abstract void scrollToPosition(int position);
}
