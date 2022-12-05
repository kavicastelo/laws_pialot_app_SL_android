package com.example.slpolicemobile;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class admin_contact extends AppCompatActivity {

    ImageButton back, btndivsearch;
    EditText digname,digmobile,digoffice,digfax,digrange,divsearch,divstation,divoic,divoffice;
    Button digclear,digadd,digup,divclear,divup;
    Spinner digspin;
    TextView txtdig,txtdiv;
    RadioGroup group;
    RadioButton dig,sdig;

    ProgressDialog mProgressDialog;

    String url = "https://slpolicemobile.000webhostapp.com/add_dig.php";
    String addResult;
    String Dig;
    String digidurl = "https://slpolicemobile.000webhostapp.com/getDigIDsToSpinner.php";
    String digurl = "https://slpolicemobile.000webhostapp.com/getDigDet.php";
    String updigurl = "https://slpolicemobile.000webhostapp.com/updateDig.php";
    String updigResult;
    String searchdivurl = "https://slpolicemobile.000webhostapp.com/searchDivs.php";
    String updivurl = "https://slpolicemobile.000webhostapp.com/updateDivs.php";
    String updivResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_contact);

        back = findViewById(R.id.acback);
        btndivsearch = findViewById(R.id.btnacdivsearch);

        digname = findViewById(R.id.acdigname);
        digmobile = findViewById(R.id.acdigtp);
        digoffice = findViewById(R.id.acdigoffice);
        digfax = findViewById(R.id.acdigfax);
        divstation = findViewById(R.id.acdivstation);
        divoic = findViewById(R.id.acdivoic);
        divoffice = findViewById(R.id.acdivoffice);
        digrange = findViewById(R.id.acdigrange);
        divsearch = findViewById(R.id.acdivsearch);

        digclear = findViewById(R.id.acdigcls);
        digadd = findViewById(R.id.acdigadd);
        digup = findViewById(R.id.acdigup);
        divclear = findViewById(R.id.acdivcls);
        divup = findViewById(R.id.acdivup);

        digspin = findViewById(R.id.acids);

        txtdig = findViewById(R.id.actxtviewdig);
        txtdiv = findViewById(R.id.actxtviewdiv);

        group = findViewById(R.id.acradioGroup);
        dig = findViewById(R.id.acradiodig);
        sdig = findViewById(R.id.acradiosdig);

        //get dig ids to spinner
        getDigIDsToSpinner();

        //back button
        callBack();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(admin_contact.this,admin.class);
                startActivity(i);
            }
        });

        digspin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                getDigs();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        digclear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                digname.setText(null);
                digmobile.setText(null);
                digoffice.setText(null);
                digfax.setText(null);
                digrange.setText(null);
                dig.setChecked(false);
                sdig.setChecked(false);
            }
        });

        divclear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                divstation.setText(null);
                divoic.setText(null);
                divoffice.setText(null);
            }
        });

        digadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addDIGs();
            }
        });

        digup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateDIGs();
            }
        });

        btndivsearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchDiv();
            }
        });

        divup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(divsearch.getText().toString().equals("")){
                    Toast.makeText(admin_contact.this, "Search station first!", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    updateDivs();
                }
            }
        });
    }

    //get digs to spinner
    private void getDigIDsToSpinner(){

        StringRequest stringRequest = new StringRequest(Request.Method.POST, digidurl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                ArrayList<String> diglist = new ArrayList<String>();
                HashSet<String> hashSet = new HashSet<String>(); //remove duplicate results from jsonArray
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray result = jsonObject.getJSONArray("result");

                    for (int i = 0; i < result.length(); i++) {
                        JSONObject jo = result.getJSONObject(i);
                        hashSet.addAll(Collections.singleton(jo.getString("DID")));
                        diglist.clear();
                        diglist.addAll(hashSet);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //set spinner to division string[] values
                digspin.setAdapter(new ArrayAdapter<String>(admin_contact.this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,diglist));
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("My error", "" + error);
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    //get Dig details
    private void getDigs(){

        Dig = digspin.getSelectedItem().toString().trim();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, digurl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                String Digname = "";
                String Digaria = "";
                String Digtp = "";
                String Digoffice = "";
                String Digfax = "";
                String Digtype = "";

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray result = jsonObject.getJSONArray("result");

                    JSONObject name = result.getJSONObject(0);
                    JSONObject aria = result.getJSONObject(0);
                    JSONObject tp = result.getJSONObject(0);
                    JSONObject office = result.getJSONObject(0);
                    JSONObject fax = result.getJSONObject(0);
                    JSONObject type = result.getJSONObject(0);
                    Digname = name.getString("name");
                    Digaria = aria.getString("aria");
                    Digtp = tp.getString("mobile");
                    Digoffice = office.getString("office");
                    Digfax = fax.getString("fax");
                    Digtype = type.getString("type");

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(admin_contact.this, "Somethings wrong!", Toast.LENGTH_SHORT).show();
                }

                digname.setText(""+Digname);
                digrange.setText(""+Digaria);
                digmobile.setText(""+Digtp);
                digoffice.setText(""+Digoffice);
                digfax.setText(""+Digfax);

                if(Digtype.equals("DIG"))
                {
                    dig.setChecked(true);
                    sdig.setChecked(false);
                }
                else if(Digtype.equals("SDIG"))
                {
                    sdig.setChecked(true);
                    dig.setChecked(false);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("myError",""+error);
            }
        })
        {
            protected Map<String,String> getParams() throws AuthFailureError{
                Map<String,String> map = new HashMap<String,String>();
                map.put("id",Dig);
                return map;
            }

        };
        RequestQueue requestQueue = Volley.newRequestQueue(admin_contact.this);
        requestQueue.add(stringRequest);
    }

    //add DIGs
    private void addDIGs(){

        mProgressDialog = new ProgressDialog(admin_contact.this);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        mProgressDialog.setMessage(getString(R.string.progress_detail));
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setCancelable(false);
        mProgressDialog.setProgress(0);
        mProgressDialog.setProgressNumberFormat(null);
        mProgressDialog.setProgressPercentFormat(null);
        mProgressDialog.show();

        //set value to radio button
        String type = "not define";
        if(dig.isChecked())
        {
            type = "DIG";
            txtdig.setText(null);
            sdig.setSelected(false);
        }
        else if(sdig.isChecked())
        {
            txtdig.setText(null);
            type = "SDIG";
            dig.setSelected(false);
        }
        else
        {
            txtdig.setText("Please choose one type");
        }

        if(digname.getText().toString().equals("") || digmobile.getText().toString().equals("")
                || digoffice.getText().toString().equals("") || digfax.getText().toString().equals("")
                || type.equals("not define"))
        {
            txtdig.setText("Fields cannot be empty"+type);
        }
        else
        {
            txtdig.setText(type);

            String name = digname.getText().toString();
            String tel = digmobile.getText().toString();
            String office = digoffice.getText().toString();
            String fax = digfax.getText().toString();
            String range = digrange.getText().toString();
            String Type = type;

            RequestQueue requestQueue = Volley.newRequestQueue(admin_contact.this);
            StringRequest request  = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    addResult=response.toString().trim();
                    switch (addResult){
                        case "add":
                            Toast.makeText(admin_contact.this, "Added Successfully!", Toast.LENGTH_SHORT).show();
                            digname.setText(null);
                            digmobile.setText(null);
                            digoffice.setText(null);
                            digfax.setText(null);
                            digrange.setText(null);
                            dig.setChecked(false);
                            sdig.setChecked(false);
                            break;
                        case "":
                        case "notadd":
                            Toast.makeText(admin_contact.this, "Not Added!", Toast.LENGTH_SHORT).show();
                        case "empty":
                            Toast.makeText(admin_contact.this, "Fields can not be empty!", Toast.LENGTH_SHORT).show();
                            break;
                    }
                    mProgressDialog.dismiss();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(admin_contact.this, "my error :" + error, Toast.LENGTH_LONG).show();
                    //Toast.makeText(admin_contact.this, "Account not created!", Toast.LENGTH_SHORT).show();
                    Log.i("My error", "" + error);
                }
            })
            {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> map = new HashMap<String, String>();
                    map.put("name",name);
                    map.put("range",range);
                    map.put("tp",tel);
                    map.put("office",office);
                    map.put("fax",fax);
                    map.put("type",Type);
                    return map;
                }
            };
            requestQueue.add(request);;
        }
    }

    //update DIGs
    private void updateDIGs(){

        mProgressDialog = new ProgressDialog(admin_contact.this);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        mProgressDialog.setMessage(getString(R.string.progress_detail));
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setCancelable(false);
        mProgressDialog.setProgress(0);
        mProgressDialog.setProgressNumberFormat(null);
        mProgressDialog.setProgressPercentFormat(null);
        mProgressDialog.show();

        //set value to radio button
        String type = "not define";
        if(dig.isChecked())
        {
            type = "DIG";
            txtdig.setText(null);
            sdig.setSelected(false);
        }
        else if(sdig.isChecked())
        {
            txtdig.setText(null);
            type = "SDIG";
            dig.setSelected(false);
        }
        else
        {
            txtdig.setText("Please choose one type");
        }

        if(digname.getText().toString().equals("") || digmobile.getText().toString().equals("")
                || digoffice.getText().toString().equals("") || digfax.getText().toString().equals("")
                || type.equals("not define"))
        {
            txtdig.setText("Fields cannot be empty"+type);
        }
        else
        {
            txtdig.setText(type);

            String id =digspin.getSelectedItem().toString();
            String name = digname.getText().toString();
            String range = digrange.getText().toString();
            String tel = digmobile.getText().toString();
            String office = digoffice.getText().toString();
            String fax = digfax.getText().toString();
            String Type = type;

            RequestQueue requestQueue = Volley.newRequestQueue(admin_contact.this);
            StringRequest request  = new StringRequest(Request.Method.POST, updigurl, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    updigResult = response.toString().trim();
                    switch (updigResult){
                        case "update":
                            Toast.makeText(admin_contact.this, "Updated!", Toast.LENGTH_SHORT).show();
                            digname.setText(null);
                            digmobile.setText(null);
                            digoffice.setText(null);
                            digfax.setText(null);
                            digrange.setText(null);
                            dig.setChecked(false);
                            sdig.setChecked(false);
                            break;
                        case "notupdate":
                        case "":
                                Toast.makeText(admin_contact.this, "Not Updated!", Toast.LENGTH_SHORT).show();
                                break;
                        case "empty":
                            Toast.makeText(admin_contact.this, "Fields can not be empty!", Toast.LENGTH_SHORT).show();
                            break;
                    }
                    mProgressDialog.dismiss();

                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(admin_contact.this, "my error :" + error, Toast.LENGTH_LONG).show();
                    //Toast.makeText(admin_contact.this, "Account not created!", Toast.LENGTH_SHORT).show();
                    Log.i("My error", "" + error);
                }
            })
            {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> map = new HashMap<String, String>();
                    map.put("id",id);
                    map.put("name",name);
                    map.put("range",range);
                    map.put("tp",tel);
                    map.put("office",office);
                    map.put("fax",fax);
                    map.put("type",Type);
                    return map;
                }
            };
            requestQueue.add(request);
        }
    }

    //search station
    private void searchDiv(){
        String station = divsearch.getText().toString().trim();

        mProgressDialog = new ProgressDialog(admin_contact.this);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        mProgressDialog.setMessage(getString(R.string.progress_detail));
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setCancelable(false);
        mProgressDialog.setProgress(0);
        mProgressDialog.setProgressNumberFormat(null);
        mProgressDialog.setProgressPercentFormat(null);
        mProgressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, searchdivurl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                String station = "";
                String oic = "";
                String office = "";

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray result = jsonObject.getJSONArray("result");

                    JSONObject Cstation = result.getJSONObject(0);
                    JSONObject Coic = result.getJSONObject(0);
                    JSONObject Coffice = result.getJSONObject(0);
                    station = Cstation.getString("station");
                    oic = Coic.getString("oic");
                    office = Coffice.getString("office");

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(admin_contact.this, "Somethings wrong!", Toast.LENGTH_SHORT).show();
                }

                divstation.setText(""+station);
                divoic.setText(""+oic);
                divoffice.setText(""+office);

                mProgressDialog.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("myError",""+error);
            }
        })
        {
            protected Map<String,String> getParams() throws AuthFailureError{
                Map<String,String> map = new HashMap<String,String>();
                map.put("station",station);
                return map;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(admin_contact.this);
        requestQueue.add(stringRequest);
    }

    //update division details
    private void updateDivs(){

        mProgressDialog = new ProgressDialog(admin_contact.this);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        mProgressDialog.setMessage(getString(R.string.progress_detail));
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setCancelable(false);
        mProgressDialog.setProgress(0);
        mProgressDialog.setProgressNumberFormat(null);
        mProgressDialog.setProgressPercentFormat(null);
        mProgressDialog.show();

        String Cstation = divsearch.getText().toString().trim();
        String Rstation = divstation.getText().toString().trim();
        String Roic = divoic.getText().toString().trim();
        String Roffice = divoffice.getText().toString().trim();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, updivurl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                updivResult = response.toString().trim();
                switch (updivResult){
                    case "update":
                        Toast.makeText(admin_contact.this, "Updated!", Toast.LENGTH_SHORT).show();
                        divsearch.setText(null);
                        divstation.setText(null);
                        divoic.setText(null);
                        divoffice.setText(null);
                        break;
                    case "":
                    case "notupdate":
                        Toast.makeText(admin_contact.this, "Not updated!", Toast.LENGTH_SHORT).show();
                        break;
                }
                mProgressDialog.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("myError",""+error);
            }
        })
        {
            protected Map<String,String> getParams() throws AuthFailureError{
                Map<String,String> map =  new HashMap<String,String>();
                map.put("cStation",Cstation);
                map.put("station",Rstation);
                map.put("oic",Roic);
                map.put("office",Roffice);
                return map;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(admin_contact.this);
        requestQueue.add(stringRequest);
    }

    //set navigation back button
    private void callBack (){
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                Intent i = new Intent(admin_contact.this,admin.class);
                startActivity(i);
                finishAffinity();
            }
        };
        getOnBackPressedDispatcher().addCallback(this,callback);
    }
}