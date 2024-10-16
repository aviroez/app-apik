package com.updkbarito.apik.services;

import android.os.Parcel;
import android.os.Parcelable;

import com.updkbarito.apik.entities.Dokumen;

import java.util.ArrayList;
import java.util.List;

public class ResponseDokumenList implements Parcelable {
    private List<Dokumen> data = new ArrayList<>();
    private int code;
    private String message;

    public ResponseDokumenList() {
    }

    protected ResponseDokumenList(Parcel in) {
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

    public static final Creator<ResponseDokumenList> CREATOR = new Creator<ResponseDokumenList>() {
        @Override
        public ResponseDokumenList createFromParcel(Parcel in) {
            return new ResponseDokumenList(in);
        }

        @Override
        public ResponseDokumenList[] newArray(int size) {
            return new ResponseDokumenList[size];
        }
    };

    public List<Dokumen> getData() {
        return data;
    }

    public void setData(List<Dokumen> data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}