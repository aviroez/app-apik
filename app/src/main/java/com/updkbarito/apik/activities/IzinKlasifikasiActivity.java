package com.updkbarito.apik.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.google.android.material.snackbar.Snackbar;
import com.updkbarito.apik.R;
import com.updkbarito.apik.adapters.ListKlasifikasiRecyclerView;
import com.updkbarito.apik.adapters.ListPekerjaRecyclerView;
import com.updkbarito.apik.entities.Izin;
import com.updkbarito.apik.entities.IzinDetail;
import com.updkbarito.apik.entities.Pekerjaan;
import com.updkbarito.apik.entities.PekerjaanPeralatan;
import com.updkbarito.apik.entities.Pengguna;
import com.updkbarito.apik.services.IzinDetailService;
import com.updkbarito.apik.services.PekerjaanService;
import com.updkbarito.apik.services.ResponseIzinDetailList;
import com.updkbarito.apik.services.ResponseIzinList;
import com.updkbarito.apik.services.ResponsePekerjaanList;
import com.updkbarito.apik.utils.Helpers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class IzinKlasifikasiActivity extends AppCompatActivity implements View.OnClickListener {

    private final String TAG = IzinKlasifikasiActivity.class.getSimpleName();
    private ImageView imageBack;
    private View layoutProgressBar;
    private Button buttonLanjut;
    private IzinKlasifikasiActivity context;
    private Intent intent;
    private String jenis;
    private Izin izin = new Izin();
    private ArrayList<Pengguna> listPekerja = new ArrayList<>();
    private List<Pekerjaan> listKlasifikasi = new ArrayList<>();
    private List<Pekerjaan> listPekerjaan = new ArrayList<>();
    private ArrayList<PekerjaanPeralatan> listPeralatan = new ArrayList<>();
    private RecyclerView recyclerView;
    private Retrofit retrofit;
    private PekerjaanService pekerjaanService;
    private int izinId = 0;
    private IzinDetailService izinDetailService;
    private List<IzinDetail> listIzinDetail = new ArrayList<>();
    private boolean edit = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_izin_klasifikasi);

        imageBack = findViewById(R.id.image_back);
        buttonLanjut = findViewById(R.id.button_lanjut);
        recyclerView = findViewById(R.id.recycler_view);
        layoutProgressBar = findViewById(R.id.layout_progress_bar);

        context = this;

        intent = getIntent();
        edit = intent.getBooleanExtra("edit", false);
        jenis = intent.getStringExtra("jenis");
        izin = intent.getParcelableExtra("izin");
        izinId = intent.getIntExtra("izin_id", 0);
        listPekerja = intent.getParcelableArrayListExtra("list_pekerja");
        listKlasifikasi = intent.getParcelableArrayListExtra("list_klasifikasi");
        listPeralatan = intent.getParcelableArrayListExtra("list_peralatan");

        imageBack.setOnClickListener(context);
        buttonLanjut.setOnClickListener(context);

        retrofit = Helpers.initRetrofit(context);
        pekerjaanService = retrofit.create(PekerjaanService.class);
        izinDetailService = retrofit.create(IzinDetailService.class);
        HashMap<String, String> map = new HashMap<>();
        izinDetailService.pekerjaanList(izinId, new HashMap<String, String>()).enqueue(new Callback<ResponsePekerjaanList>() {
            @Override
            public void onResponse(Call<ResponsePekerjaanList> call, Response<ResponsePekerjaanList> response) {
                if (response.isSuccessful()){
                    if (response.body() != null){
                        listKlasifikasi = response.body().getData();

                        if (listKlasifikasi == null || listKlasifikasi.size() <= 0){
                            listKlasifikasi = new ArrayList<>();
                            listKlasifikasi.add(new Pekerjaan());
                            listKlasifikasi.add(new Pekerjaan());
                            listKlasifikasi.add(new Pekerjaan());
                        }

                        pekerjaanService.index(new HashMap<String, String>()).enqueue(new Callback<ResponsePekerjaanList>() {
                            @Override
                            public void onResponse(Call<ResponsePekerjaanList> call, Response<ResponsePekerjaanList> response) {
                                if (response.isSuccessful()){
                                    if (response.body() != null){
                                        listPekerjaan = response.body().getData();

                                        listPekerjaan.add(0, new Pekerjaan(0, "[Pilih Klasifikasi Pekerjaan]"));

                                        ListKlasifikasiRecyclerView adapter = new ListKlasifikasiRecyclerView(context, listKlasifikasi, listPekerjaan);
                                        recyclerView.setAdapter(adapter);
                                        recyclerView.setLayoutManager(new LinearLayoutManager(context));
                                    }
                                }
                            }

                            @Override
                            public void onFailure(Call<ResponsePekerjaanList> call, Throwable t) {

                            }
                        });
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponsePekerjaanList> call, Throwable t) {

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        layoutProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == imageBack.getId()){
            kembaliAction(v);
        } else if (v.getId() == buttonLanjut.getId()){
            lanjutAction(v);
        }
    }

    @Override
    public void onBackPressed() {
        kembaliAction(null);
    }

    private void lanjutAction(View v) {
        String klasifikasiId = "";

        if (listKlasifikasi == null || listKlasifikasi.size() <= 0){
            Snackbar.make(findViewById(android.R.id.content), "Pilih Klasifikasi terlebih dahulu", Snackbar.LENGTH_LONG).show();
            return;
        }

        for (Pekerjaan klasifikasi: listKlasifikasi){
            if (klasifikasi.getId() > 0) {
                klasifikasiId += klasifikasi.getId();
                if (listKlasifikasi.indexOf(klasifikasi) < listKlasifikasi.size()-1) klasifikasiId += ",";
            }
        }
        Log.d(TAG, "lanjutAction:"+klasifikasiId);

        if (klasifikasiId.length() > 0){
            layoutProgressBar.setVisibility(View.VISIBLE);
            HashMap<String, String> map = new HashMap<>();
            map.put("pekerjaan_id", klasifikasiId);
            izinDetailService.tambahPekerjaan(izinId, map).enqueue(new Callback<ResponseIzinDetailList>() {
                @Override
                public void onResponse(Call<ResponseIzinDetailList> call, Response<ResponseIzinDetailList> response) {
                    layoutProgressBar.setVisibility(View.GONE);
                    if (response.isSuccessful()){
                        if (response.body() != null){
                            finish();
                            intent = new Intent(context, IzinApdActivity.class);
                            intent.putExtra("edit", edit);
                            intent.putExtra("jenis", jenis);
                            intent.putExtra("izin", izin);
                            intent.putExtra("izin_id", izinId);
                            startActivity(intent);
                        }
                    }
                }

                @Override
                public void onFailure(Call<ResponseIzinDetailList> call, Throwable t) {
                    layoutProgressBar.setVisibility(View.GONE);
                }
            });

        } else {
            Snackbar.make(findViewById(android.R.id.content), "Pilih Klasifikasi terlebih dahulu", Snackbar.LENGTH_LONG).show();
        }
    }

    private void kembaliAction(View v) {
        finish();
        if (jenis.equals("eksternal")){
            intent = new Intent(context, IzinTambahActivity.class);
        } else {
            intent = new Intent(context, IzinPekerjaActivity.class);
        }
        intent.putExtra("edit", edit);
        intent.putExtra("jenis", jenis);
        intent.putExtra("izin", izin);
        intent.putExtra("izin_id", izinId);
        startActivity(intent);
    }
}
