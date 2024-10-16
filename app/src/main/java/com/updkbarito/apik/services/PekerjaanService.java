package com.updkbarito.apik.services;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;

public interface PekerjaanService {

    @GET("api/pekerjaan")
    Call<ResponsePekerjaanList> index(@QueryMap Map<String, String> map);
}
