package com.example.slpolicemobile;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class welcome extends AppCompatActivity {

    Button art, rank,law,cont, log;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        //hyperlink activate
        setupHyperlink();

        //back button
        callBack();

        art=findViewById(R.id.wbtnarticle);
        rank=findViewById(R.id.wbtnranks);
        law=findViewById(R.id.wbtnlaws);
        cont=findViewById(R.id.wbtncontacts);
        log=findViewById(R.id.wbtnlogin);

        art.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkNetwork())
                {
                    Intent i = new Intent(welcome.this, article.class);
                    startActivity(i);
                }
                else
                {
                    Toast.makeText(welcome.this, "No Internet Connection! Please try again!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        rank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(welcome.this,police_ranks.class);
                startActivity(i);
            }
        });

        law.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkNetwork())
                {
                    Intent i = new Intent(welcome.this,laws.class);
                    startActivity(i);
                }
                else
                {
                    Toast.makeText(welcome.this, "No Internet Connection! Please try again!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        cont.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //show dialog box
                showDialog();
            }
        });

        log.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkNetwork())
                {
                    Intent i = new Intent(welcome.this,login.class);
                    startActivity(i);
                }
                else
                {
                    Intent i = new Intent(welcome.this,noConnection.class);
                    startActivity(i);
                }
            }
        });
    }
    //hyperlink
    private void setupHyperlink() {
        TextView tv = findViewById(R.id.textView7);
        tv.setMovementMethod(LinkMovementMethod.getInstance());
        tv.setLinkTextColor(Color.BLUE);
    }

    //contacts dialog box
    private void showDialog() {
        AlertDialog.Builder ad = new AlertDialog.Builder(welcome.this);
        ad.setCancelable(true);
        ad.setTitle("Warning!");
        ad.setMessage("Need login to the SL Police Mobile for activate this feature. Click HERE button to login.");
        ad.setIcon(getResources().getDrawable(android.R.drawable.ic_dialog_alert));
        ad.setPositiveButton("Ok",
                new DialogInterface.OnClickListener() {

                       @Override
                       public void onClick(DialogInterface dialog,
                                          int which) {
                          //close dialog
                      }
                  });
        ad.show();
    }

    //set navigation back button
    private void callBack (){
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(welcome.this);
                alertDialogBuilder.setTitle("Exit Application?");
                alertDialogBuilder
                        .setMessage("Click yes to exit!")
                        .setCancelable(false)
                        .setPositiveButton("Yes",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        moveTaskToBack(true);
                                        android.os.Process.killProcess(android.os.Process.myPid());
                                        System.exit(0);
                                        finishAffinity();
                                    }
                                })

                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                dialog.cancel();
                            }
                        });

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        };
        getOnBackPressedDispatcher().addCallback(this,callback);
    }

    boolean checkNetwork(){
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
         if(networkInfo != null)
         {
             if(networkInfo.isConnected())
             {
                 return true;
             }
             else
             {
                 return false;
             }
         }
         else
         {
             return false;
         }
    }
}