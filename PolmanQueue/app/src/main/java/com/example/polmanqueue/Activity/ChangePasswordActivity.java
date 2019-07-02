package com.example.polmanqueue.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.polmanqueue.Utils.Controller;
import com.example.polmanqueue.R;
import com.example.polmanqueue.Utils.LoginSharedPreference;

import org.json.JSONException;
import org.json.JSONObject;

public class ChangePasswordActivity extends AppCompatActivity {
    private final static String TAG = ChangePasswordActivity.class.getSimpleName();
    private EditText txtOldPassword, txtPassword, txtConfirmPassword;
    private Button btnChangePassword;
    private ImageView imgvBack;
    private LoginSharedPreference loginSharedPreference;
    private Controller controller;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        txtOldPassword = findViewById(R.id.txtOldPassword);
        txtPassword = findViewById(R.id.txtPassword);
        txtConfirmPassword = findViewById(R.id.txtConfirmPassword);
        btnChangePassword = findViewById(R.id.btnChangePassword);
        imgvBack = findViewById(R.id.imgvBack);
        loginSharedPreference = new LoginSharedPreference(this);
        controller = new Controller(this);
        imgvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intentToDashboardActivity();
            }
        });
        btnChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(txtOldPassword.getText())) {
                    txtOldPassword.setError("Ensure you have filled old password");
                } else if(TextUtils.isEmpty(txtPassword.getText())) {
                    txtPassword.setError("Ensure you have filled new password");
                } else if(TextUtils.isEmpty(txtConfirmPassword.getText())) {
                    txtConfirmPassword.setError("Ensure you have filled confirm password");
                } else if(!txtPassword.getText().toString().equals(txtConfirmPassword.getText().toString())) {
                    Toast.makeText(ChangePasswordActivity.this, "New Password must same with Confirm Password", Toast.LENGTH_SHORT).show();
                } else if(!loginSharedPreference.getCustomer().getPassword().equals(txtOldPassword.getText().toString())){
                    Toast.makeText(ChangePasswordActivity.this, "Ensure you have filled correct old password", Toast.LENGTH_SHORT).show();
                } else if(txtPassword.getText().length() > 16) {
                    Toast.makeText(ChangePasswordActivity.this, "Ensure length password must less than 16 characters", Toast.LENGTH_SHORT).show();
                } else {
                    try {
                        JSONObject object = new JSONObject();
                        object.put("id",loginSharedPreference.getCustomer().getId());
                        object.put("Auth_Token", loginSharedPreference.getToken());
                        object.put("password",txtPassword.getText().toString());
                        controller.changePassword(object);
                    } catch (JSONException e) {
                        Log.e(TAG,"Error: " + e);
                        Toast.makeText(
                                ChangePasswordActivity.this,
                                "Error: " + e,
                                Toast.LENGTH_SHORT
                        ).show();
                    }
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        intentToDashboardActivity();
    }

    public void clear() {
        txtConfirmPassword.setText("");
        txtOldPassword.setText("");
        txtPassword.setText("");
    }

    private void intentToDashboardActivity() {
        Intent intent = new Intent(ChangePasswordActivity.this,DashboardActivity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}
