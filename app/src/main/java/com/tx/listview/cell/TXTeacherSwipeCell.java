package com.tx.listview.cell;

import android.view.View;
import android.widget.TextView;

import com.tx.listview.R;
import com.tx.listview.base.cell.TXBaseSwipeListCell;
import com.tx.listview.model.TXTeacherModel;

/**
 * Created by Cheng on 17/2/16.
 */

public class TXTeacherSwipeCell implements TXBaseSwipeListCell<TXTeacherModel> {

    private TextView mTvName;

    @Override
    public void setData(TXTeacherModel model) {
        if (model == null) {
            return;
        }

        mTvName.setText(model.name);
    }

    @Override
    public int getCellLayoutId() {
        return R.layout.tx_item_teacher_swipe;
    }

    @Override
    public void initCellViews(View view) {
        mTvName = (TextView) view.findViewById(R.id.tv_name);
    }

    @Override
    public int getSwipeLayoutId() {
        return R.id.swipeLayout;
    }

    @Override
    public int getContentLayoutId() {
        return R.id.ll_content;
    }
}
