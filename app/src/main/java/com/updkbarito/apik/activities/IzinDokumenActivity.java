package com.updkbarito.apik.activities;

import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.FileUtils;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.documentfile.provider.DocumentFile;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bugsnag.android.Bugsnag;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.google.gson.Gson;
import com.updkbarito.apik.BuildConfig;
import com.updkbarito.apik.R;
import com.updkbarito.apik.adapters.ListDokumenRecyclerView;
import com.updkbarito.apik.entities.Dokumen;
import com.updkbarito.apik.entities.Izin;
import com.updkbarito.apik.entities.IzinDetail;
import com.updkbarito.apik.services.DokumenService;
import com.updkbarito.apik.services.IzinDetailService;
import com.updkbarito.apik.services.IzinService;
import com.updkbarito.apik.services.ResponseDokumenList;
import com.updkbarito.apik.services.ResponseIzin;
import com.updkbarito.apik.services.ResponseIzinDetailList;
import com.updkbarito.apik.utils.Helpers;

import org.apache.commons.io.FilenameUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class IzinDokumenActivity extends AppCompatActivity implements View.OnClickListener {

    private final String TAG = IzinDokumenActivity.class.getSimpleName();
    private ImageView imageBack;
    private View layoutProgressBar;
    private Button buttonAjukan;
    private IzinDokumenActivity context;
    private Intent intent;
    private String jenis;
    private Izin izin = new Izin();
    private int izinId = 0;
    private Retrofit retrofit;
    private DokumenService dokumenService;
    private IzinDetailService izinDetailService;
    private List<Dokumen> listDokumen = new ArrayList<>();
    private List<IzinDetail> listIzinDetail = new ArrayList<>();
    private List<Integer> dokumenIds = new ArrayList<>();
    private RecyclerView recyclerView;
    private int checked = 0;
    private int processed = 0;
    private ListDokumenRecyclerView adapter;
    private IzinService izinService;
    private boolean edit = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_izin_dokumen);
        FirebaseCrashlytics.getInstance().setCrashlyticsCollectionEnabled(true);

        imageBack = findViewById(R.id.image_back);
        recyclerView = findViewById(R.id.recycler_view);
        buttonAjukan = findViewById(R.id.button_ajukan);
        layoutProgressBar = findViewById(R.id.layout_progress_bar);

        context = this;

        intent = getIntent();
        edit = intent.getBooleanExtra("edit", false);
        jenis = intent.getStringExtra("jenis");
        izin = intent.getParcelableExtra("izin");
        izinId = intent.getIntExtra("izin_id", 0);

        imageBack.setOnClickListener(context);
        buttonAjukan.setOnClickListener(context);

        retrofit = Helpers.initRetrofit(context);
        dokumenService = retrofit.create(DokumenService.class);
        izinService = retrofit.create(IzinService.class);
        izinDetailService = retrofit.create(IzinDetailService.class);


        HashMap<String, String> map = new HashMap<>();
        map.put("jenis", jenis+",semua");
        dokumenService.index(map).enqueue(new Callback<ResponseDokumenList>() {
            @Override
            public void onResponse(Call<ResponseDokumenList> call, Response<ResponseDokumenList> response) {
                if (response.isSuccessful()){
                    if (response.body() != null){
                        listDokumen = response.body().getData();
                        izinDetailService.dokumenList(izinId, new HashMap<String, String>()).enqueue(new Callback<ResponseIzinDetailList>() {
                            @Override
                            public void onResponse(Call<ResponseIzinDetailList> call, Response<ResponseIzinDetailList> response) {
                                if (response.isSuccessful()){
                                    if (response.body() != null){
                                        listIzinDetail = response.body().getData();

                                        adapter = new ListDokumenRecyclerView(context, listDokumen, listIzinDetail);
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
            public void onFailure(Call<ResponseDokumenList> call, Throwable t) {

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

        layoutProgressBar.setVisibility(View.GONE);
        buttonAjukan.setEnabled(true);

        Helpers.checkApplicationPermission(context);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == imageBack.getId()){
            kembaliAction(v);
        } else if (v.getId() == buttonAjukan.getId()){
            ajukanAction(v);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode >= 400 && resultCode == RESULT_OK && data != null && data.getData() != null) {
            int position = requestCode - 400;
            Dokumen dokumen = listDokumen.get(position);
            Uri uri = data.getData();

            if (uri != null){
                dokumen.setUri(uri);
                listDokumen.set(position, dokumen);
                adapter.notifyItemChanged(position);

//                String path = uri.toString();
//                File file = null;
//                try {
//                    String path = getPath(context, uri);
//                    Log.d(TAG, "path: " + path);
//                    file = new File(path);
//                    Log.d(TAG, "file: " + file.getAbsolutePath());
//                    Log.d(TAG, "file: " + file.getCanonicalPath());
//                } catch (IOException e) {
//                    e.printStackTrace();
//                    FirebaseCrashlytics.getInstance().recordException(e);
//                }
//
//                if (file != null){
//
//                }

            }


        }
    }

    @Override
    public void onBackPressed() {
        kembaliAction(null);
    }

    private void ajukanAction(View v) {
        if (listDokumen == null || listDokumen.size() <= 0) {
            Snackbar.make(findViewById(android.R.id.content), "Pilih Dokumen terlebih dahulu", Snackbar.LENGTH_LONG).show();
            return;
        }
        layoutProgressBar.setVisibility(View.VISIBLE);
        buttonAjukan.setEnabled(false);
        int first = 0;
        checked = 0;
        int skipped = 0;
        for (Dokumen dokumen: listDokumen) {
            Log.d(TAG, "dokumen:"+new Gson().toJson(dokumen));
            if (dokumen.getUri() != null){
                checked++;
                dokumenIds.add(dokumen.getId());
                RequestBody dokumenId = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(dokumen.getId()));
                RequestBody firstLoad = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(first));

                Uri uri = dokumen.getUri();
                String fileName = Helpers.getFileName(context, uri);
                try {
                    fileName = URLEncoder.encode(fileName, "utf-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                Bitmap bitmap = null;
                DocumentFile sourceFile = DocumentFile.fromSingleUri(context, uri);
//                if (sourceFile != null && sourceFile.exists()){
                    String mime = Helpers.getMimeType(context, uri);
                    Log.d(TAG, "dokumen:mime:"+mime);
                    if (Helpers.containString(uri.getLastPathSegment(), "jpg", "png", "jpeg", "gif", "bmp", "webp")) {
                        try {
                            bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);

                            if (bitmap != null) {
                                if (bitmap.getHeight() > 512 || bitmap.getWidth() > 512) {
                                    bitmap = Helpers.resizeBitmap(bitmap, 512);
                                }
//                                File scaledBitmap = Helpers.fileFromBitmap(context, sourceFile.getName(), bitmap, 100);
                                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                                byte[] byteArray = stream.toByteArray();
                                RequestBody reqFile = RequestBody.create(MediaType.parse("image/*"), byteArray);
                                MultipartBody.Part body = MultipartBody.Part.createFormData("upload", fileName, reqFile);
                                izinDetailService.tambahDokumen(izinId, dokumenId, firstLoad, body).enqueue(callbackResponse);
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                            processed++;
                            nextPage(null);
                        }
                    } else {
                        InputStream inputStream = null;
//                        String fileName = sourceFile.getName();
                        try {
                            inputStream = getContentResolver().openInputStream(uri);
                            byte[] inputData = Helpers.getBytes(inputStream);
                            RequestBody reqFile = RequestBody.create(MediaType.parse("*/*"), inputData);
                            MultipartBody.Part body = MultipartBody.Part.createFormData("upload", fileName, reqFile);
                            izinDetailService.tambahDokumen(izinId, dokumenId, firstLoad, body).enqueue(callbackResponse);
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
//                } else {
//                    processed++;
//                    nextPage(null);
//                }

//                if (sourceFile == null){
//                    File file = null;
//                    try {
//                        uri = dokumen.getUri();
//                        Helpers.appendLog(TAG, "File Uri: " + uri.toString(), findViewById(android.R.id.content));
//                        String path = getPath(context, uri);
//                        if (path != null){
//                            Helpers.appendLog(TAG, "File Path: " + path, findViewById(android.R.id.content));
//                            file = new File(path);
//                            Helpers.appendLog(TAG, "file: " + file.getAbsolutePath(), findViewById(android.R.id.content));
//                        }
//                    } catch (Exception e) {
//                        Helpers.appendLog(TAG, "file:e: " + e.getMessage(), findViewById(android.R.id.content));
//                        FirebaseCrashlytics.getInstance().recordException(e);
//                        e.printStackTrace();
//                        Bugsnag.notify(e);
//                    }
//                    if (file != null){
//                        String extension = FilenameUtils.getExtension(file.getAbsolutePath());
//                        Helpers.appendLog(TAG, "extension: " + extension, findViewById(android.R.id.content));
//                        FirebaseCrashlytics.getInstance().log(TAG + ": " + "extension: " + extension);
//                        if (Helpers.containString(extension.toLowerCase(), "jpg", "png", "jpeg", "gif", "bmp", "webp")){
//                            bitmap = BitmapFactory.decodeFile(file.getAbsolutePath());
//                            if (bitmap != null){
//                                if (bitmap.getHeight() > 2048 || bitmap.getWidth() > 2048){
//                                    bitmap = Helpers.resizeBitmap(bitmap, 2048);
//                                }
//                            }
//                            File scaledBitmap = Helpers.fileFromBitmap(context, file.getName(), bitmap, 95);
//                            RequestBody reqFile = RequestBody.create(MediaType.parse("image/*"), scaledBitmap);
//                            MultipartBody.Part body = MultipartBody.Part.createFormData("upload", file.getName(), reqFile);
//                            izinDetailService.tambahDokumen(izinId, dokumenId, firstLoad, body).enqueue(callbackResponse);
//                        } else {
//                            RequestBody reqFile = RequestBody.create(MediaType.parse("*/*"), file);
//                            MultipartBody.Part body = MultipartBody.Part.createFormData("upload", file.getName(), reqFile);
//                            izinDetailService.tambahDokumen(izinId, dokumenId, firstLoad, body).enqueue(callbackResponse);
//                        }
//                    } else {
//                        processed++;
//                        nextPage(null);
//                    }
//                    first = 0;
//                }
            } else if (dokumen.getGambarId() > 0){
                checked++;
                processed++;
                dokumenIds.add(dokumen.getId());
                nextPage(null);
            }
        }
        if (checked <= 0){
            Snackbar.make(findViewById(android.R.id.content), "Pilih Dokumen terlebih dahulu", Snackbar.LENGTH_LONG).show();
            layoutProgressBar.setVisibility(View.GONE);
            buttonAjukan.setEnabled(true);
        }
    }
    private Callback<ResponseIzinDetailList> callbackResponse = new Callback<ResponseIzinDetailList>() {
        @Override
        public void onResponse(Call<ResponseIzinDetailList> call, Response<ResponseIzinDetailList> response) {
            Helpers.appendLog(TAG, "response:dokumen: " + response.raw().toString(), findViewById(android.R.id.content));
            if (response.body() != null){
                Helpers.appendLog(TAG, "response:dokumen:body: " + response.body().toString(), findViewById(android.R.id.content));
            }
            processed++;
            nextPage(null);
        }

        @Override
        public void onFailure(Call<ResponseIzinDetailList> call, Throwable t) {
            processed++;
            nextPage(null);
            t.printStackTrace();
            Helpers.appendLog(TAG, "response:dokumen:failure: " + t.getMessage(), findViewById(android.R.id.content));
            FirebaseCrashlytics.getInstance().recordException(t);
            Bugsnag.notify(t);
        }
    };

    private boolean validation() {
        return true;
    }

    private void nextPage(View v) {
        Log.d(TAG, "nextPage:"+checked+","+processed);
        if (checked == processed) {
            HashMap<String, String> map = new HashMap<>();
            map.put("dokumen_id", TextUtils.join(",", dokumenIds));
            izinDetailService.hapusIzinDetailExcept(izinId, map).enqueue(new Callback<ResponseIzinDetailList>() {
                @Override
                public void onResponse(Call<ResponseIzinDetailList> call, Response<ResponseIzinDetailList> response) {
                    prosesAction();
                }

                @Override
                public void onFailure(Call<ResponseIzinDetailList> call, Throwable t) {
                    prosesAction();
                }
            });
        }
    }

    private void prosesAction() {
        izinService.proses(izinId, new HashMap<String, String>()).enqueue(new Callback<ResponseIzin>() {
            @Override
            public void onResponse(Call<ResponseIzin> call, Response<ResponseIzin> response) {
                layoutProgressBar.setVisibility(View.GONE);
                buttonAjukan.setEnabled(true);
//                if (response.isSuccessful()){
                finish();
                intent = new Intent(context, MainActivity.class);
                intent.putExtra("message", "Berhasil Memproses Izin");
                intent.putExtra("jenis", jenis);
                intent.putExtra("izin_id", izinId);
                startActivity(intent);
//                }
            }

            @Override
            public void onFailure(Call<ResponseIzin> call, Throwable t) {
                layoutProgressBar.setVisibility(View.GONE);
                buttonAjukan.setEnabled(true);

                finish();
                intent = new Intent(context, MainActivity.class);
                intent.putExtra("message", "Berhasil Memproses Izin");
                intent.putExtra("jenis", jenis);
                intent.putExtra("izin_id", izinId);
                startActivity(intent);
                FirebaseCrashlytics.getInstance().recordException(t);
            }
        });
    }

    private void kembaliAction(View v) {
        finish();
        intent = new Intent(context, IzinApdActivity.class);
        intent.putExtra("edit", edit);
        intent.putExtra("jenis", jenis);
        intent.putExtra("izin", izin);
        intent.putExtra("izin_id", izinId);
        startActivity(intent);
    }

    /**
     * Get a file path from a Uri. This will get the the path for Storage Access
     * Framework Documents, as well as the _data field for the MediaStore and
     * other file-based ContentProviders.
     *
     * @param context The context.
     * @param uri The Uri to query.
     * @author paulburke
     */
    public static String getPath(final Context context, final Uri uri) {

        // DocumentProvider
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {
                final String id = DocumentsContract.getDocumentId(uri);

                if (id != null && id.startsWith("raw:")) {
                    return id.substring(4);
                }

                String[] contentUriPrefixesToTry = new String[]{
                        "content://downloads/public_downloads",
                        "content://downloads/my_downloads",
                        "content://downloads/all_downloads"
                };

                Uri contentUri = null;
                for (String contentUriPrefix : contentUriPrefixesToTry) {
                    try {
                        contentUri = ContentUris.withAppendedId(Uri.parse(contentUriPrefix), Long.valueOf(id));
                        String path = getDataColumn(context, contentUri, null, null);
                        if (path != null) {
                            return path;
                        }
                    } catch (Exception e) {
                        FirebaseCrashlytics.getInstance().recordException(e);
                    }
                }
                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[] {
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context The context.
     * @param uri The Uri to query.
     * @param selection (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }


    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }
}
