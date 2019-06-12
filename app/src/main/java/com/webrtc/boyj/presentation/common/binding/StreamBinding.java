package com.webrtc.boyj.presentation.common.binding;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.BindingAdapter;

import com.webrtc.boyj.api.boyjrtc.BoyjMediaStream;
import com.webrtc.boyj.presentation.call.CallAdapter;
import com.webrtc.boyj.presentation.common.view.BoyjSurfaceView;
import com.webrtc.boyj.presentation.common.view.SplitLayout;

import org.webrtc.MediaStream;
import org.webrtc.VideoTrack;

public class StreamBinding {
    @BindingAdapter({"localStream"})
    public static void bindMediaStream(@NonNull final BoyjSurfaceView surfaceView,
                                       @Nullable final MediaStream mediaStream) {
        if (mediaStream != null) {
            final VideoTrack track = mediaStream.videoTracks.get(0);
            surfaceView.setZOrderMediaOverlay(true);
            track.addSink(surfaceView);
        }
    }

    @BindingAdapter({"remoteStream"})
    public static void bindMediaStream(@NonNull final SplitLayout splitLayout,
                                       @Nullable final BoyjMediaStream mediaStream) {
        final CallAdapter adapter = (CallAdapter) splitLayout.getAdapter();
        if (adapter != null && mediaStream != null) {
            adapter.addMediaStream(mediaStream);
        }
    }

    @BindingAdapter({"leaveStream"})
    public static void unbindMediaStream(@NonNull final SplitLayout splitLayout,
                                         @Nullable final String leavedUserName) {
        final CallAdapter adapter = (CallAdapter) splitLayout.getAdapter();
        if (adapter != null && leavedUserName != null) {
            adapter.removeStreamById(leavedUserName);
        }
    }
}
