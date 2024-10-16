package com.updkbarito.apik.services;

import android.os.Parcel;
import android.os.Parcelable;

import com.updkbarito.apik.entities.Peralatan;

import java.util.ArrayList;
import java.util.List;

public class ResponsePeralatanList implements Parcelable {
    private List<Peralatan> data = new ArrayList<>();
    private int code;
    private String message;

    public ResponsePeralatanList() {
    }

    protected ResponsePeralatanList(Parcel in) {
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

    public static final Creator<ResponsePeralatanList> CREATOR = new Creator<ResponsePeralatanList>() {
        @Override
        public ResponsePeralatanList createFromParcel(Parcel in) {
            return new ResponsePeralatanList(in);
        }

        @Override
        public ResponsePeralatanList[] newArray(int size) {
            return new ResponsePeralatanList[size];
        }
    };

    public List<Peralatan> getData() {
        return data;
    }

    public void setData(List<Peralatan> data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}