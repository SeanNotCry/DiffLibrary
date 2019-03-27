package com.privat.wt.diffbase;

import android.support.annotation.Nullable;
import android.support.v7.util.DiffUtil;

import java.util.List;

public abstract class BaseDiff<T extends Object> extends DiffUtil.Callback {

    protected List<T> oldData;
    protected List<T> newData;

    public BaseDiff(List<T> oldData, List<T> newData) {
        this.oldData = oldData;
        this.newData = newData;
    }

    @Override
    public int getOldListSize() {
        return null != oldData ? oldData.size() : 0;
    }

    @Override
    public int getNewListSize() {
        return null != newData ? newData.size() : 0;
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return isItemSame(oldItemPosition, newItemPosition);
    }

    protected abstract boolean isItemSame(int oldItemPosition, int newItemPosition);

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        return isItemContentSame(oldItemPosition, newItemPosition);
    }

    protected abstract boolean isItemContentSame(int oldItemPosition, int newItemPosition);

    @Nullable
    @Override
    public Object getChangePayload(int oldItemPosition, int newItemPosition) {
        return getItemChangePayload(oldItemPosition, newItemPosition);
    }

    protected abstract Object getItemChangePayload(int oldItemPosition, int newItemPosition);
}
