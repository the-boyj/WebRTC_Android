package com.webrtc.boyj.api.signalling;

import com.webrtc.boyj.api.firebase.MyFirebaseMessagingService;
import com.webrtc.boyj.api.signalling.payload.FCMPayload;
import com.webrtc.boyj.data.model.User;
import com.webrtc.boyj.presentation.call.CallViewModel;
import com.webrtc.boyj.presentation.ringing.RingingViewModel;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import io.socket.client.Socket;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.mockStatic;

@RunWith(PowerMockRunner.class)
@PrepareForTest({SignalingClient.class, SignalingClientFactory.class})
public class SignalingClientTest {

    @Test
    public void shouldEmitDial() {
        //given


        final Socket mockSocket = mock(Socket.class);

        final SocketIOClient socketIOClient = new SocketIOClient(mockSocket);
        final SocketIOClient spySocketIOClient = spy(socketIOClient);

        //assume socket connected. escape connection error.
        doNothing().when(spySocketIOClient).connect();

        final SignalingClient signalingClient = SignalingClientFactory.getSignalingClient(spySocketIOClient);
        final SignalingClient spySignalingClient = spy(signalingClient);


        mockStatic(SignalingClientFactory.class);
        when(SignalingClientFactory.getSignalingClient())
                .thenReturn(spySignalingClient);

        //when
        //call button click
        final CallViewModel callViewModel = new CallViewModel(mock(User.class));
        callViewModel.call();

        //then
        //emit dial
        verify(spySignalingClient, times(1)).emitDial(any());
    }

    @Test
    public void shouldEmitAwaken() {
        //given
        final Socket mockSocket = mock(Socket.class);
        final SocketIOClient socketIOClient = new SocketIOClient(mockSocket);
        final SocketIOClient spySocketIOClient = spy(socketIOClient);

        //assume socket connected. escape connection error.
        doNothing().when(spySocketIOClient).connect();

        final SignalingClient signalingClient = SignalingClientFactory.getSignalingClient(spySocketIOClient);
        SignalingClient spySignalingClient = spy(signalingClient);


        mockStatic(SignalingClientFactory.class);
        when(SignalingClientFactory.getSignalingClient())
                .thenReturn(spySignalingClient);

        //when
        //fcm received
        final MyFirebaseMessagingService service = new MyFirebaseMessagingService();
        final FCMPayload payload = mock(FCMPayload.class);

        service.handleNow(payload);

        //then
        //emit awaken
        verify(spySignalingClient, times(1)).emitAwaken(any());
    }


    @Test
    public void shouldEmitAccpet() {
        //given
        final Socket mockSocket = mock(Socket.class);
        final SocketIOClient socketIOClient = new SocketIOClient(mockSocket);
        final SocketIOClient spySocketIOClient = spy(socketIOClient);

        //assume socket connected. escape connection error.
        doNothing().when(spySocketIOClient).connect();

        final SignalingClient signalingClient = SignalingClientFactory.getSignalingClient(spySocketIOClient);
        SignalingClient spySignalingClient = spy(signalingClient);


        mockStatic(SignalingClientFactory.class);
        when(SignalingClientFactory.getSignalingClient())
                .thenReturn(spySignalingClient);

        //when
        //user accpet action
        final RingingViewModel ringingViewModel = new RingingViewModel();
        ringingViewModel.acceptAction();

        //then
        //emit accept
        verify(spySignalingClient, times(1)).emitAccept();
    }

    @Test
    public void shouldEmitReject() {
        //given
        final Socket mockSocket = mock(Socket.class);
        final SocketIOClient socketIOClient = new SocketIOClient(mockSocket);
        final SocketIOClient spySocketIOClient = spy(socketIOClient);

        //assume socket connected. escape connection error.
        doNothing().when(spySocketIOClient).connect();

        final SignalingClient signalingClient = SignalingClientFactory.getSignalingClient(spySocketIOClient);
        SignalingClient spySignalingClient = spy(signalingClient);


        mockStatic(SignalingClientFactory.class);
        when(SignalingClientFactory.getSignalingClient())
                .thenReturn(spySignalingClient);

        //when
        //user reject action
        final RingingViewModel ringingViewModel = new RingingViewModel();
        ringingViewModel.rejectAction();

        //then
        //emit reject
        verify(spySignalingClient, times(1)).emitReject();
    }

    @Test
    public void shouldEmitBye() {
        //given
        final Socket mockSocket = mock(Socket.class);
        final SocketIOClient socketIOClient = new SocketIOClient(mockSocket);
        final SocketIOClient spySocketIOClient = spy(socketIOClient);

        //assume socket connected. escape connection error.
        doNothing().when(spySocketIOClient).connect();

        final SignalingClient signalingClient = SignalingClientFactory.getSignalingClient(spySocketIOClient);
        SignalingClient spySignalingClient = spy(signalingClient);


        mockStatic(SignalingClientFactory.class);
        when(SignalingClientFactory.getSignalingClient())
                .thenReturn(spySignalingClient);

        //when
        //user bye action
        final CallViewModel callViewModel = new CallViewModel(mock(User.class));
        callViewModel.hangUp();

        //then
        //emit bye
        verify(spySignalingClient, times(1)).emitBye();
    }
}

