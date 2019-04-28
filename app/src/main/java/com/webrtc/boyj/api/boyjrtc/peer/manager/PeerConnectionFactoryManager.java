package com.webrtc.boyj.api.boyjrtc.peer.manager;

import android.content.Context;
import android.support.annotation.NonNull;

import org.webrtc.DefaultVideoDecoderFactory;
import org.webrtc.DefaultVideoEncoderFactory;
import org.webrtc.PeerConnectionFactory;

public final class PeerConnectionFactoryManager {
    private static boolean isFactoryInitialized = false;

    private PeerConnectionFactoryManager() {

    }

    public static void initialize(@NonNull final Context context) {
        if (!isFactoryInitialized) {
            isFactoryInitialized = true;
            PeerConnectionFactory.initialize(
                    PeerConnectionFactory.InitializationOptions
                            .builder(context)
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
