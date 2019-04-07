package com.webrtc.boyj;

import com.webrtc.boyj.api.BoyjRTC;
import com.webrtc.boyj.api.signalling.payload.CreateRoomPayload;
import com.webrtc.boyj.presentation.call.CallViewModel;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class SocketTest {
    private CallViewModel callViewModel;
    private BoyjRTC boyjRTC;

    @Before
    public void setup() {
        boyjRTC = Mockito.spy(new BoyjRTC());
        callViewModel = new CallViewModel(boyjRTC);
    }

    /**
     * ViewModel의 createRoom을 호출했을 때 ViewModel이 가지고 있는
     * boyjRTC의 createRoom 호출 여부
     */
    @Test
    public void createRoomTest() {
        final String tel = "010-3333-4444";
        final CreateRoomPayload payload = new CreateRoomPayload.Builder(tel).build();

        callViewModel.createRoom(payload);
        Mockito.verify(boyjRTC).createRoom(payload);
    }
}
