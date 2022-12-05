package com.example.slpolicemobile;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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

public class admin_article extends AppCompatActivity {

    ImageButton back;
    TextView deletetitle;
    EditText uptitle,upbody,addtitle,addbody;
    Button delete,update,updatecls,add,addcls;
    Spinner deletespin,updatespin;

    String dateurl = "https://slpolicemobile.000webhostapp.com/getArticleDatesToSpinner.php";
    String delarturl = "https://slpolicemobile.000webhostapp.com/getArticleTitle.php";
    String delurl = "https://slpolicemobile.000webhostapp.com/deleteArticle.php";
    String delResult;
    String upurl = "https://slpolicemobile.000webhostapp.com/updateArticles.php";
    String upResult;
    String datecheckurl = "https://slpolicemobile.000webhostapp.com/matchArticleDates.php";
    String dateCheckResult;
    String addarturl = "https://slpolicemobile.000webhostapp.com/addarticle.php";
    String addResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_article);

        back = findViewById(R.id.aarticleback);

        deletetitle = findViewById(R.id.admin_article_title);

        uptitle = findViewById(R.id.admin_article_update_title);
        upbody = findViewById(R.id.admin_article_update_body);
        addtitle = findViewById(R.id.admin_article_add_title);
        addbody = findViewById(R.id.admin_article_add_body);

        delete = findViewById(R.id.admin_article_delete_btn);
        update = findViewById(R.id.admin_article_update_btn);
        updatecls = findViewById(R.id.admin_article_update_clear_btn);
        add = findViewById(R.id.admin_article_add_btn);
        addcls = findViewById(R.id.admin_article_add_clear_btn);

        deletespin = findViewById(R.id.admin_article_delete_spin);
        updatespin = findViewById(R.id.admin_article_update_spin);

        //spinner values
        getDates(deletespin);
        getDates(updatespin);

        //back button
        callBack();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(admin_article.this,admin.class);
                startActivity(i);
            }
        });

        deletespin.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                getArticleTitle();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder ab = new AlertDialog.Builder(admin_article.this);
                ab.setCancelable(false)
                        .setTitle("Warning!")
                        .setMessage("Are you sure to delete item?")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //nothing happen
                            }
                        })
                        .setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                               deleteArticle();
                            }
                        })
                        .show();
            }
        });

        updatecls.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                uptitle.setText(null);
                upbody.setText(null);
            }
        });

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(uptitle.getText().toString().equals(""))
                {
                    Toast.makeText(admin_article.this, "Required Title!", Toast.LENGTH_SHORT).show();
                    uptitle.requestFocus();
                }
                else if(upbody.getText().toString().equals(""))
                {
                    Toast.makeText(admin_article.this, "Required Article Body!", Toast.LENGTH_SHORT).show();
                    upbody.requestFocus();
                }
                else
                {
                    AlertDialog.Builder ab = new AlertDialog.Builder(admin_article.this);
                    ab.setTitle("Warning!")
                            .setMessage("Are you sure to update article?")
                            .setCancelable(false)
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .setPositiveButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    //nothing happen
                                }
                            })
                            .setNegativeButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    updateArticle();
                                }
                            })
                            .show();
                }
            }
        });

        addcls.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addtitle.setText(null);
                addbody.setText(null);
            }
        });

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

                if(!addtitle.getText().toString().equals("") || !addbody.getText().toString().equals("")) {
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, datecheckurl, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            dateCheckResult = response.toString().trim();
                            switch (dateCheckResult){
                                case "match":
                                    Toast.makeText(admin_article.this, "can not add", Toast.LENGTH_SHORT).show();
                                    AlertDialog.Builder ab = new AlertDialog.Builder(admin_article.this);
                                    ab.setIcon(android.R.drawable.ic_dialog_alert)
                                            .setCancelable(true)
                                            .setTitle("Spam Protector")
                                            .setMessage("Sorry Can not add articles at this moment!\n Please try again after 24 hours..\n\n\nThis is for spam protection!!")
                                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialogInterface, int i) {
                                                    // nothing happen
                                                    addtitle.setText(null);
                                                    addbody.setText(null);
                                                }
                                            })
                                            .show();
                                    break;
                                case "notmatch":
                                case "":
                                    //Toast.makeText(admin_article.this, "done", Toast.LENGTH_SHORT).show();
                                    addArticles();
                                    break;
                            }

                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            Log.i("myError",""+error);
                        }
                    })
                    {
                        protected Map<String,String> getParams() throws AbstractMethodError{
                            Map<String,String> map = new HashMap<String,String>();
                            map.put("date",date);
                            return map;
                        }
                    };
                    RequestQueue requestQueue = Volley.newRequestQueue(admin_article.this);
                    requestQueue.add(stringRequest);
                }
                else
                {
                    Toast.makeText(admin_article.this, "Fields can not be empty!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    //get Dates to spinner
    private void getDates(Spinner date){

        StringRequest stringRequest = new StringRequest(Request.Method.POST, dateurl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                ArrayList<String> datelist = new ArrayList<String>();
                HashSet<String> hashSet = new HashSet<String>();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray result = jsonObject.getJSONArray("result");

                    for (int i = 0; i < result.length(); i++) {
                        JSONObject jo = result.getJSONObject(i);
                        hashSet.addAll(Collections.singleton(jo.getString("date")));
                        datelist.clear();
                        datelist.addAll(hashSet);

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //set spinner to division string[] values
                date.setAdapter(new ArrayAdapter<String>(admin_article.this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,datelist));
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

    //get Title to delete
    private void getArticleTitle(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, delarturl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                String gotTitle = "";
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray result = jsonObject.getJSONArray("result");
                    JSONObject title = result.getJSONObject(0);
                    gotTitle = title.getString("title");
                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }
                deletetitle.setText(gotTitle);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("myError",""+error);
            }
        })
        {
            final String date = deletespin.getSelectedItem().toString().trim();

            protected Map<String,String> getParams() throws AbstractMethodError{
                Map<String,String> map = new HashMap<String,String>();
                map.put("date",date);
                return map;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(admin_article.this);
        requestQueue.add(stringRequest);
    }

    //delete article
    private void deleteArticle(){
        final String date = deletespin.getSelectedItem().toString().trim();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, delurl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                delResult = response.toString().trim();
                switch (delResult){
                    case "delete":
                        Toast.makeText(admin_article.this, "Deleted", Toast.LENGTH_SHORT).show();
                        break;
                    case "notdelete":
                    case "":
                        Toast.makeText(admin_article.this, "Not Deleted", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("myError",""+error);
            }
        })
        {
            protected Map<String,String> getParams(){
                Map<String,String> map = new HashMap<String,String>();
                map.put("date",date);
                return map;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(admin_article.this);
        requestQueue.add(stringRequest);
    }

    //update article
    private void updateArticle(){
        String title = uptitle.getText().toString().trim();
        String body = upbody.getText().toString().trim();
        String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
        final String cdate = updatespin.getSelectedItem().toString().trim();

        //title max length
        InputFilter[] titleFilters = uptitle.getFilters();
        InputFilter[] newtitleFilters = new InputFilter[titleFilters.length + 1];
        System.arraycopy(titleFilters, 0, newtitleFilters, 0, titleFilters.length);
        newtitleFilters[titleFilters.length] = new InputFilter.LengthFilter(100);
        uptitle.setFilters(newtitleFilters);

        //body max length
        InputFilter[] bodyFilters = upbody.getFilters();
        InputFilter[] newbodyFilters = new InputFilter[bodyFilters.length + 1];
        System.arraycopy(bodyFilters, 0, newbodyFilters, 0, bodyFilters.length);
        newbodyFilters[bodyFilters.length] = new InputFilter.LengthFilter(1000);
        upbody.setFilters(newbodyFilters);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, upurl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                upResult = response.toString().trim();
                switch (upResult){
                    case "update":
                        Toast.makeText(admin_article.this, "Updated!", Toast.LENGTH_SHORT).show();
                        uptitle.setText(null);
                        upbody.setText(null);
                        break;
                    case "notupdate":
                    case "":
                        Toast.makeText(admin_article.this, "Not Updated!", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("myError",""+error);
            }
        })
        {
            protected Map<String,String> getParams(){
                Map<String,String> map = new HashMap<String,String>();
                map.put("title",title);
                map.put("body",body);
                map.put("date",date);
                map.put("cdate",cdate);
                return map;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(admin_article.this);
        requestQueue.add(stringRequest);
    }

    private void addArticles()
    {
        String title = addtitle.getText().toString().trim();
        String body = addbody.getText().toString().trim();
        String cdate = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

        StringRequest request = new StringRequest(Request.Method.POST, addarturl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                addResult = response.toString().trim();
                switch (addResult){
                    case "update":
                        Toast.makeText(admin_article.this, "Added Successfully!", Toast.LENGTH_SHORT).show();
                        addtitle.setText(null);
                        addbody.setText(null);
                        break;
                    case "":
                    case "notupdate":
                        AlertDialog.Builder ab =new AlertDialog.Builder(admin_article.this);
                        ab.setCancelable(true)
                                .setTitle("Warning!!")
                                .setMessage("Somethings wrong! delete unwanted article and try again!")
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        //nothing happen
                                    }
                                })
                                .show();
                        break;
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("myError",""+error);
            }
        })
        {
            protected Map<String,String> getParams() throws AbstractMethodError{
                Map<String,String> map =new HashMap<String,String>();
                map.put("title",title);
                map.put("body",body);
                map.put("cdate",cdate);
                return map;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(admin_article.this);
        requestQueue.add(request);
    }

    //set navigation back button
    private void callBack (){
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                Intent i = new Intent(admin_article.this,admin.class);
                startActivity(i);
                finishAffinity();
            }
        };
        getOnBackPressedDispatcher().addCallback(this,callback);
    }
}