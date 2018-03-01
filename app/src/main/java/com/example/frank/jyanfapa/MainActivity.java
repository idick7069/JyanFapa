package com.example.frank.jyanfapa;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

//FB登入


public class MainActivity extends AppCompatActivity {

    //活動編號
    private static final int RESULT_PROFILE_ACTIVITY = 1;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Sdk初始化

        setContentView(R.layout.registered);

    }


    /*
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Sdk初始化
        FacebookSdk.sdkInitialize(getApplicationContext());
        Fresco.initialize(this);
        setContentView(R.layout.activity_main);


        //沒登入過跳轉到FaceBookLoginActivity
        // If MainActivity is reached without the user being logged in, redirect to the Login
        // Activity
        checkAccess();
        if (AccessToken.getCurrentAccessToken() == null) {
            Intent loginIntent = new Intent(MainActivity.this, FacebookLoginActivity.class);
            startActivity(loginIntent);
        }
        // Make a button which leads to profile information of the user
        Button gotoProfileButton = findViewById(R.id.btn_profile);
        gotoProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (AccessToken.getCurrentAccessToken() == null) {
                    Intent profileIntent = new Intent(MainActivity.this, FacebookLoginActivity
                            .class);
                    startActivityForResult(profileIntent, RESULT_PROFILE_ACTIVITY);
                } else {
                    Intent profilentent = new Intent(MainActivity.this, LocalProfileActivity.class);
                    startActivity(profilentent);
                }
            }
        });

        // Make a logout button
        Button fbLogoutButton = findViewById(R.id.btn_fb_logout);
        fbLogoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoginManager.getInstance().logOut();
                Intent loginIntent = new Intent(MainActivity.this, FacebookLoginActivity.class);
                startActivity(loginIntent);
            }
        });




    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case RESULT_PROFILE_ACTIVITY:
                if (resultCode == RESULT_OK) {
                    // 路徑正常
                    Intent profileIntent = new Intent(MainActivity.this, LocalProfileActivity.class);
                    startActivity(profileIntent);
                }
                else
                {
                    // 路徑正常
                    Intent profileIntent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(profileIntent);
                }
                break;
            default:
                super.onActivityResult(requestCode,resultCode, data);
        }
    }

    private void checkAccess()
    {
        String username = getSharedPreferences("UserData", MODE_PRIVATE)
                .getString("User_username", "null");
        if(username.equals("null"))
        {
            Toast.makeText(this,"尚未登入",Toast.LENGTH_SHORT).show();
        }
        else
        {
            Toast.makeText(this,"FB登入成功",Toast.LENGTH_SHORT).show();
            Log.d("暫存資料",username);
            gotoTest();
        }
    }
    private void gotoTest()
    {
        Intent intent = new Intent(this, TestActivity.class);
        startActivity(intent);
    }
    */


}
