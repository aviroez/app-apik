package com.updkbarito.apik.entities;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.io.File;

public class Dokumen implements Parcelable {
    private int id;
    private String nama;
    private String jenis;
    private Uri uri;

    @SerializedName("gambar_id")
    private int gambarId;

    @SerializedName("created_at")
    private String createdAt;

    @SerializedName("updated_at")
    private String updatedAt;

    @SerializedName("deleted_at")
    private String deletedAt;

    public Dokumen() {
    }

    protected Dokumen(Parcel in) {
        id = in.readInt();
        nama = in.readString();
        jenis = in.readString();
        uri = in.readParcelable(Uri.class.getClassLoader());
        gambarId = in.readInt();
        createdAt = in.readString();
        updatedAt = in.readString();
        deletedAt = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(nama);
        dest.writeString(jenis);
        dest.writeParcelable(uri, flags);
        dest.writeInt(gambarId);
        dest.writeString(createdAt);
        dest.writeString(updatedAt);
        dest.writeString(deletedAt);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Dokumen> CREATOR = new Creator<Dokumen>() {
        @Override
        public Dokumen createFromParcel(Parcel in) {
            return new Dokumen(in);
        }

        @Override
        public Dokumen[] newArray(int size) {
            return new Dokumen[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getJenis() {
        return jenis;
    }

    public void setJenis(String jenis) {
        this.jenis = jenis;
    }

    public int getGambarId() {
        return gambarId;
    }

    public void setGambarId(int gambarId) {
        this.gambarId = gambarId;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(String deletedAt) {
        this.deletedAt = deletedAt;
    }

    public Uri getUri() {
        return uri;
    }

    public void setUri(Uri uri) {
        this.uri = uri;
    }
}
