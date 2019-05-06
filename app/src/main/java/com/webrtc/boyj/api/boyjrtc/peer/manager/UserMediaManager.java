package com.webrtc.boyj.api.boyjrtc.peer.manager;

import android.content.Context;

import androidx.annotation.NonNull;

import com.webrtc.boyj.App;

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
    private final VideoCapturer capturer;

    public UserMediaManager(@NonNull final PeerConnectionFactory peerConnectionFactory) {
        this.peerConnectionFactory = peerConnectionFactory;

        mediaStream = peerConnectionFactory.createLocalMediaStream("LocalMediaStream");
        capturer = createVideoCapturer(App.getContext());

        mediaStream.addTrack(createVideoTrack());
        mediaStream.addTrack(createAudioTrack());
    }

    public MediaStream mediaStream() {
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

        //https://www.programcreek.com/java-api-examples/index.php?api=org.webrtc.MediaConstraints

        MediaConstraints audioConstraints = new MediaConstraints();

        // add all existing audio filters to avoid having echos
        audioConstraints.mandatory.add(new MediaConstraints.KeyValuePair("googEchoCancellation", "true"));
        audioConstraints.mandatory.add(new MediaConstraints.KeyValuePair("googEchoCancellation2", "true"));
        audioConstraints.mandatory.add(new MediaConstraints.KeyValuePair("googDAEchoCancellation", "true"));

        audioConstraints.mandatory.add(new MediaConstraints.KeyValuePair("googTypingNoiseDetection", "true"));

        audioConstraints.mandatory.add(new MediaConstraints.KeyValuePair("googAutoGainControl", "true"));
        audioConstraints.mandatory.add(new MediaConstraints.KeyValuePair("googAutoGainControl2", "true"));

        audioConstraints.mandatory.add(new MediaConstraints.KeyValuePair("googNoiseSuppression", "true"));
        audioConstraints.mandatory.add(new MediaConstraints.KeyValuePair("googNoiseSuppression2", "true"));

        audioConstraints.mandatory.add(new MediaConstraints.KeyValuePair("googAudioMirroring", "false"));
        audioConstraints.mandatory.add(new MediaConstraints.KeyValuePair("googHighpassFilter", "true"));

        final AudioSource audioSource = peerConnectionFactory.createAudioSource(audioConstraints);
        return peerConnectionFactory.createAudioTrack("AudioTrack", audioSource);
    }

    @NonNull
    private VideoCapturer createVideoCapturer(@NonNull final Context context) {
        final CameraEnumerator enumerator;

        enumerator = isCamera2Supported(context) ?
                new Camera2Enumerator(context) :
                new Camera1Enumerator(false);

        for (final String deviceName : enumerator.getDeviceNames()) {
            if (enumerator.isFrontFacing(deviceName)) {
                return enumerator.createCapturer(deviceName, null);
            }
        }
        throw new IllegalStateException("Camera is not supported");
    }

    private boolean isCamera2Supported(@NonNull final Context context) {
        return Camera2Enumerator.isSupported(context);
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
}
