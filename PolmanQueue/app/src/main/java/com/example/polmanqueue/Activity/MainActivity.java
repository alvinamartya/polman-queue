package com.example.polmanqueue.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.polmanqueue.Utils.Controller;
import com.example.polmanqueue.R;
import com.example.polmanqueue.Utils.LoginSharedPreference;

import org.json.JSONException;
import org.json.JSONObject;


public class MainActivity extends AppCompatActivity {

    private final static String TAG = MainActivity.class.getSimpleName();
    private EditText txtUsername, txtPassword;
    private Button btnLogin;
    private TextView tvCreate;
    private Controller controller;
    private LoginSharedPreference loginSharedPreference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txtUsername = findViewById(R.id.txtUsername);
        txtPassword = findViewById(R.id.txtPassword);
        btnLogin = findViewById(R.id.btnLogin);
        tvCreate = findViewById(R.id.tvCreate);
        controller = new Controller(this);
        loginSharedPreference = new LoginSharedPreference(this);
        if(loginSharedPreference.getHasLogin()) intentToDashboard();
        tvCreate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intentToSingUp();
            }
        });
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(txtUsername.getText())) {
                    txtUsername.setError("Ensure you have filled username");
                } else if(TextUtils.isEmpty(txtPassword.getText())) {
                    txtPassword.setError("Ensure you have filled password");
                } else {
                    try {
                        JSONObject object = new JSONObject();
                        object.put("username",txtUsername.getText().toString());
                        object.put("password",txtPassword.getText().toString());
                        controller.getCustomer(object);
                    } catch (JSONException e) {
                        Log.e(TAG,"Error: " + e);
                        Toast.makeText(
                                MainActivity.this,
                                "Error: " + e,
                                Toast.LENGTH_SHORT
                        ).show();
                    }
                }
            }
        });
    }

    public void intentToDashboard() {
        Intent intent = new Intent(MainActivity.this, DashboardActivity.class);
         startActivity(intent);
         finish();
    }

    private void intentToSingUp() {
        Intent intent = new Intent(MainActivity.this, SignUp1Activity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }
}
