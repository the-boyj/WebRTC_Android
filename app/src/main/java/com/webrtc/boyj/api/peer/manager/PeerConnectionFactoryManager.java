package com.webrtc.boyj.api.peer.manager;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.webrtc.boyj.utils.App;

import org.webrtc.DefaultVideoDecoderFactory;
import org.webrtc.DefaultVideoEncoderFactory;
import org.webrtc.PeerConnectionFactory;

public class PeerConnectionFactoryManager {


    @Nullable
    private static PeerConnectionFactory factory;


    static {
        PeerConnectionFactory.initialize(PeerConnectionFactory.InitializationOptions.builder(App.getContext()).createInitializationOptions());
        createPeerConnectionFactory();
    }

    private static void createPeerConnectionFactory() {
        final PeerConnectionFactory.Options options = new PeerConnectionFactory.Options();
        final DefaultVideoEncoderFactory defaultVideoEncoderFactory = new DefaultVideoEncoderFactory(EglBaseManager.getEglBaseContext(), true, true);
        final DefaultVideoDecoderFactory defaultVideoDecoderFactory = new DefaultVideoDecoderFactory(EglBaseManager.getEglBaseContext());

        factory = PeerConnectionFactory.builder()
                .setOptions(options)
                .setVideoEncoderFactory(defaultVideoEncoderFactory)
                .setVideoDecoderFactory(defaultVideoDecoderFactory)
                .createPeerConnectionFactory();
    }

    @NonNull
    public static PeerConnectionFactory getPeerConnectionFactory() {
        if (factory == null) {
            createPeerConnectionFactory();
        }
        return factory;
    }

    public static void dispose() {
        factory.dispose();
        factory = null;
    }

}
