package com.example.polmanqueue.Activity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Pattern;

public class SignUp2Activity extends AppCompatActivity {
    private final static String TAG = SignUp2Activity.class.getSimpleName();
    public final static String EXTRA_DATA = "extra_data";
    private EditText txtIdentityNumber, txtName, txtAddress, txtDoB,
            txtEmail, txtPhone;
    private Button btnSave;
    private ImageView btnDate;
    private RadioGroup radiogroup;
    private RadioButton rbMale,rbFemale;
    private Date date = new Date();
    private Controller controller;
    private String lastID = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up2);
        txtIdentityNumber = findViewById(R.id.txtIdentityNumber);
        txtName = findViewById(R.id.txtName);
        txtAddress = findViewById(R.id.txtAddress);
        txtDoB = findViewById(R.id.txtDoB);
        txtEmail = findViewById(R.id.txtEmail);
        txtPhone = findViewById(R.id.txtPhone);
        btnSave = findViewById(R.id.btnSave);
        btnDate = findViewById(R.id.btnDate);
        radiogroup = findViewById(R.id.radiogroup);
        rbMale = findViewById(R.id.rbMale);
        rbFemale = findViewById(R.id.rbFemale);
        controller = new Controller(this);
        final Customer customer = getIntent().getParcelableExtra(EXTRA_DATA);
        final Calendar calendar = Calendar.getInstance();
        final SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        lastID();

        btnDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar calendar2 = Calendar.getInstance();
                new DatePickerDialog(SignUp2Activity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        calendar2.set(year,month,dayOfMonth);
                        date = calendar2.getTime();
                        txtDoB.setText(dateFormat.format(calendar2.getTime()));
                    }
                },calendar.get(Calendar.YEAR),calendar.get(Calendar.MONTH),calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(txtIdentityNumber.getText().toString())) {
                    txtIdentityNumber.setError("Ensure you have filled identity number");
                } else if(TextUtils.isEmpty(txtName.getText().toString())) {
                    txtName.setError("Ensure you have filled name");
                } else if(TextUtils.isEmpty(txtAddress.getText().toString())) {
                    txtAddress.setError("Ensure you have filled address");
                } else if(TextUtils.isEmpty((txtEmail.getText().toString()))) {
                    txtEmail.setError("Ensure you have filled email");
                } else if(TextUtils.isEmpty(txtPhone.getText().toString())) {
                    txtPhone.setError("Ensure you have filled phone");
                } else if (!Pattern.compile("^[0-9]+$").matcher(txtIdentityNumber.getText().toString()).find()){
                    Toast.makeText(SignUp2Activity.this, "Identity number must numberic", Toast.LENGTH_SHORT).show();
                } else if (!Patterns.EMAIL_ADDRESS.matcher(txtEmail.getText().toString()).find()) {
                    Toast.makeText(SignUp2Activity.this, "Invalid format email", Toast.LENGTH_SHORT).show();
                } else if(rbMale.isChecked() == false && rbFemale.isChecked() == false) {
                    Toast.makeText(SignUp2Activity.this, "Ensure you have selected gender", Toast.LENGTH_SHORT).show();
                } else if(!Patterns.PHONE.matcher(txtPhone.getText().toString()).find()) {
                    Toast.makeText(SignUp2Activity.this, "Invalid format phone number", Toast.LENGTH_SHORT).show();
                } else if(date.compareTo(Calendar.getInstance().getTime()) > 0) {
                    Toast.makeText(SignUp2Activity.this, "Date must less or equal than today", Toast.LENGTH_SHORT).show();
                } else {
                    Log.e(TAG, "onClick: " + lastID);
                    SimpleDateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM-dd");
                    try{
                        String gender = "";
                        if(radiogroup.getCheckedRadioButtonId() != -1) {
                            switch (radiogroup.getCheckedRadioButtonId()) {
                                case R.id.rbMale:
                                    gender = "M";
                                    break;
                                case R.id.rbFemale:
                                    gender = "F";
                                    break;
                            }
                        }

                        JSONObject object = new JSONObject();
                        object.put("ID",lastID);
                        object.put("username",customer.getUsername());
                        object.put("password",customer.getPassword());
                        object.put("identity_number",txtIdentityNumber.getText().toString());
                        object.put("name",txtName.getText().toString());
                        object.put("telp_number",txtPhone.getText().toString());
                        object.put("address",txtAddress.getText().toString());
                        object.put("email",txtEmail.getText().toString());
                        object.put("date_of_birth", dateFormat2.format(date));
                        object.put("gender",gender);
                        controller.signUp(object);
                    } catch (JSONException e) {
                        Log.e(TAG,"Error: " + e);
                        Toast.makeText(
                                SignUp2Activity.this,
                                "Error: " + e,
                                Toast.LENGTH_SHORT
                        ).show();
                    }
                }
            }
        });
    }

    public void lastID() {
        lastID = "";
        AndroidNetworking.get(GlobalVars.URL_GETLASTIDCUSTOMER)
                .setPriority(Priority.LOW)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            lastID = response.getString("message");
                            Log.e(TAG, "onResponse: " + lastID);
                            if(response.getBoolean("status") == true) {
                                int getNumb = Integer.parseInt(lastID.substring(1)) + 1;
                                lastID = "CU" +
                                        "000".substring(String.valueOf(getNumb).length()) +
                                        String.valueOf(getNumb);
                            } else {
                                lastID = "CU001";
                            }

                            Log.e(TAG, "onResponse: " + lastID);
                        } catch (JSONException e) {
                            Log.e(TAG,"Error: " + e);
                            Toast.makeText(
                                    SignUp2Activity.this,
                                    "Error: " + e,
                                    Toast.LENGTH_SHORT
                            ).show();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e(TAG,"Error: " + anError.getErrorBody());
                    }
                });
    }

    public void intentToDashboard() {
        Intent intent = new Intent(SignUp2Activity.this, DashboardActivity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    @Override
    public void onBackPressed() {
        intentToSignUp1();
    }

    private void intentToSignUp1() {
        Intent intent = new Intent(SignUp2Activity.this,SignUp1Activity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}
