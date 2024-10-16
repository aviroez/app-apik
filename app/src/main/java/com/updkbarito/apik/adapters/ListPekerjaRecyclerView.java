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
import com.updkbarito.apik.entities.IzinDetail;
import com.updkbarito.apik.entities.Pengguna;

import java.util.ArrayList;
import java.util.List;

public class ListPekerjaRecyclerView extends RecyclerView.Adapter<ListPekerjaRecyclerView.ViewHolder>{
    private Context context;
    private List<Pengguna> list = new ArrayList<>();
    private List<IzinDetail> listIzinDetailPekerja = new ArrayList<>();
    private List<IzinDetail> listIzinDetail = new ArrayList<>();
    private ArrayList<String> stringArrayList = new ArrayList<>();

    public ListPekerjaRecyclerView(Context context, List<Pengguna> list, List<IzinDetail> listIzinDetailPekerja) {
        this.context = context;
        this.list = list;
        this.listIzinDetailPekerja = listIzinDetailPekerja;
        this.listIzinDetail = listIzinDetail;

        for (IzinDetail id: listIzinDetailPekerja) {
            stringArrayList.add(id.getPekerja());
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View contactView = inflater.inflate(R.layout.item_pekerja, parent, false);
        return new ViewHolder(contactView);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        final Pengguna pengguna = list.get(position);
        if (position == 0){
            holder.imageAdd.setVisibility(View.VISIBLE);
            holder.imageRemove.setVisibility(View.GONE);
        } else {
            holder.imageAdd.setVisibility(View.GONE);

            if (position == list.size()-1) holder.imageRemove.setVisibility(View.VISIBLE);
            else holder.imageRemove.setVisibility(View.GONE);
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_dropdown_item_1line, stringArrayList);

        holder.textNama.setText(pengguna.getNama());
        holder.textNama.setAdapter(adapter);
        holder.textNama.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
                for (IzinDetail izinDetail: listIzinDetailPekerja) {
                    Log.d("onItemClick", stringArrayList.get(pos)+"="+izinDetail.getPekerja()+"|"+list.size());
                    if (izinDetail.getPekerja().equals(stringArrayList.get(pos)) && holder.getAdapterPosition() < list.size()) {
                        list.set(position, new Pengguna(0, izinDetail.getPekerja()));
                        break;
                    }
                }
            }
        });
        holder.textNama.setText(pengguna.getNama());
        holder.textNama.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                Log.d("afterTextChanged", "position:"+position+"|"+s.toString());
                if (position < list.size()) {
                    pengguna.setNama(s.toString());
                    list.set(position, pengguna);
                }
            }
        });
        holder.textNama.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
                IzinDetail izinDetail = listIzinDetailPekerja.get(pos);
                list.set(position, new Pengguna(0, izinDetail.getPekerja()));
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
                    list.add(new Pengguna());
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
