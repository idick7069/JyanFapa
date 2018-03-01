package com.example.frank.jyanfapa;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import com.facebook.drawee.view.SimpleDraweeView;
import com.example.frank.jyanfapa.callbacks.GetUserCallback;
import com.example.frank.jyanfapa.entities.User;
import com.example.frank.jyanfapa.requests.UserRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Frank on 2018/1/2.
 */

public class ProfileActivity extends Activity implements GetUserCallback.IGetUserResponse {
    private SimpleDraweeView mProfilePhotoView;
    private TextView mName;
    private TextView mId;
    private TextView mEmail;
    private TextView mPermissions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mProfilePhotoView = findViewById(R.id.profile_photo);
        mName = findViewById(R.id.name);
        mId = findViewById(R.id.id);
        mEmail = findViewById(R.id.email);
        mPermissions = findViewById(R.id.permissions);
    }

    @Override
    protected void onResume() {
        super.onResume();
        UserRequest.makeUserRequest(new GetUserCallback(ProfileActivity.this).getCallback());
    }

    @Override
    public void onCompleted(User user) {
        //存資料庫
        register(user);

        //存內存
        storeUserData(user);


        mProfilePhotoView.setImageURI(user.getPicture());
        mName.setText(user.getName());
        mId.setText(user.getId());
        if (user.getEmail() == null) {
            mEmail.setText(R.string.no_email_perm);
            mEmail.setTextColor(Color.RED);
        } else {
            mEmail.setText(user.getEmail());
            mEmail.setTextColor(Color.BLACK);
        }
        mPermissions.setText(user.getPermissions());
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
    private void gotoTest()
    {
        finish();
        Intent intent = new Intent(this, TestActivity.class);
        startActivity(intent);
    }
    public void showtoast()
    {
        Toast.makeText(this,"FB註冊失敗",Toast.LENGTH_SHORT).show();
    }
}
