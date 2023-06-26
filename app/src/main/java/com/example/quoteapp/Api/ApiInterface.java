package com.example.quoteapp.Api;

import com.example.quoteapp.Response.AddPoetryResponse;
import com.example.quoteapp.Response.DeleteResponse;
import com.example.quoteapp.Response.GetPoetryResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ApiInterface {

    @GET("getpoetry.php")
    Call<GetPoetryResponse> getpoetry();
    @FormUrlEncoded
    @POST("deletepoetry.php")

    Call<DeleteResponse> deletepoetry(@Field("poetry_id") String poetry_id);


    @FormUrlEncoded
    @POST("addpoetry.php")
    Call<AddPoetryResponse> addpoetry(@Field("poem") String poem,@Field("poet_name") String poet_name);

    @FormUrlEncoded
    @POST("updatepoetry.php")
    Call<AddPoetryResponse> updatepoetry(@Field("poetry_data") String poetry_data,@Field("id") String id);
}
