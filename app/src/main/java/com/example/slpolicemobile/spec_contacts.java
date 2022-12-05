package com.example.slpolicemobile;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;

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
import java.util.HashMap;

public class spec_contacts extends AppCompatActivity {

    ImageButton back;
    RadioGroup rg;
    RadioButton dig,sdig;
    ListView lv;

    ProgressDialog mProgressDialog;

    String getdigurl = "https://slpolicemobile.000webhostapp.com/getDig.php";
    String getsdigurl = "https://slpolicemobile.000webhostapp.com/getSdig.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spec_contacts);

        back = findViewById(R.id.scback);
        rg = findViewById(R.id.scradiogroup);
        dig = findViewById(R.id.scdig);
        sdig = findViewById(R.id.scsdig);
        lv = findViewById(R.id.sclistView);

        //back button
        callBack();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(spec_contacts.this,user.class);
                startActivity(i);
            }
        });

        //default radio button
        dig.setChecked(true);

        //set radio button active
        if(dig.isChecked())
        {
            sdig.setChecked(false);
            getDig();
        }
        else if(sdig.isChecked())
        {
            dig.setChecked(false);
            getSdig();
        }

        dig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getDig();
            }
        });

        sdig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getSdig();
            }
        });
    }

    //get dig
    private void getDig(){
        mProgressDialog = new ProgressDialog(spec_contacts.this);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        mProgressDialog.setMessage(getString(R.string.progress_detail));
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setCancelable(false);
        mProgressDialog.setProgress(0);
        mProgressDialog.setProgressNumberFormat(null);
        mProgressDialog.setProgressPercentFormat(null);
        mProgressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, getdigurl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray result = jsonObject.getJSONArray("result");

                    for (int i = 0; i < result.length(); i++) {
                        JSONObject jo = result.getJSONObject(i);
                        String name = jo.getString("name");
                        String aria = jo.getString("aria");
                        String mobile = jo.getString("mobile");
                        String office = jo.getString("office");
                        String fax = jo.getString("fax");

                        final HashMap<String, String> data = new HashMap<String, String>();
                        data.put("name", "Officer = "+name);
                        data.put("aria", "" +aria);
                        data.put("mobile", "Mobile Number = " +mobile);
                        data.put("office", "Office Number = " +office);
                        data.put("fax", "Fax Number = " +fax);

                        list.add(data);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                SimpleAdapter adapter = new SimpleAdapter(spec_contacts.this,list,R.layout.spec_contact_list,
                        new String[]{"aria","name","mobile","office","fax"},
                        new int[]{R.id.tvscaria,R.id.tvscname,R.id.tvscmobile,R.id.tvscoffice,R.id.tvscfax});
                lv.setAdapter(adapter);
                mProgressDialog.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("myError",""+error);
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(spec_contacts.this);
        requestQueue.add(stringRequest);
    }

    //get sdig
    private void getSdig(){
        mProgressDialog = new ProgressDialog(spec_contacts.this);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        mProgressDialog.setMessage(getString(R.string.progress_detail));
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setCancelable(false);
        mProgressDialog.setProgress(0);
        mProgressDialog.setProgressNumberFormat(null);
        mProgressDialog.setProgressPercentFormat(null);
        mProgressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, getsdigurl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray result = jsonObject.getJSONArray("result");

                    for (int i = 0; i < result.length(); i++) {
                        JSONObject jo = result.getJSONObject(i);
                        String name = jo.getString("name");
                        String aria = jo.getString("aria");
                        String mobile = jo.getString("mobile");
                        String office = jo.getString("office");
                        String fax = jo.getString("fax");

                        final HashMap<String, String> data = new HashMap<String, String>();
                        data.put("name", "Officer = "+name);
                        data.put("aria", "" +aria);
                        data.put("mobile", "Mobile Number = " +mobile);
                        data.put("office", "Office Number = " +office);
                        data.put("fax", "Fax Number = " +fax);

                        list.add(data);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                SimpleAdapter adapter = new SimpleAdapter(spec_contacts.this,list,R.layout.spec_contact_list,
                        new String[]{"aria","name","mobile","office","fax"},
                        new int[]{R.id.tvscaria,R.id.tvscname,R.id.tvscmobile,R.id.tvscoffice,R.id.tvscfax});
                lv.setAdapter(adapter);
                mProgressDialog.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("myError",""+error);
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(spec_contacts.this);
        requestQueue.add(stringRequest);
    }

    //set navigation back button
    private void callBack (){
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                Intent i = new Intent(spec_contacts.this,user.class);
                startActivity(i);
                finishAffinity();
            }
        };
        getOnBackPressedDispatcher().addCallback(this,callback);
    }
}