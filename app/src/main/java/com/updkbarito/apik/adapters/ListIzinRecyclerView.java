package com.updkbarito.apik.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.chip.Chip;
import com.updkbarito.apik.R;
import com.updkbarito.apik.entities.Izin;
import com.updkbarito.apik.entities.IzinPersetujuan;
import com.updkbarito.apik.services.ResponseIzinList;
import com.updkbarito.apik.utils.Helpers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Callback;

public class ListIzinRecyclerView extends RecyclerView.Adapter<ListIzinRecyclerView.ViewHolder>{
    private Context context;
    private List<Izin> list = new ArrayList<>();

    public ListIzinRecyclerView(Context context, List<Izin> list, OnItemClickListener listener) {
        this.context = context;
        this.list = list;
        this.listener = listener;
    }

    // Define listener member variable
    private OnItemClickListener listener;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View contactView = inflater.inflate(R.layout.item_izin, parent, false);
        return new ViewHolder(contactView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Izin izin = list.get(position);
        holder.textNamaPekerjaan.setText(izin.getNama());
        if (izin.getLokasiLengkap() != null && !izin.getLokasiLengkap().equals("undefined")){
            holder.textLokasi.setText(izin.getLokasiLengkap());
        } else if (izin.getLokasiNama() != null){
            holder.textLokasi.setText(izin.getLokasiNama());
        }
        holder.textTanggalPengajuan.setText(izin.getTanggalPengajuan());
        holder.chipStatus.setText(Helpers.capitalize(izin.getStatus().replace("_", " ")));
        if (Helpers.containIgnoreCase(izin.getStatus(), "ditolak", "pekerjaan_ditolak")){
            holder.chipStatus.setChipBackgroundColorResource(R.color.redLight);
            holder.chipStatus.setTextColor(context.getResources().getColor(R.color.red));
        } else if (Helpers.containIgnoreCase(izin.getStatus(), "diterima", "selesai", "pekerjaan_diterima", "pekerjaan_selesai")){
            holder.chipStatus.setChipBackgroundColorResource(R.color.colorPrimary);
            holder.chipStatus.setTextColor(context.getResources().getColor(android.R.color.white));
        } else if (Helpers.containIgnoreCase(izin.getStatus(), "baru", "proses", "pekerjaan_diajukan")){
            holder.chipStatus.setChipBackgroundColorResource(R.color.colorAccent);
            holder.chipStatus.setTextColor(context.getResources().getColor(android.R.color.white));
        }
/*
        HashMap<Integer, IzinPersetujuan> map = Helpers.status(izin);
        for(Map.Entry<Integer, IzinPersetujuan> entry : map.entrySet()) {
            int key = entry.getKey();
            IzinPersetujuan izinPersetujuan = entry.getValue();
            if (key == 1){
                holder.chipStatus.setText("Diterima");
                holder.chipStatus.setChipBackgroundColorResource(R.color.colorPrimary);
                holder.chipStatus.setTextColor(context.getResources().getColor(android.R.color.white));
            } else if (key == 2){
                holder.chipStatus.setText("Diterima");
                holder.chipStatus.setChipBackgroundColorResource(R.color.colorPrimary);
                holder.chipStatus.setTextColor(context.getResources().getColor(android.R.color.white));
            } else if (key == 3){
                holder.chipStatus.setText("Diterima");
                holder.chipStatus.setChipBackgroundColorResource(R.color.colorPrimaryDark);
                holder.chipStatus.setTextColor(context.getResources().getColor(android.R.color.white));
            } else if (key == 6){
                holder.chipStatus.setText("Pekerjaan Diterima");
                holder.chipStatus.setChipBackgroundColorResource(R.color.colorPrimaryDark);
                holder.chipStatus.setTextColor(context.getResources().getColor(android.R.color.white));
            } else if (key == 7){
                holder.chipStatus.setText("Pekerjaan Diterima");
                holder.chipStatus.setChipBackgroundColorResource(R.color.colorPrimaryDark);
                holder.chipStatus.setTextColor(context.getResources().getColor(android.R.color.white));
            } else if (key == 8){
                holder.chipStatus.setText("Pekerjaan Diterima");
                holder.chipStatus.setChipBackgroundColorResource(R.color.colorPrimaryDark);
                holder.chipStatus.setTextColor(context.getResources().getColor(android.R.color.white));
            }
        }
 */
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    // Define the listener interface
    public interface OnItemClickListener {
        void onItemClick(View itemView, int position);
    }
    // Define the method that allows the parent activity or fragment to define the listener
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView textNamaPekerjaan;
        TextView textTanggalPengajuan;
        TextView textLokasi;
        Chip chipStatus;

        ViewHolder(final View itemView) {
            super(itemView);
            this.textNamaPekerjaan = (TextView) itemView.findViewById(R.id.text_nama_pekerjaan);
            this.textTanggalPengajuan = (TextView) itemView.findViewById(R.id.text_tanggal_pengajuan);
            this.textLokasi = (TextView) itemView.findViewById(R.id.text_lokasi);
            this.chipStatus = (Chip) itemView.findViewById(R.id.chip_status);
            // Setup the click listener
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Triggers click upwards to the adapter on click
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(itemView, position);
                        }
                    }
                }
            });
        }

        @Override
        public void onClick(View v) {

        }
    }
}
