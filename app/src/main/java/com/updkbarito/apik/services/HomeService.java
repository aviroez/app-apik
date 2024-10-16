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

public interface HomeService {

    @POST("api/home/login")
    @FormUrlEncoded
    Call<ResponsePengguna> login(@Field("email") String email, @Field("password") String password);

    @POST("api/home/login")
    @FormUrlEncoded
    Call<ResponsePengguna> loginToken(@Field("email") String email, @Field("token") String token);

    @POST("api/home/gmail")
    @FormUrlEncoded
    Call<ResponsePengguna> gmail(@Field("email") String email, @Field("id_token") String idToken);

    @POST("api/home/daftar")
    @FormUrlEncoded
    Call<ResponsePengguna> daftar(@FieldMap Map<String, String> map);
}
