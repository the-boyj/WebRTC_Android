package com.webrtc.boyj.data.source.remote;

import com.webrtc.boyj.data.model.User;
import com.webrtc.boyj.data.source.remote.response.ListResponse;
import com.webrtc.boyj.data.source.remote.response.Response;
import com.webrtc.boyj.data.source.remote.response.UserItem;

import io.reactivex.Single;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface BoyjApi {
    @GET("/api/v1/users/{id}")
    Single<Response<UserItem>> getProfile(@Path("id") final String userId);

    @GET("/api/v1/users")
    Single<ListResponse<UserItem>> getOthers(@Query("except") final String userId);

    @POST("/api/v1/users")
    Single<Response<UserItem>> registerUser(@Body final User user);

    @POST("/api/v1/users/{id}")
    Single<Response<UserItem>> updateDeviceToken(@Path("id") final String userId,
                                                 @Query("token") final String token);

    @POST("/api/v1/users/{id}")
    Single<Response<UserItem>> updateUserName(@Path("id") final String userId,
                                              @Query("name") final String userName);

}
