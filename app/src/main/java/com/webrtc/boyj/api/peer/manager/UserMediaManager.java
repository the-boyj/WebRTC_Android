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
import org.webrtc.SurfaceViewRenderer;
import org.webrtc.VideoCapturer;
import org.webrtc.VideoSource;
import org.webrtc.VideoTrack;

public class UserMediaManager {

    @NonNull
    private PeerConnectionFactory peerConnectionFactory;

    @NonNull
    private VideoCapturer videoCapturer;
    @NonNull
    private VideoSource videoSource;
    @NonNull
    private VideoTrack videoTrack;
    @NonNull
    private AudioSource audioSource;
    @NonNull
    private AudioTrack audioTrack;
    @NonNull
    private MediaStream mediaStream;


    public UserMediaManager() {

        peerConnectionFactory = PeerConnectionFactoryManager.getPeerConnectionFactory();

        initFrontCameraCapturer();
        initLocalAudioTrack();
        initLocalVideoTrack();
        initLocalMediaStream();
    }

    private void initFrontCameraCapturer() {

        final CameraEnumerator enumerator;
        final String[] deviceNames;

        if (isCamera2Supported()) {
            enumerator = new Camera2Enumerator(App.getContext());
        } else {
            enumerator = new Camera1Enumerator(false);
        }

        deviceNames = enumerator.getDeviceNames();

        for (String deviceName : deviceNames) {
            if (enumerator.isFrontFacing(deviceName)) {
                videoCapturer = enumerator.createCapturer(deviceName, null);
                break;
            }
        }
    }

    private void initLocalVideoTrack() {
        final SurfaceTextureHelper helper = SurfaceTextureHelper.create("SurfaceTexture", EglBaseManager.getEglBaseContext());

        videoSource = peerConnectionFactory.createVideoSource(true);
        videoTrack = peerConnectionFactory.createVideoTrack("VideoTrack", videoSource);

        videoCapturer.initialize(helper, App.getContext(), videoSource.getCapturerObserver());
    }

    private void initLocalAudioTrack() {
        audioSource = peerConnectionFactory.createAudioSource(new MediaConstraints());
        audioTrack = peerConnectionFactory.createAudioTrack("AudioTrack", audioSource);
    }

    private void initLocalMediaStream() {
        mediaStream = peerConnectionFactory.createLocalMediaStream("LocalMediaStream");

        mediaStream.addTrack(audioTrack);
        mediaStream.addTrack(videoTrack);
    }

    @NonNull
    private boolean isCamera2Supported() {
        return Camera2Enumerator.isSupported(App.getContext());
    }

    public void startCapture() {
        videoCapturer.startCapture(1000, 720, 30);
    }

    public void stopCapture() {
        try {
            videoCapturer.stopCapture();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @NonNull
    public MediaStream getUserMedia() {
        return mediaStream;
    }

    public void localStreamTo(SurfaceViewRenderer view) {
        videoCapturer.startCapture(1024, 720, 30);
        videoTrack.addSink(view);
    }
}
