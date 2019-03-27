package com.aprivate.wt.difflibrary.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.aprivate.wt.difflibrary.R;
import com.aprivate.wt.difflibrary.bean.AllInstantData;
import com.aprivate.wt.difflibrary.bean.TestBean;
import com.aprivate.wt.difflibrary.iface.ItemClickCallBack;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.core.ImagePipelineConfig;
import com.privat.wt.diffbase.DiffBaseAdapter;
import com.privat.wt.diffbase.Util.ImagePipelineConfigUtils;

import java.util.List;

public class TestAdapter extends DiffBaseAdapter<TestBean, TestAdapter.ViewHolder> {

    public ItemClickCallBack listener;

    public TestAdapter(Context context, List<TestBean> data, ItemClickCallBack listener) {
        super(context, data);
        this.listener = listener;
    }


    @Override
    protected ViewHolder userCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_test, parent, false));
    }


    @Override
    public void userBindViewHolder(ViewHolder holder, int position) {
        holder.setView(position, mList.get(position));
    }

    @Override
    public void userBindViewHolder(ViewHolder holder, int position, List payloads) {
        Bundle bundle = (Bundle) payloads.get(0);


        if (bundle.containsKey(AllInstantData.DIFF_TITLE)) {
            holder.tvTitle.setText(bundle.getString(AllInstantData.DIFF_TITLE));
        }
        if (bundle.containsKey(AllInstantData.DIFF_CONTENT)) {
            holder.tvContent.setText(bundle.getString(AllInstantData.DIFF_CONTENT));
        }
        if (bundle.containsKey(AllInstantData.DIFF_TITLE)) {
            ImagePipelineConfigUtils.setImgForWh(holder.ivCover, bundle.getString(AllInstantData.DIFF_ICON), 512, 512);
        }

    }

    @Override
    public int getUserItemViewType(int position) {
        return 1;
    }

    @Override
    public int getRealDataPositon(TestBean data) {
        for (int i = 0; i < mList.size(); i++) {
            if (mList.get(i).getId().equals(data.getId())) {
                return i;
            }
        }
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvTitle;
        SimpleDraweeView ivCover;
        TextView tvContent;
        View view;

        public ViewHolder(View itemView) {
            super(itemView);
            tvContent = itemView.findViewById(R.id.tv_content);
            ivCover = itemView.findViewById(R.id.iv_cover);
            tvTitle = itemView.findViewById(R.id.tv_title);
            view = itemView.findViewById(R.id.view);
        }

        public void setView(int position, final TestBean bean) {
            tvContent.setText(bean.getContext());
            tvTitle.setText(bean.getTitle());
            ImagePipelineConfigUtils.setImgForWh(ivCover, bean.getiConUrl(), 512, 512);
            view.setVisibility(mList.size() - 1 == position?View.GONE:View.VISIBLE);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (null != listener) {
                        listener.onItemClick(getRealDataPositon(bean));
                    }
                }
            });

        }
    }


}
