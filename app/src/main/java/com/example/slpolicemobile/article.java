package com.example.slpolicemobile;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

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

public class article extends AppCompatActivity {

    ImageButton back,a1,a2,a3,a4,a5;
    TextView a1h,a2h,a3h,a4h,a5h,a1d,a2d,a3d,a4d,a5d;

    String aDateUrl = "https://slpolicemobile.000webhostapp.com/getArticleDates.php";

    String viewarturl = "https://slpolicemobile.000webhostapp.com/viewArticle.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article);

        back = findViewById(R.id.articleback);
        a1 = findViewById(R.id.article1);
        a2 = findViewById(R.id.article2);
        a3 = findViewById(R.id.article3);
        a4 = findViewById(R.id.article4);
        a5 = findViewById(R.id.article5);

        a1h = findViewById(R.id.article1heading);
        a2h = findViewById(R.id.article2heading);
        a3h = findViewById(R.id.article3heading);
        a4h = findViewById(R.id.article4heading);
        a5h = findViewById(R.id.article5heading);
        a1d = findViewById(R.id.article1date);
        a2d = findViewById(R.id.article2date);
        a3d = findViewById(R.id.article3date);
        a4d = findViewById(R.id.article4date);
        a5d = findViewById(R.id.article5date);

        //back button
        callBack();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(article.this,welcome.class);
                startActivity(i);
            }
        });

        //set values to text views
        StringRequest stringRequest = new StringRequest(Request.Method.POST, aDateUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                String a1H ="";
                String a2H ="";
                String a3H ="";
                String a4H ="";
                String a5H ="";
                String a1D ="";
                String a2D ="";
                String a3D ="";
                String a4D ="";
                String a5D ="";
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray result = jsonObject.getJSONArray("result");

                    JSONObject a1 = result.getJSONObject(0);
                    a1H = a1.getString("title");
                    a1D = a1.getString("date");

                    JSONObject a2 = result.getJSONObject(1);
                    a2H = a2.getString("title");
                    a2D = a2.getString("date");

                    JSONObject a3 = result.getJSONObject(2);
                    a3H = a3.getString("title");
                    a3D = a3.getString("date");

                    JSONObject a4 = result.getJSONObject(3);
                    a4H = a4.getString("title");
                    a4D = a4.getString("date");

                    JSONObject a5 = result.getJSONObject(4);
                    a5H = a5.getString("title");
                    a5D = a5.getString("date");

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //set values
                a1h.setText(a1H);
                a1d.setText(a1D);
                a2h.setText(a2H);
                a2d.setText(a2D);
                a3h.setText(a3H);
                a3d.setText(a3D);
                a4h.setText(a4H);
                a4d.setText(a4D);
                a5h.setText(a5H);
                a5d.setText(a5D);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("My error", "" + error);
            }
        });
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
        //end of set values to text views

        a1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String date = a1d.getText().toString().trim();
                setViewer(date);
            }
        });
        a2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String date = a2d.getText().toString().trim();
                setViewer(date);
            }
        });
        a3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String date = a3d.getText().toString().trim();
                setViewer(date);
            }
        });
        a4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String date = a4d.getText().toString().trim();
                setViewer(date);
            }
        });
        a5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String date = a5d.getText().toString().trim();
                setViewer(date);
            }
        });
    }

    //set articles to viewer
    private void setViewer(String date){

        StringRequest stringRequest = new StringRequest(Request.Method.POST, viewarturl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                final View layout = View.inflate(article.this,R.layout.view_article,null);
                final TextView txttitle =(TextView) layout.findViewById(R.id.view_article_heading);
                final TextView txtbody =(TextView) layout.findViewById(R.id.view_article_body);
                final Button btnclose=(Button) layout.findViewById(R.id.view_article_close);

                AlertDialog.Builder ab = new AlertDialog.Builder(article.this);
                ab.setView(layout);
                ab.setCancelable(false);
                ab.show();
                String title = "";
                String body = "";

                try
                {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray result = jsonObject.getJSONArray("result");

                    JSONObject article = result.getJSONObject(0);
                    title = article.getString("title");
                    body = article.getString("body");
                }
                catch(JSONException e)
                {
                    e.printStackTrace();
                }
                //set values
                txttitle.setText(title);
                txtbody.setText(body);
                if(title.equals(""))
                {
                    txtbody.setText("! DELETED ARTICLE !");
                }

                //close
                btnclose.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent i = new Intent(article.this,article.class);
                        startActivity(i);
                    }
                });
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("myError",""+error);
            }
        })
        {
            protected Map<String,String> getParams() throws AbstractMethodError{
                Map<String,String> map =  new HashMap<String,String>();
                map.put("date",date);
                return map;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(article.this);
        requestQueue.add(stringRequest);
    }

    //set navigation back button
    private void callBack (){
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                Intent i = new Intent(article.this,welcome.class);
                startActivity(i);
                finishAffinity();
            }
        };
        getOnBackPressedDispatcher().addCallback(this,callback);
    }
}