package com.tx.listview;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cc.listview.R;
import com.daimajia.swipe.SwipeLayout;
import com.daimajia.swipe.implments.SwipeItemMangerImpl;
import com.daimajia.swipe.interfaces.SwipeAdapterInterface;
import com.daimajia.swipe.interfaces.SwipeItemMangerInterface;
import com.daimajia.swipe.util.Attributes;
import com.tx.listview.base.TXAbstractListViewAdapter;
import com.tx.listview.base.TXAbstractPTRAndLM;
import com.tx.listview.base.TXBaseViewHolder;
import com.tx.listview.base.cell.TXBaseListCell;
import com.tx.listview.base.cell.TXBaseSwipeListCell;
import com.tx.listview.base.listener.TXOnLoadMoreListener;
import com.tx.listview.base.listener.TXOnLoadingListener;
import com.tx.listview.base.listener.TXOnRefreshListener;
import com.tx.listview.base.listener.TXOnSectionHeaderListener;

import java.util.List;

/**
 * Created by Cheng on 17/2/16.
 */

public class TXListView<T> extends TXAbstractPTRAndLM<T> {

    private MyAdapter<T> mAdapter;
    private SwipeRefreshLayout mPullToRefreshView;
    private RecyclerView mRv;
    private RecyclerView.LayoutManager mLayoutManager;
    private boolean mHasHeader;
    private RecyclerView.OnScrollListener mOnScrollListener;
    private static final Object mLock = new Object();
    protected View mSectionView;

    public TXListView(Context context) {
        super(context);
    }

    public TXListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public TXListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void initView(Context context) {
        View view;
        if (isEnabledSection()) {
            view = LayoutInflater.from(context).inflate(R.layout.tx_layout_section_list_recycleview, this);
        } else {
            view = LayoutInflater.from(context).inflate(R.layout.tx_layout_default_list_recycleview, this);
        }

        mPullToRefreshView = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout);
        mPullToRefreshView.setColorSchemeColors(getLoadingColor());
        // 第一次加载的时候不显示
        setPullToRefreshEnabled(isEnabledPullToRefresh());
        mPullToRefreshView.setEnabled(false);

        mRv = (RecyclerView) view.findViewById(R.id.rv_list);
        mRv.setClipToPadding(getClipToPadding());
        int padding = getPadding();
        if (padding != -1) {
            mRv.setPadding(padding, padding, padding, padding);
        } else {
            mRv.setPadding(getPaddingLeft(), getPaddingTop(), getPaddingRight(), getPaddingBottom());
        }

        mRv.setOverScrollMode(getOverScroll());

        int layoutType = getLayoutType();
        if (layoutType == LAYOUT_TYPE_LINEAR) {
            mRv.setLayoutManager(new LinearLayoutManager(context));
        } else if (layoutType == LAYOUT_TYPE_GRID) {
            mRv.setLayoutManager(new GridLayoutManager(context, getGridSpanCount()));
        }

        if (isEnabledSwipe()) {
            mAdapter = new MySwipeAdapter<>(this);
        } else {
            mAdapter = new MyAdapter<>(this);
        }
        mHasHeader = getHeaderLayoutId() != 0;
        mAdapter.setHasHeader(mHasHeader);
        mAdapter.setLoadMoreEnabled(isEnabledLoadMore());
        mRv.setAdapter(mAdapter);

        mLayoutManager = mRv.getLayoutManager();
        if (mLayoutManager != null && mLayoutManager instanceof GridLayoutManager) {
            GridLayoutManager glm = (GridLayoutManager) mLayoutManager;
            glm.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    if (mAdapter.showFullWidth(position)) {
                        return getGridSpanCount();
                    } else {
                        return 1;
                    }
                }
            });
        }

        mAdapter.setLoadingListener(new TXOnLoadingListener() {
            @Override
            public void onLoading(boolean canPtr) {
                if (!isEnabledPullToRefresh()) {
                    return;
                }

                mPullToRefreshView.setEnabled(canPtr);
            }
        });
    }

    @Override
    public void setOnSectionHeaderListener(TXOnSectionHeaderListener<T> listener) {
        super.setOnSectionHeaderListener(listener);

        if (isEnabledSection()) {
            if (mOnSectionHeaderListener == null) {
                return;
            }

            if (mOnSectionHeaderListener.getSectionLayoutId() <= 0) {
                return;
            }

            FrameLayout flSection = (FrameLayout) findViewById(R.id.fl_section);
            mSectionView = LayoutInflater.from(getContext()).inflate(mOnSectionHeaderListener.getSectionLayoutId(), null, false);
            if (mSectionView == null) {
                return;
            }
            mOnSectionHeaderListener.initSectionViews(mSectionView);
            FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
            flSection.addView(mSectionView, layoutParams);
            mSectionView.setVisibility(View.INVISIBLE);

            mOnScrollListener = new RecyclerView.OnScrollListener() {

                private int mSectionHeight;
                private int mCurrentPos;

                @Override
                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);

                    mSectionHeight = mSectionView.getHeight();
                }

                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);

                    if (dy == 0) {
                        return;
                    }

                    if (!mSectionView.isShown() && mCurrentPos == 0) {
                        synchronized (mLock) {
                            List<T> allData = mAdapter.getAllData();
                            int size = allData.size();
                            if (size < 1) {
                                return;
                            }
                            mOnSectionHeaderListener.setSectionData(allData.get(mCurrentPos));
                            mSectionView.setVisibility(View.VISIBLE);
                        }
                    }

                    int itemViewType = mAdapter.getItemViewType(mCurrentPos + 1);
                    if (itemViewType == mOnSectionHeaderListener.getSectionViewType()) {
                        View view = mLayoutManager.findViewByPosition(mCurrentPos + 1);
                        if (view != null) {
                            if (view.getTop() <= mSectionHeight) {
                                mSectionView.setY(view.getTop() - mSectionHeight);
                            } else {
                                mSectionView.setY(0);
                            }
                        }
                    }

                    if (mCurrentPos != ((LinearLayoutManager) mLayoutManager).findFirstVisibleItemPosition()) {
                        mCurrentPos = ((LinearLayoutManager) mLayoutManager).findFirstVisibleItemPosition();

                        mSectionView.setY(0);

                        synchronized (mLock) {
                            List<T> allData = mAdapter.getAllData();
                            int size = allData.size();
                            if (mCurrentPos < 0 || mCurrentPos >= size) {
                                return;
                            }
                            mOnSectionHeaderListener.setSectionData(allData.get(mCurrentPos));
                        }
                    }
                }
            };

            mRv.addOnScrollListener(mOnScrollListener);
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mOnScrollListener != null) {
            mRv.removeOnScrollListener(mOnScrollListener);
            mOnScrollListener = null;
        }
    }

    private void setRefreshing(final boolean refreshing) {
        mPullToRefreshView.post(new Runnable() {
            @Override
            public void run() {
                mPullToRefreshView.setRefreshing(refreshing);
                if (refreshing) {
                    if (mRefreshListener != null) {
                        mRefreshListener.onRefresh();
                    }
                }
            }
        });
    }

    @Override
    public void setPullToRefreshEnabled(boolean pullToRefreshEnabled) {
        super.setPullToRefreshEnabled(pullToRefreshEnabled);
        mPullToRefreshView.setEnabled(pullToRefreshEnabled);
    }

    @Override
    public void setLoadMoreEnabled(boolean loadMoreEnabled) {
        super.setLoadMoreEnabled(loadMoreEnabled);

        mAdapter.setLoadMoreEnabled(loadMoreEnabled);
    }

    @Override
    public void showRefreshError(Context context, long code, String message) {
        mPullToRefreshView.setRefreshing(false);
        if (!isEmpty()) {
            // TODO
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
        }
        mAdapter.loadError(code, message);
    }

    @Override
    public void showLoadMoreError(Context context, long code, String message) {
        // TODO
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();

        mAdapter.loadError(code, message);

        if (mAdapter.isEmpty()) {
            return;
        }

        int lastVisibleItemPosition = 0;
        if (mLayoutManager instanceof GridLayoutManager) {
            lastVisibleItemPosition = ((GridLayoutManager) mLayoutManager).findLastVisibleItemPosition();
        } else if (mLayoutManager instanceof LinearLayoutManager) {
            lastVisibleItemPosition = ((LinearLayoutManager) mLayoutManager).findLastVisibleItemPosition();
        }

        int count = mAdapter.getCount();
        if (lastVisibleItemPosition != count) {
            return;
        }

        float height = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 48, getResources().getDisplayMetrics());
        mRv.scrollBy(0, (int) -height);
    }

    @Override
    public void refresh() {
        mAdapter.clearDataAndNotify();
        if (mRefreshListener != null) {
            mRefreshListener.onRefresh();
        }
    }

    private void reload() {
        setRefreshing(true);
    }

    private void clearDataAndNotify() {
        mAdapter.clearDataAndNotify();
    }

    @Override
    public void setOnRefreshListener(TXOnRefreshListener listener) {
        super.setOnRefreshListener(listener);

        mPullToRefreshView.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (mRefreshListener != null) {
                    mRefreshListener.onRefresh();
                }
            }
        });
    }

    @Override
    public void setOnLoadMoreListener(TXOnLoadMoreListener<T> listener) {
        super.setOnLoadMoreListener(listener);

        mAdapter.setLoadMoreListener(new TXOnLoadMoreListener<T>() {
            @Override
            public void onLoadMore(T lastData) {
                if (mLoadMoreListener != null && lastData != null) {
                    mLoadMoreListener.onLoadMore(lastData);
                }
            }
        });
    }


    public void addOnScrollListener(RecyclerView.OnScrollListener listener) {
        if (mRv != null) {
            mRv.addOnScrollListener(listener);
        }
    }

    public void removeOnScrollListener(RecyclerView.OnScrollListener listener) {
        if (mRv != null) {
            mRv.removeOnScrollListener(listener);
        }
    }

    public void setOnTouchListener(OnTouchListener listener) {
        mRv.setOnTouchListener(listener);
    }

    @Override
    public void scrollToPosition(int position) {
        if (mHasHeader) {
            position = position + 1;
        }
        ((LinearLayoutManager) mLayoutManager).scrollToPositionWithOffset(position, 0);
    }

    @Override
    public void setAllData(List<T> listData) {
        mAdapter.setAllData(listData);
        mPullToRefreshView.setRefreshing(false);
    }

    @Override
    public void appendData(List<T> listData) {
        mAdapter.appendData(listData);
    }

    @Override
    public void insertDataToFront(List<T> listData) {
        mAdapter.insertDataToFront(listData);
    }

    @Override
    public void appendData(T data) {
        mAdapter.appendData(data);
    }

    @Override
    public void insertData(T data, int position) {
        mAdapter.insertData(data, position);
    }

    @Override
    public void insertData(List<T> listData, int position) {
        mAdapter.insertData(listData, position);
    }

    @Override
    public void updateData(T data) {
        mAdapter.updateData(data);
        if (isEnabledSwipe() && mAdapter instanceof MySwipeAdapter) {
            MySwipeAdapter<T> swipeAdapter = (MySwipeAdapter<T>) mAdapter;
            swipeAdapter.closeAllItems();
        }
    }

    @Override
    public void removeData(T data) {
        mAdapter.removeData(data);
        if (isEnabledSwipe() && mAdapter instanceof MySwipeAdapter) {
            MySwipeAdapter<T> swipeAdapter = (MySwipeAdapter<T>) mAdapter;
            swipeAdapter.closeAllItems();
        }
    }

    @Override
    public boolean isEmpty() {
        return mAdapter.isEmpty();
    }

    @Override
    public void notifyDataChanged() {
        mAdapter.notifyDataChanged();
    }

    @Override
    public List<T> getAllData() {
        return mAdapter.getAllData();
    }

    private static class MyAdapter<T> extends TXAbstractListViewAdapter<T> {

        TXListView<T> listView;

        MyAdapter(TXListView<T> listView) {
            super();
            this.listView = listView;
        }

        @Override
        protected TXBaseViewHolder onDefCreateViewHolder(ViewGroup parent, int viewType) {
            if (listView.mOnCreateCellListener != null) {
                TXBaseListCell<T> txBaseListCell = listView.mOnCreateCellListener.onCreateCell(viewType);
                int cellLayoutId = txBaseListCell.getCellLayoutId();

                View itemView = LayoutInflater.from(parent.getContext()).inflate(cellLayoutId, parent, false);
                txBaseListCell.initCellViews(itemView);

                return new MyHolder(itemView, txBaseListCell);
            }
            return null;
        }

        @Override
        protected int getDefItemViewType(T data) {
            if (listView.mCellViewTypeListener != null) {
                return listView.mCellViewTypeListener.getCellViewType(data);
            }
            return super.getDefItemViewType(data);
        }

        @Override
        protected void onDefBindViewHolder(TXBaseViewHolder holder, final int position) {
            int size = mListData.size();
            if (position < 0 || position >= size) {
                return;
            }
            final T data = mListData.get(position);

            if (listView.mOnCellClickListener != null) {
                holder.itemView.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        listView.mOnCellClickListener.onCellClick(data, v);
                    }
                });
            }

            if (listView.mOnCellLongClickListener != null) {
                holder.itemView.setOnLongClickListener(new OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        return listView.mOnCellLongClickListener.onCellLongClick(data, v);
                    }
                });
            }

            MyHolder myHolder = (MyHolder) holder;
            myHolder.txBaseListCell.setData(data);
        }

        @Override
        public View getEmptyView(ViewGroup parent) {
            View view = LayoutInflater.from(parent.getContext()).inflate(listView.getEmptyLayoutId(), parent, false);

            TextView tvEmptyMsg = (TextView) view.findViewById(R.id.tx_ids_list_empty_msg);

            if (tvEmptyMsg != null) {
                tvEmptyMsg.setText(listView.getEmptyMsg());
            }

            if (listView.mOnCreateEmptyViewListener != null) {
                listView.mOnCreateEmptyViewListener.onCreateEmptyView(view);
            }

            if (listView.isEnabledSection() && listView.mSectionView != null) {
                listView.mSectionView.setVisibility(View.INVISIBLE);
            }

            return view;
        }

        @Override
        public View getErrorView(ViewGroup parent, long errorCode, String message) {
            View view;
            // TODO no network code
            if (9999 == errorCode) {
                view = LayoutInflater.from(parent.getContext()).inflate(listView.getErrorNoNetworkLayoutId(), parent, false);
            } else {
                view = LayoutInflater.from(parent.getContext()).inflate(listView.getErrorLayoutId(), parent, false);
                TextView tv = (TextView) view.findViewById(R.id.tx_ids_list_error_msg);
                if (tv != null) {
                    tv.setText(listView.getErrorMsg());
                }
            }

            View reloadView = view.findViewById(R.id.tx_ids_list_reload);
            if (reloadView != null) {
                reloadView.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onReload();

                        if (listView.mRefreshListener != null) {
                            listView.mRefreshListener.onRefresh();
                        }
                    }
                });
            }

            if (listView.mOnCreateErrorViewListener != null) {
                listView.mOnCreateErrorViewListener.onCreateErrorView(view, errorCode, message);
            }

            if (listView.isEnabledSection() && listView.mSectionView != null) {
                listView.mSectionView.setVisibility(View.INVISIBLE);
            }

            return view;
        }

        @Override
        public View getLoadMoreView(ViewGroup parent) {
            return LayoutInflater.from(parent.getContext()).inflate(listView.getLoadingMoreLayoutId(), parent, false);
        }

        @Override
        public View getHeaderView(ViewGroup parent) {
            View view = LayoutInflater.from(parent.getContext()).inflate(listView.getHeaderLayoutId(), parent, false);

            if (listView.mOnCreateHeaderViewListener != null) {
                listView.mOnCreateHeaderViewListener.onCreateHeaderView(view);
            }

            return view;
        }

        @Override
        public View getLoadMoreCompleteView(ViewGroup parent) {
            return LayoutInflater.from(parent.getContext()).inflate(listView.getLoadMoreCompleteLayoutId(), parent, false);
        }

        @Override
        public View getLoadingView(ViewGroup parent) {
            return LayoutInflater.from(parent.getContext()).inflate(listView.getLoadingLayoutId(), parent, false);
        }

        private class MyHolder extends TXBaseViewHolder {

            TXBaseListCell<T> txBaseListCell;

            MyHolder(View view, TXBaseListCell<T> txBaseListCell) {
                super(view);
                this.txBaseListCell = txBaseListCell;
            }
        }
    }

    private static class MySwipeAdapter<T> extends MyAdapter<T> implements SwipeItemMangerInterface, SwipeAdapterInterface {

        public SwipeItemMangerImpl mItemManger = new SwipeItemMangerImpl(this);

        public MySwipeAdapter(TXListView<T> listView) {
            super(listView);
        }

        @Override
        protected TXBaseViewHolder onDefCreateViewHolder(ViewGroup parent, int viewType) {
            MySwipeHolder holder = null;

            if (listView.mOnCreateCellListener != null) {
                TXBaseSwipeListCell<T> listCell = (TXBaseSwipeListCell<T>) listView.mOnCreateCellListener.onCreateCell(viewType);
                int cellLayoutId = listCell.getCellLayoutId();

                View view = LayoutInflater.from(parent.getContext()).inflate(cellLayoutId, parent, false);
                listCell.initCellViews(view);

                holder = new MySwipeHolder(view, listCell);
            }
            return holder;
        }

        @Override
        protected void onDefBindViewHolder(TXBaseViewHolder holder, final int position) {
            int size = mListData.size();
            if (position < 0 || position >= size) {
                return;
            }
            final T data = mListData.get(position);

            MySwipeHolder myHolder = (MySwipeHolder) holder;

            if (listView.mOnCellClickListener != null && myHolder.contentView != null) {
                myHolder.contentView.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        listView.mOnCellClickListener.onCellClick(data, v);
                    }
                });
            }

            if (listView.mOnCellLongClickListener != null && myHolder.contentView != null) {
                myHolder.contentView.setOnLongClickListener(new OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        return listView.mOnCellLongClickListener.onCellLongClick(data, v);
                    }
                });
            }

            myHolder.listCell.setData(data);

            mItemManger.bind(myHolder.itemView, position, myHolder.swipeLayoutId);
        }

        private class MySwipeHolder extends TXBaseViewHolder {

            protected TXBaseSwipeListCell<T> listCell;
            protected int swipeLayoutId;
            protected View contentView;

            public MySwipeHolder(View view, TXBaseSwipeListCell<T> listCell) {
                super(view);
                this.listCell = listCell;
                this.swipeLayoutId = listCell.getSwipeLayoutId();
                int contentResId = listCell.getContentLayoutId();
                if (contentResId > 0) {
                    contentView = view.findViewById(contentResId);
                }
            }
        }

        @Override
        public void notifyDatasetChanged() {
            super.notifyDataSetChanged();
        }

        @Override
        public void openItem(int position) {
            mItemManger.openItem(position);
        }

        @Override
        public void closeItem(int position) {
            mItemManger.closeItem(position);
        }

        @Override
        public void closeAllExcept(SwipeLayout layout) {
            mItemManger.closeAllExcept(layout);
        }

        @Override
        public void closeAllItems() {
            mItemManger.closeAllItems();
        }

        @Override
        public List<Integer> getOpenItems() {
            return mItemManger.getOpenItems();
        }

        @Override
        public List<SwipeLayout> getOpenLayouts() {
            return mItemManger.getOpenLayouts();
        }

        @Override
        public void removeShownLayouts(SwipeLayout layout) {
            mItemManger.removeShownLayouts(layout);
        }

        @Override
        public boolean isOpen(int position) {
            return mItemManger.isOpen(position);
        }

        @Override
        public Attributes.Mode getMode() {
            return mItemManger.getMode();
        }

        @Override
        public void setMode(Attributes.Mode mode) {
            mItemManger.setMode(mode);
        }
    }
}
