package com.example.polmanqueue.Utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.JSONObjectRequestListener;
import com.example.polmanqueue.Activity.ChangePasswordActivity;
import com.example.polmanqueue.Activity.MainActivity;
import com.example.polmanqueue.Activity.SignUp2Activity;
import com.example.polmanqueue.Model.Customer;
import com.example.polmanqueue.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;

public class Controller {
    private final String TAG = Controller.class.getSimpleName();
    private Context context;
    private ProgressDialog progress;
    private LoginSharedPreference loginSharedPreference;

    public Controller(Context context) {
        this.context = context;
        progress = new ProgressDialog(context);
        loginSharedPreference = new LoginSharedPreference(context);
    }

    public void changeProfile(JSONObject object) {
        final ProgressDialog progress = new ProgressDialog(context);
        progress.setTitle(context.getString(R.string.information));
        progress.setMessage(context.getString(R.string.please_wait));
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.show();

        AndroidNetworking.post(GlobalVars.URL_CHANGEPROFILE)
                .addJSONObjectBody(object)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if(response.getString("message").equals("success")){
                                Toast.makeText(context, "Change profile has been success",
                                        Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(
                                        context,
                                        response.getString("message"),
                                        Toast.LENGTH_SHORT
                                ).show();
                            }
                        } catch (JSONException e) {
                            Log.e(TAG,"Error: " + e);
                            Toast.makeText(
                                    context,
                                    "Error: " + e,
                                    Toast.LENGTH_SHORT
                            ).show();
                        }
                        progress.dismiss();
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e(TAG,"Error: " + anError.getErrorBody());
                        Toast.makeText(context, "Error: " + anError, Toast.LENGTH_SHORT).show();
                        progress.dismiss();
                    }
                });
    }

    public void changePassword(JSONObject object) {
        final ProgressDialog progress = new ProgressDialog(context);
        progress.setTitle(context.getString(R.string.information));
        progress.setMessage(context.getString(R.string.please_wait));
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.show();

        AndroidNetworking.post(GlobalVars.URL_CHANGEPASSWORD)
                .addJSONObjectBody(object)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if(response.getString("message").equals("success")){
                                Toast.makeText(context, "Change password has been success",
                                        Toast.LENGTH_SHORT).show();
                                ((ChangePasswordActivity)context).clear();
                            } else {
                                Toast.makeText(
                                        context,
                                        response.getString("message"),
                                        Toast.LENGTH_SHORT
                                ).show();
                            }
                        } catch (JSONException e) {
                            Log.e(TAG,"Error: " + e);
                            Toast.makeText(
                                    context,
                                    "Error: " + e,
                                    Toast.LENGTH_SHORT
                            ).show();
                        }
                        progress.dismiss();
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e(TAG,"Error: " + anError.getErrorBody());
                        Toast.makeText(context, "Error: " + anError, Toast.LENGTH_SHORT).show();
                        progress.dismiss();
                    }
                });
    }

    public void getCustomer(JSONObject object) {
        final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        final ProgressDialog progress = new ProgressDialog(context);
        progress.setTitle(context.getString(R.string.information));
        progress.setMessage(context.getString(R.string.please_wait));
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.show();
        AndroidNetworking.post(GlobalVars.URL_LOGIN)
                .addJSONObjectBody(object)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if(response.getString("message").equals("success")){
                                JSONObject dataObject = response.getJSONObject("data");
                                Customer dataCustomer = new Customer(
                                        dataObject.getString("id"),
                                        dataObject.getString("username"),
                                        dataObject.getString("password"),
                                        dataObject.getString("identity_number"),
                                        dataObject.getString("name"),
                                        dataObject.getString("telp_number"),
                                        dataObject.getString("address"),
                                        dataObject.getString("email"),
                                        dateFormat.parse(dataObject.getString("date_of_birth").split(" ")[0]),
                                        dataObject.getString("role"),
                                        dataObject.getString("gender")
                                );

                                loginSharedPreference.setCustomer(dataCustomer);
                                loginSharedPreference.setHasLogin(true);
                                loginSharedPreference.setToken(response.getString("token"));
                                ((MainActivity)context).intentToDashboard();
                            } else {
                                Toast.makeText(
                                        context,
                                        response.getString("message"),
                                        Toast.LENGTH_SHORT
                                ).show();
                            }
                        } catch (JSONException e) {
                            Log.e(TAG,"Error: " + e);
                            Toast.makeText(
                                    context,
                                    "Error: " + e,
                                    Toast.LENGTH_SHORT
                            ).show();
                        } catch (ParseException e) {
                            Log.e(TAG,"Error: " + e);
                            Toast.makeText(
                                    context,
                                    "Error: " + e,
                                    Toast.LENGTH_SHORT
                            ).show();
                        }
                        progress.dismiss();
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e(TAG,"Error: " + anError.getErrorBody());
                        Toast.makeText(context, "Error: " + anError, Toast.LENGTH_SHORT).show();
                        progress.dismiss();
                    }
                });
    }

    public void signUp(JSONObject object) {
        final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        final ProgressDialog progress = new ProgressDialog(context);
        progress.setTitle(context.getString(R.string.information));
        progress.setMessage(context.getString(R.string.please_wait));
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.show();
        AndroidNetworking.post(GlobalVars.URL_CUSTOMER)
                .addJSONObjectBody(object)
                .setPriority(Priority.MEDIUM)
                .build()
                .getAsJSONObject(new JSONObjectRequestListener() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            if(response.getString("message").equals("success")){
                                JSONObject dataObject = response.getJSONObject("data");
                                Log.e(TAG, "onResponse: Berhasil");
                                Customer dataCustomer = new Customer(
                                        dataObject.getString("ID"),
                                        dataObject.getString("username"),
                                        dataObject.getString("password"),
                                        dataObject.getString("identity_number"),
                                        dataObject.getString("name"),
                                        dataObject.getString("telp_number"),
                                        dataObject.getString("address"),
                                        dataObject.getString("email"),
                                        dateFormat.parse(dataObject.getString("date_of_birth").split(" ")[0]),
                                        dataObject.getString("role"),
                                        dataObject.getString("gender")
                                );

                                loginSharedPreference.setCustomer(dataCustomer);
                                loginSharedPreference.setHasLogin(true);
                                loginSharedPreference.setToken(dataObject.getString("Auth_Token"));
                                ((SignUp2Activity)context).intentToDashboard();
                            } else {
                                Toast.makeText(
                                        context,
                                        response.getString("message"),
                                        Toast.LENGTH_SHORT
                                ).show();
                            }
                        } catch (JSONException e) {
                            Log.e(TAG,"Error: " + e);
                            Toast.makeText(
                                    context,
                                    "Error: " + e,
                                    Toast.LENGTH_SHORT
                            ).show();
                        } catch (ParseException e) {
                            Log.e(TAG,"Error: " + e);
                            Toast.makeText(
                                    context,
                                    "Error: " + e,
                                    Toast.LENGTH_SHORT
                            ).show();
                        }
                        progress.dismiss();
                    }

                    @Override
                    public void onError(ANError anError) {
                        Log.e(TAG,"Error: " + anError.getErrorBody());
                        Toast.makeText(context, "Error: " + anError, Toast.LENGTH_SHORT).show();
                        progress.dismiss();
                    }
                });
    }
}
