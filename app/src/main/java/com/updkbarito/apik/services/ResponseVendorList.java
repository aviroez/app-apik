package com.updkbarito.apik.services;

import android.os.Parcel;
import android.os.Parcelable;

import com.updkbarito.apik.entities.Vendor;

import java.util.ArrayList;
import java.util.List;

public class ResponseVendorList implements Parcelable {
    private List<Vendor> data = new ArrayList<>();
    private int code;
    private String message;

    public ResponseVendorList() {
    }

    protected ResponseVendorList(Parcel in) {
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

    public static final Creator<ResponseVendorList> CREATOR = new Creator<ResponseVendorList>() {
        @Override
        public ResponseVendorList createFromParcel(Parcel in) {
            return new ResponseVendorList(in);
        }

        @Override
        public ResponseVendorList[] newArray(int size) {
            return new ResponseVendorList[size];
        }
    };

    public List<Vendor> getData() {
        return data;
    }

    public void setData(List<Vendor> data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}