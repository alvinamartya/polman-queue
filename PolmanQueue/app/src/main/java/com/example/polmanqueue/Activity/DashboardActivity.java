package com.example.polmanqueue.Activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.example.polmanqueue.HashTable.HashTable;
import com.example.polmanqueue.R;
import com.example.polmanqueue.Utils.GlobalVars;
import com.example.polmanqueue.Utils.LoginSharedPreference;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class DashboardActivity extends AppCompatActivity {

    private final static String TAG = DashboardActivity.class.getSimpleName();
    private TextView tvName;
    private Button btnEditProfile, btnChangePassword, btnStartQueuing, btnseeQueue;
    private TextView tvLogout;
    private LoginSharedPreference loginSharedPreference;
    private ProgressDialog progress;
    private HashTable hashTable;
    private String lastID = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        tvName = findViewById(R.id.tvName);
        btnEditProfile = findViewById(R.id.btnEditProfile);
        btnChangePassword = findViewById(R.id.btnChangePassword);
        btnStartQueuing = findViewById(R.id.btnStartQueuing);
        btnseeQueue = findViewById(R.id.btnseeQueue);
        tvLogout = findViewById(R.id.tvLogout);
        loginSharedPreference = new LoginSharedPreference(this);
        progress = new ProgressDialog(this);
        tvName.setText(loginSharedPreference.getCustomer().getName());
        tvLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });
        btnEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intentToEditProfile();
            }
        });
        btnChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intentToChangePassword();
            }
        });
        btnStartQueuing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getServiceToday();
            }
        });
        btnseeQueue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                seeServiceToday();
            }
        });
        hashTable = new HashTable();
    }

    private void seeServiceToday() {
        hashTable = new HashTable();
        progress.setTitle(getString(R.string.information));
        progress.setMessage(getString(R.string.please_wait));
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.show();
        AndroidNetworking.post(GlobalVars.URL_GETSERVICECUSTOMER)
                .addBodyParameter("id", loginSharedPreference.getCustomer().getId())
                .addBodyParameter("token", loginSharedPreference.getToken())
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.getString("message").equals("success")) {
                                intentToSeeQueue("Your are now in queue " + response.getJSONObject("data").getString("service_number"));
                            } else {
                                intentToSeeQueue("You don't have queue number");
                            }
                            progress.dismiss();
                        } catch (JSONException e) {
                            Log.e(TAG, "Error: " + e);
                            Toast.makeText(
                                    DashboardActivity.this,
                                    "Error: " + e,
                                    Toast.LENGTH_SHORT
                            ).show();
                            progress.dismiss();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e(TAG, "Error: " + anError.getErrorBody());
                        progress.dismiss();
                        Toast.makeText(DashboardActivity.this, "AnError: " + anError, Toast.LENGTH_SHORT).show();
                    }
                });

    }

    private void getServiceToday() {
        hashTable = new HashTable();
        progress.setTitle(getString(R.string.information));
        progress.setMessage(getString(R.string.please_wait));
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.show();
        lastID();
        AndroidNetworking.post(GlobalVars.URL_GETSERVICECUSTOMER)
                .addBodyParameter("id", loginSharedPreference.getCustomer().getId())
                .addBodyParameter("token", loginSharedPreference.getToken())
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.getString("message").equals("success")) {
                                intentToStartQueing("Congratulation!", response.getJSONObject("data").getString("service_number"),
                                        "Please queue to get service from our Customer Service", true);
                            } else if (response.getString("message").equals("failed")) {
                                AndroidNetworking.post(GlobalVars.URL_GETSERVICETODAY)
                                        .addBodyParameter("id", loginSharedPreference.getCustomer().getId())
                                        .addBodyParameter("token", loginSharedPreference.getToken())
                                        .setPriority(Priority.MEDIUM)
                                        .build()
                                        .getAsJSONObject(new JSONObjectRequestListener() {
                                            @Override
                                            public void onResponse(JSONObject response) {
                                                try {
                                                    if (response.getString("message").equals("success")) {
                                                        if (response.getInt("count") >= 100) {
                                                            intentToStartQueing("Failed!", "Queue number is not available",
                                                                    "The queue number has reached the limit", false);
                                                        } else {
                                                            JSONArray list = response.getJSONArray("data");
                                                            for (int i = 0; i < list.length(); i++) {
                                                                JSONObject serviceObj = list.getJSONObject(i);
                                                                hashTable.insert(serviceObj.getString("service_number"));
                                                            }

                                                            String serviceNumb = hashTable.getLastData(loginSharedPreference.getCustomer().getRole());
                                                            JSONObject object = new JSONObject();
                                                            object.put("id", loginSharedPreference.getCustomer().getId());
                                                            object.put("token", loginSharedPreference.getToken());
                                                            object.put("service_id", lastID);
                                                            object.put("service_number", serviceNumb);
                                                            insertDataToDatabase(object);
                                                        }
                                                    } else if(response.getString("message").equals("failed")) {
                                                        String serviceNumb = hashTable.getLastData(loginSharedPreference.getCustomer().getRole());
                                                        JSONObject object = new JSONObject();
                                                        object.put("id", loginSharedPreference.getCustomer().getId());
                                                        object.put("token", loginSharedPreference.getToken());
                                                        object.put("service_id", lastID);
                                                        object.put("service_number", serviceNumb);
                                                        insertDataToDatabase(object);
                                                    } else {
                                                        Toast.makeText(DashboardActivity.this, response.getString("message"), Toast.LENGTH_SHORT).show();
                                                    }
                                                } catch (JSONException e) {
                                                    Log.e(TAG, "Error: " + e);
                                                    Toast.makeText(
                                                            DashboardActivity.this,
                                                            "Error: " + e,
                                                            Toast.LENGTH_SHORT
                                                    ).show();
                                                    progress.dismiss();
                                                }
                                            }

                                            @Override
                                            public void onError(ANError anError) {
                                                Log.e(TAG, "Error: " + anError.getErrorBody());
                                                progress.dismiss();
                                                Toast.makeText(DashboardActivity.this, "AnError: " + anError, Toast.LENGTH_SHORT).show();
                                            }
                                        });
                            } else {
                                Toast.makeText(
                                        DashboardActivity.this,
                                        response.getString("message"),
                                        Toast.LENGTH_SHORT).show();
                                progress.dismiss();
                            }
                        } catch (JSONException e) {
                            Log.e(TAG, "Error: " + e);
                            Toast.makeText(
                                    DashboardActivity.this,
                                    "Error: " + e,
                                    Toast.LENGTH_SHORT
                            ).show();
                            progress.dismiss();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e(TAG, "Error: " + anError.getErrorBody());
                        progress.dismiss();
                        Toast.makeText(DashboardActivity.this, "AnError: " + anError, Toast.LENGTH_SHORT).show();
                    }
                });

    }

    private void insertDataToDatabase(final JSONObject object) {
        AndroidNetworking.post(GlobalVars.URL_SERVICE)
                .addJSONObjectBody(object)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if (response.getString("message").equals("success")) {
                                intentToStartQueing("Congratulation!", object.getString("service_number"),
                                        "Please queue to get service from our Customer Service", true);
                            } else {
                                Toast.makeText(
                                        DashboardActivity.this,
                                        response.getString("message"),
                                        Toast.LENGTH_SHORT).show();
                            }
                            progress.dismiss();
                        } catch (JSONException e) {
                            Log.e(TAG, "Error: " + e);
                            Toast.makeText(
                                    DashboardActivity.this,
                                    "Error: " + e,
                                    Toast.LENGTH_SHORT
                            ).show();
                            progress.dismiss();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e(TAG, "Error: " + anError.getErrorBody());
                        progress.dismiss();
                        Toast.makeText(DashboardActivity.this, "AnError: " + anError, Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void lastID() {
        lastID = "";
        AndroidNetworking.post(GlobalVars.URL_GETLASTIDSERVICE)
                .addBodyParameter("id", loginSharedPreference.getCustomer().getId())
                .addBodyParameter("token", loginSharedPreference.getToken())
                .setPriority(Priority.LOW)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            lastID = response.getString("message");
                            Log.e(TAG, "onResponse: " + lastID);
                            if (response.getBoolean("status") == true) {
                                int getNumb = Integer.parseInt(lastID.substring(1)) + 1;
                                lastID = "S" +
                                        "0000".substring(String.valueOf(getNumb).length()) +
                                        String.valueOf(getNumb);
                            } else {
                                lastID = "S0001";
                            }

                            Log.e(TAG, "onResponse: " + lastID);
                        } catch (JSONException e) {
                            Log.e(TAG, "Error: " + e);
                            Toast.makeText(
                                    DashboardActivity.this,
                                    "Error: " + e,
                                    Toast.LENGTH_SHORT
                            ).show();
                        }
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e(TAG, "Error: " + anError.getErrorBody());
                    }
                });
    }

    private void intentToChangePassword() {
        Intent intent = new Intent(DashboardActivity.this, ChangePasswordActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    private void intentToEditProfile() {
        Intent intent = new Intent(DashboardActivity.this, EditProfileActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    private void intentToStartQueing(String header, String body, String footer, boolean isSuccess) {
        Intent intent = new Intent(DashboardActivity.this, StartQueueActivity.class);
        intent.putExtra(StartQueueActivity.EXTRA_HEADER, header);
        intent.putExtra(StartQueueActivity.EXTRA_BODY, body);
        intent.putExtra(StartQueueActivity.EXTRA_FOOTER, footer);
        intent.putExtra(StartQueueActivity.EXTRA_SUCCESS, isSuccess);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    private void intentToSeeQueue(String desc) {
        Intent intent = new Intent(DashboardActivity.this, SeeQueueActivity.class);
        intent.putExtra(SeeQueueActivity.EXTRA_DESC, desc);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    @Override
    public void onBackPressed() {
        logout();
    }

    private void logout() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(DashboardActivity.this)
                .setTitle("Logout")
                .setMessage("Are you sure?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        loginSharedPreference.setHasLogin(false);
                        loginSharedPreference.setCustomer(null);
                        loginSharedPreference.setToken(null);
                        intentToMainActivity();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // empty code
                    }
                });
        dialog.show();
    }

    private void intentToMainActivity() {
        Intent intent = new Intent(DashboardActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}
