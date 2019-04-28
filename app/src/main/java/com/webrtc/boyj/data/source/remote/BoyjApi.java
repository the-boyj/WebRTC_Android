package com.webrtc.boyj.data.source.remote;

import com.webrtc.boyj.data.model.User;
import com.webrtc.boyj.data.source.remote.response.UserListResponse;
import com.webrtc.boyj.data.source.remote.response.UserResponse;

import io.reactivex.Single;
import retrofit2.http.Body;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface BoyjApi {
    @GET("/users/{id}")
    Single<UserResponse> getProfile(@Path("id") final String userId);

    @GET("/users")
    Single<UserListResponse> getOthers(@Query("except") final String userId);

    @FormUrlEncoded
    @POST("/users")
    Single<UserResponse> registerUser(@Body User user);

    @FormUrlEncoded
    @POST("/users/{id}")
    Single<UserResponse> updateDeviceToken(@Path("id") final String userId,
                                           @Query("token") final String token);

    @FormUrlEncoded
    @POST("/users/{id}")
    Single<UserResponse> updateUserName(@Path("id") final String userId,
                                        @Query("name") final String userName);

}
