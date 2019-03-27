package com.privat.wt.diffbase;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.privat.wt.diffbase.Util.BaseBen;

import java.util.ArrayList;
import java.util.List;

public abstract class DiffBaseAdapter<T extends BaseBen, H extends RecyclerView.ViewHolder> extends RecyclerView.Adapter {

    protected List<T> mList;  //adapter数据源
    protected Context mContext; //上下文
    private View footView;
    private View headView;

    private static final int ITEM_TYPE_HEADER = 404;
    private static final int ITEM_TYPE_FOTTER = 405;

    public DiffBaseAdapter(Context context, List<T> data) {
        this.mContext = context;
        this.mList = data;
    }

    @Override
    public void registerAdapterDataObserver(RecyclerView.AdapterDataObserver observer) {
        super.registerAdapterDataObserver(new NotifyObserver(observer, hasHead() ? 1 : 0));
    }

    public List<T> getData(){
        List<T> data = new ArrayList<>();
        for (T item:mList) {
            data.add((T) item.Clone());
        }
        return data;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);

        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            final GridLayoutManager gridLayoutManager = (GridLayoutManager) layoutManager;
            final GridLayoutManager.SpanSizeLookup spanSizeLookup = gridLayoutManager.getSpanSizeLookup();

            gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    int viewType = getItemViewType(position);
                    if (ITEM_TYPE_HEADER == viewType) {
                        return gridLayoutManager.getSpanCount();
                    } else if (ITEM_TYPE_FOTTER == viewType) {
                        return gridLayoutManager.getSpanCount();
                    }
                    if (spanSizeLookup != null) {
                        return spanSizeLookup.getSpanSize(position);
                    }
                    return 1;
                }
            });
            gridLayoutManager.setSpanCount(gridLayoutManager.getSpanCount());
        }

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case ITEM_TYPE_HEADER:
                return new ViewHolder(headView);
            case ITEM_TYPE_FOTTER:
                return new ViewHolder(footView);
            default:
                return userCreateViewHolder(parent, viewType);
        }
    }

    /**
     * 获取真正的ITEM ViewHolder
     * @param parent
     * @param viewType
     * */
    protected abstract H userCreateViewHolder(ViewGroup parent, int viewType);

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (ITEM_TYPE_FOTTER != getItemViewType(position) && ITEM_TYPE_HEADER != getUserItemViewType(position)) {
            userBindViewHolder((H) holder, position);
        }
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position, List payloads) {

        if (payloads.isEmpty()) {
            onBindViewHolder(holder, position);
        } else {
            userBindViewHolder((H) holder, position, payloads);
        }
    }

    public abstract void userBindViewHolder(H holder, int position);

    /**
     * 增量刷新需要更新的UI
     * */
    public abstract void userBindViewHolder(H holder, int position, List payloads);

    @Override
    public int getItemCount() {
        int count = mList.size();
        if (hasFoot()) {
            count++;
        }
        if (hasHead()) {
            count++;
        }
        return count;
    }

    @Override
    public int getItemViewType(int position) {
        if (hasHead() && position == 0) {
            return ITEM_TYPE_HEADER;
        }
        if (hasFoot() && position == getItemCount() - 1) {
            return ITEM_TYPE_FOTTER;
        }
        return getUserItemViewType(position);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(View itemView) {
            super(itemView);
        }
    }


    public void setHeadView(View headView) {
        this.headView = headView;
    }

    public void setFootView(View footView) {
        this.footView = footView;
    }

    public boolean hasHead() {
        return null != headView;
    }

    public boolean hasFoot() {
        return null != footView;
    }

    /**
     * 获取用户ITEM类型
     * @param  position item的位置
     * @return item类型
     * */
    public abstract int getUserItemViewType(int position);

    /**
     * 获取用户ITEM类型
     * @param  data item数据类型
     * @return item实际位置
     * */
    public abstract int getRealDataPositon(T data);
}
