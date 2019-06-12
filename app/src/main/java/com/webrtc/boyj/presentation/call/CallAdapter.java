package com.webrtc.boyj.presentation.call;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.webrtc.boyj.R;
import com.webrtc.boyj.api.boyjrtc.BoyjMediaStream;
import com.webrtc.boyj.presentation.common.view.BoyjSurfaceView;
import com.webrtc.boyj.presentation.common.view.SplitLayout;

import org.webrtc.MediaStream;
import org.webrtc.VideoTrack;

import java.util.ArrayList;
import java.util.List;

public class CallAdapter extends SplitLayout.Adapter {
    private List<BoyjMediaStream> mediaStreamList = new ArrayList<>();

    public void addMediaStream(@NonNull final BoyjMediaStream mediaStream) {
        for (int i = 0; i < mediaStreamList.size(); i++) {
            if (mediaStreamList.get(i).getId() == mediaStream.getId()) {
                mediaStreamList.set(i, mediaStream);
                notifyDataSetChanged();
                return;
            }
        }

        mediaStreamList.add(mediaStream);
        notifyDataSetChanged();
    }

    public void removeStreamById(@NonNull final String id) {
        for (int i = 0; i < mediaStreamList.size(); i++) {
            final BoyjMediaStream stream = mediaStreamList.get(i);
            if (id.equals(stream.getId())) {
                mediaStreamList.remove(i);
                notifyDataSetChanged();
                break;
            }
        }
    }

    public List<String> getUserListInRoomIncludingMe(@NonNull final String id) {
        final List<String> userList = new ArrayList<>();
        for (final BoyjMediaStream mediaStream : mediaStreamList) {
            userList.add(mediaStream.getId());
        }
        userList.add(id);
        return userList;
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
