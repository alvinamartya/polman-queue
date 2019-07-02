package com.example.polmanqueue.Utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.polmanqueue.Model.Customer;
import com.google.gson.Gson;

import java.lang.reflect.Member;

public class LoginSharedPreference {
    private String KEY_CUSTOMER = "customer";
    private String KEY_HASLOGIN = "haslogin";
    private String KEY_TOKEN = "token";
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private Gson gson;

    public LoginSharedPreference(Context context) {
        String PREF_NAME = "loginpref";
        sharedPreferences = context.getSharedPreferences(PREF_NAME,Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        gson = new Gson();
    }

    public Customer getCustomer() {
        String jsonToModel = sharedPreferences.getString(KEY_CUSTOMER,null);
        if(jsonToModel != null)return gson.fromJson(jsonToModel, Customer.class);
        else return null;
    }

    public void setCustomer(Customer customer) {
        if(customer != null) {
            String json = gson.toJson(customer);
            editor.putString(KEY_CUSTOMER,json);
            editor.apply();
        }
    }

    public boolean getHasLogin() {return sharedPreferences.getBoolean(KEY_HASLOGIN,false); }
    public void setHasLogin(boolean hashLogin) {
        editor.putBoolean(KEY_HASLOGIN,hashLogin);
        editor.apply();
    }

    public String getToken() {return sharedPreferences.getString(KEY_TOKEN,null);}
    public void setToken(String token) {
        editor.putString(KEY_TOKEN, token);
        editor.apply();
    }
}
