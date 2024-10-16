package com.updkbarito.apik.services;

import android.os.Parcel;
import android.os.Parcelable;

import com.updkbarito.apik.entities.PekerjaanPeralatan;

import java.util.ArrayList;
import java.util.List;

public class ResponsePekerjaanPeralatanList implements Parcelable {
    private ArrayList<PekerjaanPeralatan> data = new ArrayList<>();
    private int code;
    private String message;

    public ResponsePekerjaanPeralatanList() {
    }

    protected ResponsePekerjaanPeralatanList(Parcel in) {
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

    public static final Creator<ResponsePekerjaanPeralatanList> CREATOR = new Creator<ResponsePekerjaanPeralatanList>() {
        @Override
        public ResponsePekerjaanPeralatanList createFromParcel(Parcel in) {
            return new ResponsePekerjaanPeralatanList(in);
        }

        @Override
        public ResponsePekerjaanPeralatanList[] newArray(int size) {
            return new ResponsePekerjaanPeralatanList[size];
        }
    };

    public ArrayList<PekerjaanPeralatan> getData() {
        return data;
    }

    public void setData(ArrayList<PekerjaanPeralatan> data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}