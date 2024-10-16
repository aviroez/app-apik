package com.updkbarito.apik.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintManager;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.updkbarito.apik.R;
import com.updkbarito.apik.entities.Izin;
import com.updkbarito.apik.entities.Pengguna;
import com.updkbarito.apik.utils.Helpers;
import com.updkbarito.apik.utils.Session;

import java.util.HashMap;

import retrofit2.Retrofit;

public class CetakActivity extends AppCompatActivity {

    private static String TAG = CetakActivity.class.getSimpleName();
    private int izinId;
    private WebView webView;
    private Intent intent;
    private Retrofit retrofit;
    private CetakActivity context;
    private String status;
    private String jenis;
    private Izin izin = new Izin();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cetak);
        webView = findViewById(R.id.web_view);

        context = this;

        intent = getIntent();
        izinId = intent.getIntExtra("izin_id", 0);
        jenis = intent.getStringExtra("jenis");
        status = intent.getStringExtra("status");
        izin = intent.getParcelableExtra("izin");
        retrofit = Helpers.initRetrofit(this);

        webView = findViewById(R.id.web_view);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);

        Session session = new Session(context, "pengguna");
        Pengguna pengguna = session.getPengguna();

        HashMap<String, String> mapHeader = new HashMap<>();
        mapHeader.put("Authorization", "Bearer "+ pengguna.getToken());
        String url = getString(R.string.url)+"api/izin/cetak/"+izinId+"?token="+pengguna.getToken();
        Log.d(TAG, "full_url:"+url);

        webView.loadUrl(url);

        webView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                cetakWeb(view);
            }
        });
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        kembaliAction(null);
    }

    private void cetakWeb(WebView webView) {
        PrintManager printManager = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            printManager = (PrintManager) getSystemService(Context.PRINT_SERVICE);
            PrintDocumentAdapter printAdapter = webView.createPrintDocumentAdapter();
            String jobName = getString(R.string.app_name) + "_Cetak.pdf";
            printManager.print(jobName, printAdapter, new PrintAttributes.Builder().build());
            Log.d(TAG, "cetakWeb:"+jobName);
        }
    }

    private int getScale(){
        Display display = ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        int width = display.getWidth();
        Double val = new Double(width)/new Double(360);
        val = val * 100d;
        return val.intValue();
    }

    private void kembaliAction(View view) {
        finish();
        intent = new Intent(this, IzinDetailActivity.class);
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
