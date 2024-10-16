package com.updkbarito.apik.fragments;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.updkbarito.apik.R;
import com.updkbarito.apik.activities.IzinDetailActivity;
import com.updkbarito.apik.activities.LoginActivity;
import com.updkbarito.apik.activities.MainActivity;
import com.updkbarito.apik.adapters.ListIzinRecyclerView;
import com.updkbarito.apik.entities.Akses;
import com.updkbarito.apik.entities.Izin;
import com.updkbarito.apik.entities.Pengguna;
import com.updkbarito.apik.services.IzinService;
import com.updkbarito.apik.services.NotifikasiService;
import com.updkbarito.apik.services.ResponseIzinList;
import com.updkbarito.apik.utils.Helpers;
import com.updkbarito.apik.utils.Session;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link IzinListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class IzinListFragment extends Fragment {
    private static String TAG = IzinListFragment.class.getSimpleName();
    private static String jenis = "internal";
    private static String status = "waiting";
    private View view;
    private Context context;
    private Retrofit retrofit;
    private IzinService izinService;
    private List<Izin> listIzin = new ArrayList<>();
    private RecyclerView recyclerView;
    private String dateSelected;
    private String sortSelected;
    private String dateFromString;
    private String dateToString;
    private Calendar calendar;
    private NotifikasiService notifikasiService;
    private FragmentActivity activity;
    private ImageView buttonSetting;
    private Session session;
    private Pengguna pengguna = new Pengguna();

    public IzinListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment IzinListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static IzinListFragment newInstance(String param1, String param2) {
        IzinListFragment fragment = new IzinListFragment();
        Bundle args = new Bundle();
        args.putString("jenis", jenis);
        args.putString("status", status);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            jenis = getArguments().getString("jenis", "internal");
            status = getArguments().getString("status");
            dateSelected = getArguments().getString("date_selected");
            sortSelected = getArguments().getString("sort_selected");
            dateFromString = getArguments().getString("date_from");
            dateToString = getArguments().getString("date_to");
        }

        if (dateSelected == null || dateSelected.length() <= 0) dateSelected = "tahun_ini";
        if (sortSelected == null || sortSelected.length() <= 0) sortSelected = "terbaru";

        context = getContext();
        activity = getActivity();
        session = new Session(context, "pengguna");
        pengguna = session.getPengguna();
        retrofit = Helpers.initRetrofit(context);
        izinService = retrofit.create(IzinService.class);

//        ListIzinRecyclerView adapter = new ListIzinRecyclerView(context, listIzin, onItemClickListener);
//        recyclerView.setAdapter(adapter);
//        recyclerView.setLayoutManager(new LinearLayoutManager(context));

        calendar = Calendar.getInstance();

        parseIzin();
    }

    @Override
    public void onStart() {
        super.onStart();
        notifikasiService = retrofit.create(NotifikasiService.class);
        HashMap<String, String> map = new HashMap<>();
        notifikasiService.onesignal(map).enqueue(new Callback<ResponseIzinList>() {
            @Override
            public void onResponse(Call<ResponseIzinList> call, Response<ResponseIzinList> response) {
                if (response.isSuccessful()){

                }
            }

            @Override
            public void onFailure(Call<ResponseIzinList> call, Throwable t) {

            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_izin_list, container, false);
        recyclerView = view.findViewById(R.id.recycler_view);

//        if (activity != null){
            buttonSetting = activity.findViewById(R.id.button_setting);

            buttonSetting.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    settingAction(view);
                }
            });
//        }

        return view;
    }

    private ListIzinRecyclerView.OnItemClickListener onItemClickListener = new ListIzinRecyclerView.OnItemClickListener() {
        @Override
        public void onItemClick(View itemView, int position) {
            Izin izin = listIzin.get(position);
            Intent intent = new Intent(context, IzinDetailActivity.class);
            intent.putExtra("jenis", jenis);
            intent.putExtra("status", status);
            intent.putExtra("izin", izin);
            intent.putExtra("izin_id", izin.getId());
            startActivity(intent);
        }
    };
    private Callback<ResponseIzinList> enqueueList = new Callback<ResponseIzinList>() {
        @Override
        public void onResponse(Call<ResponseIzinList> call, Response<ResponseIzinList> response) {
            if (response.isSuccessful()){
                if (response.body() != null && response.body().getData() != null){
                    listIzin = response.body().getData();

                    ListIzinRecyclerView adapter = new ListIzinRecyclerView(context, listIzin, onItemClickListener);
                    recyclerView.setAdapter(adapter);
                    recyclerView.setLayoutManager(new LinearLayoutManager(context));
                } else if (response.body().getCode() == 201){
                    Session session = new Session(context, "pengguna");
                    session.setPengguna(null);

//                    ((Activity) context).finish();
                    if (getActivity() != null){
                        Intent intent = new Intent(context, LoginActivity.class);
                        startActivity(intent);
                    }
                }
            }
        }

        @Override
        public void onFailure(Call<ResponseIzinList> call, Throwable t) {

        }
    };

    private void settingAction(View v) {
        final Dialog customDialog = new Dialog(context);
        customDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        customDialog.setContentView(R.layout.dialog_filter);
        customDialog.setCancelable(true);

        final RadioButton radioHariIni = customDialog.findViewById(R.id.radio_hari_ini);
        final RadioButton radioMingguIni = customDialog.findViewById(R.id.radio_minggu_ini);
        final RadioButton radioBulanIni = customDialog.findViewById(R.id.radio_bulan_ini);
        final RadioButton radioTahunIni = customDialog.findViewById(R.id.radio_tahun_ini);
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

        if (dateSelected.equals("hari_ini")){
            radioHariIni.setChecked(true);
        } else if (dateSelected.equals("minggu_ini")){
            radioMingguIni.setChecked(true);
        } else if (dateSelected.equals("bulan_ini")){
            radioBulanIni.setChecked(true);
        } else if (dateSelected.equals("tahun_ini")){
            radioTahunIni.setChecked(true);
        }

        if (sortSelected.equals("terbaru")){
            radioTerbaru.setChecked(true);
        } else if (sortSelected.equals("terlama")){
            radioTerlama.setChecked(true);
        }

        if (layoutLokasi.getChildCount() > 0) layoutLokasi.removeAllViews();

        if (pengguna != null && pengguna.getAksesList().size() > 0){
            for (Akses akses: pengguna.getAksesList()){
                if (akses.getJabatan().getKode().equals("ADMIN")){

                } else {
                    if (akses.getLokasi() != null){
                        CheckBox checkBox = new CheckBox(context);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            checkBox.setButtonTintList(ContextCompat.getColorStateList(context, R.color.colorPrimary));
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
                    radioHariIni.setChecked(true);
                    dateSelected = "minggu_ini";
                } else if (radioBulanIni.isChecked()){
                    layoutTanggal.setVisibility(View.GONE);
                    dateSelected = "bulan_ini";
                } else if (radioTahunIni.isChecked()){
                    layoutTanggal.setVisibility(View.GONE);
                    dateSelected = "tahun_ini";
                }

                if (radioTerbaru.isChecked()){
                    sortSelected = "terbaru";
                } else if (radioTerlama.isChecked()){
                    sortSelected = "terlama";
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
                customDialog.dismiss();
                parseIzin();
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
                            dateFromString = dateString;
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
                            dateToString = dateString;
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

    private void parseIzin(){
        if (dateFromString == null || dateFromString.length() <= 0){
            dateFromString = Helpers.reformatDate(calendar.getTime(), "yyyy-MM-dd");
        }
        Calendar calendar = Calendar.getInstance();

        if (dateToString == null || dateToString.length() <= 0){
            calendar.add(Calendar.DATE, 7);
            dateToString = Helpers.reformatDate(calendar.getTime(), "yyyy-MM-dd");
        }

        calendar = Calendar.getInstance();
//        if (dateSelected == null) dateSelected = "tahun_ini";
//        if (sortSelected == null) sortSelected = "terbaru";

        if (dateSelected.equals("hari_ini")) {
            dateFromString = Helpers.reformatDate(calendar.getTime(), "yyyy-MM-dd");
            dateToString = Helpers.reformatDate(calendar.getTime(), "yyyy-MM-dd");
        } else if (dateSelected.equals("minggu_ini")) {
            calendar.set(Calendar.DAY_OF_WEEK, 0);
            dateFromString = Helpers.reformatDate(calendar.getTime(), "yyyy-MM-dd");
            calendar.add(Calendar.DATE, +7);
            dateToString = Helpers.reformatDate(calendar.getTime(), "yyyy-MM-dd");
        } else if (dateSelected.equals("bulan_ini")) {
            dateFromString = Helpers.reformatDate(calendar.getTime(), "yyyy-MM-01");
            dateToString = Helpers.reformatDate(calendar.getTime(), "yyyy-MM-31");
        } else if (dateSelected.equals("tahun_ini")) {
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            Log.d(TAG, "tahun_ini:"+year+":"+month);
            if (month > 1) {
                dateFromString = Helpers.reformatDate(calendar.getTime(), "yyyy-01-01");
            } else {
                dateFromString = (year-1) + "-01-01";
            }
            if (month < 11){
                dateToString = Helpers.reformatDate(calendar.getTime(), "yyyy-12-31");
            } else {
                dateToString = (year+1) + "-12-31";
            }
        }

        HashMap<String, String> map = new HashMap<>();
        map.put("jenis", jenis);
        if (dateFromString != null) map.put("tanggal_mulai", dateFromString);
        if (dateToString != null) map.put("tanggal_sampai", dateToString);
        if (dateSelected != null) map.put("tanggal_dipilih", dateSelected);
        if (sortSelected != null) map.put("urutan_dipilih", sortSelected);
        Log.d(TAG, "onStart:"+jenis+":"+dateFromString+" "+dateToString);

        if (status.equals("waiting")) map.put("status", "proses,ditolak");
        else if (status.equals("finish")) map.put("status", "diterima,selesai,pekerjaan_diajukan,pekerjaan_ditolak");
        else if (status.equals("closing")) map.put("status", "pekerjaan_diterima,pekerjaan_selesai");

        if (jenis.equals("internal")){
            Log.d(TAG, "onStart:internal");
            izinService.internal(map).enqueue(enqueueList);
        } else if (jenis.equals("eksternal")){
            Log.d(TAG, "onStart:eksternal");
            izinService.eksternal(map).enqueue(enqueueList);
        } else {
            Log.d(TAG, "onStart:else");
            izinService.index(map).enqueue(enqueueList);
        }
    }
}
