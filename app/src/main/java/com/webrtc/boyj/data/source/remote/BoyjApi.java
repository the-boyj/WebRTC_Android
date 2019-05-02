package com.webrtc.boyj.data.source.remote;

import com.webrtc.boyj.data.model.User;
import com.webrtc.boyj.data.source.remote.response.ListResponse;
import com.webrtc.boyj.data.source.remote.response.Response;

import io.reactivex.Single;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface BoyjApi {
    @GET("/api/v1/users/{id}")
    Single<Response<User>> getProfile(@Path("id") final String userId);

    @GET("/api/v1/users")
    Single<ListResponse<User>> getOthers(@Query("except") final String userId);

    @POST("/api/v1/users")
    Single<Response<User>> registerUser(@Body final User user);

    @POST("/api/v1/users/{id}")
    Single<Response<User>> updateDeviceToken(@Path("id") final String userId,
                                             @Query("token") final String token);

    @POST("/api/v1/users/{id}")
    Single<Response<User>> updateUserName(@Path("id") final String userId,
                                          @Query("name") final String userName);

}
