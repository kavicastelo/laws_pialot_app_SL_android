package com.example.slpolicemobile;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
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
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;

public class user extends AppCompatActivity {

    ImageButton back;
    EditText feedback, username;
    TextView feedbackMsg;
    Button feedbackSend, feedbackReset, viewContact, viewSpec;
    ImageButton settings;
    CheckBox anonymous;
    Spinner province, divs;
    ListView lv;

    String fburl = "https://slpolicemobile.000webhostapp.com/sendFeedback.php";
    String result;

    String Prov,Divs;
    String conurl ="https://slpolicemobile.000webhostapp.com/searchContacts.php";
    ProgressDialog mProgressDialog;

    String divurl = "https://slpolicemobile.000webhostapp.com/getDivisionsToSpinner.php";

    String settingurl = "https://slpolicemobile.000webhostapp.com/updatePassword.php";
    String settingResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        back = findViewById(R.id.uback);

        feedback = findViewById(R.id.txtuserfeedback);
        username = findViewById(R.id.feedbackusername);

        feedbackMsg = findViewById(R.id.userfeedbackerrormsg);

        feedbackSend = findViewById(R.id.userfeedbacksend);
        feedbackReset = findViewById(R.id.userfeedbackcls);
        viewContact = findViewById(R.id.userviewcontacts);
        viewSpec = findViewById(R.id.userviewspeccontacts);

        settings = findViewById(R.id.usetting);

        anonymous = findViewById(R.id.usercheckboxfeedback);

        province = findViewById(R.id.usercontactsprovince);
        divs = findViewById(R.id.usercontactsdiv);

        lv = findViewById(R.id.listView);

        //set back button
        callBack();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(user.this,welcome.class);
                startActivity(i);
            }
        });

        feedbackReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                feedback.setText(null);
                anonymous.setChecked(false);
                username.setText(null);
            }
        });

        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final View layout = View.inflate(user.this,R.layout.user_setting,null);
                final EditText mail =((EditText)layout.findViewById(R.id.user_setting_email));
                final EditText pass =((EditText)layout.findViewById(R.id.user_setting_pass));
                final EditText conpass =((EditText)layout.findViewById(R.id.user_setting_conpass));

                AlertDialog.Builder ab = new AlertDialog.Builder(user.this);
                ab.setView(layout)
                        .setCancelable(false)
                        .setPositiveButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //nothing happen
                            }
                        })
                        .setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                final String Email = mail.getText().toString().trim();
                                final String Pass = pass.getText().toString().trim();
                                final String Conpass = conpass.getText().toString().trim();

                                //match passwords
                                if(!Pass.equals(Conpass))
                                {
                                    Toast.makeText(user.this, "Sorry! your password is mismatch Try again!", Toast.LENGTH_LONG).show();
                                }
                                else
                                {
                                    mProgressDialog = new ProgressDialog(user.this);
                                    mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                                    mProgressDialog.setMessage(getString(R.string.progress_update));
                                    mProgressDialog.setIndeterminate(true);
                                    mProgressDialog.setCancelable(false);
                                    mProgressDialog.setProgress(0);
                                    mProgressDialog.setProgressNumberFormat(null);
                                    mProgressDialog.setProgressPercentFormat(null);
                                    mProgressDialog.show();

                                    StringRequest stringRequest = new StringRequest(Request.Method.POST, settingurl, new Response.Listener<String>() {
                                        @Override
                                        public void onResponse(String response) {
                                            settingResult =response.toString().trim();
                                            switch (settingResult){
                                                case "update":
                                                    Toast.makeText(user.this, "Password Changed Successfully!", Toast.LENGTH_LONG).show();
                                                    break;
                                                case "":
                                                case "notupdate":
                                                    Toast.makeText(user.this, "Can not change password! \n Try again!", Toast.LENGTH_LONG).show();
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
                                    RequestQueue requestQueue = Volley.newRequestQueue(user.this);
                                    requestQueue.add(stringRequest);
                                }
                            }
                        })
                        .show();
            }
        });

        feedbackSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //set user value
                String name = "";
                if(anonymous.isChecked())
                {
                    name = "@anonymous";
                }
                else
                {
                    name = "@"+username.getText().toString();
                }

                if(username.getText().toString().equals(""))
                {
                    feedbackMsg.setText("Enter your username");
                }
                else {
                        String emoji = feedback.getText().toString();
                        String user = name.replaceAll("[^\\p{L}\\p{M}\\p{N}\\p{P}\\p{Z}\\p{Cf}\\s]","");
                        String fb = emoji.replaceAll("[^\\p{L}\\p{M}\\p{N}\\p{P}\\p{Z}\\p{Cf}\\s]","");
                        String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

                        RequestQueue requestQueue = Volley.newRequestQueue(user.this);
                        StringRequest request = new StringRequest(Request.Method.POST, fburl, new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {

                                result = response.toString().trim();
                                switch (result) {
                                    case "send":
                                        Toast.makeText(user.this, "Send Successfully!", Toast.LENGTH_SHORT).show();
                                        feedback.setText(null);
                                        anonymous.setChecked(false);
                                        username.setText(null);
                                        feedbackMsg.setText(null);
                                        break;
                                    case "notsend":
                                        Toast.makeText(user.this, "Send Failed!", Toast.LENGTH_SHORT).show();
                                        feedbackMsg.setText(null);
                                        break;
                                }
                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.i("My error", "" + error);
                            }
                        }) {
                            @Override
                            protected Map<String, String> getParams() throws AuthFailureError {
                                Map<String, String> map = new HashMap<String, String>();
                                map.put("name", user);
                                map.put("feedback", fb);
                                map.put("date", date);
                                return map;
                            }
                        };
                        requestQueue.add(request);
                }
            }
        });

        viewContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(province.getSelectedItem().toString().equals("SELECT PROVINCE"))
                {
                    Toast.makeText(user.this, "Please choose province", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    getMatchData();
                }
            }
        });

        province.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                getDivisions();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        viewSpec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(user.this,spec_contacts.class);
                startActivity(i);
            }
        });
    }
    private void getMatchData(){
        Prov = province.getSelectedItem().toString();
        Divs = divs.getSelectedItem().toString();

        mProgressDialog = new ProgressDialog(user.this);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        mProgressDialog.setMessage(getString(R.string.progress_detail));
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setCancelable(false);
        mProgressDialog.setProgress(0);
        mProgressDialog.setProgressNumberFormat(null);
        mProgressDialog.setProgressPercentFormat(null);
        mProgressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, conurl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray result = jsonObject.getJSONArray("result");

                    for (int i = 0; i < result.length(); i++) {
                        JSONObject jo = result.getJSONObject(i);
                        String station = jo.getString("station");
                        String oic = jo.getString("oic");
                        String office = jo.getString("office");

                        final HashMap<String, String> data = new HashMap<String, String>();
                        data.put("station", "Station = "+station);
                        data.put("oic", "OIC Number = " +oic);
                        data.put("office", "Office Number = " +office);

                        list.add(data);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                SimpleAdapter adapter = new SimpleAdapter(user.this,list,R.layout.contact_list,
                        new String[]{"station","oic","office"},
                        new int[]{R.id.tvusercontactstation,R.id.tvusercontactoic,R.id.tvusercontactoffice});
                lv.setAdapter(adapter);

                mProgressDialog.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("My error", "" + error);
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError
            {
                Map<String, String> map = new HashMap<String, String>();
                map.put("province", Prov);
                map.put("division", Divs);
                return map;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    //get divisions to spinner
    private void getDivisions(){
        Prov = province.getSelectedItem().toString();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, divurl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                ArrayList<String> divlist = new ArrayList<String>();
                HashSet<String> hashSet = new HashSet<String>(); //remove duplicate results from jsonArray
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray result = jsonObject.getJSONArray("result");

                    for (int i = 0; i < result.length(); i++) {
                        JSONObject jo = result.getJSONObject(i);
                        hashSet.addAll(Collections.singleton(jo.getString("division")));
                        divlist.clear();
                        divlist.addAll(hashSet);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //set spinner to division string[] values
                divs.setAdapter(new ArrayAdapter<String>(user.this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,divlist));
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("My error", "" + error);
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError
            {
                Map<String, String> map = new HashMap<String, String>();
                map.put("province", Prov);
                return map;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    //set navigation back button
    private void callBack (){
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                Intent i = new Intent(user.this,welcome.class);
                startActivity(i);
                finishAffinity();
            }
        };
        getOnBackPressedDispatcher().addCallback(this,callback);
    }
}