package com.updkbarito.apik.services;

import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;

public interface IzinDetailService {

    @GET("api/izinDetail")
    Call<ResponseIzinDetailList> index(@QueryMap Map<String, String> map);

    @GET("api/izinDetail/pekerja_list/{izin_id}")
    Call<ResponseIzinDetailList> pekerjaList(@Path("izin_id") int izinId, @QueryMap Map<String, String> map);

    @FormUrlEncoded
    @POST("api/izinDetail/tambah_pekerja/{izin_id}")
    Call<ResponseIzinDetailList> tambahPekerja(@Path("izin_id") int izinId, @FieldMap Map<String, String> map);

    @GET("api/izinDetail/pekerjaan_list/{izin_id}")
    Call<ResponsePekerjaanList> pekerjaanList(@Path("izin_id") int izinId, @QueryMap Map<String, String> map);

    @FormUrlEncoded
    @POST("api/izinDetail/tambah_pekerjaan/{izin_id}")
    Call<ResponseIzinDetailList> tambahPekerjaan(@Path("izin_id") int izinId, @FieldMap Map<String, String> map);

    @GET("api/izinDetail/peralatan_list/{izin_id}")
    Call<ResponseIzinDetailList> peralatanList(@Path("izin_id") int izinId, @QueryMap Map<String, String> map);

    @Multipart
    @POST("api/izinDetail/tambah_peralatan/{izin_id}")
    Call<ResponseIzinDetailList> tambahPeralatan(@Path("izin_id") int izinId, @Part("peralatan_id") RequestBody peralatanId, @Part("first") RequestBody first, @Part MultipartBody.Part image);

    @GET("api/izinDetail/dokumen_list/{izin_id}")
    Call<ResponseIzinDetailList> dokumenList(@Path("izin_id") int izinId, @QueryMap Map<String, String> map);

    @Multipart
    @POST("api/izinDetail/tambah_dokumen/{izin_id}")
    Call<ResponseIzinDetailList> tambahDokumen(@Path("izin_id") int izinId, @Part("dokumen_id") RequestBody dokumenId, @Part("first") RequestBody first, @Part MultipartBody.Part image);

    @FormUrlEncoded
    @POST("api/izinDetail/hapus_except/{izin_id}")
    Call<ResponseIzinDetailList> hapusIzinDetailExcept(@Path("izin_id") int izinId, @FieldMap Map<String, String> map);
}
