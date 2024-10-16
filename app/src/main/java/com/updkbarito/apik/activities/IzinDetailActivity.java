package com.updkbarito.apik.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.opengl.Visibility;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.chip.Chip;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.updkbarito.apik.R;
import com.updkbarito.apik.adapters.ListIzinDetailPekerjaExpandableListAdapter;
import com.updkbarito.apik.adapters.ListIzinDetailRecyclerView;
import com.updkbarito.apik.entities.Akses;
import com.updkbarito.apik.entities.Izin;
import com.updkbarito.apik.entities.IzinDetail;
import com.updkbarito.apik.entities.IzinPersetujuan;
import com.updkbarito.apik.entities.Pengguna;
import com.updkbarito.apik.services.AksesService;
import com.updkbarito.apik.services.IzinService;
import com.updkbarito.apik.services.ResponseAksesList;
import com.updkbarito.apik.services.ResponseIzin;
import com.updkbarito.apik.utils.Helpers;
import com.updkbarito.apik.utils.Session;

import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class IzinDetailActivity extends AppCompatActivity implements View.OnClickListener {

    private static String TAG = IzinDetailActivity.class.getSimpleName();
    private Intent intent;
    private String jenis;
    private String status;
    private Izin izin = new Izin();
    private int izinId;
    private IzinDetailActivity context;
    private Retrofit retrofit;
    private IzinService izinService;
    private AksesService aksesService;
    private ImageView imageBack;
    private TextView textNama;
    private TextView textTanggal;
    private TextView textLokasi;
    private TextView textNomor;
    private Chip chipStatus;
    private TextView textMulaiPekerjaan;
    private TextView textWaktu;
    private TextView textSpk;
    private TextView textSpmk;
    private TextView textAtasan;
    private TextView textAtasanEmail;
    private RecyclerView recyclerViewKlasifikasi;
    private RecyclerView recyclerViewApd;
    private RecyclerView recyclerViewDokumen;
    private ExpandableListView expandableListViewPekerja;
    private Button buttonTerima;
    private Button buttonTolak;
    private Button buttonTerimaPekerjaan;
    private Button buttonTolakPekerjaan;
    private Button buttonEdit;
    private Button buttonClosing;
    private Button buttonCetak;
    private View layoutProgressBar;
    private LinearLayout layoutNoSpk;
    private CardView layoutCardView;
    private LinearLayout layoutAksi;
    private LinearLayout layoutTerima;
    private LinearLayout layoutApproval;
    private ListIzinDetailRecyclerView adapter;
    private List<IzinDetail> izinDetailList = new ArrayList<>();
    private List<IzinDetail> listDokumen = new ArrayList<>();
    private List<IzinDetail> listPeralatan = new ArrayList<>();
    private List<IzinDetail> listKlasifikasi = new ArrayList<>();
    private List<IzinDetail> listPekerja = new ArrayList<>();
    private List<Akses> aksesList = new ArrayList<>();
    private Pengguna pengguna = new Pengguna();
    private View layoutCatatan;
    private View layoutPekerja;
    private TextView textViewCatatan;
    private TableLayout tableLayoutPekerja;
    private Session session;
    private Session sessionAkses;
    private String catatan = "";
    private String tampilCatatan = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_izin_detail);

        textNama = findViewById(R.id.text_nama);
        textTanggal = findViewById(R.id.text_tanggal);
        textLokasi = findViewById(R.id.text_lokasi);
        textNomor = findViewById(R.id.text_nomor);
        chipStatus = findViewById(R.id.chip_status);
        textMulaiPekerjaan = findViewById(R.id.text_mulai_pekerjaan);
        textWaktu = findViewById(R.id.text_waktu);
        textSpk = findViewById(R.id.text_spk);
        textSpmk = findViewById(R.id.text_spmk);
        textAtasan = findViewById(R.id.text_atasan);
        textAtasanEmail = findViewById(R.id.text_atasan_email);
        expandableListViewPekerja = findViewById(R.id.expandable_list_pekerja);
        recyclerViewKlasifikasi = findViewById(R.id.recycler_view_klasifikasi);
        recyclerViewApd = findViewById(R.id.recycler_view_apd);
        recyclerViewDokumen = findViewById(R.id.recycler_view_dokumen);
        imageBack = findViewById(R.id.image_back);
        layoutCatatan = findViewById(R.id.layout_catatan);
        textViewCatatan = findViewById(R.id.text_catatan);
        layoutPekerja = findViewById(R.id.layout_pekerja);
        tableLayoutPekerja = findViewById(R.id.table_layout_pekerja);
        layoutProgressBar = findViewById(R.id.layout_progress_bar);
        layoutNoSpk = findViewById(R.id.layout_no_spk);
        layoutCardView = findViewById(R.id.layout_card_view);
        layoutAksi = findViewById(R.id.layout_aksi);
        layoutTerima = findViewById(R.id.layout_terima);
        layoutApproval = findViewById(R.id.layout_approval);
        buttonTerima = findViewById(R.id.button_terima);
        buttonTolak = findViewById(R.id.button_tolak);
        buttonTerimaPekerjaan = findViewById(R.id.button_terima_pekerjaan);
        buttonTolakPekerjaan = findViewById(R.id.button_tolak_pekerjaan);
        buttonEdit = findViewById(R.id.button_edit);
        buttonClosing = findViewById(R.id.button_closing);
        buttonCetak = findViewById(R.id.button_cetak);

        layoutAksi.setVisibility(View.GONE);
        buttonCetak.setVisibility(View.GONE);

        context = this;

        intent = getIntent();
        jenis = intent.getStringExtra("jenis");
        status = intent.getStringExtra("status");
        izin = intent.getParcelableExtra("izin");
        izinId = intent.getIntExtra("izin_id", 0);

        if (izin == null &&  izinId <= 0){
            finish();
            intent = new Intent(this, MenuActivity.class);
            startActivity(intent);
        }

        session = new Session(context, "pengguna");
        pengguna = session.getPengguna();

        retrofit = Helpers.initRetrofit(context);
        izinService = retrofit.create(IzinService.class);
        aksesService = retrofit.create(AksesService.class);

        imageBack.setOnClickListener(context);
        buttonTerima.setOnClickListener(context);
        buttonTolak.setOnClickListener(context);
        buttonTerimaPekerjaan.setOnClickListener(context);
        buttonTolakPekerjaan.setOnClickListener(context);
        buttonEdit.setOnClickListener(context);
        buttonClosing.setOnClickListener(context);
        buttonCetak.setOnClickListener(context);
    }

    @Override
    protected void onStart() {
        super.onStart();

        layoutProgressBar.setVisibility(View.GONE);

        izinService.show(izinId, new HashMap<String, String>()).enqueue(new Callback<ResponseIzin>() {
            @Override
            public void onResponse(Call<ResponseIzin> call, Response<ResponseIzin> response) {
                if (response.isSuccessful()){
                    if (response.body() != null){
                        if (response.body().getData() != null){
                            izin = response.body().getData();

                            textNama.setText(izin.getNama());
                            textTanggal.setText(izin.getTanggalPengajuan());
                            textLokasi.setText(izin.getLokasiLengkap());
                            textNomor.setText(izin.getNomor());
                            if ((izin.getTanggalMulai() != null && !izin.getTanggalMulai().equals("0000-00-00")) && (izin.getTanggalSampai() != null && !izin.getTanggalSampai().equals("0000-00-00"))){
                                textMulaiPekerjaan.setText(Helpers.reformatDate(izin.getTanggalMulai(), null)+" - "+Helpers.reformatDate(izin.getTanggalSampai(), null));
                            } else if (izin.getTanggalMulai() != null && !izin.getTanggalMulai().equals("0000-00-00")){
                                textMulaiPekerjaan.setText(Helpers.reformatDate(izin.getTanggalMulai(), null));
                            } else if (izin.getTanggalSampai() != null && !izin.getTanggalSampai().equals("0000-00-00")){
                                textMulaiPekerjaan.setText(Helpers.reformatDate(izin.getTanggalSampai(), null));
                            }
                            textWaktu.setText(izin.getJamMulai()+" - "+izin.getJamSampai());
                            textSpk.setText(izin.getNoSpk());
                            textSpmk.setText(izin.getNoSpmk());
                            chipStatus.setText(Helpers.capitalize(izin.getStatus().replace("_", " ")));
                            chipStatus.setTextColor(context.getResources().getColor(android.R.color.white));
                            if (Helpers.containIgnoreCase(izin.getStatus(), "ditolak", "pekerjaan_ditolak")){
                                chipStatus.setChipBackgroundColorResource(R.color.red);
                            } else if (Helpers.containIgnoreCase(izin.getStatus(), "diterima", "pekerjaan_diterima")){
                                chipStatus.setChipBackgroundColorResource(R.color.colorPrimary);
                            } else if (Helpers.containIgnoreCase(izin.getStatus(), "baru", "proses", "pekerjaan_diajukan")){
                                chipStatus.setChipBackgroundColorResource(R.color.colorAccent);
                            }

                            String lokasiKode = izin.getLokasiKode();

                            if (lokasiKode == null && izin.getLokasi() != null) lokasiKode = izin.getLokasi().getKode();

                            int showLayoutAksi = View.GONE;
                            int showLayoutTerima = View.GONE;
                            int showLayoutApproval = View.GONE;
                            int showButtonEdit = View.GONE;
                            int showButtonClosing = View.GONE;
                            int showButtonCetak = View.GONE;
                            int key = 0;
                            IzinPersetujuan izinPersetujuan = null;

                            HashMap<Integer, IzinPersetujuan> map = Helpers.status(izin);
                            for(Map.Entry<Integer, IzinPersetujuan> entry : map.entrySet()) {
                                if (entry.getKey() > key){
                                    key = entry.getKey();
                                    izinPersetujuan = entry.getValue();
                                }
                            }

                            if (izinPersetujuan != null){
                                if (key == 1){
                                    chipStatus.setText("Diterima: "+izinPersetujuan.getJabatanKode());
                                    chipStatus.setChipBackgroundColorResource(R.color.colorPrimary);
                                    chipStatus.setTextColor(context.getResources().getColor(android.R.color.white));
                                    if (izin.getJenis().equals("internal")){
                                        showLayoutAksi = View.VISIBLE;
                                    }
                                } else if (key == 2){
                                    chipStatus.setText("Diterima: "+izinPersetujuan.getJabatanKode());
                                    chipStatus.setChipBackgroundColorResource(R.color.colorPrimary);
                                    chipStatus.setTextColor(context.getResources().getColor(android.R.color.white));
                                    showLayoutAksi = View.VISIBLE;
                                } else if (key == 3){
                                    chipStatus.setText("Diterima: "+izinPersetujuan.getJabatanKode());
                                    chipStatus.setChipBackgroundColorResource(R.color.colorPrimaryDark);
                                    chipStatus.setTextColor(context.getResources().getColor(android.R.color.white));
                                    showLayoutAksi = View.VISIBLE;
                                    if (izin.getJenis().equals("eksternal")){
                                        showLayoutAksi = View.VISIBLE;
                                    }
                                } else if (key == 6){
                                    chipStatus.setText("Pekerjaan Diterima");
                                    chipStatus.setChipBackgroundColorResource(R.color.colorPrimaryDark);
                                    chipStatus.setTextColor(context.getResources().getColor(android.R.color.white));
                                } else if (key == 7){
                                    chipStatus.setText("Pekerjaan Diterima");
                                    chipStatus.setChipBackgroundColorResource(R.color.colorPrimaryDark);
                                    chipStatus.setTextColor(context.getResources().getColor(android.R.color.white));
                                } else if (key == 8){
                                    chipStatus.setText("Pekerjaan Diterima");
                                    chipStatus.setChipBackgroundColorResource(R.color.colorPrimaryDark);
                                    chipStatus.setTextColor(context.getResources().getColor(android.R.color.white));
                                }

                                if (key > 0 && Helpers.checkAccess(context, izinPersetujuan.getJabatanKode(), null)){
                                    showLayoutAksi = View.VISIBLE;
                                    showLayoutTerima = View.GONE;
                                }
//                                else if (izin.getPenggunaId() == pengguna.getId()){
//                                    showButtonEdit = View.VISIBLE;
//                                }
                                else if (Helpers.checkAccess(context, izinPersetujuan.getJabatanKode(), null)){
                                    showLayoutAksi = View.GONE;
                                    showLayoutTerima = View.GONE;
                                }
                            }

                            if (izin.getButton() != null){
                                showLayoutAksi = View.VISIBLE;
                                if (izin.getButton().equals("editable")) {
                                    showButtonEdit = View.VISIBLE;
                                } else if (izin.getButton().equals("pengajuan")) {
                                    showLayoutTerima = View.VISIBLE;
                                } else if (izin.getButton().equals("closing")) {
                                    showButtonClosing = View.VISIBLE;
                                } else if (izin.getButton().equals("pengerjaan")) {
                                    showLayoutApproval = View.VISIBLE;
                                } else if (izin.getButton().equals("cetak")) {
                                    showButtonCetak = View.VISIBLE;
                                }
                            } else if (izin.getJenis().equals("internal")){
                                if (lokasiKode != null && lokasiKode.equals("UPDKBRTO")) {
                                    // Izin Kerja Internal (kantor):
                                    // 1   Supervisor Operasi dan Pemeliharaan / PJLAKSKLK / spv sdm    Pengajuan / Cetak
                                    // 2   PJ LAKSK4 UPDK Barito                                        Verifikator(Tolak / Setuju) / Cetak
                                    // 3   Manager Unit Pelaksana UPDK Barito                           Tolak / Setuju / Cetak
                                    if (Helpers.checkAccess(context, "PJLAKSK4", lokasiKode) && key < 1){
                                        showLayoutAksi = View.VISIBLE;
                                        showLayoutTerima = View.VISIBLE;
                                    } else if (Helpers.checkAccess(context, "MUP", lokasiKode) && key == 1) {
                                        showLayoutAksi = View.VISIBLE;
                                        showLayoutTerima = View.VISIBLE;
                                    }
                                } else {
                                    // Izin Kerja Internal (ULPL):
                                    // 1   Supervisor Operasi dan Pemeliharaan                                                                  Pengajuan / Cetak
                                    // 2   PJ LAKSK4 (ULPLTD/G Trisakti, atau ULPLTA/D Gunung Bamega, atau ULPLTD Tambun Bungai)                Verifikator(Tolak / Setuju)  / Cetak
                                    // 3   Manager Unit Layanan (ULPLTD/G Trisakti, atau ULPLTA/D Gunung Bamega, atau ULPLTD Tambun Bungai)     Tolak / Setuju  / Cetak
                                    // 4   Manager Bagian OPHAR 1, Manager Bagian OPHAR 2, Manager Bagian KSA, PJLAKSK4                         Monitor pekerjaan di UL / Cuma bisa View  / Cetak
                                    // 5   Manager Unit Pelaksana UPDK Barito                                                                   Monitor pekerjaan di UL / Cuma bisa View / Cetak
                                    if (Helpers.checkAccess(context, "PJLAKSK4", lokasiKode) && key < 1){
                                        showLayoutAksi = View.VISIBLE;
                                        showLayoutTerima = View.VISIBLE;
                                    } else if (Helpers.checkAccess(context, "MANAGER", lokasiKode) && key == 1) {
                                        showLayoutAksi = View.VISIBLE;
                                        showLayoutTerima = View.VISIBLE;
                                    }
                                }

                                if (Helpers.containString(izin.getStatus(), "diterima", "pekerjaan_diterima", "pekerjaan_selesai")){
                                    showLayoutAksi = View.VISIBLE;
                                    showLayoutApproval = View.GONE;
                                    showLayoutTerima = View.GONE;
                                    showButtonCetak = View.VISIBLE;
                                } else if (Helpers.containString(izin.getStatus(), "ditolak")){
//                                        showLayoutAksi = View.GONE;
                                    showLayoutTerima = View.GONE;
                                    chipStatus.setText("Ditolak");
                                    chipStatus.setChipBackgroundColorResource(R.color.red);
                                    chipStatus.setTextColor(context.getResources().getColor(android.R.color.white));
                                } else if (Helpers.containString(izin.getStatus(), "pekerjaan_ditolak")){
                                    chipStatus.setText("Pekerjaan Ditolak");
                                    chipStatus.setChipBackgroundColorResource(R.color.red);
                                    chipStatus.setTextColor(context.getResources().getColor(android.R.color.white));
//                                        showLayoutAksi = View.GONE;
                                    showLayoutApproval = View.GONE;
                                }
                            } else if (izin.getJenis().equals("eksternal")){
                                Log.d(TAG, "eksternal:status:"+key+"|lokasiKode:"+lokasiKode);
                                if (lokasiKode != null && lokasiKode.equals("UPDKBRTO")){
                                    // 2. Izin Kerja Eksternal (Kantor) :
                                    // 1   Vendor  Pengajuan  / Cetak
                                    // 2   PJ LAKSK4 Kantor UPDK Barito    Verifikator(Tolak / Setuju) / Cetak
                                    // 3   Direksi pekerjaan (Manager Bagian OPHAR 1, Manager Bagian OPHAR 2, Manager Bagian KSA, PJLAKSK4, PJLAKSLK)  Tolak / Setuju  / Cetak
                                    // 4   Manager Unit Pelaksana UPDK Barito  Tolak / Setuju / Cetak

                                    // CLOSING
                                    // 1   PJ LAKSK4 Kantor UPDK Barito    Pengajuan / Cetak
                                    // 2   PJ LAKSLK Kantor UPDK Barito    Tolak / Setuju / Cetak
                                    // 3   Direksi pekerjaan (Manager Bagian OPHAR 1, Manager Bagian OPHAR 2, Manager Bagian KSA, PJLAKSK4, PJLAKSLK)  Tolak / Setuju / Cetak
                                    // 4   Manager Bagian OPHAR 1, Manager Bagian OPHAR 2, Manager Bagian KSA, PJLAKSK4    Monitor pekerjaan di UL / Cuma bisa View / Cetak
                                    // 5   Manager Unit Pelaksana UPDK Barito  Monitor pekerjaan di UL / Cuma bisa View / Cetak

                                    if (Helpers.checkAccess(context, "PJLAKSK4", lokasiKode)){
                                        if (izin.getStatus().equals("diterima")) {
                                            showButtonClosing = View.VISIBLE;
                                        } else if (key == 0) {
                                            showLayoutAksi = View.VISIBLE;
                                            showLayoutTerima = View.VISIBLE;
                                        }
                                    } else if (pengguna.getId() == izin.getDireksiLapanganId()){
                                        if (key == 1){
                                            showLayoutAksi = View.VISIBLE;
                                            showLayoutTerima = View.VISIBLE;
                                        } else if (key == 6){
                                            showLayoutAksi = View.VISIBLE;
                                            showLayoutApproval = View.VISIBLE;
                                        }
                                    } else if (Helpers.checkAccess(context, "MUP", lokasiKode)){
                                        if (key == 2 || key == 1) {
                                            showLayoutAksi = View.VISIBLE;
                                            showLayoutTerima = View.VISIBLE;
                                        }
                                    } else if (Helpers.checkAccess(context, "MANAGER", lokasiKode) && key == 2){
                                        showLayoutAksi = View.VISIBLE;
                                        showLayoutTerima = View.VISIBLE;
                                    } else if (Helpers.checkAccess(context, "PJLAKSKLK", lokasiKode) && (key == 3 || key == 7) && Helpers.containString(izin.getStatus(), "pekerjaan_diajukan", "pekerjaan_ditolak")){
                                        showLayoutAksi = View.VISIBLE;
                                        showLayoutApproval = View.VISIBLE;
                                    }
                                } else {
                                    // 1   Vendor  Pengajuan / Cetak
                                    // 2   PJ LAKSK4 (ULPLTD/G Trisakti, atau ULPLTA/D Gunung Bamega, atau ULPLTD Tambun Bungai)   Verifikator(Tolak / Setuju) / Cetak
                                    // 3   Manager Unit Layanan (ULPLTD/G Trisakti, atau ULPLTA/D Gunung Bamega, atau ULPLTD Tambun Bungai)    Tolak / Setuju / Cetak
                                    // 4   Manager Bagian OPHAR 1, Manager Bagian OPHAR 2, Manager Bagian KSA, PJLAKSK4    Monitor pekerjaan di UL / Cuma bisa View / Cetak
                                    // 5   Manager Unit Pelaksana UPDK Barito  Monitor pekerjaan di UL / Cuma bisa View / Cetak

                                    // CLOSING
                                    // 1   PJ LAKSK4 (ULPLTD/G Trisakti, atau ULPLTA/D Gunung Bamega, atau ULPLTD Tambun Bungai)   Pengajuan / Cetak
                                    // 2   PJ LAKSLK (ULPLTD/G Trisakti, atau ULPLTA/D Gunung Bamega, atau ULPLTD Tambun Bungai)   Tolak / Setuju / Cetak
                                    // 3   Manager Unit Layanan (ULPLTD/G Trisakti, atau ULPLTA/D Gunung Bamega, atau ULPLTD Tambun Bungai)    Tolak / Setuju / Cetak
                                    // 4   Manager Bagian OPHAR 1, Manager Bagian OPHAR 2, Manager Bagian KSA, PJLAKSK4    Monitor pekerjaan di UL / Cuma bisa View / Cetak
                                    // 5   Manager Unit Pelaksana UPDK Barito  Monitor pekerjaan di UL / Cuma bisa View / Cetak

                                    if (Helpers.checkAccess(context, "PJLAKSK4", lokasiKode)){
                                        if (izin.getStatus().equals("diterima")) {
                                            showButtonClosing = View.VISIBLE;
                                        } else if (key == 0) {
                                            showLayoutAksi = View.VISIBLE;
                                            showLayoutTerima = View.VISIBLE;
                                        }
                                    } else if (Helpers.checkAccess(context, "MANAGER", lokasiKode)){
                                        if (key == 1) {
                                            showLayoutAksi = View.VISIBLE;
                                            showLayoutTerima = View.VISIBLE;
                                        } else if (key == 6){
                                            showLayoutAksi = View.VISIBLE;
                                            showLayoutApproval = View.VISIBLE;
                                        } else if (key == 2 && Helpers.containString(izin.getStatus(), "pekerjaan_diajukan", "pekerjaan_ditolak")){
                                            showLayoutAksi = View.VISIBLE;
                                            showLayoutApproval = View.VISIBLE;
                                        }
                                    } else if (Helpers.checkAccess(context, "PJLAKSKLK", lokasiKode) && key == 2 && Helpers.containString(izin.getStatus(), "pekerjaan_diajukan", "pekerjaan_ditolak")){
                                        showLayoutAksi = View.VISIBLE;
                                        showLayoutApproval = View.VISIBLE;
                                    }

                                    if (Helpers.containString(izin.getStatus(), "diterima", "pekerjaan_diterima", "pekerjaan_selesai")){
                                        showLayoutAksi = View.VISIBLE;
                                        showLayoutApproval = View.GONE;
                                        showLayoutTerima = View.GONE;
                                        showButtonCetak = View.VISIBLE;
                                    }
                                }

                                String namaPenolak = null;
                                for (IzinPersetujuan ip: izin.getIzinPersetujuanList()) {
                                    if (Helpers.containString(ip.getStatus(), "ditolak", "pekerjaan_ditolak") && ip.getJabatanKode() != null){
                                        namaPenolak = ": "+ip.getJabatanKode();
                                        break;
                                    }
                                }

                                if (Helpers.containString(izin.getStatus(), "diterima", "selesai", "pekerjaan_diterima", "pekerjaan_selesai")){
                                    showLayoutAksi = View.VISIBLE;
                                    showLayoutApproval = View.GONE;
                                    showLayoutTerima = View.GONE;
                                    showButtonCetak = View.VISIBLE;
                                } else if (Helpers.containString(izin.getStatus(), "ditolak")){
                                    chipStatus.setText("Ditolak" + namaPenolak);
                                    chipStatus.setChipBackgroundColorResource(R.color.red);
                                    chipStatus.setTextColor(context.getResources().getColor(android.R.color.white));
//                                        showLayoutAksi = View.GONE;
                                    showLayoutTerima = View.GONE;
                                } else if (Helpers.containString(izin.getStatus(), "pekerjaan_ditolak")){
                                    chipStatus.setText("Pekerjaan Ditolak" + namaPenolak);
                                    chipStatus.setChipBackgroundColorResource(R.color.red);
                                    chipStatus.setTextColor(context.getResources().getColor(android.R.color.white));
//                                        showLayoutAksi = View.GONE;
                                    showLayoutApproval = View.GONE;
                                }
                            }
                            if (izin.getPenggunaId() == pengguna.getId() && (Helpers.containString(izin.getStatus(), "baru", "proses", "ditolak", "pekerjaan_ditolak"))){
                                showLayoutAksi = View.VISIBLE;
                                showButtonEdit = View.VISIBLE;
                            }

                            if (Helpers.contains(View.VISIBLE, new int[]{showLayoutAksi, showLayoutTerima, showLayoutApproval, showButtonEdit, showButtonClosing, showButtonCetak})){
                                layoutCardView.setVisibility(View.VISIBLE);
                            }

                            layoutAksi.setVisibility(showLayoutAksi);
                            layoutTerima.setVisibility(showLayoutTerima);
                            layoutApproval.setVisibility(showLayoutApproval);
                            buttonEdit.setVisibility(showButtonEdit);
                            buttonClosing.setVisibility(showButtonClosing);
                            buttonCetak.setVisibility(showButtonCetak);

                            if (izin.getDireksiLapanganPengguna() != null) {
                                textAtasan.setText(izin.getDireksiLapanganPengguna().getNama());
                                if (izin.getDireksiLapanganPengguna().getEmail() != null){
                                    textAtasanEmail.setVisibility(View.VISIBLE);
                                    textAtasanEmail.setText(izin.getDireksiLapanganPengguna().getEmail());
                                }
                            } else if (izin.getDireksiLapangan() != null) {
                                textAtasan.setText(izin.getDireksiLapangan());
                            } else if (izin.getPengawasVendor() != null) {
                                textAtasan.setText(izin.getPengawasVendor());
                            }

                            if (izin.getJenis().equals("internal")) layoutNoSpk.setVisibility(View.GONE);
                            else layoutPekerja.setVisibility(View.GONE);

                            izinDetailList = izin.getIzinDetailList();
                            listDokumen = new ArrayList<>();
                            listKlasifikasi = new ArrayList<>();
                            listPeralatan = new ArrayList<>();
                            listPekerja = new ArrayList<>();
                            List<String> listPekerjaGroup = new ArrayList<>();
                            HashMap<String, List<String>> mapPekerjaDetail = new HashMap<>();
                            tableLayoutPekerja.removeAllViews();
                            for (IzinDetail izinDetail: izinDetailList) {
                                if (izinDetail.getDokumenNama() != null){
                                    listDokumen.add(izinDetail);
                                } else if (izinDetail.getPeralatanNama() != null){
                                    listPeralatan.add(izinDetail);
                                } else if (izinDetail.getPekerjaanNama() != null){
                                    listKlasifikasi.add(izinDetail);
                                } else if (izinDetail.getPenggunaNama() != null || izinDetail.getPekerja() != null){
                                    listPekerjaGroup.add(izinDetail.getPekerja());
                                    TableRow tr = new TableRow(context);
                                    tr.setLayoutParams(new TableLayout.LayoutParams( TableLayout.LayoutParams.FILL_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));
                                    tr.setGravity(Gravity.END);
                                    TextView textview = new TextView(context);
                                    textview.setText(izinDetail.getPekerja());
                                    tr.addView(textview);
                                    tableLayoutPekerja.addView(tr);

//                                    listPekerja.add(izinDetail);
                                }
                            }

                            tampilCatatan = "";
                            for (IzinPersetujuan persetujuan : izin.getIzinPersetujuanList()){
                                if (Helpers.containIgnoreCase(persetujuan.getStatus(), "ditolak", "pekerjaan_ditolak") && persetujuan.getCatatan() != null){
                                    tampilCatatan += persetujuan.getPenggunaName()+": "+ persetujuan.getCatatan();
                                    tampilCatatan += System.getProperty ("line.separator");
                                }
                            }
                            if (tampilCatatan.trim().length() > 0){
                                layoutCatatan.setVisibility(View.VISIBLE);
                                textViewCatatan.setText(tampilCatatan);
                            } else {
                                layoutCatatan.setVisibility(View.GONE);
                            }
//                            mapPekerjaDetail.put("Pekerja", listPekerjaGroup);
//                            List<String> expandableListTitle = new ArrayList<>();
//                            expandableListTitle.add("Pekerja");
//                            ListIzinDetailPekerjaExpandableListAdapter adapterPekerja = new ListIzinDetailPekerjaExpandableListAdapter(context, expandableListTitle, mapPekerjaDetail);
//                            expandableListViewPekerja.setAdapter(adapterPekerja);

                            adapter = new ListIzinDetailRecyclerView(context, listKlasifikasi, liztIzinDetailListener, 1);
                            recyclerViewKlasifikasi.setAdapter(adapter);
                            recyclerViewKlasifikasi.setLayoutManager(new LinearLayoutManager(context));

                            adapter = new ListIzinDetailRecyclerView(context, listPeralatan, liztIzinDetailListener, 2);
                            recyclerViewApd.setAdapter(adapter);
                            recyclerViewApd.setLayoutManager(new LinearLayoutManager(context));

                            adapter = new ListIzinDetailRecyclerView(context, listDokumen, liztIzinDetailListener, 3);
                            recyclerViewDokumen.setAdapter(adapter);
                            recyclerViewDokumen.setLayoutManager(new LinearLayoutManager(context));

//                            showButtonAction();
                        }
                    }

                }
            }

            @Override
            public void onFailure(Call<ResponseIzin> call, Throwable t) {

            }
        });
    }
    private ListIzinDetailRecyclerView.OnItemClickListener liztIzinDetailListener = new ListIzinDetailRecyclerView.OnItemClickListener() {
        @Override
        public void onItemClick(View itemView, int position, int kode) {
            IzinDetail izinDetail = null;
            String sanitizeFileName = "";
            String kodeDetail = "";
            if (kode == 1){
                kodeDetail = "klasifikasi";
                izinDetail = listKlasifikasi.get(position);
                sanitizeFileName = Helpers.sanitizeFilename(izinDetail.getGambarNama());
            } else if (kode == 2){
                kodeDetail = "peralatan";
                izinDetail = listPeralatan.get(position);
                sanitizeFileName = Helpers.sanitizeFilename(izinDetail.getPeralatanNama());
            } else if (kode == 3){
                kodeDetail = "dokumen";
                izinDetail = listDokumen.get(position);
                sanitizeFileName = Helpers.sanitizeFilename(izinDetail.getDokumenNama());
            }
            if (izinDetail != null && izinDetail.getGambarUrl() != null){
                String extension = FilenameUtils.getExtension(izinDetail.getGambarUrl());
                Log.d(TAG, "izinDetail:extension:"+extension);
                Log.d(TAG, "izinDetail:url:"+Helpers.getUploadUrl(context, izinDetail.getGambarUrl()));
                if (Helpers.containString(extension.toLowerCase(), "jpg", "png", "jpeg", "gif", "bmp", "webp")){
                    final Dialog settingsDialog = new Dialog(context);
                    settingsDialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
                    View view = getLayoutInflater().inflate(R.layout.dialog_image, null);
                    final ImageView imageView = view.findViewById(R.id.image_view);
                    if (kode == 1) imageView.setVisibility(View.GONE);
                    else  {
                        Glide.with(context)
                                .asBitmap()
                                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                                .load(Helpers.getUploadUrl(context, izinDetail.getGambarUrl()))
                                .into(new SimpleTarget<Bitmap>() {
                                    @Override
                                    public void onResourceReady(Bitmap bitmap, Transition<? super Bitmap> transition) {
                                        imageView.setImageBitmap(bitmap);
                                    }
                                });
                        settingsDialog.setContentView(view);
                        settingsDialog.show();

                        view.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                settingsDialog.dismiss();
                            }
                        });
                    }
                } else {
                    DownloadManager downloadmanager = null;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
                        downloadmanager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
                        Uri uri = Uri.parse(Helpers.getUploadUrl(context, izinDetail.getGambarUrl()));

                        String fileNameWithOutExt = FilenameUtils.removeExtension(uri.getLastPathSegment());
                        String fileName = kodeDetail + "_" + sanitizeFileName + "_" + izin.getId() + "_" + izinDetail.getId() + "." + extension;

                        File oldFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), fileName);
                        if(oldFile.exists()){
                            oldFile.delete();
                        }

                        DownloadManager.Request request = new DownloadManager.Request(uri);
                        request.setTitle(fileName);
                        request.setDescription(izinDetail.getDokumenNama());
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                        }
                        request.setVisibleInDownloadsUi(true);
//                    request.setDestinationUri(Uri.parse("file://" + folderName + "/myfile.mp3"));
                        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, fileName);

                        if (downloadmanager != null){
                            long result = downloadmanager.enqueue(request);
                        }
                    }
                }
            }
        }
    };

    @Override
    public void onClick(View v) {
        if (v.getId() == imageBack.getId()){
            kembaliAction(v);
        } else if (v.getId() == buttonTerima.getId()){
            izinAksesAction("diterima");
        } else if (v.getId() == buttonTolak.getId()){
            tolakCatatan(v, false);
        } else if (v.getId() == buttonTerimaPekerjaan.getId()){
            izinAksesAction("pekerjaan_diterima");
        } else if (v.getId() == buttonTolakPekerjaan.getId()){
            tolakCatatan(v, true);
        } else if (v.getId() == buttonClosing.getId()){
            izinAksesAction("pekerjaan_diajukan");
        } else if (v.getId() == buttonEdit.getId()){
            finish();
            intent = new Intent(context, IzinTambahActivity.class);
            intent.putExtra("jenis", jenis);
            intent.putExtra("izin", izin);
            intent.putExtra("izin_id", izinId);
            intent.putExtra("edit", true);
            startActivity(intent);
        } else if (v.getId() == buttonCetak.getId()){
            cetakAction(v);
        }
    }

    private void izinAksesAction(final String aksi) {
        layoutProgressBar.setVisibility(View.VISIBLE);
        HashMap<String, String> map = new HashMap<>();
        map.put("jenis", jenis);
        map.put("aksi", aksi);
        if (catatan.length() > 0) map.put("catatan", catatan);
        izinService.akses(izinId, map).enqueue(new Callback<ResponseIzin>() {
            @Override
            public void onResponse(Call<ResponseIzin> call, Response<ResponseIzin> response) {
                layoutProgressBar.setVisibility(View.GONE);
                if (response.isSuccessful()){
                    if (response.body() != null){
                        if (aksi.equals("diterima")) Snackbar.make(findViewById(android.R.id.content), "Berhasil menerima izin", Snackbar.LENGTH_LONG).show();
                        else if (aksi.equals("ditolak")) Snackbar.make(findViewById(android.R.id.content), "Berhasil menolak izin", Snackbar.LENGTH_LONG).show();
                        else if (aksi.equals("pekerjaan_diterima")) Snackbar.make(findViewById(android.R.id.content), "Berhasil menerima closing pekerjaan", Snackbar.LENGTH_LONG).show();
                        else if (aksi.equals("pekerjaan_ditolak")) Snackbar.make(findViewById(android.R.id.content), "Berhasil menolak closing pekerjaan", Snackbar.LENGTH_LONG).show();
                        else if (aksi.equals("pekerjaan_diajukan")) Snackbar.make(findViewById(android.R.id.content), "Berhasil mengajukan closing pekerjaan", Snackbar.LENGTH_LONG).show();

                        kembaliAction(null);
                    }
                }

            }

            @Override
            public void onFailure(Call<ResponseIzin> call, Throwable t) {
                t.printStackTrace();
                layoutProgressBar.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        kembaliAction(null);
    }

    private void kembaliAction(View v) {
        finish();
        intent = new Intent(context, MainActivity.class);
        intent.putExtra("jenis", jenis);
        intent.putExtra("status", status);
        startActivity(intent);
    }

    private void tolakCatatan(View v, final boolean pekerjaan) {
        final Dialog customDialog = new Dialog(context);
        customDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        customDialog.setContentView(R.layout.dialog_note);
        customDialog.setCancelable(true);

        catatan = "";

        final TextInputEditText textCatatan = customDialog.findViewById(R.id.text_catatan);
        MaterialButton buttonTolak = customDialog.findViewById(R.id.button_tolak);
        MaterialButton buttonBatal = customDialog.findViewById(R.id.button_batal);
        final TextView textKeterangan = customDialog.findViewById(R.id.text_keterangan);
        textKeterangan.setVisibility(View.GONE);

        buttonBatal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textCatatan.setText("");
                customDialog.dismiss();
            }
        });

        buttonTolak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                catatan = textCatatan.getText().toString();

                if (catatan.length() <= 0){
                    textKeterangan.setText("Catatan harus diisi");
                    textKeterangan.setVisibility(View.VISIBLE);
                    textCatatan.requestFocus();
                    return;
                }
                customDialog.dismiss();
                if (pekerjaan) izinAksesAction("pekerjaan_ditolak");
                else izinAksesAction("ditolak");
            }
        });

        customDialog.show();
    }

    private void cetakAction(View view) {
        finish();
        intent = new Intent(context, CetakActivity.class);
        if (izin != null){
            intent.putExtra("izin", izin);
            intent.putExtra("izin_id", izin.getId());
        } else {
            intent.putExtra("izin_id", izinId);
        }
        intent.putExtra("jenis", jenis);
        intent.putExtra("status", status);
        startActivity(intent);
    }
}
