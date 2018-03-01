package com.example.frank.jyanfapa;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.frank.jyanfapa.callbacks.GetUserCallback;
import com.example.frank.jyanfapa.entities.LocalUser;
import com.example.frank.jyanfapa.entities.User;
import com.example.frank.jyanfapa.requests.UserRequest;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.gson.Gson;


import java.io.ByteArrayOutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Frank on 2018/1/2.
 */

public class FacebookLoginActivity extends Activity implements GetUserCallback.IGetUserResponse{

    private static final String EMAIL = "email";
    private static final String USER_POSTS = "user_posts";
    private String imageString;
    private CallbackManager mCallbackManager;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facebook_login);

        // Add code to print out the key hash
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.example.frank.brid",
                    PackageManager.GET_SIGNATURES);
            for (android.content.pm.Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }

        mCallbackManager = CallbackManager.Factory.create();

        LoginButton mLoginButton = findViewById(R.id.login_button);

        // Set the initial permissions to request from the user while logging in
        mLoginButton.setReadPermissions(Arrays.asList(EMAIL, USER_POSTS));

        // Register a callback to respond to the user
        mLoginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                setResult(RESULT_OK);
                Log.d("FB登入","成功" +  loginResult.getAccessToken().getUserId());

                //finish();
            }

            @Override
            public void onCancel() {
                setResult(RESULT_CANCELED);
                Log.d("FB登入","取消");
                finish();
            }

            @Override
            public void onError(FacebookException e) {
                // Handle exception
                Log.d("FB登入","失敗");
            }
        });
    }

    @Override
    public void onCompleted(User user) {
        //存資料庫
        register(user);

        //存內存
        storeUserData(user);
    }
    @Override
    protected void onResume() {
        super.onResume();
        UserRequest.makeUserRequest(new GetUserCallback(FacebookLoginActivity.this).getCallback());
    }
    public void register(final User user) {
        RequestQueue mQueue = Volley.newRequestQueue(this);
        String url = "http://140.125.81.27/blog2/public/App/FBregister";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Volley", response);
                        if (response.equals("FB註冊成功")) {
                            gotoTest();
                        }
                        else if (response.equals("FB註冊失敗"))
                        {
                            Log.d("fb","FB資料庫輸入失敗");
                        }
                        else
                        {
                            Log.d("fb","FB資料庫獲取: " + response);
                            jsontrans(response);
                            gotoTest();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("err", error.toString());
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<String, String>();
                Log.d("FB傳送",user.getId() +" - "+ user.getName() +" - "+ user.getEmail());
                map.put("username", user.getId());
                map.put("nickname", user.getName());
                map.put("email",user.getEmail());
//                map.put("picture",imageString);
                return map;
            }
        };
        mQueue.add(stringRequest);

    }
    public void storeUserData(User user)
    {
        SharedPreferences settings = getSharedPreferences("UserData", MODE_PRIVATE);
        settings.edit()
                .putString("User_username", user.getId())
                .putString("User_nickname", user.getName())
                .putString("User_email", user.getEmail())
                .putInt("token",2)
                .apply();
    }
    public void storeUserDataAll(LocalUser localUser)
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
                .putInt("token",2)
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
        storeUserDataAll(localUser);
        Log.d("帳號資料：",localUser.getNumber()+" : "+localUser.getUsername()+" : "+localUser.getPassword()+" : "+localUser.getRealname()+" : "+localUser.getNickname()+" : "+localUser.getEmail()+localUser.getSex());
    }
    private void gotoTest()
    {
        Intent intent = new Intent(this, Main2Activity.class);
        startActivity(intent);
    }
    public void setBase(Bitmap bmp)
    {
        // 先把 bitmpa 轉成 byte
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, stream );
        byte bytes[] = stream.toByteArray();
        // Android 2.2以上才有內建Base64，其他要自已找Libary或是用Blob存入SQLite
        String base64 = Base64.encodeToString(bytes, Base64.NO_WRAP); // 把byte變成base64
        byte[] encode = Base64.encode(bytes,Base64.DEFAULT);
        String encodeString = new String(encode);
        Log.d("base64",base64);
        Log.d("base642",encodeString);
        imageString = base64;
        getBase(base64);
    }
    private void getBase(String base64)
    {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        byte bytes[] = stream.toByteArray();
        bytes = Base64.decode(base64, Base64.DEFAULT);
        Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length); //用BitmapFactory生成bitmap

        //  BitmapDrawable ob = new BitmapDrawable(getResources(), bmp);
//        circleImageView.setImageBitmap(bmp);

    }
}
