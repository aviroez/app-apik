package com.updkbarito.apik.services;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;

public interface IzinService {

    @GET("api/izin")
    Call<ResponseIzinList> index(@QueryMap Map<String, String> map);

    @GET("api/izin/last/{pengguna_id}")
    Call<ResponseIzin> last(@Path("pengguna_id") int izinId, @QueryMap Map<String, String> map);

    @POST("api/izin/tambah")
    Call<ResponseIzin> tambah(@QueryMap Map<String, String> map);

    @GET("api/izin/proses/{izin_id}")
    Call<ResponseIzin> proses(@Path("izin_id") int izinId, @QueryMap Map<String, String> map);

    @POST("api/izin/akses/{izin_id}")
    @FormUrlEncoded
    Call<ResponseIzin> akses(@Path("izin_id") int izinId, @FieldMap Map<String, String> map);

    @GET("api/izin/internal")
    Call<ResponseIzinList> internal(@QueryMap Map<String, String> map);

    @GET("api/izin/eksternal")
    Call<ResponseIzinList> eksternal(@QueryMap Map<String, String> map);

    @GET("api/izin/show/{izin_id}")
    Call<ResponseIzin> show(@Path("izin_id") int izinId, @QueryMap Map<String, String> map);

    @GET("api/izin/cetak/{izin_id}")
    Call<ResponseIzin> cetak(@Path("izin_id") int izinId, @QueryMap Map<String, String> map);
}
