package com.updkbarito.apik.adapters;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.updkbarito.apik.R;
import com.updkbarito.apik.entities.IzinDetail;
import com.updkbarito.apik.entities.Dokumen;
import com.updkbarito.apik.utils.Helpers;

import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ListDokumenRecyclerView extends RecyclerView.Adapter<ListDokumenRecyclerView.ViewHolder>{
    private Context context;
    private List<Dokumen> list = new ArrayList<>();
    private List<IzinDetail> listIzinDetail = new ArrayList<>();
    private ArrayList<String> stringArrayList = new ArrayList<>();

    public ListDokumenRecyclerView(Context context, List<Dokumen> list, List<IzinDetail> listIzinDetail) {
        this.context = context;
        this.list = list;
        this.listIzinDetail = listIzinDetail;

        for (Dokumen dokumen: list) {
            stringArrayList.add(dokumen.getNama());
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View contactView = inflater.inflate(R.layout.item_dokumen, parent, false);
        return new ViewHolder(contactView);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        final Dokumen dokumen = list.get(position);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_dropdown_item_1line, stringArrayList);
        holder.textNama.setAdapter(adapter);
        holder.textNama.setText(dokumen.getNama());
        holder.imageView.setVisibility(View.GONE);

        if(dokumen.getUri() != null) {
            holder.textDeskripsi.setText("");
            String extension = FilenameUtils.getExtension(dokumen.getUri().getPath());
            if (extension != null && Helpers.containString(extension.toLowerCase(), "jpg", "png", "jpeg", "gif", "bmp", "webp")) {
                Glide.with(context)
                        .asBitmap()
                        .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                        .load(dokumen.getUri())
                        .into(new CustomTarget<Bitmap>() {
                            @Override
                            public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                                holder.imageView.setImageBitmap(resource);
                                holder.imageView.setVisibility(View.VISIBLE);
                                list.set(position, dokumen);
                            }

                            @Override
                            public void onLoadCleared(@Nullable Drawable placeholder) {

                            }
                        });
                ;
            }
        }

        for (IzinDetail izinDetail: listIzinDetail) {
            if (izinDetail.getDokumenId() == dokumen.getId() && izinDetail.getGambarId() > 0){
                dokumen.setGambarId(izinDetail.getGambarId());
                list.set(position, dokumen);
                holder.textDeskripsi.setText("");
                break;
            }
        }

        holder.buttonUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onclick(v, dokumen, position);
            }
        });
    }

    private void onclick(View v, Dokumen dokumen, int position) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_GET_CONTENT);
//        String[] mimetypes = {"application/msword", "application/pdf", "application/vnd.ms-powerpoint", "application/vnd.ms-excel"};
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            intent.putExtra(Intent.EXTRA_MIME_TYPES, mimetypes);
//        } else {
//            intent.setType("application/msword,application/pdf,application/vnd.ms-powerpoint,application/vnd.ms-excel");
//            intent.setType("application/pdf");
//        }
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        ((Activity) context).startActivityForResult(Intent.createChooser(intent, "Pilih Dokumen"), 400+position);

        list.set(position, dokumen);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textLabel;
        TextView textDeskripsi;
        ImageView imageView;
        AutoCompleteTextView textNama;
        Button buttonUpload;

        ViewHolder(final View itemView) {
            super(itemView);
            this.buttonUpload = (Button) itemView.findViewById(R.id.button_upload);
            this.textLabel = (TextView) itemView.findViewById(R.id.text_label);
            this.imageView = (ImageView) itemView.findViewById(R.id.image_view);
            this.textDeskripsi = (TextView) itemView.findViewById(R.id.text_deskripsi);
            this.textNama = (AutoCompleteTextView) itemView.findViewById(R.id.text_nama);
        }
    }

    private void openFile(File url) {
        try {

            Uri uri = Uri.fromFile(url);

            Intent intent = new Intent(Intent.ACTION_VIEW);
            if (url.toString().contains(".doc") || url.toString().contains(".docx")) {
                // Word document
                intent.setDataAndType(uri, "application/msword");
            } else if (url.toString().contains(".pdf")) {
                // PDF file
                intent.setDataAndType(uri, "application/pdf");
            } else if (url.toString().contains(".ppt") || url.toString().contains(".pptx")) {
                // Powerpoint file
                intent.setDataAndType(uri, "application/vnd.ms-powerpoint");
            } else if (url.toString().contains(".xls") || url.toString().contains(".xlsx")) {
                // Excel file
                intent.setDataAndType(uri, "application/vnd.ms-excel");
            } else if (url.toString().contains(".zip")) {
                // ZIP file
                intent.setDataAndType(uri, "application/zip");
            } else if (url.toString().contains(".rar")){
                // RAR file
                intent.setDataAndType(uri, "application/x-rar-compressed");
            } else if (url.toString().contains(".rtf")) {
                // RTF file
                intent.setDataAndType(uri, "application/rtf");
            } else if (url.toString().contains(".wav") || url.toString().contains(".mp3")) {
                // WAV audio file
                intent.setDataAndType(uri, "audio/x-wav");
            } else if (url.toString().contains(".gif")) {
                // GIF file
                intent.setDataAndType(uri, "image/gif");
            } else if (url.toString().contains(".jpg") || url.toString().contains(".jpeg") || url.toString().contains(".png")) {
                // JPG file
                intent.setDataAndType(uri, "image/jpeg");
            } else if (url.toString().contains(".txt")) {
                // Text file
                intent.setDataAndType(uri, "text/plain");
            } else if (url.toString().contains(".3gp") || url.toString().contains(".mpg") ||
                    url.toString().contains(".mpeg") || url.toString().contains(".mpe") || url.toString().contains(".mp4") || url.toString().contains(".avi")) {
                // Video files
                intent.setDataAndType(uri, "video/*");
            } else {
                intent.setDataAndType(uri, "*/*");
            }

            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        } catch (ActivityNotFoundException e) {
        }
    }
}
