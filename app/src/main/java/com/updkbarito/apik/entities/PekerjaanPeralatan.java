package com.updkbarito.apik.entities;

import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.io.File;

public class PekerjaanPeralatan implements Parcelable {

    @SerializedName("pekerjaan_id")
    private int pekerjaanId;
    @SerializedName("pekerjaan_nama")
    private String pekerjaanNama;
    private Pekerjaan pekerjaan = new Pekerjaan();

    @SerializedName("peralatan_id")
    private int peralatanId;
    @SerializedName("peralatan_nama")
    private String peralatanNama;
    private String url;
    private boolean check = false;
    private Peralatan peralatan = new Peralatan();
    @SerializedName("izin_detail")
    private IzinDetail izinDetail = new IzinDetail();
    private Bitmap bitmap;
    private Uri uri;
    private File file;

    public PekerjaanPeralatan(int pekerjaanId, int peralatanId) {
        this.pekerjaanId = pekerjaanId;
        this.peralatanId = peralatanId;
    }

    protected PekerjaanPeralatan(Parcel in) {
        pekerjaanId = in.readInt();
        pekerjaanNama = in.readString();
        pekerjaan = in.readParcelable(Pekerjaan.class.getClassLoader());
        peralatanId = in.readInt();
        peralatanNama = in.readString();
        url = in.readString();
        check = in.readByte() != 0;
        peralatan = in.readParcelable(Peralatan.class.getClassLoader());
        izinDetail = in.readParcelable(IzinDetail.class.getClassLoader());
        bitmap = in.readParcelable(Bitmap.class.getClassLoader());
        uri = in.readParcelable(Uri.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(pekerjaanId);
        dest.writeString(pekerjaanNama);
        dest.writeParcelable(pekerjaan, flags);
        dest.writeInt(peralatanId);
        dest.writeString(peralatanNama);
        dest.writeString(url);
        dest.writeByte((byte) (check ? 1 : 0));
        dest.writeParcelable(peralatan, flags);
        dest.writeParcelable(izinDetail, flags);
        dest.writeParcelable(bitmap, flags);
        dest.writeParcelable(uri, flags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<PekerjaanPeralatan> CREATOR = new Creator<PekerjaanPeralatan>() {
        @Override
        public PekerjaanPeralatan createFromParcel(Parcel in) {
            return new PekerjaanPeralatan(in);
        }

        @Override
        public PekerjaanPeralatan[] newArray(int size) {
            return new PekerjaanPeralatan[size];
        }
    };

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

    public boolean isCheck() {
        return check;
    }

    public void setCheck(boolean check) {
        this.check = check;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public IzinDetail getIzinDetail() {
        return izinDetail;
    }

    public void setIzinDetail(IzinDetail izinDetail) {
        this.izinDetail = izinDetail;
    }

    public Uri getUri() {
        return uri;
    }

    public void setUri(Uri uri) {
        this.uri = uri;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }
}
