package com.updkbarito.apik.utils;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.common.util.ArrayUtils;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.google.gson.Gson;
import com.updkbarito.apik.BuildConfig;
import com.updkbarito.apik.R;
import com.updkbarito.apik.entities.Akses;
import com.updkbarito.apik.entities.Izin;
import com.updkbarito.apik.entities.IzinPersetujuan;
import com.updkbarito.apik.entities.Pengguna;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLConnection;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Helpers {
    private static String TAG = Helpers.class.getSimpleName();
    public static void getKeyHash(Context context, String hashStrategy) {
        try {
            final PackageInfo info = context.getPackageManager()
                    .getPackageInfo(BuildConfig.APPLICATION_ID, PackageManager.GET_SIGNATURES);

            for (Signature signature : info.signatures) {
                final MessageDigest md = MessageDigest.getInstance(hashStrategy);
                md.update(signature.toByteArray());

                final byte[] digest = md.digest();
                final StringBuilder toRet = new StringBuilder();
                for (int i = 0; i < digest.length; i++) {
                    if (i != 0) toRet.append(":");
                    int b = digest[i] & 0xff;
                    String hex = Integer.toHexString(b);
                    if (hex.length() == 1) toRet.append("0");
                    toRet.append(hex);
                }

                Log.e(TAG, hashStrategy + ":" + toRet.toString());
            }
        } catch (PackageManager.NameNotFoundException e) {
            Log.e(TAG, e.toString());
        } catch (NoSuchAlgorithmException e) {
            Log.e(TAG, e.toString());
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
    }

    public static Retrofit initRetrofit(Context context){
        Session session = new Session(context, "pengguna");
        Pengguna pengguna = session.getPengguna();
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.connectTimeout(120, TimeUnit.SECONDS).readTimeout(120, TimeUnit.SECONDS);

        if (pengguna != null) {
            final String token = pengguna.getToken();
            httpClient.addInterceptor(new Interceptor() {
                @Override
                public Response intercept(Interceptor.Chain chain) throws IOException {
                    Request original = chain.request();

                    Request request = original.newBuilder()
                            .header("Authorization", "Bearer "+ token)
                            .build();

                    return chain.proceed(request);
                }
            });

        }
        OkHttpClient client = httpClient.addInterceptor(interceptor).build();
        return new Retrofit.Builder()
                .baseUrl(context.getString(R.string.url))
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
    }

    public static boolean isArrayString(String lokasiString, ArrayList<String> list) {
        for (String string: list) {
            if (lokasiString.equals(string)) return true;
        }
        return false;
    }

    public static void showLongSnackbar(View view, String message){
        if (view != null){
            Snackbar.make(view, message, Snackbar.LENGTH_LONG).show();
        }
    }

    public static void showLongSnackbar(View view, int message){
        if (view != null){
            Snackbar.make(view, message, Snackbar.LENGTH_LONG).show();
        }
    }

    public static void showShortSnackbar(View view, String message){
        if (view != null){
            Snackbar.make(view, message, Snackbar.LENGTH_SHORT).show();
        }
    }

    public static void showShortSnackbar(View view, int message){
        if (view != null){
            Snackbar.make(view, message, Snackbar.LENGTH_SHORT).show();
        }
    }

    public static double parseDouble(String string){
        try {
            return Double.parseDouble(string);
        } catch (Exception e){
            return 0;
        }
    }

    public static int parseInt(String string){
        try {
            return Integer.parseInt(string);
        } catch (Exception e){
            return 0;
        }
    }

    public static Date parseDateTime(String string){
        return parseDateTime(string, "yyyy-MM-dd HH:mm:ss");
    }

    public static Date parseDateTime(String string, String format){
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(format);
            return sdf.parse(string);
        } catch (Exception e){
            return null;
        }
    }

    public static Date parseDate(String string){
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            return sdf.parse(string);
        } catch (Exception e){
            return null;
        }
    }

    public static String reformatDate(String string, String format){
        try {
            if (format == null){
                format = "dd/MM/yyyy";
            }
            SimpleDateFormat sdf = new SimpleDateFormat(format, new Locale("id", "ID"));
            Date date = parseDate(string);
            return sdf.format(date);
        } catch (Exception e){
            return null;
        }
    }

    public static String reformatDate(Date date, String format){
        try {
            if (format == null){
                format = "dd/MM/yyyy";
            }
            SimpleDateFormat sdf = new SimpleDateFormat(format, new Locale("id", "ID"));
            return sdf.format(date);
        } catch (Exception e){
            return null;
        }
    }

    public static String capitalize(String str){
        try {
            String[] strArray = str.split(" ");
            StringBuilder builder = new StringBuilder();
            for (String s : strArray) {
                String cap = s.substring(0, 1).toUpperCase() + s.substring(1);
                builder.append(cap);
                builder.append(" ");
            }
            return builder.toString();
        } catch (Exception e){
            return "";
        }
    }

    public static boolean contains(final int needle, final int[] haystack) {
        return ArrayUtils.contains(haystack, needle);
    }

    public static boolean contains(final String needle, final String[] haystack) {
        return ArrayUtils.contains(haystack, needle);
    }

    public static boolean containString(final String needle, final String... haystack) {
        return ArrayUtils.contains(haystack, needle);
    }

    public static boolean containIgnoreCase(final String needle, final String... haystack) {
        if (haystack != null && haystack.length > 0){
            for (String string: haystack) {
                if (needle.toLowerCase().equals(string.toLowerCase())) return true;
            }
        }
        return false;
    }

    public static String getFileType(final String fileName) {
        String[] fileNameList = fileName.split("\\.");
        String fileType = "";
        if (fileNameList.length > 0) fileType = fileNameList[fileNameList.length-1];
        return fileType;
    }

    public static String getMimeType(String url) {
        String type = null;
        String extension = MimeTypeMap.getFileExtensionFromUrl(url);
        if (extension != null) {
            type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
        }
        return type;
    }

    public static String getUploadUrl(Context context, String uri){
        return context.getString(R.string.url)+uri;
    }

    public static String sanitizeFilename(String value) {
        return value.replaceAll("[^a-zA-Z0-9-_\\.]", "_");
    }

    public static Uri getImageUri(Context context, Bitmap bitmap) {
        if (bitmap != null){
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
            String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), bitmap, "capture_"+ System.currentTimeMillis(), null);
            return Uri.parse(path);
        }
        return null;
    }

    public static void checkApplicationPermission(Context context){
        Activity activity = (Activity) context;
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) {
            if (!ActivityCompat.shouldShowRequestPermissionRationale(activity,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                ActivityCompat.requestPermissions(activity,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 100);
            }
        }
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (!ActivityCompat.shouldShowRequestPermissionRationale(activity,
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {
                ActivityCompat.requestPermissions(activity,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 101);
            }
        }
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (!ActivityCompat.shouldShowRequestPermissionRationale(activity,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                ActivityCompat.requestPermissions(activity,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 102);
            }
        }
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            if (!ActivityCompat.shouldShowRequestPermissionRationale(activity,
                    Manifest.permission.CAMERA)) {
                ActivityCompat.requestPermissions(activity,
                        new String[]{Manifest.permission.CAMERA}, 103);
            }
        }
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.WAKE_LOCK) != PackageManager.PERMISSION_GRANTED) {
            if (!ActivityCompat.shouldShowRequestPermissionRationale(activity,
                    Manifest.permission.WAKE_LOCK)) {
                ActivityCompat.requestPermissions(activity,
                        new String[]{Manifest.permission.WAKE_LOCK}, 104);
            }
        }
    }

    public static HashMap<Integer, IzinPersetujuan> status(Izin izin){
        // 1: PJLAKSK4 diterima
        // 2: Manager Unit Layanan diterima
        // 3: Direksi pekerjaan diterima
        int result = 0;
        HashMap<Integer, IzinPersetujuan> map = new HashMap<>();
        if (izin != null && izin.getIzinPersetujuanList() != null){
            String lokasiKode = null;
            if (izin.getLokasi() != null && izin.getLokasi().getKode() != null){
                lokasiKode = izin.getLokasi().getKode();
            } else if (izin.getLokasiKode() != null && izin.getLokasiKode() != null){
                lokasiKode = izin.getLokasiKode();
            }
            if (izin.getJenis().equals("internal")){
                if (lokasiKode != null && lokasiKode.equals("UPDKBRTO")) {
                    // Izin Kerja Internal (kantor):
                    // 1   Supervisor Operasi dan Pemeliharaan / PJLAKSKLK / spv sdm    Pengajuan / Cetak
                    // 2   PJ LAKSK4 UPDK Barito                                        Verifikator(Tolak / Setuju) / Cetak
                    // 3   Manager Unit Pelaksana UPDK Barito                           Tolak / Setuju / Cetak
                    for (IzinPersetujuan izinPersetujuan: izin.getIzinPersetujuanList()) {
                        if (izinPersetujuan.getStatus().equals("diterima")){
                            if (izinPersetujuan.getJabatanKode().equals("PJLAKSK4") && result < 1) {
                                result = 1;
                                map.put(1, izinPersetujuan);
                            } else if (izinPersetujuan.getJabatanKode().equals("MUP") && result < 2) {
                                result = 2;
                                map.put(2, izinPersetujuan);
                                return map;
                            }
                        }
                    }

                } else {
                    // Izin Kerja Internal (ULPL):
                    // 1   Supervisor Operasi dan Pemeliharaan                                                                  Pengajuan / Cetak
                    // 2   PJ LAKSK4 (ULPLTD/G Trisakti, atau ULPLTA/D Gunung Bamega, atau ULPLTD Tambun Bungai)                Verifikator(Tolak / Setuju)  / Cetak
                    // 3   Manager Unit Layanan (ULPLTD/G Trisakti, atau ULPLTA/D Gunung Bamega, atau ULPLTD Tambun Bungai)     Tolak / Setuju  / Cetak
                    // 4   Manager Bagian OPHAR 1, Manager Bagian OPHAR 2, Manager Bagian KSA, PJLAKSK4                         Monitor pekerjaan di UL / Cuma bisa View  / Cetak
                    // 5   Manager Unit Pelaksana UPDK Barito                                                                   Monitor pekerjaan di UL / Cuma bisa View / Cetak

                    for (IzinPersetujuan izinPersetujuan: izin.getIzinPersetujuanList()) {
                        if (izinPersetujuan.getStatus().equals("diterima")){
                            if (izinPersetujuan.getJabatanKode().equals("PJLAKSK4") && result < 1) {
                                result = 1;
                                map.put(1, izinPersetujuan);
                            } else if (izinPersetujuan.getJabatanKode().equals("MANAGER") && result < 2) {
                                result = 2;
                                map.put(2, izinPersetujuan);
                                return map;
                            }
                        }
                    }
                }
            } else if (izin.getJenis().equals("eksternal")){
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
                    for (IzinPersetujuan izinPersetujuan: izin.getIzinPersetujuanList()) {
                        if (izinPersetujuan.getStatus().equals("diterima")){
                            if (izin.getDireksiLapanganId() == izinPersetujuan.getPenggunaId() && result < 2) {
                                result = 2;
                                map.put(2, izinPersetujuan);
                            } else if (izinPersetujuan.getJabatanKode().equals("PJLAKSK4") && result < 1) {
                                result = 1;
                                map.put(1, izinPersetujuan);
                            } else if (izinPersetujuan.getJabatanKode().equals("MUP") && result < 3) {
                                result = 3;
                                map.put(3, izinPersetujuan);
                            }
                        } else if (izinPersetujuan.getStatus().equals("pekerjaan_diterima")){
                            if (izin.getDireksiLapanganId() == izinPersetujuan.getPenggunaId() && result < 7) {
                                result = 7;
                                map.put(7, izinPersetujuan);
                            } else if (izinPersetujuan.getJabatanKode().equals("PJLAKSKLK") && result < 6) {
                                result = 6;
                                map.put(6, izinPersetujuan);
                            }
                        } else if (izinPersetujuan.getStatus().equals("pekerjaan_diajukan")){
                            if (izin.getDireksiLapanganId() == izinPersetujuan.getPenggunaId() && result < 7) {
                                result = 7;
                                map.put(7, izinPersetujuan);
                            }
                        }
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
                    for (IzinPersetujuan izinPersetujuan: izin.getIzinPersetujuanList()) {
                        if (izinPersetujuan.getStatus().equals("diterima")){
                            if (izinPersetujuan.getJabatanKode().equals("PJLAKSK4") && result < 1) {
                                result = 1;
                                map.put(1, izinPersetujuan);
                            } else if (izinPersetujuan.getJabatanKode().equals("MANAGER") && result < 2) {
                                result = 2;
                                map.put(2, izinPersetujuan);
                            }
                        } else if (izinPersetujuan.getStatus().equals("pekerjaan_diterima")){
                            if (izinPersetujuan.getJabatanKode().equals("PJLAKSKLK") && result < 6) {
                                result = 6;
                                map.put(6, izinPersetujuan);
                            } else if (izinPersetujuan.getJabatanKode().equals("MANAGER") && result < 7) {
                                result = 7;
                                map.put(7, izinPersetujuan);
                                return map;
                            }
                        }
                    }
                }
            }
        }

        return map;
    }

    public static boolean checkAccess(Context context, String kodeJabatan, String kodeLokasi){
        Session sessionAkses = new Session(context, "akses");

        if (sessionAkses.getAll() != null){
            for(Map.Entry<String, ?> entry : sessionAkses.getAll().entrySet()) {
                Log.d(TAG, "checkAccess:"+entry.getKey()+":"+entry.getValue().toString());
                Akses akses = new Gson().fromJson(entry.getValue().toString(), Akses.class);
                if (kodeLokasi != null){
                    if (akses.getJabatan() != null && kodeJabatan.equals(akses.getJabatan().getKode())
                            && akses.getLokasi() != null && kodeLokasi.equals(akses.getLokasi().getKode())) return true;
                } else {
                    if (akses.getJabatan() != null && kodeJabatan.equals(akses.getJabatan().getKode())) return true;
                }
            }
        }
        return false;
    }

    public static boolean checkAccess(Context context, String[] kodeJabatan){
        Session sessionAkses = new Session(context, "akses");

        if (sessionAkses.getAll() != null){
            for(Map.Entry<String, ?> entry : sessionAkses.getAll().entrySet()) {
                Log.d(TAG, entry.getKey()+":"+entry.getValue().toString());
                Akses akses = new Gson().fromJson(entry.getValue().toString(), Akses.class);
                if (akses.getJabatan() != null && Arrays.asList(kodeJabatan).contains(akses.getJabatan().getKode())) return true;
            }
        }
        return false;
    }

    public static Bitmap resizeBitmap(Bitmap getBitmap, int maxSize) {
        int width = getBitmap.getWidth();
        int height = getBitmap.getHeight();
        double x = 1;

        if (width >= height && width > maxSize) {
            x = Double.valueOf(width) / Double.valueOf(height);
            width = maxSize;
            height = (int) (maxSize / x);
        } else if (height >= width && height > maxSize) {
            x = Double.valueOf(height) / Double.valueOf(width);
            height = maxSize;
            width = (int) (maxSize / x);
        }
        Log.d(TAG, "resizeBitmap:from:"+getBitmap.getWidth()+","+getBitmap.getHeight());
        Log.d(TAG, "resizeBitmap:to:"+width+","+height);
        return Bitmap.createScaledBitmap(getBitmap, width, height, false);
    }

    public static File fileFromBitmap(Context context, String fileName, Bitmap bitmap, int quality){
        File file = null;
        try {
            file = new File(context.getCacheDir(), fileName);
            if (!file.exists()) file.createNewFile();

            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            if (bitmap != null){
                bitmap.compress(Bitmap.CompressFormat.JPEG, quality /*ignored for PNG*/, bos);
            }
            byte[] bitmapData = bos.toByteArray();

            FileOutputStream fos = null;
            fos = new FileOutputStream(file);

            fos.write(bitmapData);
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            FirebaseCrashlytics.getInstance().recordException(e);
        } catch (IOException e) {
            e.printStackTrace();
            FirebaseCrashlytics.getInstance().recordException(e);
        }
        return file;
    }

    public static void appendLog(String key, String text, View view) {
        Log.d(key, text);
//        Helpers.showShortSnackbar(view, text);
        String dateString = reformatDate(new Date(), "yyyyMMdd");
        String fileName = BuildConfig.APPLICATION_ID + "_" + dateString + ".log";
        try {
            File logFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), fileName);
            Log.d(key, logFile.getAbsolutePath());
            if (!logFile.exists()) {
                logFile.createNewFile();
            }
            String timeString = reformatDate(new Date(), "yyyy-MM-dd  HH:mm:ss");
            BufferedWriter buf = new BufferedWriter(new FileWriter(logFile, true));
            buf.append(key);
            buf.append(", ");
            buf.append(timeString);
            buf.append(": ");
            buf.append(text);
            buf.newLine();
            buf.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean isImageFile(String path) {
        String mimeType = URLConnection.guessContentTypeFromName(path);
        return mimeType != null && mimeType.startsWith("image");
    }

    public static byte[] getBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];

        int len = 0;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }
        return byteBuffer.toByteArray();
    }

    public static String getFileName(Context context, Uri uri) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            Cursor cursor = context.getContentResolver().query(uri, null, null, null, null);
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                }
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }
        }
        if (result == null) {
            result = uri.getPath();
            int cut = result.lastIndexOf('/');
            if (cut != -1) {
                result = result.substring(cut + 1);
            }
        }
        return result;
    }

    public static File createImageFile(Context context) throws IOException {
        // Create an image file name
        String timeStamp = Helpers.reformatDate(new Date(), "yyyyMMdd_HHmmss");
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
//        currentPhotoPath = image.getAbsolutePath();
        return image;
    }

    public static String getMimeType(Context context, Uri uri){
        ContentResolver contentResolver = context.getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(contentResolver.getType(uri));
    }

}
