package com.webrtc.boyj.data.source.remote;

import com.webrtc.boyj.data.model.User;
import com.webrtc.boyj.data.source.remote.request.UserRequest;
import com.webrtc.boyj.data.source.remote.response.ListResponse;
import com.webrtc.boyj.data.source.remote.response.Response;

import io.reactivex.Completable;
import io.reactivex.Single;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
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
    Completable registerUser(@Body final UserRequest request);

    @FormUrlEncoded
    @POST("/api/v1/users/{id}")
    Completable updateDeviceToken(@Path("id") final String userId,
                                  @Field("deviceToken") final String token);

    @FormUrlEncoded
    @POST("/api/v1/users/{id}")
    Completable updateUserName(@Path("id") final String userId,
                               @Field("name") final String name);

}
