package com.updkbarito.apik.services;

import android.os.Parcel;
import android.os.Parcelable;

import com.updkbarito.apik.entities.Izin;

import java.util.ArrayList;
import java.util.List;

public class ResponseIzinList implements Parcelable {
    private List<Izin> data = new ArrayList<>();
    private int code;
    private String message;

    public ResponseIzinList() {
    }

    protected ResponseIzinList(Parcel in) {
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

    public static final Creator<ResponseIzinList> CREATOR = new Creator<ResponseIzinList>() {
        @Override
        public ResponseIzinList createFromParcel(Parcel in) {
            return new ResponseIzinList(in);
        }

        @Override
        public ResponseIzinList[] newArray(int size) {
            return new ResponseIzinList[size];
        }
    };

    public List<Izin> getData() {
        return data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public void setData(List<Izin> data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}