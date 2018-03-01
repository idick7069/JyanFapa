package com.example.frank.jyanfapa;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;

import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;


public class LocalProfileActivity extends AppCompatActivity {

    EditText editBirthday,editRealname,editEmail,editAddress;
    Spinner sex_spinner,identity_spinner;
    private String i_spinnerIndex;
    private String s_spinnerIndex;
    String username;
    Button changeBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_local_profile);

        editBirthday = (EditText)findViewById(R.id.editBirthday);
        editRealname = (EditText)findViewById(R.id.editRealname);
        editEmail = (EditText)findViewById(R.id.editEmail);
        editAddress = (EditText)findViewById(R.id.editAddress);
        sex_spinner = (Spinner)findViewById(R.id.sexspinner);
        identity_spinner = (Spinner)findViewById(R.id.identityspinner);
        changeBtn = (Button)findViewById(R.id.changeBtn);

        //身分下拉選單
        final ArrayAdapter<CharSequence> identityList = ArrayAdapter.createFromResource(this,
                R.array.identity,
                android.R.layout.simple_spinner_dropdown_item);
        identity_spinner.setAdapter(identityList);
        identity_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                i_spinnerIndex = position + "";
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



        //性別下拉選單
        final ArrayAdapter<CharSequence> sexList = ArrayAdapter.createFromResource(this,
                R.array.sex,
                android.R.layout.simple_spinner_dropdown_item);
        sex_spinner.setAdapter(sexList);
        sex_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                s_spinnerIndex = position + "";
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        changeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                register(username);
            }
        });
        //注入暫存值
        setAllUserData();
    }

    private void setAllUserData() {

        SharedPreferences userData = getSharedPreferences("UserData", MODE_PRIVATE);
        username = userData.getString("User_username", "");
        String password = userData.getString("User_password", "");
        String nickname = userData.getString("User_nickname", "");
        String realname = userData.getString("User_realname", "");
        String email = userData.getString("User_email", "");
        String address = userData.getString("User_address", "");
        int sex = userData.getInt("User_sex", 0);
        String birthday = userData.getString("User_birthday", "");
        int identity = userData.getInt("User_identity", 0);
        int token = userData.getInt("User_token", 0);

        editBirthday.setText(birthday);
        editRealname.setText(realname);
        editEmail.setText(email);
        editAddress.setText(address);


        Log.d("token中的",sex+" : "+ identity);
        sex_spinner.setSelection(sex);
        identity_spinner.setSelection(identity);
    }


    public void register(String name) {
        RequestQueue mQueue = Volley.newRequestQueue(this);
        String url = "http://140.125.81.27/blog2/public/App/update";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("Volley", response);
                        if (response.equals("更新成功")) {
                            SharedPreferences settings = getSharedPreferences("UserData", MODE_PRIVATE);
                            settings.edit()
                                    .putString("User_realname", editRealname.getText().toString())
                                    .putString("User_email", editEmail.getText().toString())
                                    .putString("User_address", editAddress.getText().toString())
                                    .putInt("User_sex",(int) sex_spinner.getSelectedItemId() )
                                    .putInt("User_identity",(int)identity_spinner.getSelectedItemId())
                                    .putString("User_birthday", editBirthday.getText().toString())
                                    .apply();
                            showtoast("更新成功");
                        }
                        else if(response.equals("未進行更新"))
                        {
                            //donothing
                        }
                        else
                        {
                            showtoast("更新失敗");
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
                map.put("username",username);
                map.put("realname", editRealname.getText().toString());
                map.put("email", editEmail.getText().toString());
                map.put("birthday",editBirthday.getText().toString());
                map.put("address",editAddress.getText().toString());
                map.put("sex",sex_spinner.getSelectedItemId()+"");
                map.put("identity",identity_spinner.getSelectedItemId()+"");
                return map;
            }
        };
        mQueue.add(stringRequest);

    }
    public void showtoast(String s)
    {
        Toast.makeText(this,s,Toast.LENGTH_SHORT).show();
    }
}
