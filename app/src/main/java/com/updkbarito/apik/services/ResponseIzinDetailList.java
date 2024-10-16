package com.updkbarito.apik.services;

import android.os.Parcel;
import android.os.Parcelable;

import com.updkbarito.apik.entities.IzinDetail;

import java.util.ArrayList;
import java.util.List;

public class ResponseIzinDetailList implements Parcelable {
    private List<IzinDetail> data = new ArrayList<>();
    private int code;
    private String message;

    public ResponseIzinDetailList() {
    }

    protected ResponseIzinDetailList(Parcel in) {
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

    public static final Creator<ResponseIzinDetailList> CREATOR = new Creator<ResponseIzinDetailList>() {
        @Override
        public ResponseIzinDetailList createFromParcel(Parcel in) {
            return new ResponseIzinDetailList(in);
        }

        @Override
        public ResponseIzinDetailList[] newArray(int size) {
            return new ResponseIzinDetailList[size];
        }
    };

    public List<IzinDetail> getData() {
        return data;
    }

    public void setData(List<IzinDetail> data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}