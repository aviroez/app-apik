package com.updkbarito.apik.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.updkbarito.apik.R;
import com.updkbarito.apik.entities.Lokasi;
import com.updkbarito.apik.entities.Pengguna;
import com.updkbarito.apik.entities.Vendor;
import com.updkbarito.apik.services.HomeService;
import com.updkbarito.apik.services.ResponsePengguna;
import com.updkbarito.apik.services.ResponseVendorList;
import com.updkbarito.apik.services.VendorService;
import com.updkbarito.apik.utils.Helpers;
import com.updkbarito.apik.utils.Session;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {

    private final String TAG = SignUpActivity.class.getSimpleName();
    private final int RC_SIGN_IN = 100;
    private GoogleSignInClient googleSignInClient;
    private TextInputEditText textFullName;
    private AutoCompleteTextView textVendor;
    private TextInputEditText textEmail;
    private TextInputEditText textPassword;
    private TextInputEditText textPasswordRetype;
    private MaterialButton buttonSignUp;
    private SignInButton buttonGoogle;
    private Retrofit retrofit;
    private HomeService homeService;
    private VendorService vendorService;
    private String nama;
    private String vendorName;
    private String email;
    private String password;
    private String passwordRetype;
    private List<Vendor> listVendor = new ArrayList<>();
    private ArrayList<String> stringVendorList;
    private SignUpActivity context;
    private boolean loadLokasi = false;
    private int vendorId = 0;
    private Intent intent;
    private View layoutProgressBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        textFullName = findViewById(R.id.text_full_name);
        textVendor = findViewById(R.id.text_vendor);
        textEmail = findViewById(R.id.text_email);
        textPassword = findViewById(R.id.text_password);
        textPasswordRetype = findViewById(R.id.text_password_retype);
        buttonSignUp = findViewById(R.id.button_signup);
        buttonGoogle = findViewById(R.id.button_google);
        layoutProgressBar = findViewById(R.id.layout_progress_bar);

        context = this;

        buttonSignUp.setOnClickListener(context);
        buttonGoogle.setOnClickListener(context);

        String serverClientId = getResources().getString(R.string.google_server_client_id);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestProfile()
                .requestIdToken(serverClientId)
                .requestServerAuthCode(serverClientId, false)
                .requestId()
                .build();
        googleSignInClient = GoogleSignIn.getClient(this, gso);
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);

        retrofit = Helpers.initRetrofit(this);
        homeService = retrofit.create(HomeService.class);
        vendorService = retrofit.create(VendorService.class);

        initVendor();
    }

    @Override
    protected void onStart() {
        super.onStart();
        layoutProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == buttonSignUp.getId()){
            signupAction(v);
        } else if (v.getId() == buttonGoogle.getId()){
            googleLogin(v);
        }
    }

    private void signupAction(View v) {
        if (validation()){
            layoutProgressBar.setVisibility(View.VISIBLE);
            HashMap<String, String> map = new HashMap<>();
            map.put("nama", nama);
            map.put("email", email);
            map.put("vendor_id", String.valueOf(vendorId));
            map.put("password", password);
            map.put("ulangi_password", passwordRetype);
            homeService.daftar(map).enqueue(new Callback<ResponsePengguna>() {
                @Override
                public void onResponse(Call<ResponsePengguna> call, Response<ResponsePengguna> response) {
                    layoutProgressBar.setVisibility(View.GONE);
                    if (response.isSuccessful()){
                        if (response.body() != null && response.body().getData() != null){
                            Pengguna pengguna = response.body().getData();
                            Session session = new Session(context, "pengguna");
                            session.setPengguna(pengguna);
                            finish();
                            intent = new Intent(context, MenuActivity.class);
                            startActivity(intent);
                        }
                    }
                }

                @Override
                public void onFailure(Call<ResponsePengguna> call, Throwable t) {
                    layoutProgressBar.setVisibility(View.GONE);
                }
            });
        }
    }

    private boolean validation() {
        nama = textFullName.getText().toString();
        vendorName = textVendor.getText().toString();
        email = textEmail.getText().toString();
        password = textPassword.getText().toString();
        passwordRetype = textPasswordRetype.getText().toString();

//        vendorId = -1;
//
//        if (listVendor.size() > 0){
//            for (Vendor vendor: listVendor) {
//                if (vendorName.equals(vendor.getNama())) {
//                    vendorId = vendor.getId();
//                    break;
//                }
//            }
//        }

        if (nama.length() <= 0){
            Snackbar.make(findViewById(R.id.layout_login), "Nama Lengkap harus diisi", Snackbar.LENGTH_LONG).show();
            textFullName.requestFocus();
            return false;
        } else if (vendorName.length() <= 0 || vendorId < 0){
            Snackbar.make(findViewById(R.id.layout_login), "Nama Vendor harus diisi", Snackbar.LENGTH_LONG).show();
            textVendor.requestFocus();
            return false;
        } else if (email.length() <= 0){
            Snackbar.make(findViewById(R.id.layout_login), "Email harus diisi", Snackbar.LENGTH_LONG).show();
            textEmail.requestFocus();
            return false;
        } else if (password.length() <= 0){
            Snackbar.make(findViewById(R.id.layout_login), "Password harus diisi", Snackbar.LENGTH_LONG).show();
            textPassword.requestFocus();
            return false;
        } else if (passwordRetype.length() <= 0){
            Snackbar.make(findViewById(R.id.layout_login), "Ulangi Password harus diisi", Snackbar.LENGTH_LONG).show();
            textPasswordRetype.requestFocus();
            return false;
        } else if (!password.equals(passwordRetype)){
            Snackbar.make(findViewById(R.id.layout_login), "Password harus sama", Snackbar.LENGTH_LONG).show();
            textPasswordRetype.requestFocus();
            return false;
        }
        return true;
    }

    private void googleLogin(View v) {
        Intent signInIntent = googleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void initVendor() {
        HashMap<String, String> map = new HashMap<>();
        vendorService.index(map).enqueue(new Callback<ResponseVendorList>() {
            @Override
            public void onResponse(Call<ResponseVendorList> call, Response<ResponseVendorList> response) {
                if (response.isSuccessful()){
                    if (response.body() != null){
                        int loadLokasiIndex = -1;
                        listVendor = response.body().getData();
                        stringVendorList = new ArrayList<>();
                        for (Vendor vendor: listVendor) {
                            stringVendorList.add(vendor.getNama());
                            loadLokasiIndex = 0;
                        }

                        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_expandable_list_item_1, stringVendorList);
                        textVendor.setAdapter(adapter);
                        if (stringVendorList.size() == 1) {
                            textVendor.setText(stringVendorList.get(0));
//                            textVendor.setInputType(0);
                            textVendor.setFocusable(false);
                        }
//                        else if (!loadLokasi && loadLokasiIndex >= 0) {
//                            vendorId = listVendor.get(loadLokasiIndex).getId();
//                            textVendor.setText(stringVendorList.get(loadLokasiIndex));
//                            loadLokasi = true;
//                            textVendor.setFocusable(true);
//                        }

                        textVendor.setEnabled(false);

                        textVendor.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                textVendor.setEnabled(false);
                                Vendor vendor = listVendor.get(position);
                                vendorId = vendor.getId();
                            }
                        });
                        textVendor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                textVendor.setEnabled(false);
                                Vendor vendor = listVendor.get(position);
                                vendorId = vendor.getId();
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseVendorList> call, Throwable t) {

            }
        });
    }
}
