package com.example.slpolicemobile;

import static android.webkit.WebStorage.getInstance;
import static com.android.volley.VolleyLog.TAG;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
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
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class admin extends AppCompatActivity {

    Button article, con, law, search, set, viewfb;
    ImageButton back, call, mail, clear, settings;
    EditText searchfield, sname, tel;
    ListView listView;
    Spinner datespin;

    public static final String DATA_URL = "https://slpolicemobile.000webhostapp.com/searchUser.php";
    public static final String JSON_ARRAY = "result";
    String url = "https://slpolicemobile.000webhostapp.com/setAdmin.php";
    String result;

    String Date;
    String fburl ="https://slpolicemobile.000webhostapp.com/searchFeedbacks.php";
    String dateurl = "https://slpolicemobile.000webhostapp.com/getFeedbackDatesToSpinner.php";
    String delfburl = "https://slpolicemobile.000webhostapp.com/deleteFeedbacks.php";
    String delresult;
    ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        article = findViewById(R.id.aarticle);
        con = findViewById(R.id.acontact);
        law = findViewById(R.id.alaw);
        search = findViewById(R.id.asearch);
        set = findViewById(R.id.asetadmin);
        viewfb = findViewById(R.id.afbview);

        back = findViewById(R.id.aback);
        clear = findViewById(R.id.aclssearch);
        call = findViewById(R.id.call);
        mail = findViewById(R.id.amail);
        settings = findViewById(R.id.asetting);

        searchfield = findViewById(R.id.atxtsesrch);
        sname = findViewById(R.id.asearchedname);
        tel = findViewById(R.id.asearchedtp);

        listView = findViewById(R.id.alistview);

        datespin = findViewById(R.id.afeedbackspin);

        getDates();

        //set back button
        callBack();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(admin.this,welcome.class);
                startActivity(i);
            }
        });

        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchfield.setText(null);
                sname.setText(null);
                tel.setText(null);
            }
        });

        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(tel.getText().toString().length()>=10)
                {
                    call.setImageResource(R.drawable.ic_donecall);
                    //Toast.makeText(admin.this, "done", Toast.LENGTH_SHORT).show();
                    Uri number = Uri.parse("tel:"+tel.getText().toString());
                    Intent i = new Intent(Intent.ACTION_DIAL,number);
                    startActivity(i);
                }
                else
                {
                    Toast.makeText(admin.this, "Number is not valid or fields are empty!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        mail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!searchfield.getText().toString().equals(""))
                {
                    Intent i = new Intent(Intent.ACTION_SEND);
                    i.setType("message/rfc822");
                    i.putExtra(Intent.EXTRA_EMAIL, new String[]{""+searchfield.getText().toString()});
                    i.putExtra(Intent.EXTRA_SUBJECT, "SL Police Mobile");
                    startActivity(Intent.createChooser(i,"Choose a application"));
                }
                else
                {
                    Toast.makeText(admin.this, "No email address found!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(admin.this,admin_settings.class);
                startActivity(i);
            }
        });

        article.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i =  new Intent(admin.this,admin_article.class);
                startActivity(i);
            }
        });

        con.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(admin.this,admin_contact.class);
                startActivity(i);
            }
        });

        law.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(admin.this,admin_law.class);
                startActivity(i);
            }
        });

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String mail = searchfield.getText().toString().trim();
                if(mail.equals(""))
                {
                    Toast.makeText(admin.this, "enter user email address", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    String url = DATA_URL;
                    StringRequest request = new StringRequest(Request.Method.POST,url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            Log.d(TAG,response.toString());
                            showJSONS(response);
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            VolleyLog.d(TAG, "Error: " + error.getMessage());
                        }
                    }){
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> data = new HashMap<>();
                            data.put("mail",mail);
                            return data;
                        }
                    };
                    RequestQueue requestQueue = Volley.newRequestQueue(admin.this);
                    requestQueue.add(request);
                }
            }
        });

        set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!sname.getText().equals("") && !tel.getText().equals("")) {
                    String mail = searchfield.getText().toString().trim();

                    RequestQueue requestQueue = Volley.newRequestQueue(admin.this);
                    StringRequest request = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            result = response.toString().trim();
                            switch (result) {
                                case "added":
                                    Toast.makeText(admin.this, "Updated!", Toast.LENGTH_SHORT).show();
                                    searchfield.setText(null);
                                    sname.setText(null);
                                    tel.setText(null);
                                    break;
                                case "notadd":
                                case "":
                                    Toast.makeText(admin.this, "not Updated!", Toast.LENGTH_SHORT).show();
                                    break;
                                case "dberror":
                                    Toast.makeText(admin.this, "Something went wrong!", Toast.LENGTH_SHORT).show();
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
                            map.put("email", mail);
                            return map;
                        }
                    };
                    requestQueue.add(request);
                }
                else
                {
                    Toast.makeText(admin.this, "Can not add admins with empty fields!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        viewfb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getMatchData();
            }
        });
    }

    private void showJSONS(String response) {
        String name = "";
        String tp = "";

        try {
            JSONObject jsonObject = new JSONObject(response);
            JSONArray result = jsonObject.getJSONArray(JSON_ARRAY);

            JSONObject usersname = result.getJSONObject(0);
            JSONObject userstp = result.getJSONObject(0);
            name = usersname.getString("name");
            tp = userstp.getString("tp");

        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(this, "Account not found", Toast.LENGTH_SHORT).show();
        }

        sname.setText(""+name);
        tel.setText(""+tp);
    }

    //get spinner selected value contents to list view
    String name;
    String feedback;
    private void getMatchData(){
        Date = datespin.getSelectedItem().toString();

        mProgressDialog = new ProgressDialog(admin.this);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        mProgressDialog.setMessage(getString(R.string.progress_detail));
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setCancelable(false);
        mProgressDialog.setProgress(0);
        mProgressDialog.setProgressNumberFormat(null);
        mProgressDialog.setProgressPercentFormat(null);
        mProgressDialog.show();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, fburl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray result = jsonObject.getJSONArray("result");

                    for (int i = 0; i < result.length(); i++) {
                        JSONObject jo = result.getJSONObject(i);
                        name = jo.getString("name");
                        feedback = jo.getString("feedback");

                        final HashMap<String, String> data = new HashMap<String, String>();
                        data.put("name", ""+name);
                        data.put("feedback", ""+feedback);

                        list.add(data);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                SimpleAdapter adapter = new SimpleAdapter(admin.this,list,R.layout.feedback_list,
                        new String[]{"feedback","name"},
                        new int[]{R.id.tvadminfeedback,R.id.tvadminusername});
                listView.setAdapter(adapter);

                mProgressDialog.dismiss();

                listView.setItemsCanFocus(true);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        popUpMessage(name);
                    }
                });
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
                map.put("date", Date);
                return map;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
    }

    //get Dates to spinner
    private void getDates(){

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
                datespin.setAdapter(new ArrayAdapter<String>(admin.this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,datelist));
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

    private void popUpMessage(String name){
        AlertDialog.Builder ab = new AlertDialog.Builder(admin.this);
        ab.setCancelable(true);
        ab.setMessage("Are you sure to delete "+name+"'s feedback?");
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
                StringRequest stringRequest = new StringRequest(Request.Method.POST, delfburl, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        delresult = response.toString().trim();
                        switch (delresult){
                            case "notdeleted":
                                Toast.makeText(admin.this, "Not deleted!", Toast.LENGTH_SHORT).show();
                                break;
                            case "deleted":
                                Toast.makeText(admin.this, "Deleted!", Toast.LENGTH_SHORT).show();
                                break;
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
                    protected Map<String,String> getParams() throws  AuthFailureError
                    {
                        HashMap<String,String> map = new HashMap<String,String>();
                        map.put("name",name);
                        return map;
                    }
                };
                RequestQueue requestQueue = Volley.newRequestQueue(getApplication());
                requestQueue.add(stringRequest);
            }
        });
        ab.show();
    }

    //set navigation back button
    private void callBack (){
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                Intent i = new Intent(admin.this,welcome.class);
                startActivity(i);
                finishAffinity();
            }
        };
        getOnBackPressedDispatcher().addCallback(this,callback);
    }
}