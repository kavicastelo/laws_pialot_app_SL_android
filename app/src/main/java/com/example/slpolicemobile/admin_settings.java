package com.example.slpolicemobile;

import static com.android.volley.VolleyLog.TAG;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

public class admin_settings extends AppCompatActivity {

    EditText currentEmail, newEmail, newPass, banreason, banemail;
    Button CPChange, CPCancel, ban,bancls;
    ImageButton back;

    ProgressDialog mProgressDialog;

    String url = "https://slpolicemobile.000webhostapp.com/changePrivacy.php";
    String result;

    String banurl = "https://slpolicemobile.000webhostapp.com/banUser.php";
    String  banResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_settings);

        currentEmail = findViewById(R.id.ascurrentemail);
        newEmail = findViewById(R.id.asnewemail);
        newPass = findViewById(R.id.asnewpass);
        banreason = findViewById(R.id.asbanreason);
        banemail = findViewById(R.id.asbanemail);

        CPChange = findViewById(R.id.ascpok);
        CPCancel = findViewById(R.id.ascpocls);
        ban = findViewById(R.id.asbtnban);
        bancls = findViewById(R.id.asbtnbancls);

        back = findViewById(R.id.asback);

        //back button
        callBack();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(admin_settings.this,admin.class);
                startActivity(i);
            }
        });

        CPCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                currentEmail.setText(null);
                newEmail.setText(null);
                newPass.setText(null);
            }
        });

        CPChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String CMail = currentEmail.getText().toString().trim();
                String NMail = newEmail.getText().toString().trim();
                String NPass = newPass.getText().toString().trim();

                if(CMail.isEmpty() || NMail.isEmpty() || NPass.isEmpty())
                {
                    Toast.makeText(admin_settings.this, "All fields are required!", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            result = response.toString().trim();
                            switch (result) {
                                case "updatedupdated":
                                    Toast.makeText(admin_settings.this, "Updated!", Toast.LENGTH_LONG).show();
                                    currentEmail.setText(null);
                                    newEmail.setText(null);
                                    newPass.setText(null);
                                    break;
                                case "notupdatedupdated":
                                case "noupdated":
                                    Toast.makeText(admin_settings.this, "Login details not set!", Toast.LENGTH_SHORT).show();
                                    currentEmail.setText(null);
                                    newEmail.setText(null);
                                    newPass.setText(null);
                                    break;
                                case "updatednotupdated":
                                case "updatedno":
                                    Toast.makeText(admin_settings.this, "Login details Updated!", Toast.LENGTH_SHORT).show();
                                    currentEmail.setText(null);
                                    newEmail.setText(null);
                                    newPass.setText(null);
                                    break;
                                case "notupdatednotupdated":
                                    Toast.makeText(admin_settings.this, "not Updated!", Toast.LENGTH_SHORT).show();
                                    currentEmail.setText(null);
                                    newEmail.setText(null);
                                    newPass.setText(null);
                                    break;
                                case "nono":
                                case "":
                                    Toast.makeText(admin_settings.this, "Invalid current email address!", Toast.LENGTH_SHORT).show();
                                    currentEmail.setText(null);
                                    newEmail.setText(null);
                                    newPass.setText(null);
                                    break;
                                case "nonotupdated":
                                case "notupdateno":
                                    Toast.makeText(admin_settings.this, "Sorry you can not Update! Registration issue!", Toast.LENGTH_SHORT).show();
                                    currentEmail.setText(null);
                                    newEmail.setText(null);
                                    newPass.setText(null);
                                    break;
                            }
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            VolleyLog.d(TAG, "Error: " + error.getMessage());
                        }
                    }){
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> data = new HashMap<String,String>();
                            data.put("currentEmail",CMail);
                            data.put("newEmail",NMail);
                            data.put("newPassword",NPass);
                            return data;
                        }
                    };
                    RequestQueue requestQueue = Volley.newRequestQueue(admin_settings.this);
                    requestQueue.add(request);
                }
            }
        });

        bancls.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                banreason.setText(null);
                banemail.setText(null);
            }
        });

        ban.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(banreason.getText().toString().equals(""))
                {
                    Toast.makeText(admin_settings.this, "Can not ban users without reason!", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    //set max length to edit text
                    InputFilter[] editFilters = banreason.getFilters();
                    InputFilter[] newFilters = new InputFilter[editFilters.length + 1];
                    System.arraycopy(editFilters, 0, newFilters, 0, editFilters.length);
                    newFilters[editFilters.length] = new InputFilter.LengthFilter(250);
                    banreason.setFilters(newFilters);

                    String reason = banreason.getText().toString().trim();
                    String mail = banemail.getText().toString().trim();

                    //ban process
                    mProgressDialog = new ProgressDialog(admin_settings.this);
                    mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                    mProgressDialog.setMessage(getString(R.string.progress_detail));
                    mProgressDialog.setIndeterminate(true);
                    mProgressDialog.setCancelable(false);
                    mProgressDialog.setProgress(0);
                    mProgressDialog.setProgressNumberFormat(null);
                    mProgressDialog.setProgressPercentFormat(null);
                    mProgressDialog.show();

                    AlertDialog.Builder ab = new AlertDialog.Builder(admin_settings.this);
                    ab.setTitle("Ban User");
                    ab.setMessage("Sure to want ban this user?");
                    ab.setIcon(getResources().getDrawable(android.R.drawable.ic_dialog_alert));
                    ab.setPositiveButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            //nothing happen and clear fields
                            banreason.setText(null);
                            banemail.setText(null);
                        }
                    });
                    ab.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            //ban user
                            StringRequest stringRequest = new StringRequest(Request.Method.POST, banurl, new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    banResult = response.toString().trim();
                                    switch (banResult){
                                        case "added":
                                            Toast.makeText(admin_settings.this, "User Banned!", Toast.LENGTH_SHORT).show();
                                            banreason.setText(null);
                                            banemail.setText(null);
                                            break;
                                        case "notadd":
                                            Toast.makeText(admin_settings.this, "User not Banned!", Toast.LENGTH_SHORT).show();
                                            break;
                                        case "invalid":
                                        case "":
                                            Toast.makeText(admin_settings.this, "Can not ban main Admin Account", Toast.LENGTH_SHORT).show();
                                            break;
                                    }
                                    mProgressDialog.dismiss();
                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Log.i("My error", "" + error);
                                }
                            })
                            {
                                protected Map<String,String> getParams() throws AuthFailureError{
                                    Map<String,String> map = new HashMap<String,String>();
                                    map.put("email",mail);
                                    map.put("reason",reason);
                                    return map;
                                }
                            };
                            RequestQueue requestQueue = Volley.newRequestQueue(admin_settings.this);
                            requestQueue.add(stringRequest);
                        }
                    });
                    ab.show();
                }
            }
        });
    }

    //set navigation back button
    private void callBack (){
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                Intent i = new Intent(admin_settings.this,admin.class);
                startActivity(i);
                finishAffinity();
            }
        };
        getOnBackPressedDispatcher().addCallback(this,callback);
    }
}