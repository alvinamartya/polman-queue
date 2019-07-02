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

import com.example.polmanqueue.Utils.Controller;
import com.example.polmanqueue.Model.Customer;
import com.example.polmanqueue.R;
import com.example.polmanqueue.Utils.LoginSharedPreference;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Pattern;

public class EditProfileActivity extends AppCompatActivity {
    private final static String TAG = EditProfileActivity.class.getSimpleName();
    private ImageView imgvBack, btnDate;
    private EditText txtIdentityNumber, txtName, txtAddress, txtDoB, txtEmail, txtPhone;
    private Button btnSave;
    private RadioGroup radioGroup;
    private RadioButton rbMale, rbFemale;
    private Date date = new Date();
    private LoginSharedPreference loginSharedPreference;
    private Controller controller;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        final Calendar calendar = Calendar.getInstance();
        date = calendar.getTime();
        final SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        imgvBack = findViewById(R.id.imgvBack);
        btnDate = findViewById(R.id.btnDate);
        txtIdentityNumber = findViewById(R.id.txtIdentityNumber);
        txtName = findViewById(R.id.txtName);
        txtAddress = findViewById(R.id.txtAddress);
        txtDoB = findViewById(R.id.txtDoB);
        txtEmail = findViewById(R.id.txtEmail);
        txtPhone = findViewById(R.id.txtPhone);
        btnSave = findViewById(R.id.btnSave);
        radioGroup = findViewById(R.id.radiogroup);
        rbMale = findViewById(R.id.rbMale);
        rbFemale = findViewById(R.id.rbFemale);
        controller = new Controller(this);

        loginSharedPreference = new LoginSharedPreference(this);
        Customer customer = loginSharedPreference.getCustomer();
        date = customer.getDate_of_birth();
        calendar.setTime(date);
        txtDoB.setText(dateFormat.format(date));
        txtIdentityNumber.setText(customer.getIdentity_number());
        txtName.setText(customer.getName());
        txtAddress.setText(customer.getAddress());
        txtEmail.setText(customer.getEmail());
        txtPhone.setText(customer.getTelp_number());
        if(customer.getGender().toLowerCase().equals("m")) {
            rbMale.setChecked(true);
        } else {
            rbFemale.setChecked(true);
        }

        imgvBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intentToDashboardActivity();
            }
        });
        btnDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar calendar2 = Calendar.getInstance();
                new DatePickerDialog(EditProfileActivity.this, new DatePickerDialog.OnDateSetListener() {
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
                    Toast.makeText(EditProfileActivity.this, "Identity number must numberic", Toast.LENGTH_SHORT).show();
                } else if (!Patterns.EMAIL_ADDRESS.matcher(txtEmail.getText().toString()).find()) {
                    Toast.makeText(EditProfileActivity.this, "Invalid format email", Toast.LENGTH_SHORT).show();
                } else if(!Patterns.PHONE.matcher(txtPhone.getText().toString()).find()) {
                    Toast.makeText(EditProfileActivity.this, "Invalid format phone number", Toast.LENGTH_SHORT).show();
                } else if(rbMale.isChecked() == false && rbFemale.isChecked() == false) {
                    Toast.makeText(EditProfileActivity.this, "Ensure you have selected gender", Toast.LENGTH_SHORT).show();
                } else if(date.compareTo(Calendar.getInstance().getTime()) > 0) {
                    Toast.makeText(EditProfileActivity.this, "Date must less or equal than today", Toast.LENGTH_SHORT).show();
                } else {
                    SimpleDateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM-dd");
                    try{
                        String gender = "";
                        if(radioGroup.getCheckedRadioButtonId() != -1) {
                            switch (radioGroup.getCheckedRadioButtonId()) {
                                case R.id.rbMale:
                                    gender = "M";
                                    break;
                                case R.id.rbFemale:
                                    gender = "F";
                                    break;
                            }
                        }

                        JSONObject object = new JSONObject();
                        object.put("ID",loginSharedPreference.getCustomer().getId());
                        object.put("identity_number",txtIdentityNumber.getText().toString());
                        object.put("name",txtName.getText().toString());
                        object.put("telp_number",txtPhone.getText().toString());
                        object.put("address",txtAddress.getText().toString());
                        object.put("email",txtEmail.getText().toString());
                        object.put("date_of_birth", dateFormat2.format(date));
                        object.put("gender",gender);
                        object.put("Auth_Token",loginSharedPreference.getToken());

                        Customer dataCustomer = loginSharedPreference.getCustomer();

                        dataCustomer.setIdentity_number(txtIdentityNumber.getText().toString());
                        dataCustomer.setName(txtName.getText().toString());
                        dataCustomer.setTelp_number(txtPhone.getText().toString());
                        dataCustomer.setAddress(txtAddress.getText().toString());
                        dataCustomer.setEmail(txtEmail.getText().toString());
                        dataCustomer.setDate_of_birth(date);
                        dataCustomer.setGender(gender);
                        loginSharedPreference.setCustomer(dataCustomer);
                        controller.changeProfile(object);
                    } catch (JSONException e) {
                        Log.e(TAG,"Error: " + e);
                        Toast.makeText(
                                EditProfileActivity.this,
                                "Error: " + e,
                                Toast.LENGTH_SHORT
                        ).show();
                    }
                }
            }
        });
    }

    private void intentToDashboardActivity() {
        Intent intent = new Intent(EditProfileActivity.this,DashboardActivity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    @Override
    public void onBackPressed() {
        intentToDashboardActivity();
    }
}
