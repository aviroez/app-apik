package com.updkbarito.apik.services;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;

public interface PenggunaService {

    @GET("api/pengguna")
    Call<ResponsePenggunaList> penggunaList(@QueryMap Map<String, String> map);

    @GET("api/pengguna/get/{id_pengguna}")
    Call<ResponsePengguna> pengguna(@Path("id_pengguna") int penggunaId);

    @GET("api/pengguna/akses")
    Call<ResponseAksesList> aksesList(@QueryMap Map<String, String> map);

    @POST("api/pengguna/login")
    @FormUrlEncoded
    Call<ResponsePengguna> login(@Field("email_pengguna") String email, @Field("password_pengguna") String password);

    @POST("api/pengguna/edit/{id_pengguna}")
    @FormUrlEncoded
    Call<ResponsePengguna> edit(@Path("id_pengguna") int penggunaId, @FieldMap Map<String, String> map);

    @POST("api/pengguna/update")
    @FormUrlEncoded
    Call<ResponsePengguna> update(@FieldMap Map<String, String> map);

    @POST("api/pengguna/password/{id_pengguna}")
    @FormUrlEncoded
    Call<ResponsePengguna> password(@Path("id_pengguna") int penggunaId, @FieldMap Map<String, String> map);

    @GET("api/pengguna/direksi_list")
    Call<ResponsePenggunaList> direksiList(@QueryMap Map<String, String> map);
}