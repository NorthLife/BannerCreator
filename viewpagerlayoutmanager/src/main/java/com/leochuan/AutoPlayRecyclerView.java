package com.leochuan;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.MotionEvent;

import java.lang.ref.WeakReference;

/**
 * An implement of {@link RecyclerView} which support auto play.
 */

public class AutoPlayRecyclerView extends RecyclerView {
    private AutoPlaySnapHelper autoPlaySnapHelper;

    private int direction = RIGHT;
    private boolean canLoop;
    final static int LEFT = 1;
    final static int RIGHT = 2;

    public AutoPlayRecyclerView(Context context) {
        this(context, null);
    }

    public AutoPlayRecyclerView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AutoPlayRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.AutoPlayRecyclerView);
        final int timeInterval = typedArray.getInt(R.styleable.AutoPlayRecyclerView_autoTurningTime, AutoPlaySnapHelper.TIME_INTERVAL);
        direction = typedArray.getInt(R.styleable.AutoPlayRecyclerView_direction, AutoPlaySnapHelper.RIGHT);
        canLoop = typedArray.getBoolean(R.styleable.AutoPlayRecyclerView_canLoop, false);
        typedArray.recycle();
        if (canLoop) {
            autoPlaySnapHelper = new AutoPlaySnapHelper(timeInterval, direction);
        }
    }


    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        boolean result = super.dispatchTouchEvent(ev);
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (autoPlaySnapHelper != null) {
                    autoPlaySnapHelper.pause();
                }
                break;
            case MotionEvent.ACTION_UP:
                if (autoPlaySnapHelper != null) {
                    autoPlaySnapHelper.start();
                }
            default:
                break;
        }
        return result;
    }

    public void start() {
        autoPlaySnapHelper.start();
    }

    public void pause() {
        autoPlaySnapHelper.pause();
    }

    @Override
    public void setLayoutManager(LayoutManager layout) {
        super.setLayoutManager(layout);
        if (canLoop) {
            autoPlaySnapHelper.attachToRecyclerView(this);
        }
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }

    public void setTurningLoop(int turningTime) {
        if (autoPlaySnapHelper == null) {
            autoPlaySnapHelper = new AutoPlaySnapHelper(turningTime, direction);
        }
    }

    public void setCanLoop(boolean canLoop) {
        this.canLoop = canLoop;
    }
}
