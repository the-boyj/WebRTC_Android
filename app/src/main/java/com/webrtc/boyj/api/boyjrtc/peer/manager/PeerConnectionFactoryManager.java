package com.webrtc.boyj.api.boyjrtc.peer.manager;

import com.webrtc.boyj.App;

import org.webrtc.DefaultVideoDecoderFactory;
import org.webrtc.DefaultVideoEncoderFactory;
import org.webrtc.PeerConnectionFactory;

public final class PeerConnectionFactoryManager {
    private static boolean isInitialized = false;

    private PeerConnectionFactoryManager() {

    }

    public static void initialize() {
        if (!isInitialized) {
            isInitialized = true;
            PeerConnectionFactory.initialize(
                    PeerConnectionFactory.InitializationOptions
                            .builder(App.getContext())
                            .createInitializationOptions()
            );
        }
    }

    public static PeerConnectionFactory createPeerConnectionFactory() {
        final PeerConnectionFactory.Options options = new PeerConnectionFactory.Options();

        final DefaultVideoEncoderFactory defaultVideoEncoderFactory =
                new DefaultVideoEncoderFactory(
                        EglBaseManager.getEglBase().getEglBaseContext(),
                        true,
                        true
                );

        final DefaultVideoDecoderFactory defaultVideoDecoderFactory =
                new DefaultVideoDecoderFactory(EglBaseManager.getEglBase().getEglBaseContext());

        return PeerConnectionFactory.builder()
                .setOptions(options)
                .setVideoEncoderFactory(defaultVideoEncoderFactory)
                .setVideoDecoderFactory(defaultVideoDecoderFactory)
                .createPeerConnectionFactory();
    }
}
