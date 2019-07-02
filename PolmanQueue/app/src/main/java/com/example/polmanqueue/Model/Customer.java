package com.example.polmanqueue.Model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

public class Customer implements Parcelable {
    private String id;
    private String username;
    private String password;
    private String identity_number;
    private String name;
    private String telp_number;
    private String address;
    private String email;
    private Date date_of_birth;
    private String role;
    private String gender;

    public Customer() {
    }

    public Customer(String id, String username, String password, String identity_number, String name,
                    String telp_number, String address, String email, Date date_of_birth, String role,
                    String gender) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.identity_number = identity_number;
        this.name = name;
        this.telp_number = telp_number;
        this.address = address;
        this.email = email;
        this.date_of_birth = date_of_birth;
        this.role = role;
        this.gender = gender;
    }

    protected Customer(Parcel in) {
        id = in.readString();
        username = in.readString();
        password = in.readString();
        identity_number = in.readString();
        name = in.readString();
        telp_number = in.readString();
        address = in.readString();
        email = in.readString();
        role = in.readString();
        gender = in.readString();
    }

    public static final Creator<Customer> CREATOR = new Creator<Customer>() {
        @Override
        public Customer createFromParcel(Parcel in) {
            return new Customer(in);
        }

        @Override
        public Customer[] newArray(int size) {
            return new Customer[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getIdentity_number() {
        return identity_number;
    }

    public void setIdentity_number(String identity_number) {
        this.identity_number = identity_number;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTelp_number() {
        return telp_number;
    }

    public void setTelp_number(String telp_number) {
        this.telp_number = telp_number;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getDate_of_birth() {
        return date_of_birth;
    }

    public void setDate_of_birth(Date date_of_birth) {
        this.date_of_birth = date_of_birth;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(username);
        dest.writeString(password);
        dest.writeString(identity_number);
        dest.writeString(name);
        dest.writeString(telp_number);
        dest.writeString(address);
        dest.writeString(email);
        dest.writeString(role);
        dest.writeString(gender);
    }
}
