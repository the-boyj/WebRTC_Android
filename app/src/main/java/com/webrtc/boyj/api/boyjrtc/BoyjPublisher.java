package com.webrtc.boyj.api.boyjrtc;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.subjects.CompletableSubject;
import io.reactivex.subjects.PublishSubject;

class BoyjPublisher {
    @NonNull
    private final PublishSubject<String> rejectSubject = PublishSubject.create();
    @NonNull
    private final CompletableSubject callSubject = CompletableSubject.create();
    @NonNull
    private final PublishSubject<String> leaveSubject = PublishSubject.create();
    @NonNull
    private final CompletableSubject endOfCallSubject = CompletableSubject.create();
    @NonNull
    private final PublishSubject<List<BoyjMediaStream>> remoteStreamSubject = PublishSubject.create();
    @NonNull
    private final List<BoyjMediaStream> mediaStreams = new ArrayList<>();

    @NonNull
    PublishSubject<String> getRejectSubject() {
        return rejectSubject;
    }

    @NonNull
    CompletableSubject getCallSubject() {
        return callSubject;
    }

    @NonNull
    PublishSubject<String> getLeaveSubject() {
        return leaveSubject;
    }

    @NonNull
    CompletableSubject getEndOfCallSubject() {
        return endOfCallSubject;
    }

    @NonNull
    PublishSubject<List<BoyjMediaStream>> getRemoteStreamSubject() {
        return remoteStreamSubject;
    }

    void submitReject(@NonNull final String rejectUserName) {
        rejectSubject.onNext(rejectUserName);
    }

    void completeCall() {
        callSubject.onComplete();
    }

    void submitLeave(@NonNull final String id) {
        removeMediaStreamFromId(id);
        leaveSubject.onNext(id);
    }

    void completeEndOfCall() {
        endOfCallSubject.onComplete();
    }

    void submitRemoteStream(@NonNull final BoyjMediaStream mediaStream) {
        mediaStreams.add(mediaStream);
        remoteStreamSubject.onNext(mediaStreams);
    }

    private void removeMediaStreamFromId(@NonNull final String id) {
        for (int i = 0; i < mediaStreams.size(); i++) {
            final BoyjMediaStream stream = mediaStreams.get(i);
            if (stream.getId().equals(id)) {
                mediaStreams.remove(i);
                remoteStreamSubject.onNext(mediaStreams);
                return;
            }
        }
    }
}
