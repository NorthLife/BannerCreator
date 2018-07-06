package com.leochuan;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

/**
 * Beta  版本
 * <p>
 * 参考ConventBanner 源码，在ViewPagerLayoutManager的基础上添加了指示器的功能
 *
 * @author leaflc
 */
public class BannerCreator extends RelativeLayout {

    private AutoPlayRecyclerView recyclerView;
    private ViewGroup loPageTurningPoint;
    private CBPageChangeListener pageChangeListener;
    private int[] page_indicatorId;
    private ArrayList<ImageView> mPointViews = new ArrayList<ImageView>();
    private int turningTime = 0;
    private ViewPagerLayoutManager layoutManager;
    private RecyclerView.Adapter adapter;
    private int direction = RIGHT;
    private OnPageChangeListener onPageChangeListener;


    final static int LEFT = 1;
    final static int RIGHT = 2;

    public enum PageIndicatorAlign {
        ALIGN_PARENT_LEFT, ALIGN_PARENT_RIGHT, CENTER_HORIZONTAL
    }


    public BannerCreator(Context context) {
        this(context, null);
    }

    public BannerCreator(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BannerCreator(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }


    private void init(Context context, AttributeSet attrs) {
        View hView = LayoutInflater.from(context).inflate(
                R.layout.include_viewpager, this, true);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.BannerCreator);
        turningTime = typedArray.getInt(R.styleable.BannerCreator_turningTime, 0);
        typedArray.recycle();
        recyclerView = hView.findViewById(R.id.autoRecycler);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (layoutManager == null) {
                    return;
                }

                int position = layoutManager.getCurrentPosition();
                int count = layoutManager.getItemCount();
                if (onPageChangeListener != null) {
                    onPageChangeListener.onScrollStateChanged(recyclerView, newState);
                    if (count != 0)
                        onPageChangeListener.onPageSelected(position % count);
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (onPageChangeListener != null)
                    onPageChangeListener.onScrolled(recyclerView, dx, dy);
            }
        });
        recyclerView.setDirection(direction);
        loPageTurningPoint = hView.findViewById(R.id.loPageTurningPoint);
        if (turningTime > 0) {
            recyclerView.setCanLoop(true);
            recyclerView.setTurningLoop(turningTime);
        }
    }

    public void setCurrentItem(int item, boolean smoothScroll) {
        if (recyclerView == null) {
            return;
        }
        if (smoothScroll) {
            recyclerView.smoothScrollToPosition(item);
        } else {
            recyclerView.scrollToPosition(item);
        }
    }

    private BannerCreator setLayoutManager(ViewPagerLayoutManager layoutManager) {
        this.layoutManager = layoutManager;
        recyclerView.setLayoutManager(layoutManager);
        return this;
    }

    public CreatorParams with() {
        return new CreatorParams(this);
    }

    private BannerCreator setAdapter(RecyclerView.Adapter adapter) {
        this.adapter = adapter;
        if (adapter == null) {
            throw new IllegalArgumentException("banner adapter is null");
        }
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        return this;
    }

    public AutoPlayRecyclerView getRecyclerView() {
        return recyclerView;
    }

    public void setPointViewVisible(boolean visible) {
        loPageTurningPoint.setVisibility(visible ? VISIBLE : GONE);
    }

    public BannerCreator setPageIndicatorAlign(PageIndicatorAlign align) {
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) loPageTurningPoint.getLayoutParams();
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT, align == PageIndicatorAlign.ALIGN_PARENT_LEFT ? RelativeLayout.TRUE : 0);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, align == PageIndicatorAlign.ALIGN_PARENT_RIGHT ? RelativeLayout.TRUE : 0);
        layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL, align == PageIndicatorAlign.CENTER_HORIZONTAL ? RelativeLayout.TRUE : 0);
        loPageTurningPoint.setLayoutParams(layoutParams);
        return this;
    }


    /**
     * 底部指示器资源图片
     *
     * @param page_indicatorId
     */
    public BannerCreator setPageIndicator(int[] page_indicatorId) {
        loPageTurningPoint.removeAllViews();
        mPointViews.clear();
        this.page_indicatorId = page_indicatorId;
        if (adapter == null) return this;
        for (int count = 0; count < adapter.getItemCount(); count++) {
            // 翻页指示的点
            ImageView pointView = new ImageView(getContext());
            pointView.setPadding(5, 0, 5, 0);
            if (count == layoutManager.getCurrentPosition()) {
                pointView.setImageResource(page_indicatorId[1]);
            } else {
                pointView.setImageResource(page_indicatorId[0]);
            }
            mPointViews.add(pointView);
            loPageTurningPoint.addView(pointView);
        }
        pageChangeListener = new CBPageChangeListener(mPointViews, page_indicatorId);
        setOnPageChangeListener(pageChangeListener);
        return this;
    }


    public void setOnPageChangeListener(OnPageChangeListener onPageChangeListener) {
        this.onPageChangeListener = onPageChangeListener;
    }


    public class CreatorParams {

        private ViewPagerLayoutManager layoutManager;
        private RecyclerView.Adapter adapter;
        private final WeakReference<BannerCreator> bannerCreator;


        public CreatorParams(BannerCreator bannerCreator) {
            this.bannerCreator = new WeakReference<BannerCreator>(bannerCreator);
        }

        public RecyclerView.LayoutManager getLayoutManager() {
            return layoutManager;
        }

        public CreatorParams setLayoutManager(ViewPagerLayoutManager layoutManager) {
            this.layoutManager = layoutManager;
            return this;
        }

        public RecyclerView.Adapter getAdapter() {
            return adapter;
        }

        public CreatorParams setAdapter(RecyclerView.Adapter adapter) {
            this.adapter = adapter;
            return this;
        }

        public BannerCreator config() {
            bannerCreator.get().setLayoutManager(layoutManager);
            bannerCreator.get().setAdapter(adapter);
            return bannerCreator.get();
        }
    }


}

