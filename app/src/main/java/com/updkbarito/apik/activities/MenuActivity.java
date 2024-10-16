package com.updkbarito.apik.activities;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.gson.Gson;
import com.onesignal.OneSignal;
import com.updkbarito.apik.R;
import com.updkbarito.apik.entities.Akses;
import com.updkbarito.apik.entities.Pengguna;
import com.updkbarito.apik.services.PenggunaService;
import com.updkbarito.apik.services.ResponseAksesList;
import com.updkbarito.apik.services.ResponsePengguna;
import com.updkbarito.apik.utils.Helpers;
import com.updkbarito.apik.utils.Session;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MenuActivity extends AppCompatActivity implements View.OnClickListener {

    private final String TAG = MenuActivity.class.getSimpleName();
    private MaterialButton buttonKerjaInternal;
    private MaterialButton buttonKerjaEksternal;
    private MaterialButton buttonEditProfile;
    private MaterialButton buttonLogout;
    private MenuActivity context;
    private Session session;
    private Session sessionAkses;
    private Pengguna pengguna = new Pengguna();
    private Retrofit retrofit;
    private PenggunaService penggunaServoce;
    private List<Akses> aksesList = new ArrayList<>();
    private String deviceName;
    private String deviceOS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        buttonKerjaInternal = findViewById(R.id.button_kerja_internal);
        buttonKerjaEksternal = findViewById(R.id.button_kerja_eksternal);
        buttonEditProfile = findViewById(R.id.button_edit_profil);
        buttonLogout = findViewById(R.id.button_logout);

        context = this;

        buttonKerjaInternal.setOnClickListener(this);
        buttonKerjaEksternal.setOnClickListener(this);
        buttonEditProfile.setOnClickListener(this);
        buttonLogout.setOnClickListener(this);

        buttonKerjaInternal.setVisibility(View.GONE);
        buttonKerjaEksternal.setVisibility(View.GONE);

        session = new Session(context, "pengguna");
        pengguna = session.getPengguna();

        sessionAkses = new Session(context, "akses");

        if (pengguna == null) logoutAction(null);

        retrofit = Helpers.initRetrofit(context);
        penggunaServoce = retrofit.create(PenggunaService.class);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Helpers.checkApplicationPermission(context);

        penggunaServoce.aksesList(new HashMap<String, String>()).enqueue(new Callback<ResponseAksesList>() {
            @Override
            public void onResponse(Call<ResponseAksesList> call, Response<ResponseAksesList> response) {
                if (response.isSuccessful()){
                    if (response.body() != null && response.body().getData() != null){
                        aksesList = response.body().getData();

                        String[] internalKode = new String[] {"ADMIN", "SOPHAR", "SOPHAR1", "SOPHAR2", "SOPHAR3", "MANUL", "MANAGER", "PJLAKSK4", "MUP", "PJLAKSK4"};
                        String[] eksternalKode = new String[] {"ADMIN", "VENDOR", "PJLAKSK4", "MANUL", "MUP", "MANOPHAR1", "MANOPHAR2", "MANKSA", "PJLAKSK4", "PJLAKSKLK", "MANAGER"};

                        Log.d(TAG, "aksesList:"+aksesList.size());
                        sessionAkses.clear();
                        for (Akses akses: aksesList) {
                            Log.d(TAG, "akses:"+new Gson().toJson(akses));
                            sessionAkses.updateData(String.valueOf(akses.getId()), new Gson().toJson(akses));
                            if(akses.getJabatan() != null && Helpers.contains(akses.getJabatan().getKode(), internalKode)){
                                buttonKerjaInternal.setVisibility(View.VISIBLE);
                            }
                            if(akses.getJabatan() != null && Helpers.contains(akses.getJabatan().getKode(), eksternalKode)){
                                buttonKerjaEksternal.setVisibility(View.VISIBLE);
                            }
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseAksesList> call, Throwable t) {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        updateDeviceToken();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == buttonKerjaInternal.getId()){
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("jenis", "internal");
            startActivity(intent);
        } else if (v.getId() == buttonKerjaEksternal.getId()){
            Intent intent = new Intent(this, MainActivity.class);
            intent.putExtra("jenis", "eksternal");
            startActivity(intent);
        } else if (v.getId() == buttonEditProfile.getId()){
            Intent intent = new Intent(this, EditProfileActivity.class);
            startActivity(intent);
        } else if (v.getId() == buttonLogout.getId()){
            logoutAction(v);
        }
    }

    private void logoutAction(View v) {
        session.setPengguna(null);

        finish();
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
    }

    private void updateDeviceToken() {
        deviceName = android.os.Build.MANUFACTURER + " " + android.os.Build.MODEL;
        deviceOS = String.valueOf(android.os.Build.VERSION.SDK_INT);
        Log.d(TAG, "device_name:"+deviceName);
        Log.d(TAG, "device_os:"+deviceOS);
        try {
            String playerId = OneSignal.getUserDevice().getUserId();
            if (pengguna.getPlayerId() == null || !(playerId.equals(pengguna.getPlayerId()))){
                pengguna.setPlayerId(playerId);
                updatePengguna(null, playerId);
            } else if (deviceName != null || deviceOS != null){
                updatePengguna(null, null);
            }
        } catch (Exception e){
            e.printStackTrace();
        }

        FirebaseInstanceId.getInstance().getInstanceId().addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
            @Override
            public void onComplete(@NonNull Task<InstanceIdResult> task) {
                if (task.isSuccessful() && task.getResult() != null){
                    // Get new Instance ID token
                    String token = task.getResult().getToken();
                    Log.d(TAG, "token:"+token);

                    if (pengguna == null || !(token.equals(pengguna.getDeviceToken()))){
                        Log.d(TAG, "token:if:"+token.equals(pengguna.getDeviceToken()));
                        updatePengguna(token, null);
                    } else if (deviceName != null || deviceOS != null){
                        updatePengguna(null, null);
                    }
                }
            }
        });
    }

    void updatePengguna(String deviceToken, String playerId){
        final PenggunaService penggunaService = retrofit.create(PenggunaService.class);
        HashMap<String, String> map = new HashMap<>();
        if (deviceToken != null) map.put("device_token", deviceToken);
        if (playerId != null) map.put("player_id", playerId);
        if (deviceName != null) map.put("device_name", deviceName);
        if (deviceOS != null) map.put("device_os", deviceOS);
        if (map.size() > 0){
            penggunaService.update(map).enqueue(new Callback<ResponsePengguna>() {
                @Override
                public void onResponse(Call<ResponsePengguna> call, Response<ResponsePengguna> response) {
                    if (response.isSuccessful()){
                        if (response.body() != null && response.body().getData() != null){
                            pengguna = response.body().getData();
                            session.setPengguna(pengguna);
                        }
                    }
                }

                @Override
                public void onFailure(Call<ResponsePengguna> call, Throwable t) {

                }
            });
        }
    }
}
