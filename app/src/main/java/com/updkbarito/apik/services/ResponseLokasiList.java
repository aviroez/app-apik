package com.updkbarito.apik.services;

import android.os.Parcel;
import android.os.Parcelable;

import com.updkbarito.apik.entities.Lokasi;

import java.util.ArrayList;
import java.util.List;

public class ResponseLokasiList implements Parcelable {
    private List<Lokasi> data = new ArrayList<>();
    private int code;
    private String message;

    public ResponseLokasiList() {
    }

    protected ResponseLokasiList(Parcel in) {
        code = in.readInt();
        message = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(code);
        dest.writeString(message);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ResponseLokasiList> CREATOR = new Creator<ResponseLokasiList>() {
        @Override
        public ResponseLokasiList createFromParcel(Parcel in) {
            return new ResponseLokasiList(in);
        }

        @Override
        public ResponseLokasiList[] newArray(int size) {
            return new ResponseLokasiList[size];
        }
    };

    public List<Lokasi> getData() {
        return data;
    }

    public void setData(List<Lokasi> data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}