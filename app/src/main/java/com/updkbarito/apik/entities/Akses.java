package com.updkbarito.apik.entities;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Akses implements Parcelable {
    private int id;

    @SerializedName("jabatan_id")
    private int jabatanId;
    private Jabatan jabatan = new Jabatan();

    @SerializedName("pengguna_id")
    private int penggunaId;
    private Pengguna pengguna = new Pengguna();

    @SerializedName("lokasi_id")
    private int lokasiId;
    private Lokasi lokasi = new Lokasi();

    @SerializedName("vendor_id")
    private int vendorId;
    private Vendor vendor = new Vendor();

    @SerializedName("created_at")
    private String createdAt;

    @SerializedName("updated_at")
    private String updatedAt;

    @SerializedName("deleted_at")
    private String deletedAt;

    public Akses() {
    }

    protected Akses(Parcel in) {
        id = in.readInt();
        jabatanId = in.readInt();
        jabatan = in.readParcelable(Jabatan.class.getClassLoader());
        penggunaId = in.readInt();
        pengguna = in.readParcelable(Pengguna.class.getClassLoader());
        lokasiId = in.readInt();
        lokasi = in.readParcelable(Lokasi.class.getClassLoader());
        vendorId = in.readInt();
        vendor = in.readParcelable(Vendor.class.getClassLoader());
        createdAt = in.readString();
        updatedAt = in.readString();
        deletedAt = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(jabatanId);
        dest.writeParcelable(jabatan, flags);
        dest.writeInt(penggunaId);
        dest.writeParcelable(pengguna, flags);
        dest.writeInt(lokasiId);
        dest.writeParcelable(lokasi, flags);
        dest.writeInt(vendorId);
        dest.writeParcelable(vendor, flags);
        dest.writeString(createdAt);
        dest.writeString(updatedAt);
        dest.writeString(deletedAt);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Akses> CREATOR = new Creator<Akses>() {
        @Override
        public Akses createFromParcel(Parcel in) {
            return new Akses(in);
        }

        @Override
        public Akses[] newArray(int size) {
            return new Akses[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getJabatanId() {
        return jabatanId;
    }

    public void setJabatanId(int jabatanId) {
        this.jabatanId = jabatanId;
    }

    public int getPenggunaId() {
        return penggunaId;
    }

    public void setPenggunaId(int penggunaId) {
        this.penggunaId = penggunaId;
    }

    public int getLokasiId() {
        return lokasiId;
    }

    public void setLokasiId(int lokasiId) {
        this.lokasiId = lokasiId;
    }

    public int getVendorId() {
        return vendorId;
    }

    public void setVendorId(int vendorId) {
        this.vendorId = vendorId;
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

    public Jabatan getJabatan() {
        return jabatan;
    }

    public void setJabatan(Jabatan jabatan) {
        this.jabatan = jabatan;
    }

    public Pengguna getPengguna() {
        return pengguna;
    }

    public void setPengguna(Pengguna pengguna) {
        this.pengguna = pengguna;
    }

    public Lokasi getLokasi() {
        return lokasi;
    }

    public void setLokasi(Lokasi lokasi) {
        this.lokasi = lokasi;
    }

    public Vendor getVendor() {
        return vendor;
    }

    public void setVendor(Vendor vendor) {
        this.vendor = vendor;
    }
}