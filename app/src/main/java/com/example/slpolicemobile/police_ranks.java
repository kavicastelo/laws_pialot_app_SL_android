package com.example.slpolicemobile;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

public class police_ranks extends AppCompatActivity {

    ImageButton back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_police_ranks);

        //links activate
        setupMedalLink();
        setupHierarchyLink();

        //back button
        callBack();

        back=findViewById(R.id.rback);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(police_ranks.this,welcome.class);
                startActivity(i);
            }
        });
    }
    //medal link
    private void setupMedalLink() {
        TextView tv = findViewById(R.id.medal_link);
        tv.setMovementMethod(LinkMovementMethod.getInstance());
        tv.setLinkTextColor(Color.BLUE);
    }

    //medal link
    private void setupHierarchyLink() {
        TextView tv = findViewById(R.id.hierarchy_link);
        tv.setMovementMethod(LinkMovementMethod.getInstance());
        tv.setLinkTextColor(Color.BLUE);
    }

    //set navigation back button
    private void callBack (){
        OnBackPressedCallback callback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                Intent i = new Intent(police_ranks.this,welcome.class);
                startActivity(i);
                finishAffinity();
            }
        };
        getOnBackPressedDispatcher().addCallback(this,callback);
    }
}