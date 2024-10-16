package com.updkbarito.apik.services;

import android.os.Parcel;
import android.os.Parcelable;

import com.updkbarito.apik.entities.Akses;

import java.util.ArrayList;
import java.util.List;

public class ResponseAksesList implements Parcelable {
    private List<Akses> data = new ArrayList<>();
    private int code;
    private String message;

    public ResponseAksesList() {
    }

    protected ResponseAksesList(Parcel in) {
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

    public static final Creator<ResponseAksesList> CREATOR = new Creator<ResponseAksesList>() {
        @Override
        public ResponseAksesList createFromParcel(Parcel in) {
            return new ResponseAksesList(in);
        }

        @Override
        public ResponseAksesList[] newArray(int size) {
            return new ResponseAksesList[size];
        }
    };

    public List<Akses> getData() {
        return data;
    }

    public void setData(List<Akses> data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}