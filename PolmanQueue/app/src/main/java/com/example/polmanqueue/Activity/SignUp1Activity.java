package com.example.polmanqueue.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.example.polmanqueue.Model.Customer;
import com.example.polmanqueue.R;
import com.example.polmanqueue.Utils.Controller;
import com.example.polmanqueue.Utils.GlobalVars;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Pattern;

public class SignUp1Activity extends AppCompatActivity {

    private EditText txtUsername, txtPassword, txtConfirmPassword;
    private Button btnSignUp;
    private final static String TAG = SignUp1Activity.class.getSimpleName();
    private ProgressDialog progress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up1);
        txtUsername = findViewById(R.id.txtUsername);
        txtPassword = findViewById(R.id.txtPassword);
        txtConfirmPassword = findViewById(R.id.txtConfirmPassword);
        btnSignUp = findViewById(R.id.btnSignUp);
        progress = new ProgressDialog(this);
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(txtUsername.getText().toString())) {
                    txtUsername.setError("Ensure you have filled username");
                } else if(TextUtils.isEmpty(txtPassword.getText().toString())) {
                    txtPassword.setError("Ensure you have filled password");
                } else if(TextUtils.isEmpty(txtConfirmPassword.getText().toString())) {
                    txtConfirmPassword.setError("Ensure you have filled confirm password");
                } else if(!Pattern.compile("^[A-Za-z0-9_]+$").matcher(txtUsername.getText().toString()).find()) {
                    Toast.makeText(SignUp1Activity.this, "Invalid format username", Toast.LENGTH_SHORT).show();
                } else if(!Pattern.compile("^[A-Za-z0-9_]+$").matcher(txtPassword.getText().toString()).find()) {
                    Toast.makeText(SignUp1Activity.this, "Invalid format password", Toast.LENGTH_SHORT).show();
                } else if(txtUsername.getText().toString().length() > 20) {
                    txtUsername.setError("Username must have length less or equals than 20");
                } else if(txtPassword.getText().toString().length() > 16) {
                    txtPassword.setError("Password must have less or equals than 16");
                } else {
                    checkUsername(txtUsername.getText().toString());
                }
            }
        });
    }

    public void checkUsername(String username) {
        progress.setTitle(getString(R.string.information));
        progress.setMessage(getString(R.string.please_wait));
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.show();
        AndroidNetworking.post(GlobalVars.URL_CHECKUSERNAME)
                .addBodyParameter("username",username)
                .setPriority(Priority.LOW)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            boolean hasUsername = response.getString("message").equals("success");
                            Log.e(TAG, "onResponse: " + hasUsername);
                            if(hasUsername == false) {
                                Customer customer = new Customer();
                                customer.setUsername(txtUsername.getText().toString());
                                customer.setPassword(txtPassword.getText().toString());
                                intentToSignUp2(customer);
                            } else {
                                Toast.makeText(SignUp1Activity.this, "Username already exists", Toast.LENGTH_SHORT).show();
                            }
                            progress.dismiss();
                        } catch (JSONException e) {
                            Log.e(TAG,"Error: " + e);
                            Toast.makeText(SignUp1Activity.this, "Error: " + e, Toast.LENGTH_SHORT).show();
                            progress.dismiss();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e(TAG,"ANError: " + anError.getErrorBody());
                        Toast.makeText(SignUp1Activity.this, "AnError: " + anError, Toast.LENGTH_SHORT).show();
                        progress.dismiss();
                    }
                });
    }

    private void intentToSignUp2(Customer customer) {
        Intent intent = new Intent(SignUp1Activity.this,SignUp2Activity.class);
        intent.putExtra(SignUp2Activity.EXTRA_DATA,customer);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    @Override
    public void onBackPressed() {
        intentToMainActivity();
    }

    private void intentToMainActivity() {
        Intent intent = new Intent(SignUp1Activity.this,MainActivity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}
