package com.webrtc.boyj.api.signalling;

import com.webrtc.boyj.utils.Logger;
import org.json.JSONObject;
import java.net.URISyntaxException;
import io.reactivex.subjects.PublishSubject;
import io.socket.client.IO;
import io.socket.client.Socket;

public class SignalingClient {
    private static final String PROTOCOL = "http";
    private static final String IP = "13.124.41.104";
    private static final int PORT = 3000;

    private static SignalingClient instance;

    private Socket socket;
    private boolean isConnected = false;

    public final PublishSubject<String> createdEventSubject = PublishSubject.create();
    public final PublishSubject<String> knockEventSubject = PublishSubject.create();
    public final PublishSubject<String> readyEventSubject = PublishSubject.create();
    public final PublishSubject<JSONObject> rsdpEventSubject = PublishSubject.create();
    public final PublishSubject<JSONObject> riceEventSubject = PublishSubject.create();
    public final PublishSubject<String> byeEventSubject = PublishSubject.create();
    public final PublishSubject<JSONObject> errorEventSubject = PublishSubject.create();

    private SignalingClient() {
        try {
            final String url = String.format("%s://%s:%d",PROTOCOL,IP, PORT);
            socket = IO.socket(url);
        } catch (URISyntaxException e) {
            e.printStackTrace();
            Logger.e("check server URL");
        }
        attachEventListener();
        connect();
    }

    public static SignalingClient getInstance() {
        if(instance == null)
            synchronized (SignalingClient.class) {
                if(instance == null){
                    instance = new SignalingClient();
                    instance.connect();
                }
            }
        return instance;
    }

    private void attachEventListener() {
        socket.on(SignalingInterface.EVENT_CREATED, args -> createdEventSubject.onNext("created"));
        socket.on(SignalingInterface.EVENT_KNOCK, args -> knockEventSubject.onNext("knock"));
        socket.on(SignalingInterface.EVENT_READY, args ->readyEventSubject.onNext("ready"));
        socket.on(SignalingInterface.EVENT_RECEIVE_SDP, args ->rsdpEventSubject.onNext((JSONObject) args[0]));
        socket.on(SignalingInterface.EVENT_RECEIVE_ICE, args -> riceEventSubject.onNext((JSONObject)args[0]));
        socket.on(SignalingInterface.EVENT_BYE, args -> byeEventSubject .onNext("bye"));
        socket.on(SignalingInterface.EVENT_SERVER_ERROR, args -> byeEventSubject .onNext("error"));
    }

    private void connect(){
        if(!isConnected){
            socket.connect();
            isConnected = true;
        }
    }
}