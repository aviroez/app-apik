package com.updkbarito.apik.services;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;

public interface PeralatanService {

    @GET("api/peralatan")
    Call<ResponsePeralatanList> index(@QueryMap Map<String, String> map);

    @GET("api/peralatan/list/{izin_id}")
    Call<ResponsePeralatanList> list(@Path("izin_id") int izinId, @QueryMap Map<String, String> map);

    @GET("api/peralatan/pekerjaan")
    Call<ResponsePekerjaanPeralatanList> pekerjaan(@QueryMap Map<String, String> map);
}
