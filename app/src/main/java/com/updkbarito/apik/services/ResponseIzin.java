package com.updkbarito.apik.services;

import android.os.Parcel;
import android.os.Parcelable;

import com.updkbarito.apik.entities.Izin;

public class ResponseIzin implements Parcelable {
    private Izin data = new Izin();
    private int code;
    private String message;

    public ResponseIzin() {
    }

    protected ResponseIzin(Parcel in) {
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

    public static final Creator<ResponseIzin> CREATOR = new Creator<ResponseIzin>() {
        @Override
        public ResponseIzin createFromParcel(Parcel in) {
            return new ResponseIzin(in);
        }

        @Override
        public ResponseIzin[] newArray(int size) {
            return new ResponseIzin[size];
        }
    };

    public Izin getData() {
        return data;
    }

    public void setData(Izin data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}