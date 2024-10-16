package com.updkbarito.apik.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.updkbarito.apik.R;
import com.updkbarito.apik.entities.Izin;
import com.updkbarito.apik.entities.Lokasi;
import com.updkbarito.apik.entities.Pekerjaan;
import com.updkbarito.apik.entities.Pengguna;
import com.updkbarito.apik.services.IzinService;
import com.updkbarito.apik.services.LokasiService;
import com.updkbarito.apik.services.PenggunaService;
import com.updkbarito.apik.services.ResponseIzin;
import com.updkbarito.apik.services.ResponseLokasiList;
import com.updkbarito.apik.services.ResponsePenggunaList;
import com.updkbarito.apik.utils.Helpers;
import com.updkbarito.apik.utils.Session;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class IzinTambahActivity extends AppCompatActivity implements View.OnClickListener {

    private final String TAG = IzinTambahActivity.class.getSimpleName();
    private ImageView imageBack;
    private View layoutProgressBar;
    private Button buttonLanjut;
    private IzinTambahActivity context;
    private Intent intent;
    private String jenis;
    private Izin izin = new Izin();
    private ArrayList<Pengguna> listPekerja = new ArrayList<>();
    private ArrayList<Pekerjaan> listKlasifikasi = new ArrayList<>();
    private Retrofit retrofit;
    private Session session;
    private Pengguna pengguna = new Pengguna();
    private List<Lokasi> listLokasi = new ArrayList<>();
    private TextView textJudul;
    private AutoCompleteTextView textLokasi;
    private TextInputEditText textDireksiPekerjaan;
    private TextInputEditText textDireksiLapangan;
    private TextInputEditText textNomor;
    private TextInputEditText textNamaPekerjaan;
    private TextInputEditText textNoSpk;
    private TextInputEditText textNoSpmk;
    private TextInputEditText textNamaPengawas;
    private TextInputEditText textTelpPengawas;
    private TextInputEditText textNamaPengawasVendor;
    private TextInputEditText textTelpPengawasVendor;
    private TextInputEditText textNamaPengawasK3;
    private TextInputEditText textTelpPengawasK3;
    private TextInputEditText textTanggalMulai;
    private TextInputEditText textTanggalSampai;
    private TextInputEditText textJamMulai;
    private TextInputEditText textJamSampai;
    private ArrayList<String> stringLokasiList = new ArrayList<>();
    private String judul;
    private String direksiPekerjaan;
    private String direksiLapangan;
    private String lokasiString;
    private String nomor;
    private String namaPekerjaan;
    private String noSpk;
    private String noSpmk;
    private String namaPengawas;
    private String telpPengawas;
    private String namaPengawasVendor;
    private String telpPengawasVendor;
    private String pengawasK3;
    private String telpPengawasK3;
    private String tanggalMulai;
    private String tanggalSampai;
    private String jamMulai;
    private String jamSampai;
    private String namaDireksiPekerjaan;
    private String telpDireksiPekerjaan;
    private IzinService izinService;
    private LokasiService lokasiService;
    private int lokasiId = -1;
    private int direksiPekerjaanId = -1;
    private String tanggalPengajuan;
    private PenggunaService penggunaService;
    private List<Pengguna> listDireksi = new ArrayList<>();
    private ArrayList<String> stringDireksiList = new ArrayList<>();
    private boolean loadLokasi = false;
    private boolean loadDireksi = false;
    private boolean edit = false;
    private int izinId = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_izin_tambah);

        textJudul = findViewById(R.id.text_judul);
        textLokasi = findViewById(R.id.text_lokasi);
        textNomor = findViewById(R.id.text_nomor);
        textNamaPekerjaan = findViewById(R.id.text_nama_pekerjaan);
        textNoSpk = findViewById(R.id.text_no_spk);
        textNoSpmk = findViewById(R.id.text_no_spmk);
        textDireksiPekerjaan = findViewById(R.id.text_direksi_pekerjaan);
        textDireksiLapangan = findViewById(R.id.text_nama_direksi_lapangan);
        textNamaPengawas = findViewById(R.id.text_nama_pengawas_pekerjaan);
        textTelpPengawas = findViewById(R.id.text_telp_pengawas_pekerjaan);
        textNamaPengawasVendor = findViewById(R.id.text_nama_pengawas_vendor);
        textTelpPengawasVendor = findViewById(R.id.text_telp_pengawas_vendor);
        textNamaPengawasK3 = findViewById(R.id.text_nama_pengawas_k3);
        textTelpPengawasK3 = findViewById(R.id.text_telp_pengawas_k3);
        textTanggalMulai = findViewById(R.id.text_tanggal_mulai);
        textTanggalSampai = findViewById(R.id.text_tanggal_sampai);
        textJamMulai = findViewById(R.id.text_jam_mulai);
        textJamSampai = findViewById(R.id.text_jam_sampai);
        imageBack = findViewById(R.id.image_back);
        buttonLanjut = findViewById(R.id.button_lanjut);
        layoutProgressBar = findViewById(R.id.layout_progress_bar);

        context = this;

        intent = getIntent();
        edit = intent.getBooleanExtra("edit", false);
        jenis = intent.getStringExtra("jenis");
        izin = intent.getParcelableExtra("izin");
        izinId = intent.getIntExtra("izin_id", 0);
        listPekerja = intent.getParcelableArrayListExtra("list_pekerja");
        listKlasifikasi = intent.getParcelableArrayListExtra("list_klasifikasi");
        if (jenis == null) jenis = "internal";

        if (edit) judul = "Ubah Izin "+Helpers.capitalize(jenis);
        else judul = "Tambah Izin "+Helpers.capitalize(jenis);

        textJudul.setText(judul);

        imageBack.setOnClickListener(context);
        buttonLanjut.setOnClickListener(context);
        textTanggalMulai.setOnClickListener(context);
        textTanggalSampai.setOnClickListener(context);
        textJamMulai.setOnClickListener(context);
        textJamSampai.setOnClickListener(context);

        session = new Session(context, "pengguna");
        pengguna = session.getPengguna();
        retrofit = Helpers.initRetrofit(context);
        izinService = retrofit.create(IzinService.class);
        lokasiService = retrofit.create(LokasiService.class);

        if (jenis.equals("internal")){
            findViewById(R.id.layout_no_spk).setVisibility(View.GONE);
            findViewById(R.id.layout_no_spmk).setVisibility(View.GONE);
            findViewById(R.id.layout_nama_pengawas_vendor).setVisibility(View.GONE);
            findViewById(R.id.layout_telp_pengawas_vendor).setVisibility(View.GONE);
            findViewById(R.id.layout_direksi_pekerjaan).setVisibility(View.GONE);
            findViewById(R.id.layout_direksi_lapangan).setVisibility(View.GONE);
        } else if (jenis.equals("eksternal")){
            findViewById(R.id.layout_nama_pengawas_pekerjaan).setVisibility(View.GONE);
            findViewById(R.id.layout_telp_pengawas_pekerjaan).setVisibility(View.GONE);

            penggunaService = retrofit.create(PenggunaService.class);

//            textDireksiPekerjaan.setEnabled(false);
//            textDireksiPekerjaan.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                @Override
//                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                    direksiPekerjaanId = listDireksi.get(position).getId();
//                    textDireksiPekerjaan.setEnabled(false);
//                }
//            });
        }

        textLokasi.setEnabled(false);
        textLokasi.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                textLokasi.setEnabled(false);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        textLokasi.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                textLokasi.setEnabled(false);
                Lokasi lokasi = listLokasi.get(position);
                direksiPekerjaanId = -1;
                lokasiId = lokasi.getId();
                if (jenis.equals("eksternal")) initDireksi(lokasi.getKode());
            }
        });

        if (pengguna != null){
            initIzin();
        }
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
        } else if (v.getId() == textTanggalMulai.getId()){
            datePickerDialog(v);
        } else if (v.getId() == textTanggalSampai.getId()){
            datePickerDialog(v);
        } else if (v.getId() == textJamMulai.getId()){
            timePickerDialog(v);
        } else if (v.getId() == textJamSampai.getId()){
            timePickerDialog(v);
        }
    }

    private void datePickerDialog(final View v) {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month++;
                String dateSelected = year+"-"+(month>=10?month:"0"+month)+"-"+(dayOfMonth>=10?dayOfMonth:"0"+dayOfMonth);
                if (v.getId() == textTanggalMulai.getId()){
                    textTanggalMulai.setText(dateSelected);
                } else if (v.getId() == textTanggalSampai.getId()){
                    textTanggalSampai.setText(dateSelected);
                }
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE));
        datePickerDialog.show();
    }

    private void timePickerDialog(final View v) {
        Calendar calendar = Calendar.getInstance();
        TimePickerDialog timePickerDialog = new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                String timeSelected = (hourOfDay >= 10 ? hourOfDay : "0"+hourOfDay)+":"+(minute>=10?minute:"0"+minute);
                if (v.getId() == textJamMulai.getId()){
                    textJamMulai.setText(timeSelected);
                } else if (v.getId() == textJamSampai.getId()){
                    textJamSampai.setText(timeSelected);
                }
            }
        }, calendar.get(Calendar.HOUR), calendar.get(Calendar.MINUTE), true);
        timePickerDialog.show();
    }

    private void lanjutAction(View v) {
        if (validation()){
            layoutProgressBar.setVisibility(View.VISIBLE);
            HashMap<String, String> map = new HashMap<>();
            map.put("pengguna_id", String.valueOf(pengguna.getId()));
            if (lokasiId > 0) map.put("lokasi_id", String.valueOf(lokasiId));
//        map.put("vendor_id", null);
            map.put("nomor", nomor);
            map.put("nama", namaPekerjaan);
            map.put("direksi_lapangan", namaPengawas);
            map.put("direksi_lapangan_telp", telpPengawas);
            map.put("pengawas_k3", pengawasK3);
            map.put("pengawas_k3_telp", telpPengawasK3);
            if (tanggalPengajuan != null) map.put("tanggal_pengajuan", tanggalPengajuan);
            map.put("tanggal_mulai", tanggalMulai);
            map.put("tanggal_sampai", tanggalSampai);
            map.put("jam_mulai", jamMulai);
            map.put("jam_sampai", jamSampai);
            map.put("jenis", jenis);
            map.put("lokasi_lengkap", lokasiString);

            if (jenis.equals("internal")){
                map.put("pengawas_vendor", namaPengawas);
                map.put("pengawas_vendor_telp", telpPengawas);
            } else if (jenis.equals("eksternal")){
                map.put("no_spk", noSpk);
                map.put("no_spmk", noSpmk);
                map.put("pengawas_vendor", namaPengawasVendor);
                map.put("pengawas_vendor_telp", telpPengawasVendor);
                map.put("direksi_pekerjaan", direksiPekerjaan);
                map.put("direksi_lapangan", direksiLapangan);
//                map.put("direksi_pekerjaan_telp", telpDireksiPekerjaan);
                if (direksiPekerjaanId > 0) map.put("direksi_lapangan_id", String.valueOf(direksiPekerjaanId));

                penggunaService = retrofit.create(PenggunaService.class);

//                textDireksiPekerjaan.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                    @Override
//                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                        direksiPekerjaanId = listDireksi.get(position).getId();
//                    }
//                });
            }

            izinService.tambah(map).enqueue(new Callback<ResponseIzin>() {
                @Override
                public void onResponse(Call<ResponseIzin> call, Response<ResponseIzin> response) {
                    layoutProgressBar.setVisibility(View.GONE);
                    if (response.isSuccessful()){
                        if (response.body() != null){
                            izin = response.body().getData();
                            Log.d(TAG, "izin_id:"+izin.getId());
                            finish();
                            if (jenis != null && jenis.equals("eksternal")){
                                intent = new Intent(context, IzinKlasifikasiActivity.class);
                            } else {
                                intent = new Intent(context, IzinPekerjaActivity.class);
                            }

                            intent.putExtra("edit", edit);
                            intent.putExtra("jenis", jenis);
                            intent.putExtra("izin", izin);
                            intent.putExtra("izin_id", izin.getId());
                            startActivity(intent);
                        }
                    }
                }

                @Override
                public void onFailure(Call<ResponseIzin> call, Throwable t) {
                    layoutProgressBar.setVisibility(View.GONE);
                    t.printStackTrace();
                }
            });
        }
    }

    private void kembaliAction(View v) {
        finish();
        if (edit) {
            intent = new Intent(context, IzinDetailActivity.class);
            intent.putExtra("izin_id", izinId);
        } else intent = new Intent(context, MenuActivity.class);
        intent.putExtra("jenis", jenis);
        startActivity(intent);
    }

    private boolean validation() {
        lokasiString = textLokasi.getText().toString();
        nomor = textNomor.getText().toString();
        namaPekerjaan = textNamaPekerjaan.getText().toString();
        noSpk = textNoSpk.getText().toString();
        noSpmk = textNoSpmk.getText().toString();
        direksiPekerjaan = textDireksiPekerjaan.getText().toString();
        namaPengawas = textNamaPengawas.getText().toString();
        telpPengawas = textTelpPengawas.getText().toString();
        namaPengawasVendor = textNamaPengawasVendor.getText().toString();
        telpPengawasVendor = textTelpPengawasVendor.getText().toString();
        pengawasK3 = textNamaPengawasK3.getText().toString();
        telpPengawasK3 = textTelpPengawasK3.getText().toString();
        direksiLapangan = textDireksiLapangan.getText().toString();
        tanggalMulai = textTanggalMulai.getText().toString();
        tanggalSampai = textTanggalSampai.getText().toString();
        jamMulai = textJamMulai.getText().toString();
        jamSampai = textJamSampai.getText().toString();

        if (izin == null || izin.getTanggalPengajuan() == null)
            tanggalPengajuan = Helpers.reformatDate(new Date(), "yyyy-MM-dd HH:mm:ss");

//        direksiPekerjaanId = getDireksiPekerjaanId(direksiPekerjaanString);
        lokasiId = getLokasiId(lokasiString);

        if (lokasiId < 0){
            Snackbar.make(findViewById(android.R.id.content), "Pilih lokasi terlebih dahulu", Snackbar.LENGTH_LONG).show();
            textLokasi.requestFocus();
            return false;
        } else if (nomor.length() <= 0){
            Snackbar.make(findViewById(android.R.id.content), "Nomor harus diisi", Snackbar.LENGTH_LONG).show();
            textNomor.requestFocus();
            return false;
        } else if (namaPekerjaan.length() <= 0){
            Snackbar.make(findViewById(android.R.id.content), "Nama pekerjaan harus diisi", Snackbar.LENGTH_LONG).show();
            textNamaPekerjaan.requestFocus();
            return false;
        }
/*
        else if (jenis.equals("eksternal") && direksiPekerjaanId < 0){
            Snackbar.make(findViewById(android.R.id.content), "Direksi Pekerjaan harus diisi", Snackbar.LENGTH_LONG).show();
            textDireksiPekerjaan.requestFocus();
            return false;
        }
*/
        else if (jenis.equals("eksternal") && direksiPekerjaan.trim().length() < 0){
            Snackbar.make(findViewById(android.R.id.content), "Direksi Pekerjaan harus diisi", Snackbar.LENGTH_LONG).show();
            textDireksiPekerjaan.requestFocus();
            return false;
        } else if (jenis.equals("internal") && namaPengawas.length() <= 0){
            Snackbar.make(findViewById(android.R.id.content), "Nama Pengawas harus diisi", Snackbar.LENGTH_LONG).show();
            textNamaPengawas.requestFocus();
            return false;
        } else if (jenis.equals("internal") && telpPengawas.length() <= 0){
            Snackbar.make(findViewById(android.R.id.content), "No Telp Pengawas harus diisi", Snackbar.LENGTH_LONG).show();
            textTelpPengawas.requestFocus();
            return false;
        } else if (pengawasK3.length() <= 0){
            Snackbar.make(findViewById(android.R.id.content), "Nama Pengawas K3 harus diisi", Snackbar.LENGTH_LONG).show();
            textNamaPengawasK3.requestFocus();
            return false;
        } else if (telpPengawasK3.length() <= 0){
            Snackbar.make(findViewById(android.R.id.content), "No Telp Pengawas K3 harus diisi", Snackbar.LENGTH_LONG).show();
            textTelpPengawasK3.requestFocus();
            return false;
        } else if (jenis.equals("eksternal") && namaPengawasVendor.length() <= 0){
            Snackbar.make(findViewById(android.R.id.content), "Nama Pengawas vendor harus diisi", Snackbar.LENGTH_LONG).show();
            textNamaPengawasVendor.requestFocus();
            return false;
        } else if (jenis.equals("eksternal") && telpPengawasVendor.length() <= 0){
            Snackbar.make(findViewById(android.R.id.content), "No Telp Pengawas vendor harus diisi", Snackbar.LENGTH_LONG).show();
            textTelpPengawasVendor.requestFocus();
            return false;
        }

        return true;
    }

    private void initIzin() {
        HashMap<String, String> map = new HashMap<>();
        map.put("jenis", jenis);
        map.put("status", "baru");
        Log.d(TAG, "pengguna_id:"+pengguna.getId());
        if (izinId > 0){
            izinService.show(izinId, map).enqueue(callbackService);
        } else {
            izinService.last(pengguna.getId(), map).enqueue(callbackService);
        }
    }

    private void initLokasi() {
        Log.d(TAG, "initLokasi");
        HashMap<String, String> map = new HashMap<>();
        lokasiService.index(map).enqueue(new Callback<ResponseLokasiList>() {
            @Override
            public void onResponse(Call<ResponseLokasiList> call, Response<ResponseLokasiList> response) {
                if (response.isSuccessful()){
                    if (response.body() != null){
                        int loadLokasiIndex = -1;
                        listLokasi = response.body().getData();
                        stringLokasiList = new ArrayList<>();
                        for (Lokasi lokasi: listLokasi) {
                            stringLokasiList.add(lokasi.getNama());
                            if (izin.getLokasiId() == lokasi.getId()) loadLokasiIndex = listLokasi.indexOf(lokasi);
                        }

                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_dropdown_item_1line, stringLokasiList);
                        textLokasi.setAdapter(adapter);
                        Log.d(TAG, "initLokasi:"+stringLokasiList.size());
                        if (stringLokasiList.size() == 1) {
                            textLokasi.setText(stringLokasiList.get(0));
//                            textLokasi.setInputType(0);
                            textLokasi.setFocusable(false);
                            Log.d(TAG, "initLokasi:kode:"+listLokasi.get(0).getKode());
//                            if (jenis.equals("eksternal")) initDireksi(listLokasi.get(0).getKode());
                        } else if (!loadLokasi && loadLokasiIndex >= 0) {
                            lokasiId = listLokasi.get(loadLokasiIndex).getId();
                            textLokasi.setText(stringLokasiList.get(loadLokasiIndex));
                            loadLokasi = true;
                            textLokasi.setFocusable(true);
                            Log.d(TAG, "initLokasi:kode:"+listLokasi.get(loadLokasiIndex).getKode());
//                            if (jenis.equals("eksternal")) initDireksi(listLokasi.get(loadLokasiIndex).getKode());
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseLokasiList> call, Throwable t) {

            }
        });
    }

    private void initDireksi(String lokasiKode) {
        Log.d(TAG, "listDireksi:lokasiKode:"+lokasiKode);
        HashMap<String, String> map = new HashMap<>();
        if (lokasiKode != null) map.put("lokasi_kode", lokasiKode);
        penggunaService.direksiList(map).enqueue(new Callback<ResponsePenggunaList>() {
            @Override
            public void onResponse(Call<ResponsePenggunaList> call, Response<ResponsePenggunaList> response) {
                if (response.isSuccessful()){
                    if (response.body() != null && response.body().getData() != null) {
                        int loadDireksiIndex = -1;
                        listDireksi = response.body().getData();
                        Log.d(TAG, "listDireksi:"+listDireksi.size());

                        stringDireksiList = new ArrayList<>();
                        for (Pengguna direksi: listDireksi) {
                            stringDireksiList.add(direksi.getNama());
                            if (izin.getDireksiLapanganId() == direksi.getId()) loadDireksiIndex = listDireksi.indexOf(direksi);
                        }
/*
                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_dropdown_item_1line, stringDireksiList);
                        textDireksiPekerjaan.setText("");
                        textDireksiPekerjaan.setAdapter(adapter);
                        if (stringDireksiList.size() == 1) textDireksiPekerjaan.setText(stringDireksiList.get(0));
                        else if (!loadDireksi && loadDireksiIndex >= 0) {
                            direksiPekerjaanId = listDireksi.get(loadDireksiIndex).getId();
                            textDireksiPekerjaan.setText(stringDireksiList.get(loadDireksiIndex));
                            loadDireksi = true;
                        }
 */
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponsePenggunaList> call, Throwable t) {

            }
        });
    }

    private Callback<ResponseIzin> callbackService = new Callback<ResponseIzin>() {
        @Override
        public void onResponse(Call<ResponseIzin> call, Response<ResponseIzin> response) {
            if (response.isSuccessful()){
                if (response.body() != null){
                    izin = response.body().getData();
                    if (izin != null) {
                        textNomor.setText(izin.getNomor());

                        if (izin.getNama() != null) textNamaPekerjaan.setText(izin.getNama());
                        if (izin.getNoSpk() != null) textNoSpk.setText(izin.getNoSpk());
                        if (izin.getNoSpmk() != null) textNoSpmk.setText(izin.getNoSpmk());
//                            if (izin.getNama() != null) textDireksiPekerjaan.setText(izin.getNama());

                        if (jenis.equals("internal")){
                            if (izin.getPengawasVendor() != null) textNamaPengawas.setText(izin.getPengawasVendor());
                            if (izin.getPengawasVendorTelp() != null) textTelpPengawas.setText(izin.getPengawasVendorTelp());
                        } else if (jenis.equals("eksternal")){
                            if (izin.getPengawasVendor() != null) textNamaPengawasVendor.setText(izin.getPengawasVendor());
                            if (izin.getPengawasVendorTelp() != null) textTelpPengawasVendor.setText(izin.getPengawasVendorTelp());
                            if (izin.getDireksiLapangan() != null) textDireksiLapangan.setText(izin.getDireksiLapangan());
                        }
                        if (izin.getPengawasK3() != null) textNamaPengawasK3.setText(izin.getPengawasK3());
                        if (izin.getPengawasK3Telp() != null) textTelpPengawasK3.setText(izin.getPengawasK3Telp());
                        if (izin.getDireksiPekerjaan() != null) textDireksiPekerjaan.setText(izin.getDireksiPekerjaan());
                        if (izin.getTanggalMulai() != null) textTanggalMulai.setText(izin.getTanggalMulai());
                        if (izin.getTanggalSampai() != null) textTanggalSampai.setText(izin.getTanggalSampai());
                        if (izin.getJamMulai() != null) {
                            String[] jamMulaiList = izin.getJamMulai().split(":");
                            if (jamMulaiList.length >= 2) textJamMulai.setText(jamMulaiList[0]+":"+jamMulaiList[1]);
                            else textJamMulai.setText(izin.getJamMulai());
                        }
                        if (izin.getJamSampai() != null) {
                            String[] jamSampaiList = izin.getJamSampai().split(":");
                            if (jamSampaiList.length >= 2) textJamSampai.setText(jamSampaiList[0]+":"+jamSampaiList[1]);
                            else textJamSampai.setText(izin.getJamSampai());
                        }
                    }

                    initLokasi();
                }
            }
        }

        @Override
        public void onFailure(Call<ResponseIzin> call, Throwable t) {

        }
    };

    private int getLokasiId(String string){
        if (listLokasi != null && listLokasi.size() > 0){
            for (Lokasi lokasi: listLokasi){
                if (lokasi.getNama().equals(string)) return lokasi.getId();
            }
        }
        return -1;
    }

    private int getDireksiPekerjaanId(String string){
        if (listDireksi != null && listDireksi.size() > 0){
            for (Pengguna direksi: listDireksi){
                if (direksi.getNama().equals(string)) return direksi.getId();
            }
        }
        return -1;
    }
}
