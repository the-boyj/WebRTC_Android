package com.webrtc.boyj.api.firebase;

import android.support.test.runner.AndroidJUnit4;

import com.webrtc.boyj.api.firebase.FireStoreManager;
import com.webrtc.boyj.model.dao.UserDAO;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import static org.junit.Assert.assertEquals;

public class FireStoreManagerTest {
    @Test
    public void getCollection() {
        assertEquals(UserDAO.COLLECTION_NAME, FireStoreManager.getCollection(UserDAO.COLLECTION_NAME).getId());
    }

    @Test
    public void createDocument() {
        CompletableFuture<String> future=new CompletableFuture<>();
        HashMap data=new HashMap<>();
        data.put("testKey","TestValue");
        FireStoreManager.createDocument("TestCollection","TestDocument",data).subscribe(result -> {
            future.complete((String) result);

        });

        try {
            assertEquals(FireStoreManager.COMPLETE_STRING,future.get());
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }


}