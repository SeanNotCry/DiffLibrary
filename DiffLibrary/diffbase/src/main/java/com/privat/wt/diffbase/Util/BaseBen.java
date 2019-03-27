package com.privat.wt.diffbase.Util;

public class BaseBen implements Cloneable{

    public Object Clone() {
        Object object = null;
        try {
            object = super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return object;
    }
}
