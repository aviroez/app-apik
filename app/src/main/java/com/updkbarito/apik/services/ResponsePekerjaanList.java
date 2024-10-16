package com.updkbarito.apik.services;

import android.os.Parcel;
import android.os.Parcelable;

import com.updkbarito.apik.entities.Pekerjaan;

import java.util.ArrayList;
import java.util.List;

public class ResponsePekerjaanList implements Parcelable {
    private List<Pekerjaan> data = new ArrayList<>();
    private int code;
    private String message;

    public ResponsePekerjaanList() {
    }

    protected ResponsePekerjaanList(Parcel in) {
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

    public static final Creator<ResponsePekerjaanList> CREATOR = new Creator<ResponsePekerjaanList>() {
        @Override
        public ResponsePekerjaanList createFromParcel(Parcel in) {
            return new ResponsePekerjaanList(in);
        }

        @Override
        public ResponsePekerjaanList[] newArray(int size) {
            return new ResponsePekerjaanList[size];
        }
    };

    public List<Pekerjaan> getData() {
        return data;
    }

    public void setData(List<Pekerjaan> data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}