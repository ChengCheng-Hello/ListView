package com.tx.listview.cell;

import android.view.View;
import android.widget.TextView;

import com.tx.listview.R;
import com.tx.listview.base.cell.TXBaseListCell;
import com.tx.listview.model.TXTeacherModel;

/**
 * Created by Cheng on 17/2/16.
 */

public class TXTeacherGroupCell implements TXBaseListCell<TXTeacherModel> {

    private TextView mTvName;

    @Override
    public void setData(TXTeacherModel model) {
        if (model == null) {
            return;
        }

        mTvName.setText(model.tag);
    }

    @Override
    public int getCellLayoutId() {
        return R.layout.tx_item_teacher_group;
    }

    @Override
    public void initCellViews(View view) {
        mTvName = (TextView) view.findViewById(R.id.tv_tip);
    }
}
