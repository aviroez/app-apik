package com.updkbarito.apik.services;

import android.os.Parcel;
import android.os.Parcelable;

import com.updkbarito.apik.entities.Pengguna;

import java.util.ArrayList;
import java.util.List;

public class ResponsePenggunaList implements Parcelable {
    private List<Pengguna> data = new ArrayList<>();
    private int code;
    private String message;

    public ResponsePenggunaList() {
    }

    protected ResponsePenggunaList(Parcel in) {
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

    public static final Creator<ResponsePenggunaList> CREATOR = new Creator<ResponsePenggunaList>() {
        @Override
        public ResponsePenggunaList createFromParcel(Parcel in) {
            return new ResponsePenggunaList(in);
        }

        @Override
        public ResponsePenggunaList[] newArray(int size) {
            return new ResponsePenggunaList[size];
        }
    };

    public List<Pengguna> getData() {
        return data;
    }

    public void setData(List<Pengguna> data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}