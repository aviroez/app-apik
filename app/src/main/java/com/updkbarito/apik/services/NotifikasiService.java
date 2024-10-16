package com.updkbarito.apik.services;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;

public interface NotifikasiService {

    @GET("api/notifikasi")
    Call<ResponseIzinList> index(@QueryMap Map<String, String> map);

    @GET("api/notifikasi/send")
    Call<ResponseIzinList> send(@QueryMap Map<String, String> map);

    @GET("api/notifikasi/sync")
    Call<ResponseIzinList> sync(@QueryMap Map<String, String> map);

    @GET("api/notifikasi/onesignal")
    Call<ResponseIzinList> onesignal(@QueryMap Map<String, String> map);
}
