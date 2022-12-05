package com.example.slpolicemobile;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {

    EditText fn, ln, email, tp, pwd, conpwd;
    Button register, clear, login;
    TextView tv;
    ImageButton back;

    String url = "https://slpolicemobile.000webhostapp.com/register.php";
    String resultRegi;
    String mailurl = "https://slpolicemobile.000webhostapp.com/getEmail.php";
    String resultEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fn = findViewById(R.id.fname);
        ln = findViewById(R.id.lname);
        email = findViewById(R.id.email);
        tp = findViewById(R.id.tp);
        pwd = findViewById(R.id.pass);
        conpwd = findViewById(R.id.conpass);

        register = findViewById(R.id.regi);
        clear = findViewById(R.id.cls);
        login = findViewById(R.id.rlogin);

        tv = findViewById(R.id.t1);

        back = findViewById(R.id.regiback);

        //back button
        callBack();

        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fn.setText(null);
                ln.setText(null);
                email.setText(null);
                tp.setText(null);
                pwd.setText(null);
                conpwd.setText(null);
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, com.example.slpolicemobile.login.class);
                startActivity(i);
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this,welcome.class);
                startActivity(i);
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //check required fields are not empty
                if(!fn.getText().toString().equals("") || !email.getText().toString().equals("") ||
                        !tp.getText().toString().equals("") || !pwd.getText().toString().equals("") ||
                        !conpwd.getText().toString().equals("")) {

                    //add max lengths to edit texts
                    //email
                    InputFilter[] emailFilters = email.getFilters();
                    InputFilter[] newEmailFilters = new InputFilter[emailFilters.length + 1];
                    System.arraycopy(emailFilters, 0, newEmailFilters, 0, emailFilters.length);
                    newEmailFilters[emailFilters.length] = new InputFilter.LengthFilter(40);
                    email.setFilters(newEmailFilters);

                    //password
                    InputFilter[] passFilters = pwd.getFilters();
                    InputFilter[] newPassFilters = new InputFilter[passFilters.length + 1];
                    System.arraycopy(passFilters, 0, newPassFilters, 0, passFilters.length);
                    newPassFilters[passFilters.length] = new InputFilter.LengthFilter(30);
                    pwd.setFilters(newPassFilters);

                    //phone number
                    InputFilter[] pnFilters = tp.getFilters();
                    InputFilter[] newPnFilters = new InputFilter[pnFilters.length + 1];
                    System.arraycopy(pnFilters, 0, newPnFilters, 0, pnFilters.length);
                    newPnFilters[pnFilters.length] = new InputFilter.LengthFilter(10);
                    tp.setFilters(newPnFilters);

                    //check email is valid or not
                    if (isValidEmailId(email.getText().toString().trim()))
                    {
                        tv.setText("");

                        //check password is matched
                        if(pwd.getText().toString().equals(conpwd.getText().toString()))
                        {
                            //add data to database
                            String name = fn.getText().toString()+" "+ln.getText().toString();
                            String mail = email.getText().toString();
                            String tel = tp.getText().toString();
                            String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
                            String pass = pwd.getText().toString();

                            RequestQueue requestEmail = Volley.newRequestQueue(MainActivity.this);
                            StringRequest Email = new StringRequest(Request.Method.POST, mailurl, new Response.Listener<String>() {
                                @Override
                                public void onResponse(String mailResponse) {
                                    resultEmail = mailResponse.toString().trim();
                                    switch (resultEmail)
                                    {
                                        case "found":
                                            tv.setText("This Email already registered!!");
                                            break;
                                        case "":
                                        case "notfound":
                                            Register(name,mail,tel,date,pass);
                                    }
                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Log.i("My error", "" + error);
                                }
                            })
                            {
                                @Override
                                protected Map<String, String> getParams() throws AuthFailureError {
                                    Map<String, String> map = new HashMap<String, String>();
                                    map.put("email",mail);
                                    return map;
                                }
                            };
                            requestEmail.add(Email);
                        }
                        else
                        {
                            Toast.makeText(MainActivity.this, "Your password does not matched!", Toast.LENGTH_SHORT).show();
                        }
                    } else
                    {
                        tv.setText("Invalid Email address!");
                    }
                }
                else
                {
                    Toast.makeText(MainActivity.this, "Required fields are empty!", Toast.LENGTH_SHORT).show();
                }
            }
        });
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

    //set navigation back button
    private void callBack (){
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                Intent i = new Intent(MainActivity.this,login.class);
                startActivity(i);
                finishAffinity();
            }
        };
        getOnBackPressedDispatcher().addCallback(this,callback);
    }

    //register
    private void Register(String n,String m, String t, String d, String p){
        RequestQueue requestQueue = Volley.newRequestQueue(MainActivity.this);
        StringRequest request  = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                resultRegi = response.toString().trim();
                switch (resultRegi){
                    case "registered!":
                        Toast.makeText(MainActivity.this, "Account created successfully!", Toast.LENGTH_SHORT).show();
                        fn.setText(null);
                        ln.setText(null);
                        email.setText(null);
                        tp.setText(null);
                        pwd.setText(null);
                        conpwd.setText(null);
                        break;
                    case "not registered!":
                    case "":
                        Toast.makeText(MainActivity.this, "Somethings wrong! try again!", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, "my error :" + error, Toast.LENGTH_LONG).show();
                //Toast.makeText(MainActivity.this, "Account not created!", Toast.LENGTH_SHORT).show();
                Log.i("My error", "" + error);
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put("name",n);
                map.put("email",m);
                map.put("tp",t);
                map.put("date",d);
                map.put("pwd",p);
                return map;
            }
        };
        requestQueue.add(request);
    }
}