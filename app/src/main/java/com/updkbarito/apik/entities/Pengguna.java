package com.updkbarito.apik.entities;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Pengguna implements Parcelable {
    private int id;
    private String nama;
    private String email;

    @SerializedName("no_hp")
    private String noHp;
    private String password;
    private String token;

    @SerializedName("firebase_uid")
    private String firebaseUid;

    @SerializedName("firebase_token")
    private String firebaseToken;

    @SerializedName("google_token")
    private String googleToken;

    @SerializedName("device_token")
    private String deviceToken;

    @SerializedName("player_id")
    private String playerId;

    private String signature;

    @SerializedName("created_at")
    private String createdAt;

    @SerializedName("updated_at")
    private String updatedAt;

    @SerializedName("deleted_at")
    private String deletedAt;

    @SerializedName("akses_list")
    private ArrayList<Akses> aksesList = new ArrayList<>();

    public Pengguna() {
    }

    public Pengguna(int id, String nama) {
        this.id = id;
        this.nama = nama;
    }

    protected Pengguna(Parcel in) {
        id = in.readInt();
        nama = in.readString();
        email = in.readString();
        noHp = in.readString();
        password = in.readString();
        token = in.readString();
        firebaseUid = in.readString();
        firebaseToken = in.readString();
        googleToken = in.readString();
        deviceToken = in.readString();
        playerId = in.readString();
        signature = in.readString();
        createdAt = in.readString();
        updatedAt = in.readString();
        deletedAt = in.readString();
        aksesList = in.createTypedArrayList(Akses.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(nama);
        dest.writeString(email);
        dest.writeString(noHp);
        dest.writeString(password);
        dest.writeString(token);
        dest.writeString(firebaseUid);
        dest.writeString(firebaseToken);
        dest.writeString(googleToken);
        dest.writeString(deviceToken);
        dest.writeString(playerId);
        dest.writeString(signature);
        dest.writeString(createdAt);
        dest.writeString(updatedAt);
        dest.writeString(deletedAt);
        dest.writeTypedList(aksesList);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Pengguna> CREATOR = new Creator<Pengguna>() {
        @Override
        public Pengguna createFromParcel(Parcel in) {
            return new Pengguna(in);
        }

        @Override
        public Pengguna[] newArray(int size) {
            return new Pengguna[size];
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNoHp() {
        return noHp;
    }

    public void setNoHp(String noHp) {
        this.noHp = noHp;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getFirebaseUid() {
        return firebaseUid;
    }

    public void setFirebaseUid(String firebaseUid) {
        this.firebaseUid = firebaseUid;
    }

    public String getFirebaseToken() {
        return firebaseToken;
    }

    public void setFirebaseToken(String firebaseToken) {
        this.firebaseToken = firebaseToken;
    }

    public String getGoogleToken() {
        return googleToken;
    }

    public void setGoogleToken(String googleToken) {
        this.googleToken = googleToken;
    }

    public String getDeviceToken() {
        return deviceToken;
    }

    public void setDeviceToken(String deviceToken) {
        this.deviceToken = deviceToken;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public ArrayList<Akses> getAksesList() {
        return aksesList;
    }

    public void setAksesList(ArrayList<Akses> aksesList) {
        this.aksesList = aksesList;
    }

    public String getPlayerId() {
        return playerId;
    }

    public void setPlayerId(String playerId) {
        this.playerId = playerId;
    }
}
