package com.updkbarito.apik.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;

import com.github.gcacace.signaturepad.views.SignaturePad;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.updkbarito.apik.R;
import com.updkbarito.apik.entities.Akses;
import com.updkbarito.apik.entities.Pengguna;
import com.updkbarito.apik.services.PenggunaService;
import com.updkbarito.apik.services.ResponsePengguna;
import com.updkbarito.apik.utils.Helpers;
import com.updkbarito.apik.utils.Session;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import com.caverock.androidsvg.SVG;
import com.caverock.androidsvg.SVGParseException;

public class EditProfileActivity extends AppCompatActivity implements View.OnClickListener{

    private static String TAG = EditProfileActivity.class.getSimpleName();
    private TextInputEditText textFullName;
    private TextInputEditText textVendor;
    private TextInputEditText textEmail;
    private TextInputEditText textPassword;
    private TextInputEditText textPasswordRetype;
    private CheckBox checkboxEditPassword;
    private MaterialButton buttonEdit;
    private MaterialButton buttonClear;
    private String nama;
    private String vendorName;
    private String email;
    private String password;
    private String passwordRetype;
    private EditProfileActivity context;
    private Retrofit retrofit;
    private PenggunaService penggunaService;
    private Pengguna pengguna = new Pengguna();
    private Session session;
    private Session sessionAkses;
    private Intent intent;
    private View layoutProgressBar;
    private View layoutVendor;
    private View layoutPassword;
    private View layoutPasswordRetype;
    private SignaturePad signaturePad;
    private boolean firstLoad = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        textFullName = findViewById(R.id.text_full_name);
        layoutVendor = findViewById(R.id.layout_vendor);
        textVendor = findViewById(R.id.text_vendor);
        textEmail = findViewById(R.id.text_email);
        checkboxEditPassword = findViewById(R.id.checkbox_edit_password);
        layoutPassword = findViewById(R.id.layout_password);
        layoutPasswordRetype = findViewById(R.id.layout_password_retype);
        textPassword = findViewById(R.id.text_password);
        textPasswordRetype = findViewById(R.id.text_password_retype);
        buttonEdit = findViewById(R.id.button_edit);
        buttonClear = findViewById(R.id.button_clear);
        layoutProgressBar = findViewById(R.id.layout_progress_bar);
        signaturePad = findViewById(R.id.signature_pad);
        layoutVendor.setVisibility(View.GONE);

        context = this;
        checkboxEditPassword.setOnClickListener(context);
        buttonEdit.setOnClickListener(context);
        buttonClear.setOnClickListener(context);

        retrofit = Helpers.initRetrofit(context);
        penggunaService = retrofit.create(PenggunaService.class);

        session = new Session(context, "pengguna");
        pengguna = session.getPengguna();

        if (pengguna.getSignature() != null){
            Bitmap bitmap = generateSignature(pengguna.getSignature());
            if (bitmap != null) signaturePad.setSignatureBitmap(bitmap);

            firstLoad = true;
            signaturePad.setOnSignedListener(new SignaturePad.OnSignedListener() {
                @Override
                public void onStartSigning() {
                    if (firstLoad) signaturePad.clear();
                    firstLoad = false;
                }

                @Override
                public void onSigned() {

                }

                @Override
                public void onClear() {

                }
            });
        }

        textFullName.setText(pengguna.getNama());
        textEmail.setText(pengguna.getEmail());

        sessionAkses = new Session(context, "akses");
        textFullName.setText(pengguna.getNama());

        Map<String, ?> map = sessionAkses.getAll();

        Iterator it = map.values().iterator();

        while (it.hasNext()) {
            Akses akses = new Gson().fromJson(it.next().toString(), Akses.class);
            Log.d(TAG, new Gson().toJson(akses));
            if (akses.getVendorId() > 0 && akses.getVendor() != null && akses.getVendor().getNama() != null) {
                textVendor.setText(akses.getVendor().getNama());
                layoutVendor.setVisibility(View.VISIBLE);
            }
        }

    }

    @Override
    protected void onStart() {
        super.onStart();

        layoutProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == buttonEdit.getId()){
            editAction(v);
        } if (v.getId() == buttonClear.getId()){
            signaturePad.clear();
        } else if (v.getId() == checkboxEditPassword.getId()){
            if (checkboxEditPassword.isChecked()){
                layoutPassword.setVisibility(View.VISIBLE);
                layoutPasswordRetype.setVisibility(View.VISIBLE);
            } else {
                layoutPassword.setVisibility(View.GONE);
                layoutPasswordRetype.setVisibility(View.GONE);
            }
        }
    }

    private void editAction(View v) {
        if (validation()){
            layoutProgressBar.setVisibility(View.VISIBLE);

            HashMap<String, String> map = new HashMap<>();
            map.put("nama", nama);
            map.put("email", email);

//            if (signaturePad.getSignatureSvg() != null) map.put("signature", signaturePad.getSignatureSvg());
            if (checkboxEditPassword.isChecked()) map.put("password", password);
            penggunaService.update(map).enqueue(new Callback<ResponsePengguna>() {
                @Override
                public void onResponse(Call<ResponsePengguna> call, Response<ResponsePengguna> response) {
                    layoutProgressBar.setVisibility(View.GONE);
                    if (response.isSuccessful()){
                        if (response.body() != null && response.body().getData() != null){
                            pengguna = response.body().getData();
                            session.setPengguna(pengguna);
                            Snackbar.make(findViewById(R.id.layout_login), "Profil berhasil diubah", Snackbar.LENGTH_LONG).show();
                            kembaliAction(null);
                        } else {
                            Snackbar.make(findViewById(R.id.layout_login), "Profil gagal diubah", Snackbar.LENGTH_LONG).show();
                        }
                    } else {
                        Snackbar.make(findViewById(R.id.layout_login), "Profil gagal diubah", Snackbar.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(Call<ResponsePengguna> call, Throwable t) {
                    Snackbar.make(findViewById(R.id.layout_login), t.getMessage(), Snackbar.LENGTH_LONG).show();
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

        if (nama.length() <= 0){
            Snackbar.make(findViewById(R.id.layout_login), "Nama Lengkap harus diisi", Snackbar.LENGTH_LONG).show();
            textFullName.requestFocus();
            return false;
        }
//        else if (vendorName.length() <= 0){
//            Snackbar.make(findViewById(R.id.layout_login), "Nama Vendor harus diisi", Snackbar.LENGTH_LONG).show();
//            textVendor.requestFocus();
//            return false;
//        }
        else if (email.length() <= 0){
            Snackbar.make(findViewById(R.id.layout_login), "Email harus diisi", Snackbar.LENGTH_LONG).show();
            textEmail.requestFocus();
            return false;
        } else if (checkboxEditPassword.isChecked() && password.length() <= 0){
            Snackbar.make(findViewById(R.id.layout_login), "Password harus diisi", Snackbar.LENGTH_LONG).show();
            textPassword.requestFocus();
            return false;
        } else if (checkboxEditPassword.isChecked() && passwordRetype.length() <= 0){
            Snackbar.make(findViewById(R.id.layout_login), "Ulangi Password harus diisi", Snackbar.LENGTH_LONG).show();
            textPasswordRetype.requestFocus();
            return false;
        } else if (checkboxEditPassword.isChecked() && !password.equals(passwordRetype)){
            Snackbar.make(findViewById(R.id.layout_login), "Password harus sama", Snackbar.LENGTH_LONG).show();
            textPasswordRetype.requestFocus();
            return false;
        }
        return true;
    }

    public void kembaliAction(View view){
        finish();
        intent = new Intent(context, MenuActivity.class);
        startActivity(intent);
    }

    private Bitmap generateSignature(String signature) {
        Bitmap bitmap = null;
        SVG svg = null;
        try {
            svg = SVG.getFromString(signature);

            bitmap = Bitmap.createBitmap((int) Math.ceil(svg.getDocumentWidth()),
                    (int) Math.ceil(svg.getDocumentHeight()),
                    Bitmap.Config.ARGB_8888);

            Canvas canvas = new Canvas(bitmap);

            // Clear background to white
            canvas.drawRGB(255, 255, 255);

            // Render our document onto our canvas
            svg.renderToCanvas(canvas);
        } catch (SVGParseException e) {
            e.printStackTrace();
        }
        return bitmap;
    }
}
