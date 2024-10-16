package com.updkbarito.apik.activities;

import android.app.Activity;

import androidx.annotation.NonNull;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.gson.Gson;
import com.updkbarito.apik.R;
import com.updkbarito.apik.entities.Pengguna;
import com.updkbarito.apik.services.HomeService;
import com.updkbarito.apik.services.ResponsePengguna;
import com.updkbarito.apik.utils.Helpers;
import com.updkbarito.apik.utils.Session;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private final String TAG = LoginActivity.class.getSimpleName();
    private final int RC_SIGN_IN = 100;
    private GoogleSignInClient googleSignInClient;
    private TextInputEditText textEmail;
    private TextInputEditText textPassword;
    private View layoutProgressBar;
    private MaterialButton buttonLogin;
    private MaterialButton buttonSignUp;
    private SignInButton buttonGoogle;
    private ProgressBar progressBar;
    private FirebaseAuth auth;
    private String email;
    private String password;
    private Retrofit retrofit;
    private HomeService homeService;
    private Context context;
    private Intent intent;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        textEmail = findViewById(R.id.text_email);
        textPassword = findViewById(R.id.text_password);
        buttonLogin = findViewById(R.id.button_login);
        buttonSignUp = findViewById(R.id.button_signup);
        buttonGoogle = findViewById(R.id.button_google);
        progressBar = findViewById(R.id.loading);
        layoutProgressBar = findViewById(R.id.layout_progress_bar);

        TextWatcher afterTextChangedListener = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // ignore
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // ignore
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        };
        textEmail.addTextChangedListener(afterTextChangedListener);
        textEmail.addTextChangedListener(afterTextChangedListener);
        textEmail.setOnEditorActionListener(editorActionListener);
        textPassword.setOnEditorActionListener(editorActionListener);

        context = this;

        buttonLogin.setOnClickListener(this);
        buttonSignUp.setOnClickListener(this);
        buttonGoogle.setOnClickListener(this);

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
        if (account != null){
            Log.w(TAG, "onCreate:try:" + account.getIdToken());
        }

        retrofit = Helpers.initRetrofit(this);
        homeService = retrofit.create(HomeService.class);
    }

    @Override
    protected void onStart() {
        super.onStart();
        layoutProgressBar.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == buttonLogin.getId()){
            actionLogin(v);
        } else if (v.getId() == buttonSignUp.getId()){
            actionDaftar(v);
        } else if (v.getId() == buttonGoogle.getId()){
            googleLogin(v);
        }
    }

    private void actionDaftar(View v) {
        intent = new Intent(this, SignUpActivity.class);
        startActivity(intent);
    }

    private void actionLogin(View v) {
        if (validation()){
            layoutProgressBar.setVisibility(View.VISIBLE);
            homeService.login(email, password).enqueue(new Callback<ResponsePengguna>() {
                @Override
                public void onResponse(Call<ResponsePengguna> call, Response<ResponsePengguna> response) {
                    layoutProgressBar.setVisibility(View.GONE);
                    if (response.isSuccessful()){
                        Log.d(TAG, "JSON:"+response.body().toString());
                        if (response.body() != null && response.body().getData() != null){
                            Pengguna pengguna = response.body().getData();
                            Session session = new Session(context, "pengguna");
                            session.setPengguna(pengguna);
                            finish();
                            intent = new Intent(context, MenuActivity.class);
                            startActivity(intent);
                        } else {
                            Snackbar.make(findViewById(R.id.layout_login), "Login gagal", Snackbar.LENGTH_LONG).show();
                            textPassword.requestFocus();
                        }
                    }
                }

                @Override
                public void onFailure(Call<ResponsePengguna> call, Throwable t) {
                    t.printStackTrace();
                    layoutProgressBar.setVisibility(View.GONE);
                }
            });
        }
    }

    private boolean validation() {
        email = textEmail.getText().toString().trim();
        password = textPassword.getText().toString().trim();

        if (email.length() <= 0){
            Snackbar.make(findViewById(R.id.layout_login), "Email harus diisi", Snackbar.LENGTH_LONG).show();
            textEmail.requestFocus();
            return false;
        } else if (password.length() <= 0){
            Snackbar.make(findViewById(R.id.layout_login), "Password harus diisi", Snackbar.LENGTH_LONG).show();
            textPassword.requestFocus();
            return false;
        }
        return true;
    }

    private void googleLogin(View v) {
        Intent signInIntent = googleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            if (task.isSuccessful()) {
                handleSignInResult(task);
            }
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            layoutProgressBar.setVisibility(View.VISIBLE);
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            if (account != null){
                // Signed in successfully, show authenticated UI.
                Log.w(TAG, "signInResult:try:" + account.getIdToken());
                Log.w(TAG, "signInResult:try:" + account.getEmail());

                homeService.gmail(account.getEmail(), account.getIdToken()).enqueue(new Callback<ResponsePengguna>() {
                    @Override
                    public void onResponse(Call<ResponsePengguna> call, Response<ResponsePengguna> response) {
                        layoutProgressBar.setVisibility(View.GONE);
                        if (response.isSuccessful()){
                            if (response.body() != null){
                                Pengguna pengguna = response.body().getData();
                                Session session = new Session(context, "pengguna");
                                session.setPengguna(pengguna);
                                finish();
                                intent = new Intent(context, MainActivity.class);
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


//            firebaseAuthWithGoogle(account.getIdToken());
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(TAG, "signInResult:failed code=" + e.getStatusCode());
//            updateUI(null);
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            FirebaseUser user = auth.getCurrentUser();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
//                            Snackbar.make(mBinding.mainLayout, "Authentication Failed.", Snackbar.LENGTH_SHORT).show();
//                            updateUI(null);
                        }
                    }
                });
    }

    private TextView.OnEditorActionListener editorActionListener = new TextView.OnEditorActionListener() {

        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                actionLogin(v);
            }
            return false;
        }
    };
}
