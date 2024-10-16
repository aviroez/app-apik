package com.updkbarito.apik.adapters;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.updkbarito.apik.R;
import com.updkbarito.apik.entities.Pekerjaan;

import java.util.ArrayList;
import java.util.List;

public class ListKlasifikasiRecyclerView extends RecyclerView.Adapter<ListKlasifikasiRecyclerView.ViewHolder>{
    private Context context;
    private List<Pekerjaan> list = new ArrayList<>();
    private List<Pekerjaan> listPekerjaan = new ArrayList<>();
    private ArrayList<String> stringArrayList = new ArrayList<>();

    public ListKlasifikasiRecyclerView(Context context, List<Pekerjaan> list, List<Pekerjaan> listPekerjaan) {
        this.context = context;
        this.list = list;
        this.listPekerjaan = listPekerjaan;

        for (Pekerjaan p: listPekerjaan) {
            stringArrayList.add(p.getNama());
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View contactView = inflater.inflate(R.layout.item_klasifikasi, parent, false);
        return new ViewHolder(contactView);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        final Pekerjaan pekerjaan = list.get(position);
        if (position == 0){
            holder.imageAdd.setVisibility(View.VISIBLE);
            holder.imageRemove.setVisibility(View.GONE);
        } else {
            holder.imageAdd.setVisibility(View.GONE);
            if (position == list.size()-1) holder.imageRemove.setVisibility(View.VISIBLE);
            else holder.imageRemove.setVisibility(View.GONE);
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_dropdown_item_1line, stringArrayList);

        holder.textNama.setText(pekerjaan.getNama());
        holder.textNama.setAdapter(adapter);
        holder.textNama.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                for (Pekerjaan p: listPekerjaan) {
                    Log.d("onItemClick", stringArrayList.get(position)+"="+p.getNama()+"|"+list.size());
                    if (p.getNama().equals(stringArrayList.get(position)) && holder.getAdapterPosition() < list.size()) {
                        list.set(holder.getAdapterPosition(), p);
                        break;
                    }
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textLabel;
        AutoCompleteTextView textNama;
        ImageView imageRemove;
        ImageView imageAdd;

        ViewHolder(final View itemView) {
            super(itemView);
            this.textLabel = (TextView) itemView.findViewById(R.id.text_label);
            this.textNama = (AutoCompleteTextView) itemView.findViewById(R.id.text_nama);
            this.imageRemove = (ImageView) itemView.findViewById(R.id.image_remove);
            this.imageAdd = (ImageView) itemView.findViewById(R.id.image_add);

            this.imageAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    list.add(new Pekerjaan());
                    notifyItemInserted(list.size()-1);
                    notifyItemChanged(list.size()-2);
                }
            });

            this.imageRemove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (list.size() > 1) {
                        list.remove(list.size()-1);
                        notifyItemRemoved(list.size());
                        notifyItemChanged(list.size()-1);
                    }
                }
            });
        }
    }
}
