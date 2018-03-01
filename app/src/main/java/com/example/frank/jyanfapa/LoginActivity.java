package com.example.frank.jyanfapa;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.frank.jyanfapa.entities.LocalUser;
import com.facebook.AccessToken;
import com.facebook.FacebookSdk;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity{


    public String[][] data = {
            {"生日","請輸入生日"},
            {"電子郵件","請輸入電子郵件"},
            {"真實姓名","請輸入真實姓名"},
            {"地址","請輸入地址"},
    };

//    public String[][] locationData = {
//            {"鴿舍名稱","請輸入鴿舍名稱"},
//            {"鴿舍地址","請輸入鴿舍地址"},
//            {"精度","請輸入精度"},
//            {"請書入","請輸入緯度"},
//    };

    EditText etUsername, etPassword;
    TextView regText,btnReg;
    Button btnLogin;
    ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fresco.initialize(this);
        setContentView(R.layout.login);
        FacebookSdk.sdkInitialize(getApplicationContext());

        etUsername = (EditText)findViewById(R.id.login_username);
        etPassword = (EditText)findViewById(R.id.login_password);
        btnLogin = (Button)findViewById(R.id.login_login_btn);
        btnReg = (TextView)findViewById(R.id.login_register_btn);
        regText = (TextView)findViewById(R.id.login_facebook_btn);
//        imageView =(ImageView)findViewById(R.id.imageView);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newLogin();
            }

        });
        btnReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoRegister();
            }

        });
        regText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoFBLogin();
            }
        });


//        Picasso.with(this).load(R.drawable.relogo).into(imageView);

        SimpleDraweeView draweeView = (SimpleDraweeView) findViewById(R.id.logoview);
        Uri uri = Uri.parse("res:/" + R.drawable.relogo);

        draweeView.setImageURI(uri);
        checkAccess();
    }
    public void newLogin()
    {
        RequestQueue mQueue = Volley.newRequestQueue(this);
        String url ="http://140.125.81.27/blog2/public/App/login";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Volley",response);

                        if(response.equals("Login Faield Wrong Data Passed"))
                        {
                            showToast("帳號密碼有誤請重新輸入");
                        }
                        else
                        {
                            jsontrans(response);
                            //檢查
                            checkAccess();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("err", error.toString());
                        showToast("網路不穩或伺服器出現異常，請重試或聯絡官方人員");
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put("username", etUsername.getText().toString());
                map.put("password", etPassword.getText().toString());
                return map;
            }
        };
        mQueue.add(stringRequest);
    }
    public void StoreUserData(LocalUser localUser)
    {
        SharedPreferences settings = getSharedPreferences("UserData", MODE_PRIVATE);
        settings.edit()
                .putInt("User_number", localUser.getNumber())
                .putString("User_username", localUser.getUsername())
                .putString("User_password",localUser.getPassword())
                .putString("User_nickname", localUser.getNickname())
                .putString("User_realname", localUser.getRealname())
                .putString("User_email", localUser.getEmail())
                .putString("User_address", localUser.getAddress())
                .putInt("User_sex", localUser.getSex())
                .putInt("User_identity",localUser.getIdentity())
                .putString("User_birthday", localUser.getBirthday())
                .putInt("token",1)
                .apply();
    }
    //轉換Json格式並存暫存
    public void jsontrans(String output){
        //json
        Gson gson = new Gson();
        Log.d("回傳值",output.replace("[","").replace("]",""));
        //暫時處理去物件
        String jsonString = output.replace("[","").replace("]","");
        //Json轉物件
        LocalUser localUser = gson.fromJson(jsonString,LocalUser.class);
        //存暫存
        StoreUserData(localUser);
        Log.d("帳號資料：",localUser.getNumber()+localUser.getUsername()+localUser.getPassword());
    }
    private void gotoRegister()
    {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }
    private void gotoFBLogin()
    {
//        Intent intent = new Intent(this, MainActivity.class);
//        startActivity(intent);
        if (AccessToken.getCurrentAccessToken() == null) {
            Intent loginIntent = new Intent(LoginActivity.this, FacebookLoginActivity.class);
            startActivity(loginIntent);
        }
        else
        {
            Intent loginIntent = new Intent(LoginActivity.this, FacebookLoginActivity.class);
            startActivity(loginIntent);
        }

    }
    private void showToast(String string)
    {
        Toast.makeText(this,string,Toast.LENGTH_SHORT).show();
    }
    private void checkAccess()
    {
        String username = getSharedPreferences("UserData", MODE_PRIVATE)
                .getString("User_username", "null");
        int usertoken = getSharedPreferences("UserData",MODE_PRIVATE)
                .getInt("token",0);
        switch (usertoken)
        {
            case 0:
            {
                //    Log.d("登入","尚未登入");
                Log.d(this.getApplicationInfo().className+"","尚未登入");
                break;
            }
            case 1:
            {
                Log.d(this.getApplicationInfo().className+"","手機登入");
                Toast.makeText(this,"手機登入成功",Toast.LENGTH_SHORT).show();
                gotoTest();
                break;
            }
            case 2:
            {
                Log.d(this.getApplicationInfo().className+"","FB登入");
                Toast.makeText(this,"FB登入成功",Toast.LENGTH_SHORT).show();
                gotoFBLogin();
                break;
            }
            default:
            {
                Log.d(getApplicationInfo().className+"","default");
                break;
            }
        }
    }
    private void gotoTest()
    {
        finish();
        Intent intent = new Intent(this, Main2Activity.class);
        startActivity(intent);
    }


//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        //Sdk初始化
//
//        setContentView(R.layout.location);
//
//        //設定toolbar
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//顯示上一層
//
//
//
//        //取得資料 請將他替換成地點的資料
//        List<Map<String, Object>> items = new ArrayList<Map<String,Object>>();
//        for (int i=0;i < data.length;i++){
//            Map<String, Object> item = new HashMap<String, Object>();
//            item.put("title", locationData[i][0]);
//            item.put("data", locationData[i][1]);
//            items.add(item);
//        }
//
//        //帶入對應資料
//        SimpleAdapter adapter = new SimpleAdapter(
//                this,
//                items,
//                R.layout.list_item_2line,
//                new String[]{"title", "data"},
//                new int[]{R.id.line1, R.id.line2}
//        );
//        ListView listview = (ListView) findViewById(R.id.profile_list);
//        listview.setAdapter(adapter);
//        listview.setOnItemClickListener(onClickListView);
//
//
//    }
//
//    private AdapterView.OnItemClickListener onClickListView = new AdapterView.OnItemClickListener(){
//        @Override
//        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//            //到時候用來打開dialog的ＵＩ用
//
//            customDialogEvent();//之後請傳送資料給Dialog以方便顯示標題
//        }
//
//    };
//
//    //塞入icon到toolbar
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.toolbar_yes, menu);
//        return true;
//    }
//    //點擊toolBarIcon
//    private Toolbar.OnMenuItemClickListener onMenuItemClick = new Toolbar.OnMenuItemClickListener() {
//        @Override
//        public boolean onMenuItemClick(MenuItem menuItem) {
//
//
//
//            return true;
//        }
//    };
//
//    private void customDialogEvent() {
//        final View item = LayoutInflater.from(LoginActivity.this).inflate(R.layout.dialog_edit_text, null);
//        new AlertDialog.Builder(LoginActivity.this)
//                .setTitle("鴿舍名稱")//之後請接收傳入的標題
//                .setView(item)
//                .setPositiveButton("完成", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        //事件監聽
//                    }
//                })
//                .show();
//    }




/***********for profile
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Sdk初始化

        setContentView(R.layout.profile);

        //設定toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);//顯示上一層



        //取得資料 請將他替換成個人的資料
        List<Map<String, Object>> items = new ArrayList<Map<String,Object>>();
        for (int i=0;i < data.length;i++){
            Map<String, Object> item = new HashMap<String, Object>();
            item.put("title", data[i][0]);
            item.put("data", data[i][1]);
            items.add(item);
        }

        //帶入對應資料
        SimpleAdapter adapter = new SimpleAdapter(
                this,
                items,
                R.layout.list_item_2line,
                new String[]{"title", "data"},
                new int[]{R.id.line1, R.id.line2}
        );
        ListView listview = (ListView) findViewById(R.id.profile_list);
        listview.setAdapter(adapter);
        listview.setOnItemClickListener(onClickListView);


    }

    private AdapterView.OnItemClickListener onClickListView = new AdapterView.OnItemClickListener(){
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            //到時候用來打開dialog的ＵＩ用

            customDialogEvent();//之後請傳送資料給Dialog以方便顯示標題
        }

    };

    //塞入icon到toolbar
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_yes, menu);
        return true;
    }
    //點擊toolBarIcon
    private Toolbar.OnMenuItemClickListener onMenuItemClick = new Toolbar.OnMenuItemClickListener() {
        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {



            return true;
        }
    };

    private void customDialogEvent() {
        final View item = LayoutInflater.from(LoginActivity.this).inflate(R.layout.dialog_edit_text, null);
        new AlertDialog.Builder(LoginActivity.this)
                .setTitle("姓名")//之後請接收傳入的標題
                .setView(item)
                .setPositiveButton("完成", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //事件監聽
                    }
                })
                .show();
    }


*/

/***************************For registered





    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Sdk初始化

        setContentView(R.layout.registered);


         Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
         setSupportActionBar(toolbar);
         getSupportActionBar().setDisplayHomeAsUpEnabled(true);//顯示上一層


    }
*/








/*
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        FacebookSdk.sdkInitialize(getApplicationContext());

        etUsername = (EditText)findViewById(R.id.text_username);
        etPassword = (EditText)findViewById(R.id.text_password);
        btnLogin = (Button)findViewById(R.id.login_button);
        btnReg = (Button)findViewById(R.id.regButton);
        regText = (TextView)findViewById(R.id.regText);


        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newLogin();
            }

        });
        btnReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gotoRegister();
            }

        });
        regText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoFBLogin();
            }
        });

        checkAccess();
    }
    public void newLogin()
    {
        RequestQueue mQueue = Volley.newRequestQueue(this);
        String url ="http://140.125.81.27/blog2/public/App/login";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Volley",response);

                        if(response.equals("Login Faield Wrong Data Passed"))
                        {
                            showToast("帳號密碼有誤請重新輸入");
                        }
                        else
                        {
                            jsontrans(response);
                            //檢查
                            checkAccess();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("err", error.toString());
                        showToast("網路不穩或伺服器出現異常，請重試或聯絡官方人員");
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                map.put("username", etUsername.getText().toString());
                map.put("password", etPassword.getText().toString());
                return map;
            }
        };
        mQueue.add(stringRequest);
    }
    public void StoreUserData(LocalUser localUser)
    {
        SharedPreferences settings = getSharedPreferences("UserData", MODE_PRIVATE);
        settings.edit()
                .putInt("User_number", localUser.getNumber())
                .putString("User_username", localUser.getUsername())
                .putString("User_password",localUser.getPassword())
                .putString("User_nickname", localUser.getNickname())
                .putString("User_realname", localUser.getRealname())
                .putString("User_email", localUser.getEmail())
                .putString("User_address", localUser.getAddress())
                .putInt("User_sex", localUser.getSex())
                .putInt("User_identity",localUser.getIdentity())
                .putString("User_birthday", localUser.getBirthday())
                .putInt("token",1)
                .apply();
    }
    //轉換Json格式並存暫存
    public void jsontrans(String output){
        //json
        Gson gson = new Gson();
        Log.d("回傳值",output.replace("[","").replace("]",""));
        //暫時處理去物件
        String jsonString = output.replace("[","").replace("]","");
        //Json轉物件
        LocalUser localUser = gson.fromJson(jsonString,LocalUser.class);
        //存暫存
        StoreUserData(localUser);
        Log.d("帳號資料：",localUser.getNumber()+localUser.getUsername()+localUser.getPassword());
    }
    private void gotoRegister()
    {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }
    private void gotoFBLogin()
    {
//        Intent intent = new Intent(this, MainActivity.class);
//        startActivity(intent);
        if (AccessToken.getCurrentAccessToken() == null) {
            Intent loginIntent = new Intent(LoginActivity.this, FacebookLoginActivity.class);
            startActivity(loginIntent);
        }
        else
        {
            Intent loginIntent = new Intent(LoginActivity.this, FacebookLoginActivity.class);
            startActivity(loginIntent);
        }

    }
    private void showToast(String string)
    {
        Toast.makeText(this,string,Toast.LENGTH_SHORT).show();
    }
    private void checkAccess()
    {
        String username = getSharedPreferences("UserData", MODE_PRIVATE)
                .getString("User_username", "null");
        int usertoken = getSharedPreferences("UserData",MODE_PRIVATE)
                .getInt("token",0);
        switch (usertoken)
        {
            case 0:
            {
                //    Log.d("登入","尚未登入");
                Log.d(this.getApplicationInfo().className+"","尚未登入");
                break;
            }
            case 1:
            {
                Log.d(this.getApplicationInfo().className+"","手機登入");
                Toast.makeText(this,"手機登入成功",Toast.LENGTH_SHORT).show();
                gotoTest();
                break;
            }
            case 2:
            {
                Log.d(this.getApplicationInfo().className+"","FB登入");
                Toast.makeText(this,"FB登入成功",Toast.LENGTH_SHORT).show();
                gotoFBLogin();
                break;
            }
            default:
            {
                Log.d(getApplicationInfo().className+"","default");
                break;
            }
        }
    }
    private void gotoTest()
    {
        finish();
        Intent intent = new Intent(this, TestActivity.class);
        startActivity(intent);
    }
*/
}
