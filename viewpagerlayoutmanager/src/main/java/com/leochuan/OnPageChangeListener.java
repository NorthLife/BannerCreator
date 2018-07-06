package com.leochuan;

import android.support.v7.widget.RecyclerView;

public interface OnPageChangeListener {
    void onScrollStateChanged(RecyclerView recyclerView, int newState);
    void onScrolled(RecyclerView recyclerView, int dx, int dy);
    void onPageSelected(int index);
}
