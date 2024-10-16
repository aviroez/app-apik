package com.updkbarito.apik.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.updkbarito.apik.R;
import com.updkbarito.apik.entities.IzinDetail;
import com.updkbarito.apik.utils.Helpers;

import org.apache.commons.io.FilenameUtils;

import java.util.ArrayList;
import java.util.List;

public class ListIzinDetailRecyclerView extends RecyclerView.Adapter<ListIzinDetailRecyclerView.ViewHolder>{
    private final int kode; // 1:klasifikasi, 2:apd, 3:dokumen
    private final String TAG = ListIzinDetailRecyclerView.class.getSimpleName(); // 1:klasifikasi, 2:apd, 3:dokumen
    private Context context;
    private List<IzinDetail> list = new ArrayList<>();

    public ListIzinDetailRecyclerView(Context context, List<IzinDetail> list, OnItemClickListener listener, int kode) {
        this.context = context;
        this.list = list;
        this.listener = listener;
        this.kode = kode;
    }

    // Define listener member variable
    private OnItemClickListener listener;

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View contactView = inflater.inflate(R.layout.item_detail, parent, false);
        return new ViewHolder(contactView);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        IzinDetail izinDetail = list.get(position);
        holder.progressBar.setVisibility(View.GONE);
        if (izinDetail.getDokumenNama() != null){
            holder.textLabel.setText("Dokumen");
            holder.textValue.setText(izinDetail.getDokumenNama());
        } else if (izinDetail.getPeralatanNama() != null){
            holder.textLabel.setText("APD");
            holder.textValue.setText(izinDetail.getPeralatanNama());
        } else if (izinDetail.getPekerjaanNama() != null){
            holder.textLabel.setText("Klasifikasi");
            holder.textValue.setText(izinDetail.getPekerjaanNama());
            holder.imageValue.setVisibility(View.GONE);
        } else if (izinDetail.getPenggunaNama() != null){
            holder.textLabel.setText("Pengguna");
            holder.textValue.setText(izinDetail.getPenggunaNama());
        }

        if (izinDetail.getGambarUrl() != null){
            String extension = FilenameUtils.getExtension(izinDetail.getGambarUrl());
            if (Helpers.containString(extension.toLowerCase(), "jpg", "png", "jpeg", "gif", "bmp", "webp")){
                holder.progressBar.setVisibility(View.VISIBLE);
                Log.d(TAG, "upload_url:"+Helpers.getUploadUrl(context, izinDetail.getGambarUrl()));
                Glide.with(context)
                        .asBitmap()
                        .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                        .load(Helpers.getUploadUrl(context, izinDetail.getGambarUrl()))
                        .listener(new RequestListener<Bitmap>() {
                            @Override
                            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
                                holder.progressBar.setVisibility(View.GONE);
                                Log.d(TAG, "onBindViewHolder:onLoadFailed");
                                return false;
                            }

                            @Override
                            public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
                                holder.imageValue.setVisibility(View.VISIBLE);
                                holder.progressBar.setVisibility(View.GONE);
                                holder.imageValue.setImageBitmap(resource);
                                Log.d(TAG, "onBindViewHolder:onResourceReady");
                                return false;
                            }
                        })
                        .into(holder.imageValue)
//                        .into(new SimpleTarget<Bitmap>() {
//                            @Override
//                            public void onResourceReady(Bitmap bitmap, Transition<? super Bitmap> transition) {
//                                holder.imageValue.setImageBitmap(bitmap);
//                            }
//                        })
                ;
            } else {
                holder.imageValue.setImageResource(R.drawable.ic_cloud_download_24dp);
            }
        } else {
            holder.imageValue.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    // Define the listener interface
    public interface OnItemClickListener {
        void onItemClick(View itemView, int position, int kode);
    }
    // Define the method that allows the parent activity or fragment to define the listener
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView textLabel;
        TextView textValue;
        ImageButton imageValue;
        ProgressBar progressBar;

        ViewHolder(final View itemView) {
            super(itemView);
            this.textLabel = (TextView) itemView.findViewById(R.id.text_label);
            this.textValue = (TextView) itemView.findViewById(R.id.text_value);
            this.imageValue = (ImageButton) itemView.findViewById(R.id.image_value);
            this.progressBar = (ProgressBar) itemView.findViewById(R.id.progress_bar);

            // Setup the click listener
            this.imageValue.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Triggers click upwards to the adapter on click
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(itemView, position, kode);
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
