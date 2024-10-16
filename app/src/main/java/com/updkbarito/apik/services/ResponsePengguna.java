package com.updkbarito.apik.services;

import android.os.Parcel;
import android.os.Parcelable;

import com.updkbarito.apik.entities.Pengguna;

public class ResponsePengguna implements Parcelable {
    private Pengguna data = new Pengguna();
    private int code;
    private String message;

    public ResponsePengguna() {
    }

    protected ResponsePengguna(Parcel in) {
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

    public static final Creator<ResponsePengguna> CREATOR = new Creator<ResponsePengguna>() {
        @Override
        public ResponsePengguna createFromParcel(Parcel in) {
            return new ResponsePengguna(in);
        }

        @Override
        public ResponsePengguna[] newArray(int size) {
            return new ResponsePengguna[size];
        }
    };

    public Pengguna getData() {
        return data;
    }

    public void setData(Pengguna data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}