package com.updkbarito.apik.utils;
import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.updkbarito.apik.entities.Pengguna;

import java.util.Map;
import java.util.Set;

public class Session {
    private Context context;
    private String key;
    private SharedPreferences sharedPref;

    public Session(Context context) {
        this.context = context;
        this.key = "default";

        sharedPref = context.getSharedPreferences(key, Context.MODE_PRIVATE);
    }

    public Session(Context context, String key) {
        this.context = context;
        this.key = key;

        sharedPref = context.getSharedPreferences(key, Context.MODE_PRIVATE);
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
        sharedPref = context.getSharedPreferences(key, Context.MODE_PRIVATE);
    }

    public String getValue(String key, String defaultValue){
        return sharedPref.getString(key, defaultValue);
    }

    public boolean getValue(String key, boolean defaultValue){
        return sharedPref.getBoolean(key, defaultValue);
    }

    public int getValue(String key, int defaultValue){
        return sharedPref.getInt(key, defaultValue);
    }

    public float getValue(String key, float defaultValue){
        return sharedPref.getFloat(key, defaultValue);
    }

    public long getValue(String key, long defaultValue){
        return sharedPref.getLong(key, defaultValue);
    }

    public Set<String> getValue(String key){
        return sharedPref.getStringSet(key, null);
    }

    public Map<String, ?> getAll(){
        return sharedPref.getAll();
    }

    public Pengguna getPengguna(){
        String jsonPengguna = sharedPref.getString("pengguna", null);
        return new Gson().fromJson(jsonPengguna, Pengguna.class);
    }

    public void setPengguna(Pengguna pengguna){
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("pengguna", new Gson().toJson(pengguna));
        editor.apply();
    }

    public void updateData(String key, Object value){
        SharedPreferences.Editor editor = sharedPref.edit();
        if (value instanceof Integer){
            editor.putInt(key, (Integer) value);
        } else if (value instanceof Boolean){
            editor.putBoolean(key, (Boolean) value);
        } else if (value instanceof Float){
            editor.putFloat(key, (Float) value);
        }else if (value instanceof Long){
            editor.putLong(key, (Long) value);
        } else {
            editor.putString(key, (String) value);
        }
//        editor.putStringSet(key, value);
        editor.apply();
    }

    public void clear(){
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.clear();
        editor.apply();
    }
}