package com.example.slpolicemobile;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Application;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class login extends AppCompatActivity {

    EditText user, pass;
    Button login, reset, register;
    ImageButton back;
    TextView tvmail,tvpass, forgetPass;
    CheckBox check;

    String url = "https://slpolicemobile.000webhostapp.com/login.php";
    String result;

    ProgressDialog mProgressDialog;
    String forgeturl = "https://slpolicemobile.000webhostapp.com/checkEmails.php";
    String forgetresult;

    String forgetpassurl = "https://slpolicemobile.000webhostapp.com/updatePassword.php";
    String passresult;

    String email;

    String responseurl = "https://slpolicemobile.000webhostapp.com/banReason.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        user = findViewById(R.id.lemail);
        pass = findViewById(R.id.lpass);

        login = findViewById(R.id.login);
        reset = findViewById(R.id.lcls);
        register = findViewById(R.id.lregi);

        tvmail = findViewById(R.id.lt1);
        tvpass = findViewById(R.id.lt2);
        forgetPass = findViewById(R.id.lforgetpass);

        back = findViewById(R.id.lback);

        check = findViewById(R.id.lcheakbox);

        //back button
        callBack();

        internalDB myDB = new internalDB(this);

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                user.setText(null);
                pass.setText(null);
                tvmail.setText(null);
                tvpass.setText(null);
                check.setChecked(false);
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(login.this,MainActivity.class);
                startActivity(i);
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(login.this,welcome.class);
                startActivity(i);
            }
        });

        //if password is remembered
        Cursor getDet = myDB.getloginfromDB();
        while (getDet.moveToNext())
        {
            if(getDet.getCount()!=0)
            {
                String gotUser = getDet.getString(1).toString().trim();
                String gotPass = getDet.getString(2).toString().trim();
                user.setText(gotUser);
                pass.setText(gotPass);
            }
            else
            {
                user.setText(null);
                pass.setText(null);
            }
        }

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = user.getText().toString();
                String pwd = pass.getText().toString();

                if(!user.getText().toString().equals("") || !pass.getText().toString().equals(""))
                {
                    if(isValidEmailId(user.getText().toString().trim())) {
                        RequestQueue requestQueue = Volley.newRequestQueue(login.this);
                        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Log.d("response", response);
                                result = response.toString().trim();

                                switch (result) {
                                    case "user":
                                        //user panel
                                        Intent u = new Intent(login.this, user.class);
                                        startActivity(u);
                                        user.setText(null);
                                        pass.setText(null);
                                        //Toast.makeText(login.this, "user", Toast.LENGTH_SHORT).show();
                                        break;
                                    case "admin":
                                        //admin panel
                                        Intent a = new Intent(login.this, admin.class);
                                        startActivity(a);
                                        user.setText(null);
                                        pass.setText(null);
                                        //Toast.makeText(login.this, "admin", Toast.LENGTH_SHORT).show();
                                        break;
                                    case "ban":
                                    case "":
                                        //go to ban account function
                                        banAccount();
                                        break;
                                    case "invalidPass":
                                        //Toast.makeText(login.this, "Invalid Password!", Toast.LENGTH_SHORT).show();
                                        pass.setText(null);
                                        tvpass.setText("Invalid Password!");
                                        break;
                                    case "invalidEmail":
                                        //Toast.makeText(login.this, "Invalid Email address!", Toast.LENGTH_SHORT).show();
                                        user.setText(null);
                                        tvmail.setText("Invalid Email address!");
                                        break;
                                }

                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(login.this, "Somethings wrong. try again later!", Toast.LENGTH_SHORT).show();
                                Log.i("My error", "" + error);
                            }
                        }) {
                            @Override
                            protected Map<String, String> getParams() throws AuthFailureError {
                                Map<String, String> data = new HashMap<>();
                                data.put("email", email);
                                data.put("pass", pwd);
                                return data;
                            }
                        };
                        requestQueue.add(stringRequest);
                    }
                    else
                    {
                        tvmail.setText("Invalid Email Address");
                        user.setText(null);
                    }
                }
                else
                {
                    //Toast.makeText(login.this, "Fields can not be empty!", Toast.LENGTH_SHORT).show();
                    tvpass.setText("Fields can not be empty!");
                }
                //clear error messages
                if(!user.getText().toString().equals(""))
                {
                    tvmail.setText(null);
                }
                else if(!pass.getText().toString().equals(""))
                {
                    tvpass.setText(null);
                }

                //if set check, put values to internal database
                boolean isAdded = check.isChecked();
                if(isAdded)
                {
                    boolean ischeck = myDB.addLoginDet(user.getText().toString(),pass.getText().toString());
                    if(ischeck)
                    {
                        Toast.makeText(login.this, "Login Details are Remembered!", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        Toast.makeText(login.this, "not remembered", Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    //nothing happen
                }
            }
        });

        forgetPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //LayoutInflater inflater = login.this.getLayoutInflater();
                final View layout = View.inflate(login.this,R.layout.forget_pass,null);
                final EditText mail =((EditText)layout.findViewById(R.id.forget_Current_mail));

                AlertDialog.Builder ab = new AlertDialog.Builder(login.this);

                ab.setView(layout)
                        .setCancelable(false)
                        .setPositiveButton("Done", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                email = mail.getText().toString().trim();
                                user.setText(email);

                                mProgressDialog = new ProgressDialog(login.this);
                                mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                                mProgressDialog.setMessage(getString(R.string.progress_check_email));
                                mProgressDialog.setIndeterminate(true);
                                mProgressDialog.setCancelable(false);
                                mProgressDialog.setProgress(0);
                                mProgressDialog.setProgressNumberFormat(null);
                                mProgressDialog.setProgressPercentFormat(null);
                                mProgressDialog.show();

                                StringRequest request = new StringRequest(Request.Method.POST, forgeturl, new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {
                                        forgetresult = response.toString().trim();
                                        switch (forgetresult){
                                            case "match":

                                                final View layout1 = View.inflate(login.this,R.layout.reset_pass,null);
                                                final EditText pass =((EditText)layout1.findViewById(R.id.forgetpass));
                                                final EditText conpass =((EditText) layout1.findViewById(R.id.forgetpasscon));
                                                final TextView tvemail =((TextView) layout1.findViewById(R.id.reset_pass_email));
                                                tvemail.setText(user.getText().toString().trim());
                                                AlertDialog.Builder ab = new AlertDialog.Builder(login.this);

                                                ab.setView(layout1)
                                                        .setCancelable(false)
                                                        .setPositiveButton("Set", new DialogInterface.OnClickListener() {
                                                            @Override
                                                            public void onClick(DialogInterface dialogInterface, int i) {

                                                                final String Email = tvemail.getText().toString().trim();
                                                                final String Pass = pass.getText().toString().trim();
                                                                final String Conpass = conpass.getText().toString().trim();

                                                                //match passwords
                                                                if(!Pass.equals(Conpass))
                                                                {
                                                                    Toast.makeText(login.this, "Sorry! your password is mismatch Try again!", Toast.LENGTH_LONG).show();
                                                                }
                                                                else
                                                                {
                                                                    //update statement
                                                                    mProgressDialog = new ProgressDialog(login.this);
                                                                    mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                                                                    mProgressDialog.setMessage(getString(R.string.progress_update));
                                                                    mProgressDialog.setIndeterminate(true);
                                                                    mProgressDialog.setCancelable(false);
                                                                    mProgressDialog.setProgress(0);
                                                                    mProgressDialog.setProgressNumberFormat(null);
                                                                    mProgressDialog.setProgressPercentFormat(null);
                                                                    mProgressDialog.show();

                                                                    StringRequest stringRequest = new StringRequest(Request.Method.POST, forgetpassurl, new Response.Listener<String>() {
                                                                        @Override
                                                                        public void onResponse(String response) {
                                                                            passresult = response.toString().trim();
                                                                            Toast.makeText(login.this, response, Toast.LENGTH_SHORT).show();
                                                                            switch (passresult){
                                                                                case "update":
                                                                                    Toast.makeText(login.this, "Password Reset Successfully!", Toast.LENGTH_LONG).show();
                                                                                    user.setText(null);
                                                                                    break;
                                                                                case "":
                                                                                case "notupdate":
                                                                                    Toast.makeText(login.this, "Can not reset password! \n Try again!", Toast.LENGTH_LONG).show();
                                                                                    user.setText(null);
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
                                                                        protected Map<String,String> getParams() throws AuthFailureError
                                                                        {
                                                                            Map<String,String> map = new HashMap<String,String>();
                                                                            map.put("email",Email);
                                                                            map.put("pass",Pass);
                                                                            return map;
                                                                        }
                                                                    };
                                                                    RequestQueue requestQueue = Volley.newRequestQueue(login.this);
                                                                    requestQueue.add(stringRequest);
                                                                }
                                                            }
                                                        })
                                                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                                            @Override
                                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                                //nothing happen
                                                            }
                                                        });
                                                ab.show();
                                                break;
                                            case "notmatched":
                                            case "":
                                                Toast.makeText(login.this, "Incorrect Email!", Toast.LENGTH_SHORT).show();
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
                                    protected Map<String,String> getParams() throws AuthFailureError
                                    {
                                        Map<String,String> map = new HashMap<String,String>();
                                        map.put("email",email);
                                        return map;
                                    }
                                };
                                RequestQueue requestQueue = Volley.newRequestQueue(login.this);
                                requestQueue.add(request);
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //nothing happen
                            }
                        });
                ab.show();
            }
        });
    }

    //login fields click and clear error message
    public void tvClear(View view){
        tvpass.setText(null);
        tvmail.setText(null);
    }

    //check email is valid (regular expression)
    private boolean isValidEmailId(String mail){

        return Pattern.compile("^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$").matcher(mail).matches();
    }

    private void banAccount(){
        final View layout = View.inflate(login.this, R.layout.banned_account, null);

        String email = user.getText().toString().trim();

        AlertDialog.Builder ab = new AlertDialog.Builder(login.this);
        ab.setView(layout)
                .setCancelable(true)
                .setPositiveButton("See Why", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        StringRequest stringrequest = new StringRequest(Request.Method.POST, responseurl, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String res) {
                                String Reason = "";

                                try {
                                    JSONObject jsonObject = new JSONObject(res);
                                    JSONArray result = jsonObject.getJSONArray("result");

                                    JSONObject reason = result.getJSONObject(0);
                                    Reason = reason.getString("reason");

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    Toast.makeText(login.this, "Account not found", Toast.LENGTH_SHORT).show();
                                }

                                StringBuffer sb = new StringBuffer();
                                sb.append(Reason);
                                AlertDialog.Builder ab = new AlertDialog.Builder(login.this);
                                ab.setTitle("Reason")
                                        .setMessage(sb.toString())
                                        .setCancelable(true)
                                        .setPositiveButton("Close", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                user.setText(null);
                                                pass.setText(null);
                                                Intent b = new Intent(login.this, welcome.class);
                                                startActivity(b);
                                            }
                                        })
                                        .show();

                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.i("myError", "" + error);
                            }
                        }) {
                            protected Map<String, String> getParams() throws AuthFailureError {
                                Map<String, String> map = new HashMap<String, String>();
                                map.put("email", email);
                                return map;
                            }
                        };
                        RequestQueue request = Volley.newRequestQueue(login.this);
                        request.add(stringrequest);
                    }
                })
                .setNegativeButton("Exit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        user.setText(null);
                        pass.setText(null);
                        Intent w = new Intent(login.this, welcome.class);
                        startActivity(w);
                    }
                })
                .show();
    }

    //set navigation back button
    private void callBack (){
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                Intent i = new Intent(login.this,welcome.class);
                startActivity(i);
                finishAffinity();
            }
        };
        getOnBackPressedDispatcher().addCallback(this,callback);
    }
}