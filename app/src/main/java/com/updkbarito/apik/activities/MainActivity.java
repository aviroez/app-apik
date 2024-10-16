package com.updkbarito.apik.activities;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.CalendarView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.onesignal.OneSignal;
import com.updkbarito.apik.R;
import com.updkbarito.apik.entities.Akses;
import com.updkbarito.apik.entities.Izin;
import com.updkbarito.apik.entities.Pengguna;
import com.updkbarito.apik.fragments.IzinListFragment;
import com.updkbarito.apik.services.AksesService;
import com.updkbarito.apik.services.NotifikasiService;
import com.updkbarito.apik.services.PenggunaService;
import com.updkbarito.apik.services.ResponseAksesList;
import com.updkbarito.apik.services.ResponseIzinList;
import com.updkbarito.apik.services.ResponsePengguna;
import com.updkbarito.apik.utils.Helpers;
import com.updkbarito.apik.utils.Session;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private final String TAG = MainActivity.class.getSimpleName();
    private BottomNavigationView navView;
    private Fragment fragment;
    private Izin izin = new Izin();
    private int izinId;
    private String status = "waiting";
    private String jenis = "internal";
    private String dateFromString = null;
    private String dateToString = null;
    private Intent intent;
    private FloatingActionButton floatingActionButton;
    private MainActivity context;
    private TextView textJudul;
    private String message;
    private Session session;
    private Pengguna pengguna = new Pengguna();
    private Retrofit retrofit;
    private ImageView imageBack;
    private NotifikasiService notifikasiService;
    private Calendar calendar;
    private int viewId;
    private String dateSelected;
    private String sortSelected;
    private AksesService aksesService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        navView = findViewById(R.id.nav_view);
        textJudul = findViewById(R.id.text_judul);
        floatingActionButton = findViewById(R.id.floating_action_button);
        imageBack = findViewById(R.id.image_back);

        context = this;

        intent = getIntent();
        jenis = intent.getStringExtra("jenis");
        message = intent.getStringExtra("message");

        floatingActionButton.setVisibility(View.GONE);

        textJudul.setText("Daftar Kerja "+ Helpers.capitalize(jenis));

        if (jenis != null && jenis.equals("internal")){
            navView.getMenu().findItem(R.id.navigation_closing).setVisible(false);
        }

        navView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener);
        floatingActionButton.setOnClickListener(context);
        session = new Session(context, "pengguna");
        pengguna = session.getPengguna();

        if (pengguna == null) kembaliAction(null);

        retrofit = Helpers.initRetrofit(context);
        notifikasiService = retrofit.create(NotifikasiService.class);
        aksesService = retrofit.create(AksesService.class);

        if (message != null) {
            Helpers.showLongSnackbar(findViewById(android.R.id.content), message);
        }

        imageBack.setOnClickListener(this);

        showFragment(viewId, null);;
        showButtonAction();

//        notifikasiService.send(new HashMap<String, String>()).enqueue(new Callback<ResponseIzinList>() {
//            @Override
//            public void onResponse(Call<ResponseIzinList> call, Response<ResponseIzinList> response) {
//                if (response.isSuccessful()){
//                    if (response.body() != null && response.body().getData() != null){
//
//                    }
//                }
//            }
//
//            @Override
//            public void onFailure(Call<ResponseIzinList> call, Throwable t) {
//
//            }
//        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        Helpers.checkApplicationPermission(context);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    private boolean showFragment(int id, MenuItem item){
        viewId = id;
        int selectId = id;
        if (item != null){
            Log.d(TAG, "onNavigationItemSelected:"+item.getItemId());
            // Highlight the selected item has been done by NavigationView
            item.setChecked(true);
            // Set action bar title
            setTitle(item.getTitle());
            selectId = item.getItemId();

        }
        Class fragmentClass = null;
        switch (selectId) {
            case R.id.navigation_waiting:
                fragmentClass = IzinListFragment.class;
                status= "waiting";
                break;

            case R.id.navigation_finish:
                fragmentClass = IzinListFragment.class;
                status= "finish";
                break;

            case R.id.navigation_closing:
                fragmentClass = IzinListFragment.class;
                status= "closing";
                break;

            default : fragmentClass = IzinListFragment.class; break;
        }

        try {
            fragment = (Fragment) fragmentClass.newInstance();
            Bundle bundle = new Bundle();
            bundle.putString("jenis", jenis);
            bundle.putString("status", status);
            bundle.putString("date_from", dateFromString);
            bundle.putString("date_to", dateToString);
            bundle.putString("date_selected", dateSelected);
            bundle.putString("sort_selected", sortSelected);
            fragment.setArguments(bundle);
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.nav_host_fragment, fragment).commitAllowingStateLoss();
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private BottomNavigationView.OnNavigationItemSelectedListener onNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            return showFragment(0, menuItem);
        }
    };


    @Override
    public void onClick(View v) {
        if (v.getId() == floatingActionButton.getId()){
            finish();
            intent = new Intent(context, IzinTambahActivity.class);
            intent.putExtra("jenis", jenis);
            startActivity(intent);
        } else if (v.getId() == imageBack.getId()){
            kembaliAction(v);
        }
    }

    @Override
    public void onBackPressed() {
        kembaliAction(null);
    }

    private void settingAction(View v) {
        final Dialog customDialog = new Dialog(MainActivity.this);
        customDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        customDialog.setContentView(R.layout.dialog_filter);
        customDialog.setCancelable(true);

        final RadioButton radioHariIni = customDialog.findViewById(R.id.radio_hari_ini);
        final RadioButton radioMingguIni = customDialog.findViewById(R.id.radio_minggu_ini);
        final RadioButton radioBulanIni = customDialog.findViewById(R.id.radio_bulan_ini);
        final RadioButton radioTanggal = customDialog.findViewById(R.id.radio_tanggal);
        final LinearLayout layoutTanggal = customDialog.findViewById(R.id.layout_tanggal);
        final LinearLayout layoutLokasi = customDialog.findViewById(R.id.layout_lokasi);
        final TextInputEditText textTanggalDari = customDialog.findViewById(R.id.text_tanggal_dari);
        final TextInputEditText textTanggalSampai = customDialog.findViewById(R.id.text_tanggal_sampai);
        final RadioButton radioTerbaru = customDialog.findViewById(R.id.radio_terbaru);
        final RadioButton radioTerlama = customDialog.findViewById(R.id.radio_terlama);
//        final CalendarView calendarViewFrom = customDialog.findViewById(R.id.calendar_view_from);
//        final CalendarView calendarViewTo = customDialog.findViewById(R.id.calendar_view_to);
        final MaterialButton buttonCari = customDialog.findViewById(R.id.button_cari);
        layoutLokasi.setVisibility(View.GONE);

        if (layoutLokasi.getChildCount() > 0) layoutLokasi.removeAllViews();

        if (pengguna != null && pengguna.getAksesList().size() > 0){
            for (Akses akses: pengguna.getAksesList()){
                if (akses.getJabatan().getKode().equals("ADMIN")){

                } else {
                    if (akses.getLokasi() != null){
                        CheckBox checkBox = new CheckBox(this);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            checkBox.setButtonTintList(ContextCompat.getColorStateList(this, R.color.colorPrimary));
                        }
                        checkBox.setTag(akses.getLokasi().getId());
                        checkBox.setText(akses.getLokasi().getNama());
                        checkBox.setChecked(true);

                        layoutLokasi.addView(checkBox);
                    }
                }
            }
            layoutLokasi.setVisibility(View.GONE);
        }

        CompoundButton.OnCheckedChangeListener onCheckedChangeListener = new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (radioTanggal.isChecked()){
                    layoutTanggal.setVisibility(View.VISIBLE);
                    dateSelected = "tanggal";
                } else if (radioHariIni.isChecked()){
                    layoutTanggal.setVisibility(View.GONE);
                    dateSelected = "hari_ini";
                } else if (radioMingguIni.isChecked()){
                    layoutTanggal.setVisibility(View.GONE);
                    dateSelected = "minggu_ini";
                } else if (radioBulanIni.isChecked()){
                    layoutTanggal.setVisibility(View.GONE);
                    dateSelected = "bulan_ini";
                }

                if (radioTerbaru.isChecked()){
                    sortSelected = "terbaru";
                } else if (radioTerlama.isChecked()){
                    sortSelected = "telama";
                }
            }
        };

        radioHariIni.setOnCheckedChangeListener(onCheckedChangeListener);
        radioMingguIni.setOnCheckedChangeListener(onCheckedChangeListener);
        radioBulanIni.setOnCheckedChangeListener(onCheckedChangeListener);
        radioBulanIni.setOnCheckedChangeListener(onCheckedChangeListener);
        radioTanggal.setOnCheckedChangeListener(onCheckedChangeListener);

        radioTerbaru.setOnCheckedChangeListener(onCheckedChangeListener);
        radioTerlama.setOnCheckedChangeListener(onCheckedChangeListener);
        buttonCari.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFragment(viewId, null);
                customDialog.dismiss();
            }
        });

        textTanggalDari.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                calendarViewFrom.setVisibility(View.VISIBLE);
//                calendarViewTo.setVisibility(View.GONE);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    Calendar calendar = Calendar.getInstance();
                    DatePickerDialog datePickerDialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                            String dateString = String.format("%01d" , year) + "-" + String.format("%01d" , month) +"-" + String.format("%01d" , dayOfMonth);
                            textTanggalDari.setText(dateString);
                        }
                    }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                    datePickerDialog.show();
                }
            }
        });

        textTanggalSampai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                calendarViewFrom.setVisibility(View.GONE);
//                calendarViewTo.setVisibility(View.VISIBLE);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    Calendar calendar = Calendar.getInstance();
                    DatePickerDialog datePickerDialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                            String dateString = String.format("%01d" , year) + "-" + String.format("%01d" , month) +"-" + String.format("%01d" , dayOfMonth);
                            textTanggalSampai.setText(dateString);
                        }
                    }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                    datePickerDialog.show();
                } else {

                }
            }
        });

//        calendarViewFrom.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
//            @Override
//            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
//                String dateString = year + "-" + month +"-" + dayOfMonth;
//                textTanggalDari.setText(dateString);
//            }
//        });
//
//        calendarViewTo.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
//            @Override
//            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
//                String dateString = year + "-" + month +"-" + dayOfMonth;
//                textTanggalSampai.setText(dateString);
//            }
//        });

        customDialog.show();
    }

    public void kembaliAction(View view){
        finish();
        intent = new Intent(context, MenuActivity.class);
        startActivity(intent);
    }

    private void showButtonAction(){
        if (pengguna == null) return;
        HashMap<String, String> map = new HashMap<>();
        map.put("pengguna_id", String.valueOf(pengguna.getId()));
        aksesService.index(map).enqueue(new Callback<ResponseAksesList>() {
            @Override
            public void onResponse(Call<ResponseAksesList> call, Response<ResponseAksesList> response) {
                if (response.isSuccessful()){
                    if (response.body() != null){
                        List<Akses> aksesList = response.body().getData();
                        boolean showButton = false;
                        if (aksesList != null){
                            for (Akses akses: aksesList) {
                                if (akses.getJabatan().getKode().equals("ADMIN") || akses.getJabatan().getKode().equals("SOPHAR1") || akses.getJabatan().getKode().equals("VENDOR") ) {
                                    floatingActionButton.setVisibility(View.VISIBLE);
                                    break;
                                }
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
}
