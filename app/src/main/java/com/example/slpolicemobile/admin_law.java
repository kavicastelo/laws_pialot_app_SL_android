package com.example.slpolicemobile;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
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

import java.io.ByteArrayOutputStream;
import java.nio.file.FileStore;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class admin_law extends AppCompatActivity {

    ImageButton back;
    EditText addcat,addbook,addbookurl,addamendment,amendmenturl,updateamendment,updateamendmentname;
    Button addlawadd,addlawclear,deletecat,btnupdateamendment;
    Spinner deletecatspin,updateamendmentspin;
    RadioGroup rg,uprg;
    RadioButton addsinhala, addenglish, upsinhala, upenglish;

    ProgressDialog mProgressDialog;

    String addcaturl ="https://slpolicemobile.000webhostapp.com/add_law_category.php";
    String addCatResult;

    String getcaturl = "https://slpolicemobile.000webhostapp.com/getlawCatNamesToSpinner.php";

    String delcaturl = "https://slpolicemobile.000webhostapp.com/deleteLawCategory.php";
    String delresult;

    String updateamenurl = "https://slpolicemobile.000webhostapp.com/updateAmendment.php";
    String updateResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_law);

        back = findViewById(R.id.alawback);

        addcat = findViewById(R.id.aladdcat);
        addbook = findViewById(R.id.aladdlow);
        addbookurl = findViewById(R.id.aladdlowurl);
        addamendment = findViewById(R.id.aladdamendment);
        amendmenturl = findViewById(R.id.aladdamendmenturl);
        updateamendment = findViewById(R.id.alupdateamendment);
        updateamendmentname = findViewById(R.id.alupdateamendmentname);

        addlawadd = findViewById(R.id.albtnaddcat);
        addlawclear = findViewById(R.id.albtnclscat);
        deletecat = findViewById(R.id.albtndeletecat);
        btnupdateamendment = findViewById(R.id.albtnupdateamendment);

        deletecatspin = findViewById(R.id.aldeletecatspin);
        updateamendmentspin = findViewById(R.id.alupdateamendmentspin);

        addsinhala = findViewById(R.id.aladdcatsinhala);
        addenglish = findViewById(R.id.aladdcatenglish);
        upsinhala = findViewById(R.id.alupdatesinhala);
        upenglish = findViewById(R.id.alupdateenglish);

        //put spinner values
        getLawCatToSpinner(deletecatspin);
        getLawCatToSpinner(updateamendmentspin);

        //back button
        callBack();

        //set default radio buttons
        addsinhala.setChecked(true);
        upsinhala.setChecked(true);

        //move radio
        if(addsinhala.isChecked())
        {
            addenglish.setChecked(false);
        }
        else if(addenglish.isChecked())
        {
            addsinhala.setChecked(false);
        }
        else if(upsinhala.isChecked())
        {
            upenglish.setChecked(false);
        }
        else if(upenglish.isChecked())
        {
            upsinhala.setChecked(false);
        }

        addlawadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(addamendment.getText().toString().equals("") && amendmenturl.getText().toString().equals(""))
                {
                    AlertDialog.Builder ab = new AlertDialog.Builder(admin_law.this);
                    ab.setTitle("Submit")
                            .setMessage("Are you sure to submit without an amendment?")
                            .setPositiveButton("No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    //nothing happen
                                }
                            })
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    addLaw();
                                }
                            })
                            .show();
                }
                else
                {
                    if(addcat.getText().toString().equals("") || addbook.getText()
                            .toString().equals("") || addbookurl.getText().toString().equals(""))
                    {
                        Toast.makeText(admin_law.this, "Required fields are empty!", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        addLaw();
                    }
                }
            }
        });

        deletecat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteCategory(deletecatspin.getSelectedItem().toString().trim());
            }
        });

        btnupdateamendment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!updateamendmentname.getText().toString().equals("")||!updateamendment.getText().toString().equals(""))
                {
                    updateAmendment();
                }
                else
                {
                    Toast.makeText(admin_law.this, "Required fields are empty!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(admin_law.this,admin.class);
                startActivity(i);
            }
        });

        addlawclear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addbook.setText(null);
                addbookurl.setText(null);
                addamendment.setText(null);
                amendmenturl.setText(null);
                addcat.setText(null);
                addsinhala.setChecked(true);
            }
        });
    }

    //add new law category
    private void addLaw(){
        //set values to radio
        mProgressDialog = new ProgressDialog(admin_law.this);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        mProgressDialog.setMessage(getString(R.string.progress_detail));
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setCancelable(false);
        mProgressDialog.setProgress(0);
        mProgressDialog.setProgressNumberFormat(null);
        mProgressDialog.setProgressPercentFormat(null);
        mProgressDialog.show();

        String lang = "";
        if(addsinhala.isChecked())
        {
            lang = "S";
        }
        else if(addenglish.isChecked())
        {
            lang = "E";
        }

        String cat = addcat.getText().toString().trim();
        String bookname = addbook.getText().toString().trim();
        String bookurl = addbookurl.getText().toString().trim();
        String language = lang.toString().trim();
        String amendment = addamendment.getText().toString().trim();
        String amendmentUrl = amendmenturl.getText().toString().trim();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, addcaturl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                addCatResult = response.toString().trim();
                switch (addCatResult){
                    case "bookadd":
                        Toast.makeText(admin_law.this, "Added Successfully!", Toast.LENGTH_SHORT).show();
                        addbook.setText(null);
                        addbookurl.setText(null);
                        addamendment.setText(null);
                        amendmenturl.setText(null);
                        addcat.setText(null);
                        addsinhala.setChecked(true);
                        break;
                    case "booknotadd":
                    case "":
                        Toast.makeText(admin_law.this, "Not added!", Toast.LENGTH_SHORT).show();
                        break;
                }
                mProgressDialog.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.i("myError",""+volleyError);
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map = new HashMap<String,String>();
                map.put("cat",cat);
                map.put("book",bookname);
                map.put("bookurl",bookurl);
                map.put("language",language);
                map.put("amendment",amendment);
                map.put("amendmenturl",amendmentUrl);
                return map;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(admin_law.this);
        requestQueue.add(stringRequest);
    }

    //get law categories to spinner
    private void getLawCatToSpinner(Spinner spin){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, getcaturl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                ArrayList<String> catlist = new ArrayList<String>();
                HashSet<String> hashSet = new HashSet<String>(); //remove duplicate results from jsonArray
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray result = jsonObject.getJSONArray("result");

                    for (int i = 0; i < result.length(); i++) {
                        JSONObject jo = result.getJSONObject(i);
                        hashSet.addAll(Collections.singleton(jo.getString("cat")));
                        catlist.clear();
                        catlist.addAll(hashSet);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                spin.setAdapter(new ArrayAdapter<String>(admin_law.this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,catlist));
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.i("myError",""+volleyError);
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(admin_law.this);
        requestQueue.add(stringRequest);
    }

    //delete law category
    private void deleteCategory(String cat){
        mProgressDialog = new ProgressDialog(admin_law.this);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        mProgressDialog.setMessage(getString(R.string.progress_detail));
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setCancelable(false);
        mProgressDialog.setProgress(0);
        mProgressDialog.setProgressNumberFormat(null);
        mProgressDialog.setProgressPercentFormat(null);
        mProgressDialog.show();

        android.app.AlertDialog.Builder ab = new android.app.AlertDialog.Builder(admin_law.this);
        ab.setCancelable(true);
        ab.setMessage("Are you sure to delete "+cat+" ?");
        ab.setTitle("Delete");
        ab.setIcon(getResources().getDrawable(android.R.drawable.ic_dialog_alert));
        ab.setPositiveButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //nothing happen
            }
        });
        ab.setNegativeButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //delete
                StringRequest stringRequest = new StringRequest(Request.Method.POST, delcaturl, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        delresult = response.toString().trim();
                        switch (delresult){
                            case "notdeleted":
                                Toast.makeText(admin_law.this, "Not deleted!", Toast.LENGTH_SHORT).show();
                                break;
                            case "deleted":
                                Toast.makeText(admin_law.this, "Deleted!", Toast.LENGTH_SHORT).show();
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
                    @Override
                    protected Map<String,String> getParams() throws  AuthFailureError
                    {
                        HashMap<String,String> map = new HashMap<String,String>();
                        map.put("cat",cat);
                        return map;
                    }
                };
                RequestQueue requestQueue = Volley.newRequestQueue(getApplication());
                requestQueue.add(stringRequest);
            }
        });
        ab.show();
    }

    //update amendment
    private void updateAmendment(){
        //set values to radio
        mProgressDialog = new ProgressDialog(admin_law.this);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        mProgressDialog.setMessage(getString(R.string.progress_detail));
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setCancelable(false);
        mProgressDialog.setProgress(0);
        mProgressDialog.setProgressNumberFormat(null);
        mProgressDialog.setProgressPercentFormat(null);
        mProgressDialog.show();

        String lang = "";
        if(upsinhala.isChecked())
        {
            lang = "S";
        }
        else if(upenglish.isChecked())
        {
            lang = "E";
        }

        String cat = updateamendmentspin.getSelectedItem().toString().trim();
        String language = lang.toString().trim();
        String amendment = updateamendmentname.getText().toString().trim();
        String amendmentUrl = updateamendment.getText().toString().trim();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, updateamenurl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                updateResult = response.toString().trim();
                switch (updateResult){
                    case "update":
                        Toast.makeText(admin_law.this, "Updated!", Toast.LENGTH_SHORT).show();
                        addbook.setText(null);
                        addbookurl.setText(null);
                        addamendment.setText(null);
                        amendmenturl.setText(null);
                        addcat.setText(null);
                        addsinhala.setChecked(true);
                        break;
                    case "notupdate":
                    case "":
                        Toast.makeText(admin_law.this, "Not updated!", Toast.LENGTH_SHORT).show();
                        break;
                }
                mProgressDialog.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.i("myError",""+volleyError);
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> map = new HashMap<String,String>();
                map.put("cat",cat);
                map.put("language",language);
                map.put("amendment",amendment);
                map.put("amendmenturl",amendmentUrl);
                return map;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(admin_law.this);
        requestQueue.add(stringRequest);
    }

    //set navigation back button
    private void callBack (){
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                Intent i = new Intent(admin_law.this,admin.class);
                startActivity(i);
                finishAffinity();
            }
        };
        getOnBackPressedDispatcher().addCallback(this,callback);
    }
}