package com.updkbarito.apik.entities;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Notifikasi implements Parcelable {
    private int id;
    private String judul;
    private String pesan;
    private String web;
    private String status;

    @SerializedName("pengguna_id")
    private int penggunaId;
    private Pengguna pengguna = new Pengguna();

    @SerializedName("izin_id")
    private int izinId;
    private Izin izin = new Izin();

    @SerializedName("device_token")
    private String deviceToken;

    @SerializedName("player_id")
    private String playerId;

    @SerializedName("created_at")
    private String createdAt;

    @SerializedName("updated_at")
    private String updatedAt;

    @SerializedName("deleted_at")
    private String deletedAt;

    public Notifikasi() {
    }

    protected Notifikasi(Parcel in) {
        id = in.readInt();
        judul = in.readString();
        pesan = in.readString();
        web = in.readString();
        status = in.readString();
        penggunaId = in.readInt();
        pengguna = in.readParcelable(Pengguna.class.getClassLoader());
        izinId = in.readInt();
        deviceToken = in.readString();
        playerId = in.readString();
        createdAt = in.readString();
        updatedAt = in.readString();
        deletedAt = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(judul);
        dest.writeString(pesan);
        dest.writeString(web);
        dest.writeString(status);
        dest.writeInt(penggunaId);
        dest.writeParcelable(pengguna, flags);
        dest.writeInt(izinId);
        dest.writeString(deviceToken);
        dest.writeString(playerId);
        dest.writeString(createdAt);
        dest.writeString(updatedAt);
        dest.writeString(deletedAt);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Notifikasi> CREATOR = new Creator<Notifikasi>() {
        @Override
        public Notifikasi createFromParcel(Parcel in) {
            return new Notifikasi(in);
        }

        @Override
        public Notifikasi[] newArray(int size) {
            return new Notifikasi[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getJudul() {
        return judul;
    }

    public void setJudul(String judul) {
        this.judul = judul;
    }

    public String getPesan() {
        return pesan;
    }

    public void setPesan(String pesan) {
        this.pesan = pesan;
    }

    public String getWeb() {
        return web;
    }

    public void setWeb(String web) {
        this.web = web;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getPenggunaId() {
        return penggunaId;
    }

    public void setPenggunaId(int penggunaId) {
        this.penggunaId = penggunaId;
    }

    public int getIzinId() {
        return izinId;
    }

    public void setIzinId(int izinId) {
        this.izinId = izinId;
    }

    public String getDeviceToken() {
        return deviceToken;
    }

    public void setDeviceToken(String deviceToken) {
        this.deviceToken = deviceToken;
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

    public Pengguna getPengguna() {
        return pengguna;
    }

    public void setPengguna(Pengguna pengguna) {
        this.pengguna = pengguna;
    }

    public Izin getIzin() {
        return izin;
    }

    public void setIzin(Izin izin) {
        this.izin = izin;
    }

    public String getPlayerId() {
        return playerId;
    }

    public void setPlayerId(String playerId) {
        this.playerId = playerId;
    }
}
