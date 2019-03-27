package com.aprivate.wt.difflibrary.diff;

import android.os.Bundle;

import com.aprivate.wt.difflibrary.bean.AllInstantData;
import com.aprivate.wt.difflibrary.bean.TestBean;
import com.privat.wt.diffbase.BaseDiff;

import java.util.List;

public class TestDiff extends BaseDiff<TestBean> {


    public TestDiff(List<TestBean> oldData, List<TestBean> newData) {
        super(oldData, newData);
    }

    @Override
    protected boolean isItemSame(int oldItemPosition, int newItemPosition) {
        return oldData.get(oldItemPosition).getId().equals(newData.get(newItemPosition).getId());
    }

    @Override
    protected boolean isItemContentSame(int oldItemPosition, int newItemPosition) {
        TestBean oldBean = oldData.get(oldItemPosition);
        TestBean newBean = newData.get(newItemPosition);
        if (!oldBean.getTitle().equals(newBean.getTitle())) {
            return false;
        }
        if (!oldBean.getContext().equals(newBean.getContext())) {
            return false;
        }
        if (!oldBean.getiConUrl().equals(newBean.getiConUrl())) {
            return false;
        }

        return true;
    }

    @Override
    protected Object getItemChangePayload(int oldItemPosition, int newItemPosition) {
        TestBean oldBean = oldData.get(oldItemPosition);
        TestBean newBean = newData.get(newItemPosition);

        Bundle bundle = new Bundle();

        if (!oldBean.getTitle().equals(newBean.getTitle())) {
            bundle.putString(AllInstantData.DIFF_TITLE, newBean.getTitle());
        }
        if (!oldBean.getContext().equals(newBean.getContext())) {
            bundle.putString(AllInstantData.DIFF_CONTENT, newBean.getContext());
        }
        if (!oldBean.getiConUrl().equals(newBean.getiConUrl())) {
            bundle.putString(AllInstantData.DIFF_ICON, newBean.getiConUrl());
        }
        return bundle;
    }
}
