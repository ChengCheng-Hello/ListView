package com.tx.listview;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.tx.listview.base.TXBaseListActivity;
import com.tx.listview.base.cell.TXBaseListCell;
import com.tx.listview.cell.TXTeacherSwipeCell;
import com.tx.listview.model.TXTeacherModel;

import java.util.ArrayList;
import java.util.List;

import static android.R.attr.data;

public class TeacherSwipeListActivity extends TXBaseListActivity<TXTeacherModel> {

    private static final String TAG = "TeacherSwipeListActivity";

    private static final int TYPE_NORMAL = 0;
    private static final int TYPE_ERROR = 1;
    private static final int TYPE_LM_ERROR = 2;
    private static final int TYPE_EMPTY = 3;
    private static final int TYPE_LM_EMPTY = 4;

    private int mType = TYPE_NORMAL;
    private List<TXTeacherModel> list;

    public static void launch(Context context) {
        Intent intent = new Intent(context, TeacherSwipeListActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected boolean bindContentView() {
        setContentView(R.layout.activity_teacher_swipe_list);
        return true;
    }

    @Override
    protected void initData() {
        initTitle();

        list = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            TXTeacherModel txTeacherModel = new TXTeacherModel();
            txTeacherModel.name = "teacher name " + i;
            list.add(txTeacherModel);
        }
    }

    @Override
    public void onLoadMore(TXTeacherModel lastData) {
        Toast.makeText(this, "onLoadMore " + data, Toast.LENGTH_SHORT).show();

        mListView.postDelayed(new Runnable() {
            @Override
            public void run() {
                switch (mType) {
                    case TYPE_NORMAL:
                    case TYPE_EMPTY:
                    case TYPE_ERROR:
                        mListView.appendData(list);
                        break;
                    case TYPE_LM_ERROR:
                        mListView.showLoadMoreError(TeacherSwipeListActivity.this, 1234, "error");
                        break;
                    case TYPE_LM_EMPTY:
                        mListView.appendData(new ArrayList<TXTeacherModel>());
                        break;
                }
            }
        }, 2000);
    }

    @Override
    public void onRefresh() {
        Toast.makeText(this, "onRefresh ", Toast.LENGTH_SHORT).show();

        mListView.postDelayed(new Runnable() {
            @Override
            public void run() {
                switch (mType) {
                    case TYPE_NORMAL:
                    case TYPE_LM_EMPTY:
                    case TYPE_LM_ERROR:
                        mListView.setAllData(list);
                        break;
                    case TYPE_ERROR:
                        mListView.showRefreshError(TeacherSwipeListActivity.this, 12234, "error hh");
                        break;
                    case TYPE_EMPTY:
                        mListView.setAllData(null);
                        break;
                }
            }
        }, 2000);
    }

    @Override
    public TXBaseListCell<TXTeacherModel> onCreateCell(int viewType) {
        return new TXTeacherSwipeCell();
    }

    @Override
    public void onCellClick(TXTeacherModel data, View view) {
        Toast.makeText(view.getContext(), "data is " + data, Toast.LENGTH_SHORT).show();

        super.onCellClick(data, view);
    }

    @Override
    public boolean onCellLongClick(TXTeacherModel data, View view) {
        Toast.makeText(view.getContext(), "long click data is " + data, Toast.LENGTH_SHORT).show();

        return true;
    }

    private void initTitle() {
        final Toolbar tb = (Toolbar) findViewById(R.id.tb);
        tb.setTitle(TAG);
        tb.inflateMenu(R.menu.toolbar_menu);
        tb.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                int menuId = item.getItemId();
                switch (menuId) {
                    case R.id.action_normal:
                        mType = TYPE_NORMAL;
                        break;
                    case R.id.action_error:
                        mType = TYPE_ERROR;
                        break;
                    case R.id.action_loadmore_error:
                        mType = TYPE_LM_ERROR;
                        break;
                    case R.id.action_loadmore_empty:
                        mType = TYPE_LM_EMPTY;
                        break;
                    case R.id.action_empty:
                        mType = TYPE_EMPTY;
                        break;
                    case R.id.action_add_font:
                        List<TXTeacherModel> list = new ArrayList<>();
                        for (int i = 0; i < 3; i++) {
                            list.add(new TXTeacherModel("this is appendData to front" + i));
                        }
                        mListView.insertDataToFront(list);
                        break;
                    case R.id.action_add_one:
                        mListView.appendData(new TXTeacherModel("this is appendData one"));
                        break;
                    case R.id.action_insert:
                        mListView.insertData(new TXTeacherModel("this is insertData to 5"), 5);
                        break;
                    case R.id.action_update: {
                        List<TXTeacherModel> allData = mListView.getAllData();
                        TXTeacherModel txTeacherModel = allData.get(0);
                        txTeacherModel.name = "update one";
                        mListView.updateData(txTeacherModel);
                        break;
                    }
                    case R.id.action_remove: {
                        List<TXTeacherModel> allData = mListView.getAllData();
                        TXTeacherModel txTeacherModel = allData.get(0);
                        mListView.removeData(txTeacherModel);
                    }
                    break;
                }
                return false;
            }
        });
    }
}
