package com.updkbarito.apik.entities;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class IzinDetail implements Parcelable {
    private int id;

    @SerializedName("izin_id")
    private int izinId;

    @SerializedName("dokumen_id")
    private int dokumenId;
    private Dokumen dokumen = new Dokumen();

    @SerializedName("pekerjaan_id")
    private int pekerjaanId;
    private Pekerjaan pekerjaan = new Pekerjaan();

    @SerializedName("peralatan_id")
    private int peralatanId;
    private Peralatan peralatan = new Peralatan();

    @SerializedName("gambar_id")
    private int gambarId;
    private Gambar gambar = new Gambar();

    @SerializedName("pengguna_id")
    private int penggunaId;
    private Pengguna pengguna = new Pengguna();
    private String pekerja;

    @SerializedName("created_at")
    private String createdAt;

    @SerializedName("updated_at")
    private String updatedAt;

    @SerializedName("deleted_at")
    private String deletedAt;
    private String nama;
    private String url;

    @SerializedName("dokumen_nama")
    private String dokumenNama;

    @SerializedName("pekerjaan_nama")
    private String pekerjaanNama;

    @SerializedName("peralatan_nama")
    private String peralatanNama;

    @SerializedName("gambar_nama")
    private String gambarNama;

    @SerializedName("gambar_url")
    private String gambarUrl;

    @SerializedName("pengguna_nama")
    private String penggunaNama;

    public IzinDetail() {
    }

    protected IzinDetail(Parcel in) {
        id = in.readInt();
        izinId = in.readInt();
        dokumenId = in.readInt();
        dokumen = in.readParcelable(Dokumen.class.getClassLoader());
        pekerjaanId = in.readInt();
        pekerjaan = in.readParcelable(Pekerjaan.class.getClassLoader());
        peralatanId = in.readInt();
        peralatan = in.readParcelable(Peralatan.class.getClassLoader());
        gambarId = in.readInt();
        gambar = in.readParcelable(Gambar.class.getClassLoader());
        penggunaId = in.readInt();
        pengguna = in.readParcelable(Pengguna.class.getClassLoader());
        pekerja = in.readString();
        createdAt = in.readString();
        updatedAt = in.readString();
        deletedAt = in.readString();
        nama = in.readString();
        url = in.readString();
        dokumenNama = in.readString();
        pekerjaanNama = in.readString();
        peralatanNama = in.readString();
        gambarNama = in.readString();
        gambarUrl = in.readString();
        penggunaNama = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(izinId);
        dest.writeInt(dokumenId);
        dest.writeParcelable(dokumen, flags);
        dest.writeInt(pekerjaanId);
        dest.writeParcelable(pekerjaan, flags);
        dest.writeInt(peralatanId);
        dest.writeParcelable(peralatan, flags);
        dest.writeInt(gambarId);
        dest.writeParcelable(gambar, flags);
        dest.writeInt(penggunaId);
        dest.writeParcelable(pengguna, flags);
        dest.writeString(pekerja);
        dest.writeString(createdAt);
        dest.writeString(updatedAt);
        dest.writeString(deletedAt);
        dest.writeString(nama);
        dest.writeString(url);
        dest.writeString(dokumenNama);
        dest.writeString(pekerjaanNama);
        dest.writeString(peralatanNama);
        dest.writeString(gambarNama);
        dest.writeString(gambarUrl);
        dest.writeString(penggunaNama);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<IzinDetail> CREATOR = new Creator<IzinDetail>() {
        @Override
        public IzinDetail createFromParcel(Parcel in) {
            return new IzinDetail(in);
        }

        @Override
        public IzinDetail[] newArray(int size) {
            return new IzinDetail[size];
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

    public int getDokumenId() {
        return dokumenId;
    }

    public void setDokumenId(int dokumenId) {
        this.dokumenId = dokumenId;
    }

    public int getPekerjaanId() {
        return pekerjaanId;
    }

    public void setPekerjaanId(int pekerjaanId) {
        this.pekerjaanId = pekerjaanId;
    }

    public int getPeralatanId() {
        return peralatanId;
    }

    public void setPeralatanId(int peralatanId) {
        this.peralatanId = peralatanId;
    }

    public int getGambarId() {
        return gambarId;
    }

    public void setGambarId(int gambarId) {
        this.gambarId = gambarId;
    }

    public int getPenggunaId() {
        return penggunaId;
    }

    public void setPenggunaId(int penggunaId) {
        this.penggunaId = penggunaId;
    }

    public String getPekerja() {
        return pekerja;
    }

    public void setPekerja(String pekerja) {
        this.pekerja = pekerja;
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

    public Dokumen getDokumen() {
        return dokumen;
    }

    public void setDokumen(Dokumen dokumen) {
        this.dokumen = dokumen;
    }

    public Pekerjaan getPekerjaan() {
        return pekerjaan;
    }

    public void setPekerjaan(Pekerjaan pekerjaan) {
        this.pekerjaan = pekerjaan;
    }

    public Peralatan getPeralatan() {
        return peralatan;
    }

    public void setPeralatan(Peralatan peralatan) {
        this.peralatan = peralatan;
    }

    public Gambar getGambar() {
        return gambar;
    }

    public void setGambar(Gambar gambar) {
        this.gambar = gambar;
    }

    public Pengguna getPengguna() {
        return pengguna;
    }

    public void setPengguna(Pengguna pengguna) {
        this.pengguna = pengguna;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDokumenNama() {
        return dokumenNama;
    }

    public void setDokumenNama(String dokumenNama) {
        this.dokumenNama = dokumenNama;
    }

    public String getPekerjaanNama() {
        return pekerjaanNama;
    }

    public void setPekerjaanNama(String pekerjaanNama) {
        this.pekerjaanNama = pekerjaanNama;
    }

    public String getPeralatanNama() {
        return peralatanNama;
    }

    public void setPeralatanNama(String peralatanNama) {
        this.peralatanNama = peralatanNama;
    }

    public String getGambarNama() {
        return gambarNama;
    }

    public void setGambarNama(String gambarNama) {
        this.gambarNama = gambarNama;
    }

    public String getGambarUrl() {
        return gambarUrl;
    }

    public void setGambarUrl(String gambarUrl) {
        this.gambarUrl = gambarUrl;
    }

    public String getPenggunaNama() {
        return penggunaNama;
    }

    public void setPenggunaNama(String penggunaNama) {
        this.penggunaNama = penggunaNama;
    }
}
