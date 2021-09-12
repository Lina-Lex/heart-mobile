package com.example.GoandDo.request;

import com.google.gson.JsonObject;


import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface APIInterface {

    @POST("/authenticate/get_otp")
    Call<JsonObject> get_otp(@Body JsonObject post);

    @POST("/authenticate/validate_otp")
    Call<JsonObject> validate_otp(@Body JsonObject post);
}
