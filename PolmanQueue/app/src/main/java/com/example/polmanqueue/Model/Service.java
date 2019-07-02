package com.example.polmanqueue.Model;

import android.os.Parcel;
import android.os.Parcelable;

public class Service implements Parcelable {
    private String id;
    private String service_number;
    private String customer_service;

    public Service() {
    }

    public Service(String id, String service_number, String customer_service) {
        this.id = id;
        this.service_number = service_number;
        this.customer_service = customer_service;
    }

    protected Service(Parcel in) {
        id = in.readString();
        service_number = in.readString();
        customer_service = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(service_number);
        dest.writeString(customer_service);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Service> CREATOR = new Creator<Service>() {
        @Override
        public Service createFromParcel(Parcel in) {
            return new Service(in);
        }

        @Override
        public Service[] newArray(int size) {
            return new Service[size];
        }
    };

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getService_number() {
        return service_number;
    }

    public void setService_number(String service_number) {
        this.service_number = service_number;
    }

    public String getCustomer_service() {
        return customer_service;
    }

    public void setCustomer_service(String customer_service) {
        this.customer_service = customer_service;
    }
}
