package com.webrtc.boyj.model.dao;

import android.util.Log;

import com.webrtc.boyj.api.firebase.FireStoreManager;
import com.webrtc.boyj.model.dto.User;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;

import io.reactivex.Observable;
import io.reactivex.Scheduler;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.Subject;

import static org.junit.Assert.*;

public class UserDAOTest {


    @Test
    public void create() {

        CompletableFuture<String> future=new CompletableFuture<>();
        UserDAO.create("testcase").subscribe((s, throwable) -> {
            future.complete(s);
        });

        try {
            assertEquals(FireStoreManager.COMPLETE_STRING,future.get());
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }


    }
    @Test
    public void readAll() {
        CompletableFuture<List> future=new CompletableFuture<>();

        UserDAO.readAll().subscribe((users, throwable) -> {
            future.complete(users);
        });
        try {
            assertNotNull(future.get());
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }


}