package com.example.polmanqueue.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.example.polmanqueue.R;
import com.example.polmanqueue.Utils.GlobalVars;
import com.example.polmanqueue.Utils.LoginSharedPreference;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class StartQueueActivity extends AppCompatActivity {

    private final static String TAG = StartQueueActivity.class.getSimpleName();
    private TextView tvHeader, tvQueueNumber, tvDesc;
    private ImageView imgvBack;
    public final static String EXTRA_HEADER = "header";
    public final static String EXTRA_SUCCESS = "success";
    public final static String EXTRA_BODY = "body";
    public final static String EXTRA_FOOTER = "footer";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_queue);
        imgvBack = findViewById(R.id.imgvBack);
        tvHeader = findViewById(R.id.tvHeader);
        tvQueueNumber = findViewById(R.id.tvQueueNumber);
        tvDesc = findViewById(R.id.tvDesc);
        imgvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intentToDashboardActivity();
            }
        });

        Intent intent = getIntent();
        boolean isSucces = intent.getBooleanExtra(EXTRA_SUCCESS,false);
        if(isSucces) tvQueueNumber.setTextSize(60);
        else tvQueueNumber.setTextSize(40);
        tvHeader.setText(intent.getStringExtra(EXTRA_HEADER));
        tvQueueNumber.setText(intent.getStringExtra(EXTRA_BODY));
        tvDesc.setText(intent.getStringExtra(EXTRA_FOOTER));
    }

    private void intentToDashboardActivity() {
        Intent intent = new Intent(StartQueueActivity.this,DashboardActivity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}
