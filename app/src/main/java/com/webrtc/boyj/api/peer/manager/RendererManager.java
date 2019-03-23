package com.webrtc.boyj.api.peer.manager;


import org.webrtc.RendererCommon;
import org.webrtc.SurfaceViewRenderer;

public class RendererManager {

    public static void initSurfaceView(SurfaceViewRenderer renderer) {
        renderer.init(EglBaseManager.getEglBaseContext(), null);
        renderer.setMirror(true);
        renderer.setScalingType(RendererCommon.ScalingType.SCALE_ASPECT_FIT);
        renderer.setEnableHardwareScaler(true);
        renderer.setZOrderMediaOverlay(true);
    }
}
