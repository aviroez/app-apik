package com.updkbarito.apik.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import com.google.android.play.core.tasks.OnCompleteListener;
import com.google.android.play.core.tasks.OnFailureListener;
import com.google.android.play.core.tasks.Task;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.google.android.play.core.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.google.gson.Gson;
import com.onesignal.OneSignal;
import com.updkbarito.apik.R;
import com.updkbarito.apik.entities.Pengguna;
import com.updkbarito.apik.services.HomeService;
import com.updkbarito.apik.services.PenggunaService;
import com.updkbarito.apik.services.ResponsePengguna;
import com.updkbarito.apik.utils.Helpers;
import com.updkbarito.apik.utils.Session;

import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class SplashActivity extends AppCompatActivity {

    private static String TAG = SplashActivity.class.getSimpleName();
    private FirebaseAuth auth;
    private FirebaseUser firebaseUser;
    private SplashActivity context;
    private Retrofit retrofit;
    private Intent intent;
    private int notify = 0;
    private HomeService homeService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        context = this;

        intent = getIntent();
        notify = intent.getIntExtra("notify", 0);
        Log.d(TAG, "notify:"+notify);

        auth = FirebaseAuth.getInstance();

        Helpers.getKeyHash(this, "SHA");
        retrofit = Helpers.initRetrofit(context);
        homeService = retrofit.create(HomeService.class);

        // Logging set to help debug issues, remove before releasing your app.
//         OneSignal.setLogLevel(OneSignal.LOG_LEVEL.DEBUG, OneSignal.LOG_LEVEL.DEBUG);
        // OneSignal.setLogLevel(OneSignal.LOG_LEVEL.VERBOSE, OneSignal.LOG_LEVEL.NONE);

        // OneSignal Initialization
        OneSignal.startInit(this)
                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                .unsubscribeWhenNotificationsAreDisabled(true)
                .init();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();

        updateApp();
    }

    private void updateApp(){
        final AppUpdateManager appUpdateManager = AppUpdateManagerFactory.create(SplashActivity.this);
        com.google.android.play.core.tasks.Task<AppUpdateInfo> appUpdateInfoTask = appUpdateManager.getAppUpdateInfo();
        appUpdateInfoTask.addOnSuccessListener(new OnSuccessListener<AppUpdateInfo>() {
            @Override
            public void onSuccess(AppUpdateInfo appUpdateInfo) {
                if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE){
                    final String appPackageName = getPackageName();
                    new AlertDialog.Builder(SplashActivity.this)
                            .setTitle(R.string.confirmation)
                            .setMessage(R.string.new_version_found_please_update)
                            .setPositiveButton(R.string.update_app, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    try {
                                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                                    } catch (android.content.ActivityNotFoundException e) {
                                        e.printStackTrace();
                                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                                    }
                                }
                            })
                            .setNegativeButton(R.string.exit, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                        SplashActivity.this.finishAndRemoveTask();
                                    }
                                    SplashActivity.this.finishAffinity();
                                }
                            })
                            .show();
                } else {
                    startApp();
                }
            }
        });
        appUpdateInfoTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception e) {
                startApp();
            }
        });

        appUpdateInfoTask.addOnCompleteListener(new OnCompleteListener<AppUpdateInfo>() {
            @Override
            public void onComplete(Task<AppUpdateInfo> task) {
            }
        });
    }

    private void startApp() {
        if (auth != null){
            firebaseUser = auth.getCurrentUser();
            if (firebaseUser == null){
                Session session = new Session(context, "pengguna");
                Pengguna pengguna = session.getPengguna();
                if (pengguna != null){
                    if (pengguna.getAksesList().size() > 0){
                        goToMenu();
                    } else {
                        homeService.loginToken(pengguna.getEmail(), pengguna.getToken()).enqueue(new Callback<ResponsePengguna>() {
                            @Override
                            public void onResponse(Call<ResponsePengguna> call, Response<ResponsePengguna> response) {
                                if (response.isSuccessful()){
                                    if (response.body() != null && response.body().getData() != null){
                                        Pengguna p = response.body().getData();
                                        Session session = new Session(context, "pengguna");
                                        session.setPengguna(p);
                                        goToMenu();
                                    } else {
                                        goToLogin();
                                    }
                                } else {
                                    goToLogin();
                                }
                            }

                            @Override
                            public void onFailure(Call<ResponsePengguna> call, Throwable t) {
                                goToLogin();
                            }
                        });
                    }
                } else {
                    goToMenu();
                }
            } else {
                goToMenu();
            }
        } else {
            goToMenu();
        }
    }

    private void goToLogin(){
        finish();
        intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    private void goToMenu(){
        finish();
        intent = new Intent(this, MenuActivity.class);
        startActivity(intent);
    }
}
