package com.updkbarito.apik.activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.collection.ArraySet;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.documentfile.provider.DocumentFile;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bugsnag.android.Bugsnag;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.updkbarito.apik.R;
import com.updkbarito.apik.adapters.ListApdRecyclerView;
import com.updkbarito.apik.entities.Izin;
import com.updkbarito.apik.entities.IzinDetail;
import com.updkbarito.apik.entities.Pekerjaan;
import com.updkbarito.apik.entities.PekerjaanPeralatan;
import com.updkbarito.apik.entities.Pengguna;
import com.updkbarito.apik.entities.Peralatan;
import com.updkbarito.apik.services.IzinDetailService;
import com.updkbarito.apik.services.PeralatanService;
import com.updkbarito.apik.services.ResponseIzinDetailList;
import com.updkbarito.apik.services.ResponsePekerjaanPeralatanList;
import com.updkbarito.apik.services.ResponsePeralatanList;
import com.updkbarito.apik.utils.Helpers;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class IzinApdActivity extends AppCompatActivity implements View.OnClickListener {

    private final String TAG = IzinApdActivity.class.getSimpleName();
    private ImageView imageBack;
    private View layoutProgressBar;
    private Button buttonLanjut;
    private IzinApdActivity context;
    private Intent intent;
    private String jenis;
    private Izin izin = new Izin();
    private Retrofit retrofit;
    private PeralatanService peralatanService;
    private RecyclerView recyclerView;
    private ListApdRecyclerView adapter;
    private IzinDetailService izinDetailService;
    private int izinId = 0;
    private List<Peralatan> listPeralatan = new ArrayList<>();
    private List<Peralatan> listKlasifikasi = new ArrayList<>();
    private ArrayList<PekerjaanPeralatan> listPekerjaanPeralatan = new ArrayList<>();
    private List<IzinDetail> listIzindetail = new ArrayList<>();
    private int checked = 0;
    private int processed = 0;
    private boolean edit = false;
    private IzinApdActivity activity;
    private List<Integer> peralatanIds = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_izin_apd);

        imageBack = findViewById(R.id.image_back);
        buttonLanjut = findViewById(R.id.button_lanjut);
        recyclerView = findViewById(R.id.recycler_view);
        layoutProgressBar = findViewById(R.id.layout_progress_bar);

        context = this;
        activity = this;

        Bugsnag.start(this);

        FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(true);

        intent = getIntent();
        edit = intent.getBooleanExtra("edit", false);
        jenis = intent.getStringExtra("jenis");
        izin = intent.getParcelableExtra("izin");
        izinId = intent.getIntExtra("izin_id", 0);

        imageBack.setOnClickListener(context);
        buttonLanjut.setOnClickListener(context);

        retrofit = Helpers.initRetrofit(context);
        peralatanService = retrofit.create(PeralatanService.class);
        izinDetailService = retrofit.create(IzinDetailService.class);

        izinDetailService.peralatanList(izinId, new HashMap<String, String>()).enqueue(new Callback<ResponseIzinDetailList>() {
            @Override
            public void onResponse(Call<ResponseIzinDetailList> call, Response<ResponseIzinDetailList> response) {
                if (response.isSuccessful()){
                    if (response.body() != null){
                        listIzindetail = response.body().getData();

                        HashMap<String, String> map = new HashMap<>();
                        String pekerjaanIdList = "";
                        for (Peralatan peralatan: listKlasifikasi) {
                            if (peralatan.getId() > 0) pekerjaanIdList += peralatan.getId();
                            if (listKlasifikasi.indexOf(peralatan) < listKlasifikasi.size() - 1) pekerjaanIdList += ",";
                        }
                        if (pekerjaanIdList.length() > 0) map.put("pekerjaan_id", pekerjaanIdList);
                        map.put("izin_id", String.valueOf(izinId));
                        peralatanService.pekerjaan(map).enqueue(new Callback<ResponsePekerjaanPeralatanList>() {
                            @Override
                            public void onResponse(Call<ResponsePekerjaanPeralatanList> call, Response<ResponsePekerjaanPeralatanList> response) {
                                if (response.isSuccessful()){
                                    if (response.body() != null){
                                        listPekerjaanPeralatan = response.body().getData();

                                        adapter = new ListApdRecyclerView(context, listPekerjaanPeralatan, listIzindetail, activity);
                                        recyclerView.setAdapter(adapter);
                                        recyclerView.setLayoutManager(new LinearLayoutManager(context));
                                    }
                                }
                            }

                            @Override
                            public void onFailure(Call<ResponsePekerjaanPeralatanList> call, Throwable t) {
                                Bugsnag.notify(t);
                            }
                        });
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseIzinDetailList> call, Throwable t) {
                Bugsnag.notify(t);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        layoutProgressBar.setVisibility(View.GONE);
        buttonLanjut.setEnabled(true);
        Helpers.checkApplicationPermission(context);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult:"+requestCode+", "+resultCode+", "+data);

        if (requestCode >= 200 && resultCode == RESULT_OK) {
            int position = requestCode - 200;
            Bitmap bitmap = null;
            Uri filePath = null;
            if (data != null && data.getData() != null) {
                Log.d(TAG, "onActivityResult:if:");
                filePath = data.getData();
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);

                    if (bitmap != null) {
                        Log.d(TAG, "resizeBitmap:onActivityResult:" + bitmap.getHeight() + "," + bitmap.getWidth());
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    Bugsnag.notify(e);
                }
            } else if (data != null && data.getExtras() != null){
                Log.d(TAG, "onActivityResult:else if:");
                try {
                    bitmap = (Bitmap) data.getExtras().get("data");
                    if (bitmap != null) filePath = Helpers.getImageUri(context, bitmap);

                    if (bitmap != null) {
                        Log.d(TAG, "resizeBitmap:onActivityResult:" + bitmap.getHeight() + "," + bitmap.getWidth());
                    }
                } catch (NullPointerException e) {
                    e.printStackTrace();
                    Bugsnag.notify(e);
                } catch (IndexOutOfBoundsException e) {
                    e.printStackTrace();
                    Bugsnag.notify(e);
                }
            } else {
                Log.d(TAG, "onActivityResult:else:");
            }

            if (listPekerjaanPeralatan.size() > position){
                PekerjaanPeralatan peralatan = listPekerjaanPeralatan.get(position);
                if (peralatan.getFile() != null){
                    filePath = Uri.fromFile(peralatan.getFile());
                } else if (bitmap != null){
                    if (bitmap.getHeight() > 512 || bitmap.getWidth() > 512){
                        bitmap = Helpers.resizeBitmap(bitmap, 512);
                    }
                    Log.d(TAG, "resizeBitmap:onActivityResult:"+bitmap.getHeight()+","+bitmap.getWidth());
                    peralatan.setBitmap(bitmap);
                }
//                peralatan.setCheck(true);
                if (filePath != null) peralatan.setUri(filePath);
                listPekerjaanPeralatan.set(position, peralatan);
                adapter.notifyItemChanged(position);
            }
        }
    }

    private void lanjutAction(View v) {
        if (listPekerjaanPeralatan == null || listPekerjaanPeralatan.size() <= 0){
            Snackbar.make(findViewById(android.R.id.content), "Pilih APD terlebih dahulu", Snackbar.LENGTH_LONG).show();
            return;
        }

        int first = 0;
        checked = 0;
        processed = 0;
        for (PekerjaanPeralatan pp: listPekerjaanPeralatan) {
            if (pp.isCheck()) {
                layoutProgressBar.setVisibility(View.VISIBLE);
                buttonLanjut.setEnabled(false);
                checked++;
                peralatanIds.add(pp.getPeralatanId());
                RequestBody peralatanId = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(pp.getPeralatanId()));
                RequestBody firstLoad = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(first));
//                File file = null;
                first = 0;
                if (pp.getUri() != null){
//                    file = new File(getRealPathFromURI(context, pp.getUri()));
//                    Log.d(TAG, "path:"+pp.getUri().getPath()+" file:"+file.getAbsolutePath());
                    // upload file
                    InputStream inputStream = null;
//                    DocumentFile sourceFile = DocumentFile.fromSingleUri(context, pp.getUri());
//                    if (sourceFile != null && sourceFile.exists()){
                        try {
                            inputStream = getContentResolver().openInputStream(pp.getUri());
                            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                            String fileName = Helpers.getFileName(context, pp.getUri());
                            try {
                                fileName = URLEncoder.encode(fileName, "utf-8");
                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            }
                            if (bitmap != null){
                                if (bitmap.getHeight() > 512 || bitmap.getWidth() > 512){
                                    bitmap = Helpers.resizeBitmap(bitmap, 512);
                                } else {
                                    Log.d(TAG, "resizeBitmap:"+bitmap.getHeight()+","+bitmap.getWidth());
                                }
//                            File scaledBitmap = Helpers.fileFromBitmap(context, file.getName(), bitmap, 90);
                                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                                byte[] byteArray = stream.toByteArray();
                                RequestBody reqFile = RequestBody.create(MediaType.parse("image/*"), byteArray);
                                MultipartBody.Part body = MultipartBody.Part.createFormData("upload", fileName, reqFile);
                                izinDetailService.tambahPeralatan(izinId, peralatanId, firstLoad, body).enqueue(callbackResponse);
                            } else {
                                Bugsnag.notify(new RuntimeException("Peralatan: "+pp.getPeralatanId()+" image null"));
                                izinDetailService.tambahPeralatan(izinId, peralatanId, firstLoad, null).enqueue(callbackResponse);
                            }
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
//                    }
                } else {
                    izinDetailService.tambahPeralatan(izinId, peralatanId, firstLoad, null).enqueue(callbackResponse);
                }
            }
        }

        if (checked == 0){
            Snackbar.make(findViewById(android.R.id.content), "Pilih APD terlebih dahulu", Snackbar.LENGTH_LONG).show();
        }
    }

    private void nextPage(){
        Log.d(TAG, "checked:"+checked+" processed:"+processed);
        if (checked == processed && checked > 0){
            HashMap<String, String> map = new HashMap<>();
            map.put("peralatan_id", TextUtils.join(",", peralatanIds));
            izinDetailService.hapusIzinDetailExcept(izinId, map).enqueue(new Callback<ResponseIzinDetailList>() {
                @Override
                public void onResponse(Call<ResponseIzinDetailList> call, Response<ResponseIzinDetailList> response) {
                    layoutProgressBar.setVisibility(View.GONE);
                    buttonLanjut.setEnabled(true);
                    finish();
                    intent = new Intent(context, IzinDokumenActivity.class);
                    intent.putExtra("edit", edit);
                    intent.putExtra("jenis", jenis);
                    intent.putExtra("izin", izin);
                    intent.putExtra("izin_id", izinId);
                    startActivity(intent);
                }

                @Override
                public void onFailure(Call<ResponseIzinDetailList> call, Throwable t) {
                    layoutProgressBar.setVisibility(View.GONE);
                    buttonLanjut.setEnabled(true);
                    finish();
                    intent = new Intent(context, IzinDokumenActivity.class);
                    intent.putExtra("edit", edit);
                    intent.putExtra("jenis", jenis);
                    intent.putExtra("izin", izin);
                    intent.putExtra("izin_id", izinId);
                    startActivity(intent);
                }
            });
        } else if (checked == 0){
            layoutProgressBar.setVisibility(View.GONE);
            buttonLanjut.setEnabled(true);
            Helpers.showShortSnackbar(findViewById(android.R.id.content), "Pilih APD Terlebih dahulu");
        }
    }

    private void kembaliAction(View v) {
        finish();
        intent = new Intent(context, IzinKlasifikasiActivity.class);
        intent.putExtra("edit", edit);
        intent.putExtra("jenis", jenis);
        intent.putExtra("izin", izin);
        intent.putExtra("izin_id", izinId);
        startActivity(intent);
    }

    private Callback<ResponseIzinDetailList> callbackResponse = new Callback<ResponseIzinDetailList>() {
        @Override
        public void onResponse(Call<ResponseIzinDetailList> call, Response<ResponseIzinDetailList> response) {
//            if (response.isSuccessful()){
                processed++;
                nextPage();
//            }
        }

        @Override
        public void onFailure(Call<ResponseIzinDetailList> call, Throwable t) {
            processed++;
            t.printStackTrace();
            layoutProgressBar.setVisibility(View.GONE);
            buttonLanjut.setEnabled(true);
            nextPage();
            FirebaseCrashlytics.getInstance().log(t.getMessage());
            FirebaseCrashlytics.getInstance().recordException(t);
            Bugsnag.notify(t);
        }
    };

    public String getRealPathFromURI(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = {MediaStore.Images.Media.DATA};
            cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } catch (NullPointerException e){
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return null;
    }
}
