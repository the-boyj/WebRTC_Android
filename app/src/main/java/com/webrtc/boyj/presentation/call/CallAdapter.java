package com.webrtc.boyj.presentation.call;

import androidx.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.webrtc.boyj.R;
import com.webrtc.boyj.api.boyjrtc.BoyjMediaStream;
import com.webrtc.boyj.extension.custom.BoyjSurfaceView;
import com.webrtc.boyj.extension.custom.SplitLayout;

import org.webrtc.MediaStream;
import org.webrtc.VideoTrack;

import java.util.ArrayList;
import java.util.List;

public class CallAdapter extends SplitLayout.Adapter {
    private List<BoyjMediaStream> mediaStreamList = new ArrayList<>();

    public void submitMediaStreams(@NonNull final List<BoyjMediaStream> mediaStreams) {
        if (!mediaStreams.isEmpty()) {
            mediaStreamList = new ArrayList<>();
            mediaStreamList.addAll(mediaStreams);
            notifyDataSetChanged();
        }
    }

    @Override
    protected View onCreateView(@NonNull ViewGroup container) {
        return LayoutInflater.from(container.getContext())
                .inflate(R.layout.item_boyj_surface, container, false);
    }

    @Override
    protected void onBindView(@NonNull View view, int position) {
        final BoyjMediaStream boyjMediaStream = mediaStreamList.get(position);
        final BoyjSurfaceView surfaceView = view.findViewById(R.id.surface);
        final MediaStream mediaStream = boyjMediaStream.getMediaStream();

        final VideoTrack track = mediaStream.videoTracks.get(0);
        track.addSink(surfaceView);
    }

    @Override
    protected int getCount() {
        return mediaStreamList.size();
    }
}
