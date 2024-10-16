package com.updkbarito.apik.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.google.android.material.snackbar.Snackbar;
import com.updkbarito.apik.R;
import com.updkbarito.apik.adapters.ListPekerjaRecyclerView;
import com.updkbarito.apik.entities.Izin;
import com.updkbarito.apik.entities.IzinDetail;
import com.updkbarito.apik.entities.Pengguna;
import com.updkbarito.apik.services.IzinDetailService;
import com.updkbarito.apik.services.ResponseIzinDetailList;
import com.updkbarito.apik.services.ResponseIzinList;
import com.updkbarito.apik.utils.Helpers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class IzinPekerjaActivity extends AppCompatActivity implements View.OnClickListener {

    private final String TAG = IzinPekerjaActivity.class.getSimpleName();
    private ImageView imageBack;
    private View layoutProgressBar;
    private Button buttonLanjut;
    private IzinPekerjaActivity context;
    private Intent intent;
    private String jenis;
    private Izin izin = new Izin();
    private RecyclerView recyclerView;
    private ArrayList<Pengguna> listPekerja = new ArrayList<>();
    private Retrofit retrofit;
    private IzinDetailService izinDetailservice;
    private List<IzinDetail> listIzinDetail = new ArrayList<>();
    private List<IzinDetail> listIzinDetailPekerja = new ArrayList<>();
    private int izinId = 0;
    private boolean edit = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_izin_pekerja);

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

        imageBack.setOnClickListener(context);
        buttonLanjut.setOnClickListener(context);

        listPekerja.add(new Pengguna());
        listPekerja.add(new Pengguna());
        listPekerja.add(new Pengguna());

        retrofit = Helpers.initRetrofit(context);
        izinDetailservice = retrofit.create(IzinDetailService.class);
        HashMap<String, String> map = new HashMap<>();
        izinDetailservice.pekerjaList(0, map).enqueue(new Callback<ResponseIzinDetailList>() {
            @Override
            public void onResponse(Call<ResponseIzinDetailList> call, Response<ResponseIzinDetailList> response) {
                if (response.isSuccessful()){
                    if (response.body() != null){
                        listIzinDetailPekerja = response.body().getData();

                        izinDetailservice.pekerjaList(izinId, new HashMap<String, String>()).enqueue(new Callback<ResponseIzinDetailList>() {
                            @Override
                            public void onResponse(Call<ResponseIzinDetailList> call, Response<ResponseIzinDetailList> response) {
                                if (response.isSuccessful()){
                                    if (response.body() != null){
                                        listIzinDetail = response.body().getData();

                                        if (listIzinDetail != null && listIzinDetail.size() > 0){
                                            listPekerja = new ArrayList<>();
                                            for (IzinDetail izinDetail: listIzinDetail) {
                                                listPekerja.add(new Pengguna(0, izinDetail.getPekerja()));
                                            }
                                        }

                                        ListPekerjaRecyclerView adapter = new ListPekerjaRecyclerView(context, listPekerja, listIzinDetailPekerja);
                                        recyclerView.setAdapter(adapter);
                                        recyclerView.setLayoutManager(new LinearLayoutManager(context));
                                    }
                                }
                            }

                            @Override
                            public void onFailure(Call<ResponseIzinDetailList> call, Throwable t) {

                            }
                        });
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseIzinDetailList> call, Throwable t) {

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
        if (listPekerja == null || listPekerja.size() <= 0) {
            Snackbar.make(findViewById(android.R.id.content), "Pilih Pekerja terlebih dahulu", Snackbar.LENGTH_LONG).show();
            return;
        }
        String stringList = "";
        for (Pengguna pengguna: listPekerja) {
            if (pengguna.getNama() != null && pengguna.getNama().trim().length() > 0){
                stringList += pengguna.getNama();
                if (listPekerja.indexOf(pengguna) < listPekerja.size()) stringList += ",";
            }
        }
        if (stringList.length() <= 0){
            Snackbar.make(findViewById(android.R.id.content), "Pilih pekerja terlebih dahulu", Snackbar.LENGTH_LONG).show();
            return;
        }
        layoutProgressBar.setVisibility(View.VISIBLE);
        HashMap<String, String> map = new HashMap<>();
        map.put("pekerja", stringList);
        izinDetailservice.tambahPekerja(izin.getId(), map).enqueue(new Callback<ResponseIzinDetailList>() {
            @Override
            public void onResponse(Call<ResponseIzinDetailList> call, Response<ResponseIzinDetailList> response) {
                layoutProgressBar.setVisibility(View.GONE);
                if (response.isSuccessful()){
                    if (response.body() != null){
                        List<IzinDetail> list = response.body().getData();
                        finish();
                        intent = new Intent(context, IzinKlasifikasiActivity.class);
                        intent.putExtra("edit", edit);
                        intent.putExtra("jenis", jenis);
                        intent.putExtra("izin", izin);
                        intent.putExtra("izin_id", izinId);
                        intent.putParcelableArrayListExtra("list_pekerja", listPekerja);
                        startActivity(intent);
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseIzinDetailList> call, Throwable t) {
                layoutProgressBar.setVisibility(View.GONE);
            }
        });
    }

    private void kembaliAction(View v) {
        finish();
        intent = new Intent(context, IzinTambahActivity.class);
        intent.putExtra("edit", edit);
        intent.putExtra("jenis", jenis);
        intent.putExtra("izin", izin);
        startActivity(intent);
    }
}
