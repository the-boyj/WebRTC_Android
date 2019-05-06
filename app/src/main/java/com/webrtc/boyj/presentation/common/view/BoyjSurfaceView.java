package com.webrtc.boyj.presentation.common.view;

import android.content.Context;
import android.util.AttributeSet;

import com.webrtc.boyj.api.boyjrtc.peer.manager.EglBaseManager;

import org.webrtc.RendererCommon;
import org.webrtc.SurfaceViewRenderer;

public class BoyjSurfaceView extends SurfaceViewRenderer {
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
}
