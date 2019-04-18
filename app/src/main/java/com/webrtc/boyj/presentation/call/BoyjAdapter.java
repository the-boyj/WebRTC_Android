package com.webrtc.boyj.presentation.call;

import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.webrtc.boyj.R;
import com.webrtc.boyj.data.model.BoyjMediaStream;
import com.webrtc.boyj.extension.custom.BoyjSurfaceView;
import com.webrtc.boyj.extension.custom.SplitLayout;

import org.webrtc.MediaStream;
import org.webrtc.VideoTrack;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("SpellCheckingInspection")
public class BoyjAdapter extends SplitLayout.Adapter {
    public BoyjAdapter(@NonNull final OnEndOfCallListener onEndOfCallListener) {
        this.onEndOfCallListener = onEndOfCallListener;
    }

    interface OnEndOfCallListener {
        void onEnd();
    }

    private final OnEndOfCallListener onEndOfCallListener;
    private final List<BoyjMediaStream> mediaStreamList = new ArrayList<>();

    public void addMediaStream(@NonNull final BoyjMediaStream mediaStream) {
        this.mediaStreamList.add(mediaStream);
        notifyDataSetChanged();
    }

    public void removeMediaStreamfromId(@NonNull final String id) {
        for (int i = 0; i < mediaStreamList.size(); i++) {
            final BoyjMediaStream mediaStream = mediaStreamList.get(i);
            if (mediaStream.getId().equals(id)) {
                mediaStreamList.remove(i);
                notifyDataSetChanged();
                return;
            }
        }

        if (mediaStreamList.isEmpty()) {
            onEndOfCallListener.onEnd();
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
