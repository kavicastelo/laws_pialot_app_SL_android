package com.example.slpolicemobile;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.util.Linkify;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;

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

public class laws extends AppCompatActivity {

    ImageButton back;
    Spinner chooselawcat;
    RadioButton act, amendment;
    RadioGroup rg;
    ListView lv;

    String getcaturl = "https://slpolicemobile.000webhostapp.com/getlawCatNamesToSpinner.php";
    String getacturl = "https://slpolicemobile.000webhostapp.com/getCatBook.php";
    String getcatamendmenturl = "https://slpolicemobile.000webhostapp.com/getCatAmendment.php";

    String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_laws);

        back = findViewById(R.id.lawback);

        chooselawcat = findViewById(R.id.llawcat);

        lv = findViewById(R.id.catlistView);

        act = findViewById(R.id.rbtnact);
        amendment = findViewById(R.id.rbtnamendment);
        rg = findViewById(R.id.radioGroup);

        //get categories to spinner
        getLawCatToSpinner();

        //back button
        callBack();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(laws.this,welcome.class);
                startActivity(i);
            }
        });

        //set radio button active
        if(act.isChecked())
        {
            amendment.setChecked(false);
            getAct();
        }
        else if(amendment.isChecked())
        {
            act.setChecked(false);
            getAmendment();
        }

        act.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getAct();
            }
        });

        amendment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getAmendment();
            }
        });
    }

    //get law categories to spinner
    private void getLawCatToSpinner(){
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
                chooselawcat.setAdapter(new ArrayAdapter<String>(laws.this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,catlist));
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.i("myError",""+volleyError);
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(laws.this);
        requestQueue.add(stringRequest);
    }

    //get book values
    private void getAct(){
        String cat = chooselawcat.getSelectedItem().toString().trim();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, getacturl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray result = jsonObject.getJSONArray("result");

                    for (int i = 0; i < result.length(); i++) {
                        JSONObject jo = result.getJSONObject(i);
                        String name = jo.getString("name");
                        String language = jo.getString("language");
                        url = jo.getString("url");

                        String lang = "";
                        if(language.equals("S"))
                        {
                            lang = "Sinhala";
                        }
                        else if(language.equals("E"))
                        {
                            lang = "English";
                        }

                        String Uri = "";
                        if(!url.equals(""))
                        {
                            Uri = "Click to Open PDF";
                        }

                        final HashMap<String, String> data = new HashMap<String, String>();
                        data.put("name",name);
                        data.put("language",lang);
                        data.put("url",Uri);

                        list.add(data);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                SimpleAdapter adapter = new SimpleAdapter(laws.this,list,R.layout.cat_list,
                        new String[]{"name","language","url"},
                        new int[]{R.id.tvcatname,R.id.tvcatlanguage,R.id.tvcaturl});

                lv.setAdapter(adapter);
                lv.setItemsCanFocus(true);
                lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                        popupPdf(url);
                    }
                });
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
                return map;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(laws.this);
        requestQueue.add(stringRequest);
    }

    //get book values
    private void getAmendment(){
        String cat = chooselawcat.getSelectedItem().toString().trim();

        StringRequest stringRequest = new StringRequest(Request.Method.POST,getcatamendmenturl , new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray result = jsonObject.getJSONArray("result");

                    for (int i = 0; i < result.length(); i++) {
                        JSONObject jo = result.getJSONObject(i);
                        String name = jo.getString("name");
                        String language = jo.getString("language");
                        url = jo.getString("url");

                        String lang = "";
                        if(language.equals("S"))
                        {
                            lang = "Sinhala";
                        }
                        else if(language.equals("E"))
                        {
                            lang = "English";
                        }

                        String Uri = "";
                        if(!url.equals(""))
                        {
                            Uri = "Click to Open PDF";
                        }

                        final HashMap<String, String> data = new HashMap<String, String>();
                        data.put("name", name);
                        data.put("language",lang);
                        data.put("url",Uri);

                        list.add(data);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                SimpleAdapter adapter = new SimpleAdapter(laws.this,list,R.layout.cat_list,
                        new String[]{"name","language","url"},
                        new int[]{R.id.tvcatname,R.id.tvcatlanguage,R.id.tvcaturl});

                lv.setAdapter(adapter);
                lv.setItemsCanFocus(true);
                lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                        popupPdf(url);
                    }
                });
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
                return map;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(laws.this);
        requestQueue.add(stringRequest);
    }

    //popup to view pdf
    private void popupPdf(String url){
        AlertDialog.Builder ab = new AlertDialog.Builder(laws.this);
        ab.setCancelable(true);
        ab.setMessage("Open "+url+" PDF");
        ab.setTitle("View PDF");
        ab.setCancelable(true);
        ab.setPositiveButton("open", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent link = new Intent(Intent.ACTION_VIEW);
                link.setData(Uri.parse(url));
                startActivity(link);
            }
        });
        ab.show();
    }

    //set navigation back button
    private void callBack (){
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                Intent i = new Intent(laws.this,welcome.class);
                startActivity(i);
                finishAffinity();
            }
        };
        getOnBackPressedDispatcher().addCallback(this,callback);
    }
}