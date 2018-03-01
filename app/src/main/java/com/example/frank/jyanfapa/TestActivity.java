package com.example.frank.jyanfapa;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.facebook.login.LoginManager;


public class TestActivity extends AppCompatActivity {
    Button profilebtn,productbtn,loftbtn,addPigeonbtn;
    Button logoutButton;
    TextView data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        profilebtn  = (Button) findViewById(R.id.button2);
        productbtn = (Button) findViewById(R.id.button5);
        loftbtn = (Button)findViewById(R.id.button6);
        addPigeonbtn = (Button)findViewById(R.id.button3);

        profilebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoProfile();
            }
        });
        productbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoProductList();
            }
        });
        loftbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoLoft();
            }
        });
        addPigeonbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoAddPigeon();
            }
        });

        // Make a logout button
        logoutButton = findViewById(R.id.logoutBtn);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //FB帳號的
                LoginManager.getInstance().logOut();
                //內存清空
                clearData();
                //回初始畫面
                Intent loginIntent = new Intent(TestActivity.this, LoginActivity.class);
                startActivity(loginIntent);
            }
        });

    }
    private void gotoProfile()
    {
        Intent intent = new Intent(this, UserProfileActivity.class);
        startActivity(intent);
    }
    private void gotoProductList()
    {
        Intent intent = new Intent(this, ProductListActivity.class);
        startActivity(intent);
    }
    private void gotoLoft()
    {
        Intent intent = new Intent(this, LoftActivity.class);
        startActivity(intent);
    }
    private void gotoAddPigeon()
    {
        Intent intent = new Intent(this, AddPigeonActivity.class);
        startActivity(intent);
    }
    public void clearData()
    {
        SharedPreferences settings = getSharedPreferences("UserData", MODE_PRIVATE);
        settings.edit().clear().commit();
    }
}

