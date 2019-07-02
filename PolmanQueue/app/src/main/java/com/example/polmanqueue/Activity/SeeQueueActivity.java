package com.example.polmanqueue.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.example.polmanqueue.Model.Service;
import com.example.polmanqueue.R;
import com.example.polmanqueue.Utils.ServiceAdapter;
import com.example.polmanqueue.Utils.GlobalVars;
import com.example.polmanqueue.Utils.LoginSharedPreference;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SeeQueueActivity extends AppCompatActivity {

    private final static String TAG = SeeQueueActivity.class.getSimpleName();
    private ImageView imgvBack;
    private TextView tvDesc, tvError;
    private RecyclerView rv;
    public final static String EXTRA_DESC = "desc";
    private List<Service> serviceList = new ArrayList<>();
    private ServiceAdapter serviceAdapter;
    private LoginSharedPreference loginSharedPreference;
    private ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see_queue);
        imgvBack = findViewById(R.id.imgvBack);
        tvDesc = findViewById(R.id.tvDesc);
        imgvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intentToDashboardActivity();
            }
        });
        rv = findViewById(R.id.rv);
        tvError = findViewById(R.id.tvError);
        Intent intent = getIntent();
        loginSharedPreference = new LoginSharedPreference(this);
        tvDesc.setText(intent.getStringExtra(EXTRA_DESC));
        progressBar = findViewById(R.id.progressBar);
        serviceAdapter = new ServiceAdapter(serviceList,this);
        rv.setAdapter(serviceAdapter);

        progressBar.setVisibility(View.VISIBLE);
        tvError.setVisibility(View.GONE);
        rv.setVisibility(View.GONE);
        getData();
    }

    private void getData() {
        serviceList = new ArrayList<Service>();
        AndroidNetworking.post(GlobalVars.URL_GETSEEQUEUE)
                .addBodyParameter("id", loginSharedPreference.getCustomer().getId())
                .addBodyParameter("token", loginSharedPreference.getToken())
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            serviceList = new ArrayList<>();
                            if (response.getString("message").equals("success")) {
                                JSONArray list = response.getJSONArray("data");
                                for(int i = 0; i < list.length(); i++) {
                                    JSONObject obj = list.getJSONObject(i);
                                    serviceList.add(new Service(
                                            obj.getString("id"),
                                            obj.getString("service_number"),
                                            obj.getString("customer_service_id")
                                    ));

                                }

                                if(serviceList.size() > 0) {
                                    serviceAdapter.setData(serviceList);
                                    progressBar.setVisibility(View.GONE);
                                    rv.setVisibility(View.VISIBLE);
                                    tvError.setVisibility(View.GONE);
                                } else {
                                    progressBar.setVisibility(View.GONE);
                                    rv.setVisibility(View.GONE);
                                    tvError.setVisibility(View.VISIBLE);
                                }
                            } else {
                                progressBar.setVisibility(View.GONE);
                                rv.setVisibility(View.GONE);
                                tvError.setVisibility(View.VISIBLE);
                            }
                        } catch (JSONException e) {
                            Log.e(TAG, "Error: " + e);
                            Toast.makeText(
                                    SeeQueueActivity.this,
                                    "Error: " + e,
                                    Toast.LENGTH_SHORT
                            ).show();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e(TAG, "Error: " + anError.getErrorBody());
                        Toast.makeText(SeeQueueActivity.this, "AnError: " + anError, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void intentToDashboardActivity() {
        Intent intent = new Intent(SeeQueueActivity.this,DashboardActivity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

}
