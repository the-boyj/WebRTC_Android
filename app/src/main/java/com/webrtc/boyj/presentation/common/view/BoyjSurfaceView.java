package com.webrtc.boyj.presentation.common.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.webrtc.boyj.api.boyjrtc.peer.manager.EglBaseManager;

import org.webrtc.RendererCommon;
import org.webrtc.SurfaceViewRenderer;

public class BoyjSurfaceView extends SurfaceViewRenderer {
    private float dx, dy;

    public BoyjSurfaceView(Context context) {
        super(context);
        initView();
    }

    public BoyjSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    private void initView() {
        init(EglBaseManager.getEglBase().getEglBaseContext(), null);
        setMirror(true);
        setScalingType(RendererCommon.ScalingType.SCALE_ASPECT_FIT);
        setEnableHardwareScaler(true);
    }

    public void dragStart() {
        setOnTouchListener(onTouchListener);
    }

    private final OnTouchListener onTouchListener = new OnTouchListener() {
        @SuppressLint("ClickableViewAccessibility")
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()) {

                case MotionEvent.ACTION_DOWN:

                    dx = v.getX() - event.getRawX();
                    dy = v.getY() - event.getRawY();
                    break;

                case MotionEvent.ACTION_MOVE:

                    v.animate()
                            .x(event.getRawX() + dx)
                            .y(event.getRawY() + dy)
                            .setDuration(0)
                            .start();
                    break;
                default:
                    return false;
            }
            return true;
        }
    };
}
