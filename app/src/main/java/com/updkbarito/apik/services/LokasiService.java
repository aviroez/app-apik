package com.updkbarito.apik.services;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;

public interface LokasiService {

    @GET("api/lokasi")
    Call<ResponseLokasiList> index(@QueryMap Map<String, String> map);

}
