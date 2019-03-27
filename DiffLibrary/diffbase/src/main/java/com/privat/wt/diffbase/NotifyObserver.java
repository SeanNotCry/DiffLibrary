package com.privat.wt.diffbase;


import android.support.v7.widget.RecyclerView;

public class NotifyObserver extends RecyclerView.AdapterDataObserver {

    private RecyclerView.AdapterDataObserver dataObserver;
    private int hasHead;

    public NotifyObserver(RecyclerView.AdapterDataObserver observer, int hasHead) {

        this.dataObserver = observer;
        this.hasHead = hasHead;
    }


    @Override
    public void onChanged() {
        dataObserver.onChanged();
    }

    @Override
    public void onItemRangeChanged(int positionStart, int itemCount) {
        dataObserver.onItemRangeChanged(positionStart + hasHead, itemCount);
    }

    @Override
    public void onItemRangeChanged(int positionStart, int itemCount, Object payload) {
        dataObserver.onItemRangeChanged(positionStart+hasHead, itemCount, payload);
    }

    @Override
    public void onItemRangeInserted(int positionStart, int itemCount) {
        dataObserver.onItemRangeInserted(positionStart + hasHead, itemCount);
    }

    @Override
    public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
        dataObserver.onItemRangeMoved(fromPosition , toPosition, itemCount);
    }

    @Override
    public void onItemRangeRemoved(int positionStart, int itemCount) {
        dataObserver.onItemRangeRemoved(positionStart + hasHead, itemCount);
    }
}
