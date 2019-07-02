package com.example.polmanqueue.Utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.polmanqueue.Model.Service;
import com.example.polmanqueue.R;

import java.util.ArrayList;
import java.util.List;

public class ServiceAdapter extends RecyclerView.Adapter<ServiceAdapter.ViewHolder> {
    private final static String TAG = ServiceAdapter.class.getSimpleName();
    private List<Service> services = new ArrayList<>();
    private Context context;

    public ServiceAdapter(List<Service> services, Context context) {
        this.services = services;
        this.context = context;
    }

    public void setData(List<Service> data) {
        this.services = data;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.view_customer_service,viewGroup,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int i) {
        Service item = services.get(i);
        holder.tvQueueNumber.setText(item.getService_number());
        holder.tvCustomerService.setText(item.getCustomer_service());
    }

    @Override
    public int getItemCount() {
        if(services == null) return 0;
        return services.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvQueueNumber, tvCustomerService;
        public ViewHolder(@NonNull View v) {
            super(v);
            tvQueueNumber = v.findViewById(R.id.tvQueueNumber);
            tvCustomerService = v.findViewById(R.id.tvCustomerService);
        }
    }
}
