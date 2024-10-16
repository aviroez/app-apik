package com.updkbarito.apik.entities;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class IzinPersetujuan implements Parcelable {
    private int id;

    @SerializedName("izin_id")
    private int izinId;
    private Izin izin = new Izin();

    @SerializedName("pengguna_id")
    private int penggunaId;
    private Pengguna pengguna = new Pengguna();
    private String status;
    private String catatan;

    @SerializedName("jabatan_kode")
    private String jabatanKode;

    @SerializedName("jabatan_nama")
    private String jabatanName;

    @SerializedName("lokasi_kode")
    private String lokasiKode;

    @SerializedName("lokasi_nama")
    private String lokasiName;

    @SerializedName("pengguna_nama")
    private String penggunaName;

    @SerializedName("created_at")
    private String createdAt;

    @SerializedName("updated_at")
    private String updatedAt;

    @SerializedName("deleted_at")
    private String deletedAt;

    public IzinPersetujuan() {
    }

    protected IzinPersetujuan(Parcel in) {
        id = in.readInt();
        izinId = in.readInt();
        penggunaId = in.readInt();
        pengguna = in.readParcelable(Pengguna.class.getClassLoader());
        status = in.readString();
        catatan = in.readString();
        jabatanKode = in.readString();
        jabatanName = in.readString();
        lokasiKode = in.readString();
        lokasiName = in.readString();
        penggunaName = in.readString();
        createdAt = in.readString();
        updatedAt = in.readString();
        deletedAt = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(izinId);
        dest.writeInt(penggunaId);
        dest.writeParcelable(pengguna, flags);
        dest.writeString(status);
        dest.writeString(catatan);
        dest.writeString(jabatanKode);
        dest.writeString(jabatanName);
        dest.writeString(lokasiKode);
        dest.writeString(lokasiName);
        dest.writeString(penggunaName);
        dest.writeString(createdAt);
        dest.writeString(updatedAt);
        dest.writeString(deletedAt);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<IzinPersetujuan> CREATOR = new Creator<IzinPersetujuan>() {
        @Override
        public IzinPersetujuan createFromParcel(Parcel in) {
            return new IzinPersetujuan(in);
        }

        @Override
        public IzinPersetujuan[] newArray(int size) {
            return new IzinPersetujuan[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIzinId() {
        return izinId;
    }

    public void setIzinId(int izinId) {
        this.izinId = izinId;
    }

    public int getPenggunaId() {
        return penggunaId;
    }

    public void setPenggunaId(int penggunaId) {
        this.penggunaId = penggunaId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCatatan() {
        return catatan;
    }

    public void setCatatan(String catatan) {
        this.catatan = catatan;
    }

    public Izin getIzin() {
        return izin;
    }

    public void setIzin(Izin izin) {
        this.izin = izin;
    }

    public Pengguna getPengguna() {
        return pengguna;
    }

    public void setPengguna(Pengguna pengguna) {
        this.pengguna = pengguna;
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

    public String getJabatanKode() {
        return jabatanKode;
    }

    public void setJabatanKode(String jabatanKode) {
        this.jabatanKode = jabatanKode;
    }

    public String getJabatanName() {
        return jabatanName;
    }

    public void setJabatanName(String jabatanName) {
        this.jabatanName = jabatanName;
    }

    public String getLokasiKode() {
        return lokasiKode;
    }

    public void setLokasiKode(String lokasiKode) {
        this.lokasiKode = lokasiKode;
    }

    public String getLokasiName() {
        return lokasiName;
    }

    public void setLokasiName(String lokasiName) {
        this.lokasiName = lokasiName;
    }

    public String getPenggunaName() {
        return penggunaName;
    }

    public void setPenggunaName(String penggunaName) {
        this.penggunaName = penggunaName;
    }
}
