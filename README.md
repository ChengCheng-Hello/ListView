# ListView
列表封装

# TXListView
### 通过XML进行各种配置
```xml
        <!-- 数据头部布局 -->
        <attr name="txLayoutHeader" format="reference" />
        <!-- 首次加载中布局 -->
        <attr name="txLayoutLoading" format="reference" />
        <!-- 无数据时布局 -->
        <attr name="txLayoutEmpty" format="reference" />
        <!-- 错误通用布局 -->
        <attr name="txLayoutError" format="reference" />
        <!-- 错误无网络布局 -->
        <attr name="txLayoutErrorNoNetwork" format="reference" />
        <!-- 加载更多布局 -->
        <attr name="txLayoutLoadMore" format="reference" />
        <!-- 加载更多完成布局 -->
        <attr name="txLayoutLoadMoreComplete" format="reference" />

        <!-- 下拉刷新 -->
        <attr name="txEnabledPullToRefresh" format="boolean" />
        <!-- 加载更多 -->
        <attr name="txEnabledLoadMore" format="boolean" />

        <!-- 无数据时文案 -->
        <attr name="txEmptyMsg" format="string" />
        <!-- 错误时文案 -->
        <attr name="txErrorMsg" format="string" />

        <!-- 布局类型 -->
        <attr name="txLayoutType">
            <flag name="linear" value="0x0" />
            <flag name="grid" value="0x1" />
        </attr>
        <!-- 如果布局类型是grid的话,列数-->
        <attr name="txGridSpanCount" format="integer" />
        <!-- 是否支持滑动操作 -->
        <attr name="txEnabledSwipe" format="boolean" />

        <!-- 下拉Loading颜色 -->
        <attr name="txPtrLoadingColor" format="color" />

        <!-- 列表属性 -->
        <attr name="txClipToPadding" format="boolean" />
        <attr name="txPadding" format="dimension" />
        <attr name="txPaddingTop" format="dimension" />
        <attr name="txPaddingBottom" format="dimension" />
        <attr name="txPaddingLeft" format="dimension" />
        <attr name="txPaddingRight" format="dimension" />
        <attr name="txOverScrollMode">
            <flag name="always" value="0x0" />
            <flag name="ifContentScrolls " value="0x1" />
            <flag name="never" value="0x2" />
        </attr>
```

### 处理数据方法
```java
    // 设置加载错误信息
    void showRefreshError(Context context, long code, String message);

    // 加载更多设置加载错误信息
    void showLoadMoreError(Context context, long code, String message);

    // 刷新列表，如筛选刷新
    void refresh();

    // 首次or下拉刷新 添加数据
    void setAllData(List<T> listData);

    // 加载更多 添加数据
    void appendData(List<T> listData);

    // 添加到头部
    void insertDataToFront(List<T> listData);

    // 添加到尾部
    void appendData(T data);

    // 插入一条数据
    void insertData(T data, int position);

    // 插入多条数据
    void insertData(List<T> listData, int position);

    // 移除数据，注意：因为是根据data进行删除，data需要重写equals方法。
    void removeData(T data);

    // 判断数据是否为空
    boolean isEmpty();

    // 通知列表刷新
    void notifyDataChanged();

    // 滚动到指定位置
    void scrollToPosition(int position);
```

### 支持两种cell
1. 普通cell，实现`TXBaseListCellV2<T>`。
2. 滑动cell，实现`TXBaseSwipeListCellV2<T>`。

### `TXBaseListActivityV2<T>` 集成TXListView的Activity
- 可替换`bindContentView`: 布局id。
- 可替换`getListViewId`: TXListView的id。
- `initData`: 子类需要在初始化list相关类之前初始其他数据的可以重写这个方法。
- `onLoadMore(T lastData)`: 加载更多回调，`lastData`最后一条数据。
- `onRefresh`: 下拉刷新回调。
- `TXBaseListCellV2<T> onCreateCell(int viewType)`: 用于创建相应`viewType`对应的cell。
- `getItemViewType(T data)`: 设置不同的type。`注意：data可能为空`
- `onItemClick`: 点击事件回调。
- `onItemLongClick`: 长按事件回调。
- `onCreateEmptyView`: 创建空布局的回调。
- `onCreateErrorView`: 创建错误布局的回调。
- `onCreateHeaderView`: 创建Header布局的回调。
- `refresh`: 用于主动刷新，如筛选刷新。

### 实际使用
```java
if (result.code == TXErrorConst.ERROR_CODE_SUCCESS) {
    if (lastId == -1) {
        // 下拉刷新
        mListView.setAllData(list);
    } else {
        // 加载更多
        mListView.appendData(list);
    }
} else {
    if (lastId == -1) {
        // 下拉刷新
        mListView.showRefreshError(context, result.code, result.message);
    } else {
        // 加载更多
        mListView.showLoadMoreError(context, result.code, result.message);
    }
}
```

```xml
    <!-- 列表样式 -->
    <style name="tx_list_u2">
        <item name="txLayoutLoading">@layout/tx_layout_lv_loading_u2</item>
        <item name="txLayoutEmpty">@layout/tx_layout_lv_empty_u2</item>
        <item name="txLayoutError">@layout/tx_layout_lv_error_u2</item>
        <item name="txLayoutLoadMore">@layout/tx_layout_lv_load_more_u2</item>
        <item name="txLayoutLoadMoreComplete">@layout/tx_layout_lv_load_more_complete_u2</item>
        <item name="txPtrLoadingColor">@color/TX_CO_BLUESEC</item>
        <item name="txEmptyMsg">@string/tx_load_no_data</item>
        <item name="txErrorMsg">@string/tx_load_error_fail</item>
    </style>
```

### 注意事项
- 列表`PageSize>=20`，如果小于20会有问题。
- 如果是多种cell，使用`removeData(T data)`之后，需要再调用一遍`notifyDataChanged()`，因为`removeData(T data)`只会更新一条数据。
