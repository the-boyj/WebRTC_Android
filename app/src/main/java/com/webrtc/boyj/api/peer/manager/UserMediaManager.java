package com.webrtc.boyj.api.peer.manager;

import android.support.annotation.NonNull;

import com.webrtc.boyj.utils.App;

import org.webrtc.AudioSource;
import org.webrtc.AudioTrack;
import org.webrtc.Camera1Enumerator;
import org.webrtc.Camera2Enumerator;
import org.webrtc.CameraEnumerator;
import org.webrtc.MediaConstraints;
import org.webrtc.MediaStream;
import org.webrtc.PeerConnectionFactory;
import org.webrtc.SurfaceTextureHelper;
import org.webrtc.VideoCapturer;
import org.webrtc.VideoSource;
import org.webrtc.VideoTrack;

public final class UserMediaManager {
    @NonNull
    private final MediaStream mediaStream;
    @NonNull
    private final PeerConnectionFactory peerConnectionFactory;
    @NonNull
    private final VideoCapturer capturer = createVideoCapturer();

    public UserMediaManager(@NonNull final PeerConnectionFactory peerConnectionFactory) {
        this.peerConnectionFactory = peerConnectionFactory;
        mediaStream = peerConnectionFactory.createLocalMediaStream("LocalMediaStream");
        mediaStream.addTrack(createVideoTrack());
        mediaStream.addTrack(createAudioTrack());
    }

    public void startCapture() {
        capturer.startCapture(1024, 720, 30);
    }

    public void stopCapture() {
        try {
            capturer.stopCapture();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public MediaStream getLocalMediaStream() {
        return mediaStream;
    }

    @NonNull
    private VideoTrack createVideoTrack() {
        final SurfaceTextureHelper helper = SurfaceTextureHelper.create(
                "SurfaceTexture",
                EglBaseManager.getEglBase().getEglBaseContext()
        );
        final VideoSource videoSource = peerConnectionFactory.createVideoSource(true);
        final VideoTrack videoTrack = peerConnectionFactory.createVideoTrack("VideoTrack", videoSource);
        capturer.initialize(helper, App.getContext(), videoSource.getCapturerObserver());

        return videoTrack;
    }

    @NonNull
    private AudioTrack createAudioTrack() {
        final AudioSource audioSource = peerConnectionFactory.createAudioSource(new MediaConstraints());
        return peerConnectionFactory.createAudioTrack("AudioTrack", audioSource);
    }

    @NonNull
    private VideoCapturer createVideoCapturer() {
        final CameraEnumerator enumerator;

        enumerator = isCamera2Supported() ?
                new Camera2Enumerator(App.getContext()) :
                new Camera1Enumerator(false);

        for (final String deviceName : enumerator.getDeviceNames()) {
            if (enumerator.isFrontFacing(deviceName)) {
                return enumerator.createCapturer(deviceName, null);
            }
        }
        throw new IllegalStateException("Camera is not supported");
    }

    private boolean isCamera2Supported() {
        return Camera2Enumerator.isSupported(App.getContext());
    }
}
