package com.updkbarito.apik.adapters;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.gson.Gson;
import com.updkbarito.apik.R;
import com.updkbarito.apik.entities.IzinDetail;
import com.updkbarito.apik.entities.PekerjaanPeralatan;
import com.updkbarito.apik.entities.Peralatan;
import com.updkbarito.apik.utils.Helpers;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ListApdRecyclerView extends RecyclerView.Adapter<ListApdRecyclerView.ViewHolder>{
    private static String TAG = ListApdRecyclerView.class.getSimpleName();
    private Context context;
    private Activity activity;
    private List<PekerjaanPeralatan> list = new ArrayList<>();
    private List<IzinDetail> listIzinDetail = new ArrayList<>();

    public ListApdRecyclerView(Context context, List<PekerjaanPeralatan> list, List<IzinDetail> listIzinDetail, Activity activity) {
        this.context = context;
        this.list = list;
        this.listIzinDetail = listIzinDetail;
        this.activity = activity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View contactView = inflater.inflate(R.layout.item_apd_check, parent, false);
        return new ViewHolder(contactView);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        final PekerjaanPeralatan pekerjaanPeralatan = list.get(position);
        Log.d(TAG, "onBindViewHolder:"+position + " {check:"+pekerjaanPeralatan.isCheck()+", peralatan_nama:"+pekerjaanPeralatan.getPeralatanNama()+"}");
//        Log.d(TAG, "onBindViewHolder:"+new Gson().toJson(pekerjaanPeralatan));
        if (pekerjaanPeralatan.getIzinDetail() != null) pekerjaanPeralatan.setCheck(true);
        holder.imageUpload.setImageResource(R.drawable.ic_upload);
        holder.textNama.setText(pekerjaanPeralatan.getPeralatanNama());
        if (pekerjaanPeralatan.getUri() != null){
            Glide.with(context).load(pekerjaanPeralatan.getUri()).into(holder.imageUpload);
        } else if (pekerjaanPeralatan.isCheck() && pekerjaanPeralatan.getBitmap() != null) {
            holder.imageUpload.setImageBitmap(pekerjaanPeralatan.getBitmap());
        } else if (pekerjaanPeralatan.isCheck() && pekerjaanPeralatan.getIzinDetail() != null && pekerjaanPeralatan.getIzinDetail().getUrl() != null){
            Glide.with(context)
                    .asBitmap()
                    .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                    .load(Helpers.getUploadUrl(context, pekerjaanPeralatan.getIzinDetail().getUrl()))
                    .into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(Bitmap bitmap, Transition<? super Bitmap> transition) {
                            holder.imageUpload.setImageBitmap(bitmap);
                            pekerjaanPeralatan.setBitmap(bitmap);
                            pekerjaanPeralatan.setCheck(true);
                            holder.checkBox.setChecked(true);
                            list.set(position, pekerjaanPeralatan);
                        }
                    });
        }

        holder.imageUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onclickCheckBox(v, holder, pekerjaanPeralatan, position);
            }
        });

        holder.checkBox.setChecked(pekerjaanPeralatan.isCheck());
        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                list.set(position, pekerjaanPeralatan);

                onclickCheckBox(v, holder, pekerjaanPeralatan, position);
            }
        });
    }

    private void onclickCheckBox(View v, ViewHolder holder, PekerjaanPeralatan pekerjaanPeralatan, int position) {
//                Intent intent = new Intent();
//                intent.setType("image/*");
//                intent.setAction(Intent.ACTION_GET_CONTENT);
//                ((Activity) context).startActivityForResult(Intent.createChooser(intent, "Select Picture"), 200+position);
        if (holder.checkBox.isChecked()) {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                holder.checkBox.setChecked(false);
                if (!ActivityCompat.shouldShowRequestPermissionRationale(activity,
                        Manifest.permission.CAMERA)) {
                    ActivityCompat.requestPermissions(activity,
                            new String[]{Manifest.permission.CAMERA}, 103);
                }
            } else {
//                try{
//                    Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                    // Ensure that there's a camera activity to handle the intent
//                    ComponentName checkActivity = takePictureIntent.resolveActivity(context.getPackageManager());
//                    if (checkActivity != null) {
//                        // Create the File where the photo should go
//                        File file = null;
//                        try {
//                            file = Helpers.createImageFile(context);
//                        } catch (IOException ex) {
//                            // Error occurred while creating the File
//                        }
//                        // Continue only if the File was successfully created
//                        if (file != null) {
//                            Uri photoURI = FileProvider.getUriForFile(context,
//                                    "com.example.android.fileprovider",
//                                    file);
//                            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
//                            ((Activity) context).startActivityForResult(takePictureIntent, 200 + position);
//                        }
//                    }
//                } catch (Exception e){
//                    e.printStackTrace();
//                }

                Intent picIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE).addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);


                String file_path = Environment.getExternalStorageDirectory().toString() +
                        "/" + context.getResources().getString(R.string.app_name);

                File dir = new File(file_path);
                if (!dir.exists())
                    dir.mkdirs();
                // IMAGE_PATH = new File(dir, mContext.getResources().getString(R.string.app_name) + AppConstants.USER_ID + System.currentTimeMillis() + ".png");

                File file = new File(dir, context.getResources().getString(R.string.app_name) + System.currentTimeMillis() + ".jpg");
                pekerjaanPeralatan.setFile(file);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    picIntent.putExtra(MediaStore.EXTRA_OUTPUT, FileProvider.getUriForFile(context, context.getPackageName()+".fileprovider", file));
                }
                else {
                    picIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
                }

                try {
                    ((Activity) context).startActivityForResult(picIntent, 200 + position);
                } catch (Exception e){

                }



//                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                if (takePictureIntent.resolveActivity(context.getPackageManager()) != null) {
//                    ((Activity) context).startActivityForResult(takePictureIntent, 200 + position);
//                }
            }
        } else {
            pekerjaanPeralatan.setUri(null);
            pekerjaanPeralatan.setBitmap(null);
            holder.imageUpload.setImageResource(R.drawable.ic_upload);
        }
        pekerjaanPeralatan.setCheck(holder.checkBox.isChecked());
        list.set(position, pekerjaanPeralatan);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textLabel;
        TextView textNama;
        ImageButton imageUpload;
        CheckBox checkBox;

        ViewHolder(final View itemView) {
            super(itemView);
            this.imageUpload = (ImageButton) itemView.findViewById(R.id.image_upload);
            this.textLabel = (TextView) itemView.findViewById(R.id.text_label);
            this.textNama = (TextView) itemView.findViewById(R.id.text_nama);
            this.checkBox = (CheckBox) itemView.findViewById(R.id.check_box);
        }
    }
}
